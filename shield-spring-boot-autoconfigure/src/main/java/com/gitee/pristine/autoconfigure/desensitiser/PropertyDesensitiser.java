package com.gitee.pristine.autoconfigure.desensitiser;

/**
 * 属性脱敏器
 * @author xzb
 */
public interface PropertyDesensitiser {

    /**
     * 设置秘钥
     * @param secret 秘钥
     */
    void setSecret(String secret);

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
