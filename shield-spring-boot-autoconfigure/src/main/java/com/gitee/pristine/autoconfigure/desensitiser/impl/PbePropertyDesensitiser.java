package com.gitee.pristine.autoconfigure.desensitiser.impl;

import com.gitee.pristine.autoconfigure.desensitiser.AbstractPropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.extd.AlgorithmException;
import com.gitee.pristine.autoconfigure.exception.extd.InvalidSecretException;
import com.gitee.pristine.autoconfigure.factory.SaltFactory;
import org.bouncycastle.util.encoders.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * PBE 脱敏器
 * @author Pristine Xu
 * @date 2022/3/10 17:10
 * @description: 密钥长度只需要大于1位，不含特殊符号即可
 */
public class PbePropertyDesensitiser extends AbstractPropertyDesensitiser {

    /**
     * 算法名称
     */
    private final static String PBE_ALG = "PBEWithMD5AndDES";

    @Override
    public void checkSecret(String secret) throws InvalidSecretException {
        // 不为空或NULL
        if (secret==null || "".equalsIgnoreCase(secret)) {
            throw new InvalidSecretException("PBE secret is not null or empty.");
        }
    }

    @Override
    public String encode(String value) {
        Key key = this.generateKey();
        PBEParameterSpec spec = new PBEParameterSpec(getSalt(), 100);
        try
        {
            Cipher cipher = Cipher.getInstance(PBE_ALG);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            return new String(Base64.encode(cipher.doFinal(value.getBytes(Charset.forName(configParam.getCharset())))));
        }
        catch (Exception e)
        {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }

    @Override
    public String decode(String value) {
        Key key = this.generateKey();
        PBEParameterSpec spec = new PBEParameterSpec(getSalt(), 100);
        try
        {
            Cipher cipher = Cipher.getInstance(PBE_ALG);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            return new String(cipher.doFinal(Base64.decode(value)), Charset.forName(configParam.getCharset()));
        }
        catch (Exception e)
        {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }


    /**
     * 生成密钥
     * @return
     */
    private Key generateKey() {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(configParam.getSecret().toCharArray());
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALG);
            return factory.generateSecret(pbeKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AlgorithmException("生成PBE密钥时错误", e);
        }
    }

    private byte[] getSalt() {
        try {
            return SaltFactory.generateFixedSalt(configParam.getSecret(), 8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
