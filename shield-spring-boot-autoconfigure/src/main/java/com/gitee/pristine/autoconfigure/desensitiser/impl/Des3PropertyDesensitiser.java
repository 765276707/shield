package com.gitee.pristine.autoconfigure.desensitiser.impl;

import com.gitee.pristine.autoconfigure.desensitiser.AbstractPropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.extd.AlgorithmException;
import com.gitee.pristine.autoconfigure.exception.extd.InvalidSecretException;
import com.gitee.pristine.autoconfigure.factory.IvParameterFactory;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.Security;

/**
 * DES3 脱敏器
 * @author Pristine Xu
 * @date 2022/3/10 17:10
 * @description: 密钥长度必须不少于24位，切需要是8的倍数，如24位，32位等
 */
public class Des3PropertyDesensitiser extends AbstractPropertyDesensitiser {

    /**
     * 算法名称
     */
    private final static String DES3 = "DESede";
    /**
     * CBC的填充方式
     */
    private final static String DES3_CBC_PADDING = "DESede/CBC/PKCS5Padding";

    @Override
    public void checkSecret(String secret) throws InvalidSecretException {
        // 不为空或NULL
        if (secret==null || "".equalsIgnoreCase(secret)) {
            throw new InvalidSecretException("3DES secret is not null or empty.");
        }
        // 密钥长度
        int secretLength = secret.length();
        if (secretLength!=24 && secretLength!=32) {
            throw new InvalidSecretException("3DES secret length need be 24 byte or 32 byte.");
        }
    }

    @Override
    public String encode(String value) {
        Key deskey = null;
        // 添加pkcs7支持
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            // 转key，java机制必须截取前面24位
            byte[] secretBytes = configParam.getSecret().getBytes(Charset.forName(configParam.getCharset()));
            DESedeKeySpec spec = new DESedeKeySpec(secretBytes);
            // 获取安全工厂,获取加密算法
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES3,"BC");
            deskey = keyFactory.generateSecret(spec);
            // 设置加密策略
            Cipher cipher = Cipher.getInstance(DES3_CBC_PADDING);
            // 设置偏移量
            byte[] ivBytes = getIvParameter().getBytes(Charset.forName(configParam.getCharset()));
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            // 初始化加密属性
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ivParameterSpec);
            // 执行加密并且返回
            return new String(Base64.encode(cipher.doFinal(value.getBytes(Charset.forName(configParam.getCharset())))));
        }
        catch (Exception e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }

    @Override
    public String decode(String value) {
        Key deskey = null;
        // 添加pkcs7支持
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            // 转key，java机制必须截取前面24位
            byte[] secretBytes = configParam.getSecret().getBytes(Charset.forName(configParam.getCharset()));
            DESedeKeySpec spec = new DESedeKeySpec(secretBytes);
            // 获取安全工厂,获取解密算法
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES3,"BC");
            deskey = keyFactory.generateSecret(spec);
            // 设置解密策略
            Cipher cipher = Cipher.getInstance(DES3_CBC_PADDING);
            // 设置偏移量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(getIvParameter().getBytes(Charset.forName(configParam.getCharset())));
            // 初始化解密属性
            cipher.init(Cipher.DECRYPT_MODE, deskey, ivParameterSpec);
            // 执行解密并返回
            return new String(cipher.doFinal(Base64.decode(value)), Charset.forName(configParam.getCharset()));
        }
        catch (Exception e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }

    /**
     * DES3的偏移量
     * @return 必须是8位长度
     */
    private String getIvParameter() {
        return IvParameterFactory.getIvParameter(configParam.getSecret(), 8);
    }
}
