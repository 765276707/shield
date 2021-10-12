package com.gitee.pristine.autoconfigure.factory;

import com.gitee.pristine.autoconfigure.exception.ShieldException;
import com.gitee.pristine.autoconfigure.properties.ShieldProperties;
import com.gitee.pristine.autoconfigure.util.ReflectUtil;
import org.springframework.core.env.ConfigurableEnvironment;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 属性值工厂
 * @author xzb
 */
public class PropertiesFactory {

    // 大写字母正则编译
    private static Pattern capitalPattern = Pattern.compile("[A-Z]");
    private final static String SHIELD_PREFIX = "shield";

    /**
     * 从环境中解析 ShieldProperties 的值
     * @param environment 当前项目环境
     * @return
     */
    public static ShieldProperties loadShieldProperties(ConfigurableEnvironment environment)  {
        // 通过反射获取 ShieldProperties 所有属性值反驼峰模式名称
        ShieldProperties shieldProperties = new ShieldProperties();
        List<Field> fields = ReflectUtil.getFields(ShieldProperties.class);
        try {
            for (Field field : fields) {
                String fieldName = field.getName();
                String propName = convertConfPropName(fieldName, SHIELD_PREFIX);
                Class<?> type = field.getType();
                Object property = environment.getProperty(propName, type);
                // 如果属性有额外设定，则重新赋值
                if (property != null) {
                    ReflectUtil.setFieldValue(field, shieldProperties, property);
                }
            }
        } catch (IllegalAccessException e) {
            throw new ShieldException(e.getMessage(), e);
        }
        return shieldProperties;
    }

    /**
     * 对象属性名转成配置属性名
     * @param objectFieldName 对象属性名
     * @param prefix 固定前缀
     * @return
     */
    private static String convertConfPropName(String objectFieldName, String prefix) {
        String fixedPrefix = prefix + ".";
        return fixedPrefix + camelToLine(objectFieldName, "-");
    }


    /**
     * 驼峰转换
     * 示例： userName -> user-name
     * @param value 属性值
     * @param splitChar 分隔符
     * @return
     */
    private static String camelToLine(String value, String splitChar) {
        Matcher matcher = capitalPattern.matcher(value);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, splitChar + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

}
