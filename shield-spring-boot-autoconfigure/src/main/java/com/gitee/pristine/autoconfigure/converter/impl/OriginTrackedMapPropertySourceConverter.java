package com.gitee.pristine.autoconfigure.converter.impl;

import com.gitee.pristine.autoconfigure.converter.PropertyConverter;
import com.gitee.pristine.autoconfigure.proxy.DesensitiserProxy;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 转换 OriginTrackedMapPropertySource
 * @author xzb
 */
public class OriginTrackedMapPropertySourceConverter implements PropertyConverter {

    @Override
    public boolean invoke(PropertySource<?> source) {
        return source instanceof OriginTrackedMapPropertySource;
    }

    @Override
    public void convertSourceInternal(PropertySource<?> currentSource, DesensitiserProxy desensitiserProxy, MutablePropertySources originSources) {
        // 解析 OriginTrackedMapPropertySource
        OriginTrackedMapPropertySource otmpSource = (OriginTrackedMapPropertySource) currentSource;
        Map<String, Object> sourceMap = otmpSource.getSource();

        Map<String, Object> newSourceMap = new HashMap<>();
        sourceMap.forEach((key, value) -> {
            Object newVal = desensitiserProxy.decode(key, value);
            newSourceMap.put(key, newVal);
        });

        // 重新包装一个 OriginTrackedMapPropertySource 覆盖
        originSources.addLast(new OriginTrackedMapPropertySource(otmpSource.getName(), newSourceMap));
    }



}
