package com.gitee.pristine.sample;

import com.gitee.pristine.autoconfigure.desensitiser.PropertyDesensitiser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ShieldSpringBootSampleApplicationTests {

    @Resource
    PropertyDesensitiser propertyDesensitiser;

    @Test
    void contextLoads() {
        System.out.println(propertyDesensitiser.encode("demo1 param value"));
        System.out.println(propertyDesensitiser.encode("demo2 param value"));
        System.out.println(propertyDesensitiser.encode("demo3 param value"));
    }

}
