package com.gitee.pristine.autoconfigure.desensitiser.impl;

import com.gitee.pristine.autoconfigure.desensitiser.AbstractPropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.extd.InvalidSecretException;
import com.gitee.pristine.autoconfigure.util.Rc4Util;
import org.bouncycastle.crypto.engines.RC4Engine;

/**
 * RC4 脱敏器
 * @author Pristine Xu
 * @date 2022/3/11 17:10
 * @description: 密钥长度只需要大于1位，不含特殊符号即可
 */
public class Rc4PropertyDesensitiser extends AbstractPropertyDesensitiser {

    @Override
    public void checkSecret(String secret) throws InvalidSecretException {
        // 不为空或NULL
        if (secret==null || "".equalsIgnoreCase(secret)) {
            throw new InvalidSecretException("RC4 secret is not null or empty.");
        }
    }

    @Override
    public String encode(String value) {
        return Rc4Util.encryptToString(value, configParam.getSecret());
    }

    @Override
    public String decode(String value) {
        return Rc4Util.decrypt(value, configParam.getSecret());
    }

}
