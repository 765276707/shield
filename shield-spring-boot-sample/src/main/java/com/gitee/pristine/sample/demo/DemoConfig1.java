package com.gitee.pristine.sample.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 测试配置属性 1
 * @author Pristine Xu
 * @description: @Component + @ConfigurationProperties模式
 */
@Component
@ConfigurationProperties(prefix = "demo1")
public class DemoConfig1 {

    private String param;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "DemoConfig{" +
                "param='" + param + '\'' +
                '}';
    }

}
