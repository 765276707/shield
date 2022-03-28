package com.gitee.pristine.autoconfigure.desensitiser;

import com.gitee.pristine.autoconfigure.exception.extd.InvalidSecretException;

/**
 * 属性脱敏器
 * @author Pristine Xu
 */
public interface PropertyDesensitiser {

    /**
     * 设置配置参数
     * @param configParam 配置参数
     */
    void setConfigParam(ConfigParam configParam);

    /**
     * 检查密钥是否合法
     * @param secret 密钥
     * @throws InvalidSecretException 密钥检测无效则抛出该异常
     */
    void checkSecret(String secret) throws InvalidSecretException;

    /**
     * 加密属性值
     * @param value 属性值
     * @return
     */
    String encode(String value);

    /**
     * 解密属性值
     * @param value 属性值
     * @return
     */
    String decode(String value);

}
