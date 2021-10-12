package com.gitee.pristine.sample.expand;

import com.gitee.pristine.autoconfigure.converter.PropertyConverter;
import com.gitee.pristine.autoconfigure.proxy.DesensitiserProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * 自定义属性转换器
 * @author xzb
 */
public class DemoPropertyConverter implements PropertyConverter {

    private final Logger log = LoggerFactory.getLogger(DemoPropertyConverter.class);

    @Override
    public boolean invoke(PropertySource<?> source) {
        // 返回true，才会执行 convertSourceInternal 方法，可按照需求自行判断
        return true;
    }

    @Override
    public void convertSourceInternal(PropertySource<?> currentSource, DesensitiserProxy desensitiserProxy, MutablePropertySources originSources) {
        log.info("自定义属性转换器生效了......");
    }

}
