package com.gitee.pristine.autoconfigure.converter;

import com.gitee.pristine.autoconfigure.converter.impl.OriginTrackedMapPropertySourceConverter;
import com.gitee.pristine.autoconfigure.converter.impl.ResourcePropertySourceConverter;
import com.gitee.pristine.autoconfigure.proxy.DesensitiserProxy;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public class PropertySourceProcessor {

    /**
     * 解析配置
     * @param additionalConverts 自定义的转换器集合
     * @param originSources 配置资源
     */
    public void process(@NonNull MutablePropertySources originSources,
                         DesensitiserProxy propertyDesensitiserProxy, List<PropertyConverter> additionalConverts) {

        for (PropertySource<?> propertySource : originSources) {
            // 构建处理器链
            ConverterChain chain = new ConverterChain(getAllConverts(additionalConverts));
            chain.convertSource(propertySource, propertyDesensitiserProxy, originSources, chain);
        }

        // 结束所有解析
        propertyDesensitiserProxy.stopPublish();
    }


    /**
     * 获取所有处理器
     * @param additionalConverts 拓展的转换器
     * @return
     */
    private List<PropertyConverter> getAllConverts(List<PropertyConverter> additionalConverts) {
        List<PropertyConverter> defaultHandlers = new ArrayList<>();
        // 默认的转换器
        defaultHandlers.add(new OriginTrackedMapPropertySourceConverter());
        defaultHandlers.add(new ResourcePropertySourceConverter());

        // 添加自定义的处理器
        if (additionalConverts!=null && !additionalConverts.isEmpty()) {
            defaultHandlers.addAll(additionalConverts);
        }
        return defaultHandlers;
    }

}
