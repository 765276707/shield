package com.gitee.pristine.sample.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 测试配置属性 1
 * @author xzb
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

    @PostConstruct
    public void init() {
        System.out.println("DemoConfig: " + this.toString());
    }

}
