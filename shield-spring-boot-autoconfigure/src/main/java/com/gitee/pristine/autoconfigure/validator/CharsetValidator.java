package com.gitee.pristine.autoconfigure.validator;

/**
 * 字符集校验器
 * @author Pristine Xu
 * @date 2022/3/24 12:04
 * @description: 对字符集进行校验
 */
public class CharsetValidator {

    /**
     * 支持的字符集类型
     */
    private final static String[] ALLOWED_CHARSETS = {"UTF-8", "GBK"};

    /**
     * 判断是否支持某个字符集
     * @param charset
     * @return
     */
    public static boolean support(String charset) {
        boolean result = false;
        for (String allowedCharset : ALLOWED_CHARSETS) {
            if (allowedCharset.equalsIgnoreCase(charset)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
