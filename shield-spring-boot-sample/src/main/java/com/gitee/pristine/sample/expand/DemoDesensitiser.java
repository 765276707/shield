package com.gitee.pristine.sample.expand;

import com.gitee.pristine.autoconfigure.desensitiser.AbstractPropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.extd.InvalidSecretException;

/**
 * 自定义脱敏器
 * @author Pristine Xu
 */
public class DemoDesensitiser extends AbstractPropertyDesensitiser {

    @Override
    public void checkSecret(String secret) throws InvalidSecretException {
        // 此处检测密钥是否合法，不合法抛出 InvalidSecretException 异常
    }

    @Override
    public String encode(String value) {
        // 获取密钥
        String secret = super.configParam.getSecret();
        // 获取编码类型
        String charset = super.configParam.getCharset();
        // 此处进行属性加密......
        return value;
    }

    @Override
    public String decode(String value) {
        // 获取密钥
        String secret = super.configParam.getSecret();
        // 获取编码类型
        String charset = super.configParam.getCharset();
        // 此处进行属性解密......
        return value;
    }

}
