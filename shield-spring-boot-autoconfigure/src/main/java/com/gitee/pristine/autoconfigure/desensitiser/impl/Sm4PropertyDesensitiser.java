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

/**
 * SM4(国密4) 脱敏器
 * @author Pristine Xu
 * @date 2022/3/26 10:09
 * @description: 密钥长度必须是16位（128bit）
 */
public class Sm4PropertyDesensitiser extends AbstractPropertyDesensitiser {

    /**
     * 算法名称
     */
    private final static String SM4 = "SM4";
    /**
     * 填充方式，基于Bouncy Castle, 只支持ECB
     */
    private final static String SM4_ECB_PADDING = "SM4/ECB/PKCS5Padding";

    @Override
    public void checkSecret(String secret) throws InvalidSecretException {
        // 不为空或NULL
        if (secret==null || "".equalsIgnoreCase(secret)) {
            throw new InvalidSecretException("SM4 secret is not null or empty.");
        }
        // 密钥长度
        int secretLength = secret.length();
        if (secretLength != 16) {
            throw new InvalidSecretException("3DES secret length need be 16 byte.");
        }
    }

    @Override
    public String encode(String value) {
        Key key = this.generateKey(configParam.getSecret());
        try {
            // 基于Bouncy Castle下实现
            Cipher cipher = Cipher.getInstance(SM4_ECB_PADDING, new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeBase64URLSafeString(cipher.doFinal(value.getBytes(configParam.getCharset())));
        }
        catch (Exception e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }

    @Override
    public String decode(String value) {
        Key key = this.generateKey(configParam.getSecret());
        try {
            // 基于Bouncy Castle下实现
            Cipher cipher = Cipher.getInstance(SM4_ECB_PADDING, new BouncyCastleProvider());
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
        return new SecretKeySpec(key.getBytes(Charset.forName(configParam.getCharset())), SM4);
    }

}
