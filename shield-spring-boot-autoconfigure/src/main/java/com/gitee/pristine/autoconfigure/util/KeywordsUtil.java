package com.gitee.pristine.autoconfigure.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 关键词工具类
 * @author Pristine Xu
 * @date 2022/3/22 15:10
 * @description:
 */
public class KeywordsUtil {

    /**
     * 拆分关键词
     * @param keyword 关键词
     * @return
     */
    public static Set<String> splitToSet(String keyword) {
        String[] splitArr = keyword.split(",");
        if (splitArr.length == 0) {
            return new HashSet<>(0);
        }
        return Arrays.stream(splitArr)
                .collect(Collectors.toSet());
    }

    /**
     * 合并关键词
     * @param keywords 关键词
     * @return
     */
    public static String join(String ... keywords) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i] != null && !"".equals(keywords[i])) {
                String temp = trimSuffixAndPrefixSplitChar(keywords[i]);
                if (i != keywords.length-1) {
                    builder.append(temp).append(",");
                } else {
                    builder.append(temp);
                }
            }
        }
        return builder.toString();
    }

    /**
     * 去首位分隔符
     * @param val 字符串参数
     * @return
     */
    private static String trimSuffixAndPrefixSplitChar(String val) {
        if (val.startsWith(",")) {
            val = val.substring(1);
        }
        if (val.endsWith(",")) {
            val = val.substring(0, val.length()-1);
        }
        return val;
    }

}
