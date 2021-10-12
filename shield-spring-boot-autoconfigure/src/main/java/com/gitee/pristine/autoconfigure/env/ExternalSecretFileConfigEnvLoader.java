package com.gitee.pristine.autoconfigure.env;

import com.gitee.pristine.autoconfigure.constant.SecretOrigin;
import com.gitee.pristine.autoconfigure.exception.ExternalSecretFileLoadException;
import com.gitee.pristine.autoconfigure.factory.PropertiesFactory;
import com.gitee.pristine.autoconfigure.properties.ShieldProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 外部秘钥配置加载器
 * @author xzb
 */
public class ExternalSecretFileConfigEnvLoader implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        ShieldProperties shieldProperties = this.loadShieldPropertiesFromEnvironment(environment);
        SecretOrigin secretOrigin = shieldProperties.getSecretOrigin();
        // 当配置类型为 EXTERNAL_CONF_FILE 时才进行解析操作
        if (secretOrigin == SecretOrigin.EXTERNAL_CONF_FILE) {

            String externalSecretFile = shieldProperties.getExternalSecretFile();
            try {
                File file = new File(externalSecretFile);
                PropertiesPropertySource pps = this.buildPropertiesPropertySource(new FileInputStream(file));
                environment.getPropertySources().addFirst(pps);
            } catch (IOException e) {
                throw new ExternalSecretFileLoadException(
                        "[shield] 外秘钥配置文件未找到或读取异常，请检查该配置文件路径或格式是否正确.", e);
            }

        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    /**
     * 从环境中读取 ShieldProperties 的值
     * @return
     */
    private ShieldProperties loadShieldPropertiesFromEnvironment(ConfigurableEnvironment environment) {
        return PropertiesFactory.loadShieldProperties(environment);
    }

    /**
     * 构建 PropertiesPropertySource
     * @param is
     * @return
     * @throws IOException
     */
    private PropertiesPropertySource buildPropertiesPropertySource(InputStream is) throws IOException {
        Properties properties = new Properties();
        properties.load(is);
        return new PropertiesPropertySource("externalFilePropertySource", properties);
    }

}
