package com.gitee.pristine.autoconfigure.desensitiser;

/**
 * 脱敏器所需要的配置参数值
 * @author Pristine Xu
 * @date 2022/3/22 14:13
 * @description:
 */
public class ConfigParam {

    /**
     * 密钥
     */
    private String secret;

    /**
     * 字符集
     */
    private String charset;

    private ConfigParam() {}

    public ConfigParam(String secret, String charset) {

        this.secret = secret;
        this.charset = charset;
    }

    public String getSecret() {
        return secret;
    }

    public String getCharset() {
        return charset;
    }
}
