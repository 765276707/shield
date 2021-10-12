package com.gitee.pristine.autoconfigure.desensitiser.impl;

import com.gitee.pristine.autoconfigure.desensitiser.AbstractPropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.AlgorithmException;
import com.gitee.pristine.autoconfigure.factory.IvParameterFactory;
import com.gitee.pristine.autoconfigure.util.HexUtil;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.Security;

public class Des3PropertyDesensitiser extends AbstractPropertyDesensitiser {

    // 编码类型
    private final static String ENCODED = "UTF-8";

    @Override
    public String encode(String value) {
        Key deskey = null;
        // 添加pkcs7支持
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            // 转key，java机制必须截取前面24位
            DESedeKeySpec spec = new DESedeKeySpec(HexUtil.hexStr2Bytes(secret));
            // 获取安全工厂,获取加密算法
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede","BC");
            deskey = keyFactory.generateSecret(spec);
            // 设置加密策略
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            // 设置偏移量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(getIvParameter().getBytes(Charset.forName(ENCODED)));
            // 初始化加密属性
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ivParameterSpec);
            // 执行加密并且返回
            return new String(Base64.encode(cipher.doFinal(value.getBytes(Charset.forName(ENCODED)))));
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
            DESedeKeySpec spec = new DESedeKeySpec(HexUtil.hexStr2Bytes(secret));
            // 获取安全工厂,获取解密算法
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede","BC");
            deskey = keyFactory.generateSecret(spec);
            // 设置解密策略
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            // 设置偏移量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(getIvParameter().getBytes(Charset.forName(ENCODED)));
            // 初始化解密属性
            cipher.init(Cipher.DECRYPT_MODE, deskey, ivParameterSpec);
            // 执行解密并返回
            return new String(cipher.doFinal(Base64.decode(value)), Charset.forName(ENCODED));
        }
        catch (Exception e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }

    private String getIvParameter() {
        return IvParameterFactory.getIvParameter(secret, 16);
    }
}
