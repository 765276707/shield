package com.gitee.pristine.autoconfigure.converter;

import com.gitee.pristine.autoconfigure.proxy.DesensitiserProxy;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.List;

/**
 * 转换器链
 * @author Pristine Xu
 */
public class ConverterChain implements PropertyConverter {

    private final List<PropertyConverter> handlers;
    private final int size;
    private int position;

    public ConverterChain(List<PropertyConverter> handlers) {
        this.handlers = handlers;
        this.size = handlers.size();
        this.position = 0;
    }

    @Override
    public boolean invoke(PropertySource<?> source) {
        return false;
    }

    @Override
    public void convertSource(PropertySource<?> currentSource, DesensitiserProxy desensitiserProxy, MutablePropertySources originSources, PropertyConverter converterChain) {
        if (this.position != this.size) {
            // 调用过滤链中过滤器的执行方法
            ++ this.position;
            PropertyConverter nextHandler = this.handlers.get(this.position - 1);
            nextHandler.convertSource(currentSource, desensitiserProxy, originSources, this);
        }
    }

    @Override
    public void convertSourceInternal(PropertySource<?> currentSource, DesensitiserProxy desensitiserProxy, MutablePropertySources originSources) {

    }
}
