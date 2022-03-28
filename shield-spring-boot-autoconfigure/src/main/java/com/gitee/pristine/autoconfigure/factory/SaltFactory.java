package com.gitee.pristine.autoconfigure.factory;

import com.gitee.pristine.autoconfigure.util.DigestUtil;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * 生成盐值的工厂
 * @author Pristine Xu
 * @date 2022/3/23 17:51
 * @description:
 */
public class SaltFactory {

    /**
     * 盐的长度
     */
    private final static int LENGTH = 6;

    /**
     * 生成随机盐，默认6位
     * @return
     */
    public static byte[] randomSalt() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.generateSeed(LENGTH);
    }

    /**
     * 生成随机盐，指定位数
     * @return
     */
    public static byte[] randomSalt(int length) {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.generateSeed(length);
    }

    /**
     * 根据参考值生成固定的盐
     * @param referenceValue 参考值，一般为密钥
     * @param length 盐的长度
     * @return
     */
    public static byte[] generateFixedSalt(String referenceValue, int length) throws UnsupportedEncodingException {
        String ivVal = DigestUtil.md5(referenceValue, "utf-8").trim();
        StringBuilder md5IV = new StringBuilder(ivVal);
        int ivLength = md5IV.length();
        if (ivLength >= length) {
            return md5IV.substring(0, length).getBytes("utf-8");
        }
        int supplySize = length - ivLength;
        for (int i=0; i<supplySize; i++) {
            md5IV.append("0");
        }
        return md5IV.toString().getBytes("utf-8");
    }


}
