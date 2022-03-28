package com.gitee.pristine.autoconfigure.util;

import java.util.Locale;

/**
 * Hex工具类
 * @author Pristine Xu
 * @date 2022/3/22 15:10
 * @description:
 */
public class HexUtil {

    /**
     * 16进制的hex字符串转byte[]
     * @param src 字符串参数
     * @return
     */
    public static byte[] hexStrToBytes(String src) {
        /*对输入值进行规范化整理*/
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);
        //处理值初始化
        int m=0,n=0;
        //计算长度
        int iLen = src.length() / 2;
        //分配存储空间
        byte[] ret = new byte[iLen];
        for (int i = 0; i < iLen; i++){
            m=i*2+1;
            n=m+1;
            ret[i] = (byte)(Integer.decode("0x"+ src.substring(i*2, m) + src.substring(m,n)) & 0xFF);
        }
        return ret;
    }

    /**
     * byte[]转16进制的hex字符串
     * @param src byte数组参数
     * @return
     */
    public static String bytesToHexStr(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
