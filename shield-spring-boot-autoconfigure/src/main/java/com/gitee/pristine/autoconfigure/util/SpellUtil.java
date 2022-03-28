package com.gitee.pristine.autoconfigure.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 拼写工具类
 * @author Pristine Xu
 * @date 2022/3/23 17:59
 * @description:
 */
public class SpellUtil {

    /**
     * 大写字母正则
     */
    private final static Pattern CAPITAL_PATTERN = Pattern.compile("[A-Z]");

    /**
     * 驼峰转换，转换成指定分隔符
     * 示例： 分隔符为[-] userName -> user-name
     *       分隔符为[.] userName -> user.name
     * @param value 属性值
     * @param splitChar 分隔符
     * @return
     */
    public static String camelTo(String value, String splitChar) {
        Matcher matcher = CAPITAL_PATTERN.matcher(value);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, splitChar + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    /**
     * 驼峰转换，默认转换成短横线
     * 示例： userName -> user-name
     * @param value 属性值
     * @return
     */
    public static String camelToLine(String value) {
        Matcher matcher = CAPITAL_PATTERN.matcher(value);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, "-" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }


}
