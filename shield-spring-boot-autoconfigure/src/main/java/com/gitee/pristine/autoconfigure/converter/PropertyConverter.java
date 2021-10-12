package com.gitee.pristine.autoconfigure.converter;

import com.gitee.pristine.autoconfigure.proxy.DesensitiserProxy;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

public interface PropertyConverter {

    /**
     * 指定当前要处理的 PropertySource
     * @param source
     * @return
     */
    default boolean invoke(PropertySource<?> source) {
        return true;
    }

    default void convertSource(PropertySource<?> currentSource,
                              DesensitiserProxy desensitiserProxy,
                              MutablePropertySources originSources,
                              PropertyConverter converterChain) {
        // 确认是否指向
        if (invoke(currentSource)) {
            this.convertSourceInternal(currentSource, desensitiserProxy, originSources);
        }

        // 执行下个转换器
        converterChain.convertSource(currentSource, desensitiserProxy, originSources, converterChain);
    }


    /**
     * 处理 PropertySource
     * @param currentSource
     * @param desensitiserProxy
     */
    void convertSourceInternal(PropertySource<?> currentSource,
                              DesensitiserProxy desensitiserProxy,
                              MutablePropertySources originSources);

}
