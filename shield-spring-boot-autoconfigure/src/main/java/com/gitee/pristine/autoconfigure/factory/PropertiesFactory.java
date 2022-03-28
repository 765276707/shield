package com.gitee.pristine.autoconfigure.factory;

import com.gitee.pristine.autoconfigure.constant.Constant;
import com.gitee.pristine.autoconfigure.exception.extd.ConfigValueParseException;
import com.gitee.pristine.autoconfigure.properties.ShieldProperties;
import com.gitee.pristine.autoconfigure.util.ReflectUtil;
import com.gitee.pristine.autoconfigure.util.SpellUtil;
import org.springframework.core.env.ConfigurableEnvironment;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 属性值工厂
 * @author Pristine Xu
 */
public class PropertiesFactory {

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
                String propName = convertConfPropName(fieldName,  Constant.CONFIG_PROPERTIES_PREFIX);
                Class<?> type = field.getType();
                Object property = environment.getProperty(propName, type);
                // 如果属性有额外设定，则重新赋值
                if (property != null) {
                    ReflectUtil.setFieldValue(field, shieldProperties, property);
                }
            }
        } catch (IllegalAccessException e) {
            throw new ConfigValueParseException(e.getMessage(), e);
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
        return fixedPrefix + SpellUtil.camelToLine(objectFieldName);
    }

}
