package com.gitee.pristine.autoconfigure.desensitiser.impl;

import com.gitee.pristine.autoconfigure.desensitiser.AbstractPropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.AlgorithmException;
import com.gitee.pristine.autoconfigure.factory.IvParameterFactory;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

public class AesPropertyDesensitiser extends AbstractPropertyDesensitiser {

    // 算法
    private final static String AES = "AES";
    // CBC的填充方式
    private final static String AES_CBC_PADDING = "AES/CBC/PKCS5Padding";
    // 编码类型
    private final static String ENCODED = "UTF-8";

    @Override
    public String encode(String value) {
        // 为了能与 ios 统一，这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), AES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(getIvParameter().getBytes(Charset.forName(ENCODED)));
        try {
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes(Charset.forName(ENCODED)));
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
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), AES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(getIvParameter().getBytes(Charset.forName(ENCODED)));
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return new String(cipher.doFinal(Base64.decodeBase64(value)), Charset.forName(ENCODED));
        }
        catch (Exception e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }

    private String getIvParameter() {
        return IvParameterFactory.getIvParameter(secret, 16);
    }
}
