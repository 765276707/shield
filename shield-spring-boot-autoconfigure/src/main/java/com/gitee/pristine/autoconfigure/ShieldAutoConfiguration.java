package com.gitee.pristine.autoconfigure;

import com.gitee.pristine.autoconfigure.conf.ExpandPoint;
import com.gitee.pristine.autoconfigure.conf.ShieldConfiguration;
import com.gitee.pristine.autoconfigure.converter.PropertySourceProcessor;
import com.gitee.pristine.autoconfigure.desensitiser.PropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.ShieldException;
import com.gitee.pristine.autoconfigure.exception.extd.ConfigValueParseException;
import com.gitee.pristine.autoconfigure.factory.DesensitiserFactory;
import com.gitee.pristine.autoconfigure.factory.PropertiesFactory;
import com.gitee.pristine.autoconfigure.listener.PropertyListener;
import com.gitee.pristine.autoconfigure.listener.ShieldPublisher;
import com.gitee.pristine.autoconfigure.listener.impl.DetailsPropertyListener;
import com.gitee.pristine.autoconfigure.listener.impl.RiskingPropertyListener;
import com.gitee.pristine.autoconfigure.properties.ShieldProperties;
import com.gitee.pristine.autoconfigure.proxy.DesensitiserProxy;
import com.gitee.pristine.autoconfigure.validator.CharsetValidator;
import com.gitee.pristine.autoconfigure.util.KeywordsUtil;
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
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Shield自动装配
 * @author Pristine Xu
 * @date 2022/3/23 17:59
 * @description:
 */
public class ShieldAutoConfiguration implements BeanFactoryPostProcessor, EnvironmentAware, Ordered {

    private final Logger log = LoggerFactory.getLogger(ShieldAutoConfiguration.class);

    /**
     * 脱敏器注入Spring容器的默认Bean名称
     */
    private final static String DESENSITINER_BEAN_NAME = "propertyDesensitiser";
    /**
     * 配置环境
     */
    private ConfigurableEnvironment configurableEnvironment;
    /**
     * 组件优先级
     */
    private static final int ORDER = 10;
    /**
     * PropertySource处理器
     */
    private final PropertySourceProcessor propertySourceProcessor = new PropertySourceProcessor();

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
        ExpandPoint expandPoint = ExpandPoint.createEmpty();
        String[] beanNames = factory.getBeanNamesForType(ShieldConfiguration.class);
        for (String beanName : beanNames)
        {
            BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
            if (beanDefinition != null)
            {
                this.postExpandPointMethod(expandPoint, beanDefinition.getBeanClassName());
            }
        }


        // 实例化脱敏器，并且注入到Spring容器中
        PropertyDesensitiser propertyDesensitiser = this.initDesensitiser(expandPoint, shieldProperties);
        factory.registerSingleton(DESENSITINER_BEAN_NAME, propertyDesensitiser);


        // 初始化监听器
        ShieldPublisher shieldPublisher = this.initializePublisher(shieldProperties, expandPoint);


        // 解析所有配置
        DesensitiserProxy desensitiserProxy = this.createProxy(propertyDesensitiser, shieldPublisher, shieldProperties);
        this.propertySourceProcessor.process(this.configurableEnvironment.getPropertySources(),
                        desensitiserProxy, expandPoint.getAdditionalConverters());

        // 打印日志
        log.info("Shield config has been loaded successfully.");

    }

    /**
     * 从环境中读取 ShieldProperties 的值
     * @return
     */
    private ShieldProperties loadShieldPropertiesFromEnvironment() {
        // 读取 ShieldProperties
        ShieldProperties shieldProperties = PropertiesFactory.loadShieldProperties(this.configurableEnvironment);
        // 校验编码类型是否有效，暂时只支持UTF-8、GBK
        String charset = shieldProperties.getCharset();
        if (!CharsetValidator.support(charset)) {
            throw new ConfigValueParseException(String.format("Charset type [%s] is not supported.", charset));
        }
        // 如果开启风险提示，则校验是否填写风险关键词
        Boolean enableRisking = shieldProperties.getEnableRisking();
        if (Boolean.TRUE.equals(enableRisking)) {
            String riskingKeywords = shieldProperties.getRiskingKeywords();
            if (riskingKeywords==null || "".equals(riskingKeywords)) {
                throw new ConfigValueParseException("Risking keywords not be empty when you turn on risk warning.");
            }
        }
        return shieldProperties;
    }

    /**
     * 执行自定义配置的方法
     * @param expandPoint 拓展配置
     * @param configClazz 配置类
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
            throw new ShieldException(" [%s] can not be found, check ShieldConfiguration class path or it exists.", configClazz);
        }
        catch (Exception e) {
            throw new ShieldException(e.getMessage(), e);
        }
    }

    /**
     * 初始化脱敏器
     * @param expandPoint 拓展端点
     * @param shieldProperties 配置属性
     * @return
     */
    private PropertyDesensitiser initDesensitiser(ExpandPoint expandPoint, ShieldProperties shieldProperties) {
        PropertyDesensitiser desensitiser = expandPoint.getCustomizeDesensitiser();
        if (desensitiser == null) {
            desensitiser = DesensitiserFactory.delegate(shieldProperties.getAlgorithm());
        }
        // 检测密钥是否有效
        desensitiser.checkSecret(shieldProperties.getSecret());
        // 检测通过
        return desensitiser;
    }


    /**
     * 创建脱敏器的代理类
     * @param propertyDesensitiser 属性脱敏器
     * @param shieldPublisher 广播器
     * @param shieldProperties 配置属性
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
     * @param properties 配置属性
     * @param expandPoint 拓展配置
     * @return
     */
    private ShieldPublisher initializePublisher(ShieldProperties properties, ExpandPoint expandPoint) {
        ShieldPublisher publisher = new ShieldPublisher();
        // 注册默认的监听器
        if (properties.getEnableRisking()) {
            // 对不同配置途径的关键词进行合并
            // 如：k1,k2 + k3,k4,k5 ==> k1,k2,k3,k4,k5
            String riskingKeywords = KeywordsUtil.join(expandPoint.getRiskingKeywords(), properties.getRiskingKeywords());
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
