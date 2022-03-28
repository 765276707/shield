package com.gitee.pristine.sample.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 测试配置属性 3
 * @author Pristine Xu
 * @description: @PropertySource模式
 */
@PropertySource(value = {"classpath:demo3.properties"})
@Component
@ConfigurationProperties(prefix = "demo3")
public class DemoConfig3 {

    private String param;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "DemoConfig3{" +
                "param='" + param + '\'' +
                '}';
    }

}
