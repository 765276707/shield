package com.gitee.pristine.sample.config;

import com.gitee.pristine.autoconfigure.conf.ExpandPoint;
import com.gitee.pristine.autoconfigure.conf.ShieldConfiguration;
import com.gitee.pristine.sample.expand.DemoDesensitiser;
import com.gitee.pristine.sample.expand.DemoPropertyConverter;
import com.gitee.pristine.sample.expand.DemoPropertyListener;
import org.springframework.context.annotation.Configuration;

/**
 * Shield提供拓展配置
 * @author xzb
 */
@Configuration
public class DemoShieldConfig extends ShieldConfiguration {

    @Override
    public void config(ExpandPoint expandPoint) {
//        expandPoint.registerDesensitiser(new DemoDesensitiser()) // 自定义脱敏器
//
//                // 添加自定义的属性转换器
//                .addConverter(new DemoPropertyConverter())
//
//                // 添加自定义属性解析监听器
//                .addListener(new DemoPropertyListener())
//
//                // 设置风险提示关键词，以逗号隔开，默认值 ‘password,secret’，
//                // 覆盖优先级： Java类配置 > 配置文件 > 默认配置
//                .setRiskingKeywords("demo_password,demo_secret");
    }

}
