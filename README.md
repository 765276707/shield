# shield <a href='https://gitee.com/xu_zhibin/shield/stargazers'><img src='https://gitee.com/xu_zhibin/shield/badge/star.svg?theme=white' alt='star'></img></a> <a href='https://gitee.com/xu_zhibin/shield/members'><img src='https://gitee.com/xu_zhibin/shield/badge/fork.svg?theme=dark' alt='fork'></img></a>
[Shield] (https://gitee.com/xu_zhibin/shield/blob/master/docs/shield.png)

#### 1.框架介绍 
shield是一个SpringBoot配置加密框架，内置了多种加密算法，支持风险提示等功能，提供了多个拓展点。

#### 2.适用场景
对于SpringBoot的重要配置属性进行加密，保证敏感数据信息不被泄露。

#### 3.安装教程

目前支持Maven依赖安装， 如果中央仓库没有，则需要打包安装在本地仓库：

~~~xml
<!-- 导入 starter 包-->
<dependency>
    <groupId>com.gitee.pristine</groupId>
    <artifactId>shield-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
~~~

#### 4.使用说明

1.  添加 shield-spring-boot-starter 依赖

2.  配置加密密钥，支持多种方式
    - 配置文件设置（不建议生产环境使用）
    ~~~yaml
    # shield 示例配置
    shield:
      # 秘钥值
      secret: 'ABCDEFGHIJKL0123'
    ~~~
    
    - 启动时候，添加启动参数（建议）
    ~~~shell script
    java -jar project.jar -Dshield.secret='xxxxx'
    ~~~
    
    - 项目外配置密钥文件，支持properties格式，启动成功后安全移除该文件即可（建议）
    ~~~yaml
    # shield 示例配置
    shield:
      # 设置为外部密钥配置文件
      secret-origin: external_conf_file
      # 密钥文件存放路径
      external-secret-file: 'H:\\secret\\secret.properties'
    ~~~
    
3.  对需要脱敏的属性值，生成加密后的值
~~~java
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
~~~

4.  对需要脱敏的属性值，添加加密标识
~~~yaml
# 参数脱敏配置
demo1:
  param: 'SED[s2onk7fwhLyLtzw4xlBMVdqNjLCqG36R9SZoroO3In8]'
demo2:
  param: 'SED[IsCk-O-Ey9lo_JvOp5jqGfmPp-ZEXJNhC5m7nGXuRLI]'
~~~

#### 5.示例配置
~~~yaml
# shield 示例配置
shield:
  # 秘钥值
  secret: 'ABCDEFGHIJKL0123'
  # 密钥文件来源
#  secret-origin: external_conf_file
#  external-secret-file: 'H:\\secret\\secret.properties'
  # 脱敏器算法
#  algorithm: aes
  # 开启风险提示功能
#  enable-risking: true
#  risking-keywords: 'password,secret,pwd'
  # 开启输出解密详情
#  enable-details: true
  # 加密标识前缀和后缀
#  prefix: 'SED['
#  suffix: ']'
~~~

#### 5.框架拓展
~~~java
/**
 * Shield提供拓展配置
 * @author xzb
 */
@Configuration
public class DemoShieldConfig extends ShieldConfiguration {

    @Override
    public void config(ExpandPoint expandPoint) {
        expandPoint.registerDesensitiser(new DemoDesensitiser()) // 自定义脱敏器

                // 添加自定义的属性转换器
                .addConverter(new DemoPropertyConverter())

                // 添加自定义属性解析监听器
                .addListener(new DemoPropertyListener())

                // 设置风险提示关键词，以逗号隔开，默认值 ‘password,secret’，
                // 覆盖优先级： Java类配置 > 配置文件 > 默认配置
                .setRiskingKeywords("demo_password,demo_secret");
    }

}
~~~

#### 6.参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


