package com.gitee.pristine.autoconfigure.factory;

import com.gitee.pristine.autoconfigure.util.DigestUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 偏移量工厂
 * @author xzb
 */
public class IvParameterFactory {

    // 偏移量存储池
    private final static Map<String, String> ivPool = new ConcurrentHashMap<>(1);

    /**
     * 获取IV值
     * @param secret 秘钥
     * @return
     */
    public static synchronized String getIvParameter(String secret, int length) {
        // 从缓存中获取
        String iv = ivPool.get(secret);
        if (iv == null) {
            // 创建一个iv
            String genIV = generateIvParameter(secret, length);
            ivPool.put(secret, genIV);
            iv = genIV;
        }
        return iv;
    }


    /**
     * 随机生成指定16长度的偏移量
     * @return
     */
    private static String generateIvParameter(String secret, int length) {
        String ivVal = DigestUtil.md5(secret, "utf-8").trim();
        StringBuilder md5IV = new StringBuilder(ivVal);
        int ivLength = md5IV.length();
        if (ivLength >= length) {
            return md5IV.substring(0, length);
        }
        int supplySize = length - ivLength;
        for (int i=0; i<supplySize; i++) {
            md5IV.append("0");
        }
        return md5IV.toString();
    }

}
