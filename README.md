# shield <a href='https://gitee.com/xu_zhibin/shield/stargazers'><img src='https://gitee.com/xu_zhibin/shield/badge/star.svg?theme=white' alt='star'></img></a> <a href='https://gitee.com/xu_zhibin/shield/members'><img src='https://gitee.com/xu_zhibin/shield/badge/fork.svg?theme=dark' alt='fork'></img></a>
![Shield](https://gitee.com/xu_zhibin/shield/raw/master/docs/shield.png)

#### 1.框架介绍 
Shield是一个基于SpringBoot 2.x的配置脱敏框架（SpringBoot 3.x正在适配中），内置了AES、DES3、SM4等多种加密算法（支持自定义的脱敏器算法），提供监听器功能，已支持详情输出、风险提示等功能，提供了丰富的拓展点。

#### 2.环境要求
1. JDK版本：JDK8+
2. Maven版本：3.6.2+
3. SpringBoot版本：2.x

#### 3.安装教程

支持Maven依赖安装，如果中央仓库（后续会上传）没有，则需要打包安装在本地仓库（版本号为最新版本号）：

~~~xml
<!-- 导入 starter 包-->
<dependency>
    <groupId>com.gitee.pristine</groupId>
    <artifactId>shield-spring-boot-starter</artifactId>
    <version>1.1.6</version>
</dependency>
~~~

#### 4.内置加密算法
| 算法 | 填充模式                | 密钥长度(字节) | 安全性 | 是否默认 |
| ---- | ----------------------- | -------------- | ------ | -------- |
| AES  | AES/CBC/PKCS5Padding    | 16 / 24 / 32   | 很高   | 是       |
| DES3 | DESede/CBC/PKCS5Padding | 24 / 32        | 高     | 否       |
| IDEA | IDEA/ECB/PKCS5Padding   | > 0            | 高     | 否       |
| PBE  |                         | > 0            | 一般   | 否       |
| RC4  |                         | > 0            | 一般   | 否       |
| SM4  | SM4/ECB/PKCS5Padding    | 16             | 高     | 否       |

#### 5.支持的属性读取方式
|      | 获取配置方式                                   | 示例                                       |
| ---- | ---------------------------------------------- | ------------------------------------------ |
| 1    | @Component + @ConfigurationProperties          | com.gitee.pristine.sample.demo.DemoConfig1 |
| 2    | @Component + @Value                            | com.gitee.pristine.sample.demo.DemoConfig2 |
| 3    | @PropertySource 读取 自定义配置文件.properties | com.gitee.pristine.sample.demo.DemoConfig3 |
| 4    | 从ConfigurableEnvironment中getProperty()获取   |                                            |

#### 6.使用说明

1.  添加 shield-spring-boot-starter 依赖

2.  配置加密密钥，支持多种方式
    - 配置文件设置（不建议生产环境使用，可在开发时使用方便调试）
    ~~~yaml
    # shield 示例配置
    shield:
      # 秘钥值
      secret: 'ABCDEFGH12345678'
    ~~~
    
    - 启动时候，添加启动参数（建议）
    ~~~shell script
    java -jar project.jar -Dshield.secret='xxxxx'
    ~~~
    
    - 项目外配置密钥文件，支持properties格式，启动成功后安全移除该文件即可或开启读取密钥后自动删除（建议）
    ~~~yaml
    # shield 示例配置
    shield:
      # 密钥文件来源
      secret-origin: external_conf_file
      # 密钥文件路径，当前 secret-origin 配置为 external_conf_file 时该配置方才有效，请确保该文件路径可以正常访问
      external-secret-file: 'L:\\secret.sed'
      # 是否在读取完密钥文件后删除该文件，默认：不删除
      delete-external-secret-file-after-read: false
    ~~~
    
3.  对需要脱敏的属性值，生成加密后的值
    - 手动初始化来获取（推荐使用）
    ~~~java
    public class EncryptData {
    
        /**
         * 生成所需要的加密数据，放入配置文件中
         * @param args 参数
         */
        public static void main(String[] args) {
            // 创建脱敏器，可以根据不同的算法生成
            PropertyDesensitiser desensitiser = DesensitiserFactory.delegate(ShieldAlgorithm.AES);
    
            // 设置初始化参数（必须进行初始化）
            String secret = "ABCDEFGH12345678";
            String charset = "UTF-8";
            ConfigParam configParam = new ConfigParam(secret, charset);
            desensitiser.setConfigParam(configParam);
    
            // 检测密钥是否合法（可选）
            desensitiser.checkSecret(secret);
    
            // 加密数据
            System.out.println(desensitiser.encode("demo1 param value"));
            System.out.println(desensitiser.encode("demo2 param value"));
            System.out.println(desensitiser.encode("demo3 param value"));
        }
    
    }
    ~~~
    
    - 通过Spring自动注入（使用这种方式的时候，要注意如果改变密钥或算法，则项目中原来已配置的加密后的数据将解密失败异常，
    建议是清理已配置加密的属性值）
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

#### 7.完整示例配置
~~~yaml
# shield 示例配置
shield:
  # 脱敏器算法
  algorithm: aes
  # 秘钥值
  secret: 'ABCDEFGH123'
  # 密钥文件来源
  secret-origin: external_conf_file
  # 密钥文件路径，当前 secret-origin 配置为 external_conf_file 时该配置方才有效，请确保该文件路径可以正常访问
  external-secret-file: 'L:\\secret.sed'
  # 是否在读取完密钥文件后删除该文件
  delete-external-secret-file-after-read: false
  # 开启风险提示功能
  enable-risking: true
  # 风险提示关键词，如果 enable-risking=true，则该项必须进行配置
  risking-keywords: 'password,secret,pwd'
  # 开启输出解密详情
  enable-details: true
  # 加密标识前缀和后缀
  prefix: 'SED['
  suffix: ']'
~~~

#### 8.框架拓展
- 支持注册自定义的脱敏器，继承 `AbstractPropertyDesensitiser` 抽象类或实现 `PropertyDesensitiser` 接口 
- 支持添加自定义监听器，实现 `PropertyListener` 接口
    - onEachPropertyReadEvent(PropertyEvent e) ：每次读取一个配置属性值时候触发
    - afterAllPropertyReadEvent() ：读取完全部配置属性值时触发，只在最后触发一次
- 支持添加自定义转换器，实现 `PropertyConverter` 接口
    
~~~java
/**
 * Shield提供拓展配置
 * @author xzb
 */
@Configuration
public class DemoShieldConfig extends ShieldConfiguration {

    @Override
    public void config(ExpandPoint expandPoint) {
        // 注册自定义属性脱敏器，
        // 一旦注册了自定义属性脱敏器，则本脱敏器将会直接生效直接替换配置的脱敏器
        // 也就是说，shield.algorithm 配置项将会失效
        expandPoint.registerDesensitiser(new DemoDesensitiser());

        // 添加自定义的属性转换器
        expandPoint.addConverter(new DemoPropertyConverter());

        // 添加自定义属性解析监听器
        expandPoint.addListener(new DemoPropertyListener());

        // 设置风险提示关键词，以逗号隔开，默认值 ‘password,secret’，多种配置的keywords则会进行合并
        expandPoint.setRiskingKeywords("demo_password,demo_secret");
    }

}
~~~

#### 9.参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
5.  如果您对项目有什么建议欢迎您留言


