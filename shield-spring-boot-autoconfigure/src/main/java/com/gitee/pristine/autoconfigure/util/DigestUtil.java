package com.gitee.pristine.autoconfigure.util;

import com.gitee.pristine.autoconfigure.exception.extd.AlgorithmException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 签名工具类
 * @author Pristine Xu
 */
public class DigestUtil {

    /**
     * 速度：MD5 > SHA-1 > SHA-256
     * 安全性：MD5 < SHA-1 << SHA-256
     * 哈希值长度：MD5(128bit) < SHA-1(160bit) < SHA-256(256bit)
     */
    private final static String ALG_MD5 = "MD5";
    private final static String ALG_SHA1 = "SHA1";
    private final static String ALG_SHA256 = "SHA256";


    /**
     * md5 签名
     * @param origin 签名内容
     * @param charset 字符集
     * @return
     * @throws AlgorithmException
     */
    public static String md5(String origin, String charset) throws AlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance(ALG_MD5);
            return HexUtil.bytesToHexStr(md.digest(origin.getBytes(charset)));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }


    /**
     * sha1 签名
     * @param origin 签名内容
     * @param charset 字符集
     * @return
     * @throws AlgorithmException
     */
    public static String sha1(String origin, String charset) throws AlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance(ALG_SHA1);
            return HexUtil.bytesToHexStr(md.digest(origin.getBytes(charset)));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }


    /**
     * sha256 签名
     * @param origin 签名内容
     * @param charset 字符集
     * @return
     * @throws AlgorithmException
     */
    public static String sha256(String origin, String charset) throws AlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance(ALG_SHA256);
            // 正确的写法
            return HexUtil.bytesToHexStr(md.digest(origin.getBytes(charset)));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new AlgorithmException(e.getMessage(), e);
        }
    }
}
