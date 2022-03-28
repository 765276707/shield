package com.gitee.pristine.autoconfigure.desensitiser.impl;

import com.gitee.pristine.autoconfigure.desensitiser.AbstractPropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.extd.AlgorithmException;
import com.gitee.pristine.autoconfigure.exception.extd.InvalidSecretException;
import com.gitee.pristine.autoconfigure.factory.IvParameterFactory;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 * AES 脱敏器
 * @author Pristine Xu
 * @date 2022/3/10 17:10
 * @description: 密钥长度必须不少于16位，切需要是8的倍数，如16位，32位等
 */
public class AesPropertyDesensitiser extends AbstractPropertyDesensitiser {

    /**
     * 算法名称
     */
    private final static String AES = "AES";
    /**
     * CBC的填充方式
     */
    private final static String AES_CBC_PADDING = "AES/CBC/PKCS5Padding";

    @Override
    public void checkSecret(String secret) throws InvalidSecretException {
        // 不为空或NULL
        if (secret==null || "".equalsIgnoreCase(secret)) {
            throw new InvalidSecretException("AES secret is not null or empty.");
        }
        // 密钥长度
        int secretLength = secret.length();
        if (secretLength!=16 && secretLength!=24 && secretLength!=32) {
            throw new InvalidSecretException("AES secret length need be 16 byte or 24 byte or 32 byte.");
        }
    }

    @Override
    public String encode(String value) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(configParam.getSecret().getBytes(), AES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(getIvParameter().getBytes(Charset.forName(configParam.getCharset())));
        try {
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes(Charset.forName(configParam.getCharset())));
            // 对加密后数据进行 base64 编码
            return Base64.encodeBase64URLSafeString(encryptedBytes);
        }
        catch (Exception e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }

    @Override
    public String decode(String value) {
        // 生成秘钥
        SecretKeySpec secretKey = new SecretKeySpec(configParam.getSecret().getBytes(), AES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(getIvParameter().getBytes(Charset.forName(configParam.getCharset())));
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return new String(cipher.doFinal(Base64.decodeBase64(value)), Charset.forName(configParam.getCharset()));
        }
        catch (Exception e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }

    private String getIvParameter() {
        return IvParameterFactory.getIvParameter(configParam.getSecret(), 16);
    }
}
