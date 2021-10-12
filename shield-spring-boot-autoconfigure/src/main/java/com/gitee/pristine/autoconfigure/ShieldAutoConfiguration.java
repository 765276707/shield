package com.gitee.pristine.autoconfigure;

import com.gitee.pristine.autoconfigure.conf.ExpandPoint;
import com.gitee.pristine.autoconfigure.conf.ShieldConfiguration;
import com.gitee.pristine.autoconfigure.converter.PropertySourceProcessor;
import com.gitee.pristine.autoconfigure.desensitiser.PropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.ShieldException;
import com.gitee.pristine.autoconfigure.factory.DesensitiserFactory;
import com.gitee.pristine.autoconfigure.factory.PropertiesFactory;
import com.gitee.pristine.autoconfigure.listener.PropertyListener;
import com.gitee.pristine.autoconfigure.listener.ShieldPublisher;
import com.gitee.pristine.autoconfigure.listener.impl.DetailsPropertyListener;
import com.gitee.pristine.autoconfigure.listener.impl.RiskingPropertyListener;
import com.gitee.pristine.autoconfigure.properties.ShieldProperties;
import com.gitee.pristine.autoconfigure.proxy.DesensitiserProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.StringUtils;
import java.lang.reflect.Constructor;
import java.util.List;

public class ShieldAutoConfiguration implements BeanFactoryPostProcessor, EnvironmentAware, Ordered {

    // 脱敏器注入Spring容器的默认Bean名称
    private final static String DESENSITINER_NAME = "propertyDesensitiser";

    private Logger log = LoggerFactory.getLogger(ShieldAutoConfiguration.class);
    private ConfigurableEnvironment configurableEnvironment;
    private static final int ORDER = 10;

    @Override
    public void setEnvironment(Environment environment) {
        // 强转 ConfigurableEnvironment
        this.configurableEnvironment = (ConfigurableEnvironment) environment;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + ORDER;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
        // 从环境中读取 ShieldProperties 的值
        ShieldProperties shieldProperties = this.loadShieldPropertiesFromEnvironment();


        // 自定义配置类
        ExpandPoint expandPoint = new ExpandPoint();
        String[] beanNames = factory.getBeanNamesForType(ShieldConfiguration.class);
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
            if (beanDefinition != null) {
                this.postExpandPointMethod(expandPoint, beanDefinition.getBeanClassName());
            }
        }


        // 实例化脱敏器，并且注入到Spring容器中
        PropertyDesensitiser propertyDesensitiser = this.initDesensitiser(expandPoint, shieldProperties);
        factory.registerSingleton(DESENSITINER_NAME, propertyDesensitiser);


        // 初始化监听器
        ShieldPublisher shieldPublisher = this.initializePublisher(shieldProperties, expandPoint);


        // 解析所有配置
        DesensitiserProxy desensitiserProxy = this.createProxy(propertyDesensitiser, shieldPublisher, shieldProperties);
        new PropertySourceProcessor()
                .process(getMutablePropertySources(), desensitiserProxy, expandPoint.getAdditionalConverters());

    }

    /**
     * 从环境中读取 ShieldProperties 的值
     * @return
     */
    private ShieldProperties loadShieldPropertiesFromEnvironment() {
        return PropertiesFactory.loadShieldProperties(this.configurableEnvironment);
    }

    /**
     * 执行自定义配置的方法
     * @param expandPoint
     * @param configClazz
     */
    private void postExpandPointMethod(ExpandPoint expandPoint, String configClazz) {
        try
        {
            // 实例化指定配置类
            Class<?> algClazz = Class.forName(configClazz);
            Constructor<?> constructor = algClazz.getDeclaredConstructor();
            ShieldConfiguration shieldConfiguration = (ShieldConfiguration) constructor.newInstance();
            // 执行自定义配置方法
            shieldConfiguration.config(expandPoint);
        }
        catch (ClassNotFoundException e) {
            throw new ShieldException(" [%s] can not be found, check encoder class path or it exists.", configClazz);
        }
        catch (Exception e) {
            throw new ShieldException("DesensitizeEncoder bean init failure.", e);
        }
    }

    /**
     * 初始化脱敏器
     * @param expandPoint 拓展端点
     * @param shieldProperties
     * @return
     */
    private PropertyDesensitiser initDesensitiser(ExpandPoint expandPoint, ShieldProperties shieldProperties) {
        PropertyDesensitiser desensitiser = expandPoint.getCustomizeDesensitiser();
        if (desensitiser == null) {
            desensitiser = DesensitiserFactory.delegate(shieldProperties.getAlgorithm());
        }
        return desensitiser;
    }


    private MutablePropertySources getMutablePropertySources() {
        return this.configurableEnvironment.getPropertySources();
    }

    /**
     * 创建脱敏器的代理类
     * @param propertyDesensitiser
     * @param shieldPublisher
     * @param shieldProperties
     * @return
     */
    private DesensitiserProxy createProxy(PropertyDesensitiser propertyDesensitiser,
                                          ShieldPublisher shieldPublisher, ShieldProperties shieldProperties) {
        DesensitiserProxy proxyBean = new DesensitiserProxy(propertyDesensitiser);
        proxyBean.initProxyArgs(shieldProperties, shieldPublisher);
        return proxyBean;
    }


    /**
     * 初始化监听器
     * @param properties
     * @param expandPoint
     * @return
     */
    private ShieldPublisher initializePublisher(ShieldProperties properties, ExpandPoint expandPoint) {
        ShieldPublisher publisher = new ShieldPublisher();
        // 注册默认的监听器
        if (properties.getEnableRisking()) {
            String riskingKeywords = expandPoint.getRiskingKeywords();
            // JavaConfig 优先于 配置文件的配置
            if (StringUtils.isEmpty(riskingKeywords)) {
                riskingKeywords = properties.getRiskingKeywords();
            }
            publisher.addListener(new RiskingPropertyListener(riskingKeywords));
        }
        if (properties.getEnableDetails()) {
            publisher.addListener(new DetailsPropertyListener());
        }
        // 添加拓展的监听器
        List<PropertyListener> additionalListeners = expandPoint.getAdditionalListeners();
        if (additionalListeners!=null && !additionalListeners.isEmpty()) {
            additionalListeners.forEach(publisher::addListener);
        }
        return publisher;
    }

}
