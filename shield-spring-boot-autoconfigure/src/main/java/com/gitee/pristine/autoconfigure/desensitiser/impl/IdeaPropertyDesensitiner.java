package com.gitee.pristine.autoconfigure.desensitiser.impl;

import com.gitee.pristine.autoconfigure.desensitiser.AbstractPropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.extd.AlgorithmException;
import com.gitee.pristine.autoconfigure.exception.extd.InvalidSecretException;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.Security;

/**
 * 国际数据加密标准算法 脱敏器
 * @author Pristine Xu
 * @date 2022/3/25 18:33
 * @description: 密钥长度大于1，不含特殊符号
 */
public class IdeaPropertyDesensitiner extends AbstractPropertyDesensitiser {

    /**
     * 算法名称
     */
    private final static String IDEA = "IDEA";
    /**
     * 填充方式，基于Bouncy Castle, 只支持ECB
     */
    private final static String IDEA_ECB_PADDING = "IDEA/ECB/PKCS5Padding";

    @Override
    public void checkSecret(String secret) throws InvalidSecretException {
        // 不为空或NULL
        if (secret==null || "".equalsIgnoreCase(secret)) {
            throw new InvalidSecretException("IDEA secret is not null or empty.");
        }
    }

    @Override
    public String encode(String value) {
        Key key = generateKey(configParam.getSecret());
        try {
            // 基于Bouncy Castle下实现
            Cipher cipher = Cipher.getInstance(IDEA_ECB_PADDING, new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeBase64URLSafeString(cipher.doFinal(value.getBytes(configParam.getCharset())));
        }
        catch (Exception e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }

    @Override
    public String decode(String value) {
        Key key = generateKey(configParam.getSecret());
        try {
            // 基于Bouncy Castle下实现
            Cipher cipher = Cipher.getInstance(IDEA_ECB_PADDING, new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.decodeBase64(value)), Charset.forName(configParam.getCharset()));
        }
        catch (Exception e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }

    /**
     * 生成密钥，支持128位
     * @return
     */
    private Key generateKey(String key) {
        return new SecretKeySpec(key.getBytes(Charset.forName(configParam.getCharset())), IDEA);
    }
}
