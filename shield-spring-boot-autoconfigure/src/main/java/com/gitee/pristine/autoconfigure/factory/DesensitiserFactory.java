package com.gitee.pristine.autoconfigure.factory;

import com.gitee.pristine.autoconfigure.constant.ShieldAlgorithm;
import com.gitee.pristine.autoconfigure.desensitiser.PropertyDesensitiser;
import com.gitee.pristine.autoconfigure.exception.ShieldException;
import java.lang.reflect.Constructor;

/**
 * 脱敏器工厂
 * @author xzbcode
 */
public class DesensitiserFactory {

    /**
     * 生成内置的脱敏器
     * @param algorithm
     * @return
     */
    public static PropertyDesensitiser delegate(ShieldAlgorithm algorithm) {
        if (algorithm == null) {
            throw new IllegalArgumentException("未配置属性加密算法类型");
        }

        String clazzRef = algorithm.getValue();
        try {
            Class<?> algClazz = Class.forName(clazzRef);
            Constructor<?> constructor = algClazz.getDeclaredConstructor();
            return (PropertyDesensitiser) constructor.newInstance();
        }
        catch (ClassNotFoundException e) {
            throw new ShieldException(" [%s] can not be found, check encoder class path or it exists.", clazzRef);
        }
        catch (Exception e) {
            throw new ShieldException("DesensitizeEncoder bean init failure.", e);
        }
    }

}
