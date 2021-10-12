package com.gitee.pristine.sample.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 测试配置属性 2
 * @author xzb
 */
@Component
public class DemoConfig2 {

    @Value("${demo2.param}")
    private String param;

    @Override
    public String toString() {
        return "DemoConfig2{" +
                "param='" + param + '\'' +
                '}';
    }

    @PostConstruct
    public void test() {
        System.out.println("DemoConfig2: " + this.toString());
    }
}
