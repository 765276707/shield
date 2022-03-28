package com.gitee.pristine.sample.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 测试配置属性 2
 * @author Pristine Xu
 * @description: @Component + @Value模式
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
}
