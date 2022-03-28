package com.gitee.pristine.autoconfigure.env;

import com.gitee.pristine.autoconfigure.constant.Constant;
import com.gitee.pristine.autoconfigure.constant.SecretOrigin;
import com.gitee.pristine.autoconfigure.exception.extd.ExternalSecretFileLoadException;
import com.gitee.pristine.autoconfigure.factory.PropertiesFactory;
import com.gitee.pristine.autoconfigure.properties.ShieldProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.*;
import java.util.Properties;

/**
 * 外部秘钥配置加载器
 * @author Pristine Xu
 */
public class ExternalSecretFileConfigEnvLoader implements EnvironmentPostProcessor, Ordered {

    private Logger log = LoggerFactory.getLogger(ExternalSecretFileConfigEnvLoader.class);

    /**
     * PropertySource 名称
     */
    private final static String PROPERTY_SOURCE_NAME = "externalFilePropertySource";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        ShieldProperties shieldProperties = this.loadShieldPropertiesFromEnvironment(environment);
        SecretOrigin secretOrigin = shieldProperties.getSecretOrigin();
        // 当配置类型为 EXTERNAL_CONF_FILE 时才进行解析操作
        if (secretOrigin == SecretOrigin.EXTERNAL_CONF_FILE) {

            String externalSecretFile = shieldProperties.getExternalSecretFile();
            File file = new File(externalSecretFile);

            // 校验文件格式，格式必须为指定的 [.sed]
            boolean isSedFile = externalSecretFile.endsWith(".sed") || externalSecretFile.endsWith(".SED");
            if (!isSedFile) {
                throw new ExternalSecretFileLoadException("The external secret configuration file must be in SED format.");
            }

            // 加载密钥
            String secret = this.readSecretFromExternalConfFile(file);
            PropertiesPropertySource pps = this.buildPropertiesPropertySource(secret);
            environment.getPropertySources().addFirst(pps);

            // 删除密钥
            if (Boolean.TRUE.equals(shieldProperties.getDeleteExternalSecretFileAfterRead()) && file.exists()) {
                boolean isDelete = file.delete();
                if (isDelete) {
                    log.info("The external secret configuration file has been delete.");
                }
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
     * @param secret 密钥
     * @return
     */
    private PropertiesPropertySource buildPropertiesPropertySource(String secret) {
        Properties properties = new Properties();
        properties.put(Constant.CONFIG_PROPERTIES_PREFIX+".secret", secret);
        return new PropertiesPropertySource(PROPERTY_SOURCE_NAME, properties);
    }


    /**
     * 读取密钥文件的密钥值
     * @param file 密钥文件
     * @return
     * @throws IOException
     */
    private String readSecretFromExternalConfFile(File file) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            // 读取密钥
            StringBuffer stringBuffer = new StringBuffer();
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String s = null;
            while ((s = bufferedReader.readLine())!=null) {
                stringBuffer.append(s);
            }
            return stringBuffer.toString();
        }
        catch (IOException e)
        {
            throw new ExternalSecretFileLoadException(
                    "The external secret configuration file is not found or read abnormally." +
                            " Please check whether the path or format of the configuration file is correct.", e);
        }
        finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
