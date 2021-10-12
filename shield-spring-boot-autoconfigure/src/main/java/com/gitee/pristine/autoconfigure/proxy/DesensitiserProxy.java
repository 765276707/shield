package com.gitee.pristine.autoconfigure.proxy;

import com.gitee.pristine.autoconfigure.desensitiser.PropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.ShieldException;
import com.gitee.pristine.autoconfigure.listener.PropertyEvent;
import com.gitee.pristine.autoconfigure.listener.ShieldPublisher;
import com.gitee.pristine.autoconfigure.properties.ShieldProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class DesensitiserProxy {

    private final Logger log = LoggerFactory.getLogger(DesensitiserProxy.class);

    // 代理对象
    private boolean isInitProxy = false;
    private final PropertyDesensitiser propertyDesensitiser;
    private ShieldPublisher shieldPublisher;
    private String prefix;
    private String suffix;

    public DesensitiserProxy(PropertyDesensitiser propertyDesensitiser) {
        this.propertyDesensitiser = propertyDesensitiser;
    }

    /**
     * 初始化参数，创建代理对象后必须执行本方法
     * @param shieldProperties
     * @param shieldPublisher
     */
    public void initProxyArgs(ShieldProperties shieldProperties, ShieldPublisher shieldPublisher) {
        if (!this.isInitProxy) {
            this.prefix = shieldProperties.getPrefix();
            this.suffix = shieldProperties.getSuffix();
            // 设置监听器
            this.shieldPublisher = shieldPublisher;
            // 代理的对象赋秘钥值
            this.propertyDesensitiser.setSecret(shieldProperties.getSecret());
            this.isInitProxy = true;
        }
    }


    public void stopPublish() {
        this.shieldPublisher.notifyOfEvent();
    }


    public Object decode(String propertyName, Object propertyValue) {

        // 校验是否执行了初始化方法
        this.prepareVerification();

        // 无需解密
        if (!isEncryptedValue(propertyValue)) {
            // 监听未加密的属性
            PropertyEvent propertyEvent = new PropertyEvent(propertyName, propertyValue);
            this.shieldPublisher.notifyOnEvent(propertyEvent);
            return propertyValue;
        }

        // 执行解密
        try
        {
            String encValue = this.getValueWithoutPrefixAndSuffix(String.valueOf(propertyValue));
            String decValue = this.propertyDesensitiser.decode(encValue);

            // 监听已加密的属性
            PropertyEvent propertyEvent = new PropertyEvent(true, propertyName, propertyValue, decValue);
            this.shieldPublisher.notifyOnEvent(propertyEvent);

            return decValue;
        }
        catch (Exception e) {
            log.error("[fog] decrypting property value errors: {}", e.getMessage(), e);
            throw new ShieldException(e.getMessage(), e);
        }
    }


    /**
     * 校验是否执行了初始化方法
     */
    private void prepareVerification() {
        if (!this.isInitProxy) {
            throw new ShieldException("未调用脱敏器代理类的初始化方法： DesensitiserProxy.initProxyArgs()");
        }
    }

    /**
     * 解析的值是否被加密
     * @param propertyValue 属性值
     * @return
     */
    private boolean isEncryptedValue(Object propertyValue) {
        // 当前仅仅支持字符串
        if (!(propertyValue instanceof CharSequence)) {
            return false;
        }
        if (StringUtils.isEmpty(propertyValue)) {
            return false;
        }
        String strPropertyValue = String.valueOf(propertyValue);
        return strPropertyValue.startsWith(this.prefix)
                && strPropertyValue.endsWith(this.suffix);
    }


    /**
     * 获取真实的属性值
     * @param propertyValue 属性值，带加密标识
     * @return
     */
    private String getValueWithoutPrefixAndSuffix(String propertyValue) {
        if (!isEncryptedValue(propertyValue)) {
            return propertyValue;
        }
        // 获取真实的属性值
        return propertyValue.substring(this.prefix.length(), propertyValue.length()-this.suffix.length());
    }

}
