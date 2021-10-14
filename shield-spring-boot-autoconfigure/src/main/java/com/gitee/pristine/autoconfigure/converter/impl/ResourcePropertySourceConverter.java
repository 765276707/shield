package com.gitee.pristine.autoconfigure.converter.impl;

import com.gitee.pristine.autoconfigure.converter.PropertyConverter;
import com.gitee.pristine.autoconfigure.proxy.DesensitiserProxy;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.util.Map;

/**
 * 转换 ResourcePropertySource
 * @author xzb
 */
public class ResourcePropertySourceConverter implements PropertyConverter {

    @Override
    public boolean invoke(PropertySource<?> source) {
        return source instanceof ResourcePropertySource;
    }

    @Override
    public void convertSourceInternal(PropertySource<?> currentSource, DesensitiserProxy desensitiserProxy, MutablePropertySources originSources) {

        // 解析 ResourcePropertySource
        ResourcePropertySource rpSource = (ResourcePropertySource) currentSource;
        Map<String, Object> sourceMap = rpSource.getSource();
        sourceMap.forEach((key, value) -> {
            Object newVal = desensitiserProxy.decode(key, value);
            if (value!=null && !value.equals(newVal)) {
                // 重新赋值
                sourceMap.put(key, newVal);
            }
        });

    }
}
