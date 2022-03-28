package com.gitee.pristine.sample.config;

import com.gitee.pristine.autoconfigure.conf.ExpandPoint;
import com.gitee.pristine.autoconfigure.conf.ShieldConfiguration;
import com.gitee.pristine.sample.expand.DemoDesensitiser;
import com.gitee.pristine.sample.expand.DemoPropertyConverter;
import com.gitee.pristine.sample.expand.DemoPropertyListener;
import org.springframework.context.annotation.Configuration;

/**
 * Shield提供拓展配置
 * @author Pristine Xu
 */
@Configuration
public class DemoShieldConfig extends ShieldConfiguration {

    /**
     * 拓展配置
     * @param expandPoint 拓展点
     */
    @Override
    public void config(ExpandPoint expandPoint) {
        // 注册自定义属性脱敏器，
        // 一旦注册了自定义属性脱敏器，则本脱敏器将会直接生效直接替换配置的脱敏器
        // 也就是说，shield.algorithm配置将会失效
        expandPoint.registerDesensitiser(new DemoDesensitiser());

        // 添加自定义的属性转换器
        expandPoint.addConverter(new DemoPropertyConverter());

        // 添加自定义属性解析监听器
        expandPoint.addListener(new DemoPropertyListener());

        // 设置风险提示关键词，以逗号隔开，默认值 ‘password,secret’，多种配置的keywords则会进行合并
        expandPoint.setRiskingKeywords("demo_password,demo_secret");
    }

}
