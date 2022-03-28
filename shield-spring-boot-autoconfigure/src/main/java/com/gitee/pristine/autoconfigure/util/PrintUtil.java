package com.gitee.pristine.autoconfigure.util;

import java.util.List;

/**
 * 打印工具类
 * @author Pristine Xu
 * @date 2022/3/23 17:51
 * @description:
 */
public class PrintUtil {

    /**
     * 打印消息
     * @param message
     * @param args
     */
    public static void println(String message, Object ... args) {
        System.out.println(String.format(message, args));
    }

    /**
     * 打印详情
     * @param messages
     */
    public static void printDetails(List<String> messages) {
        println("");
        for (String message : messages) {
            println(message);
        }
        println("");
    }

}
