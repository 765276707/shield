package com.gitee.pristine.autoconfigure.properties;

import com.gitee.pristine.autoconfigure.constant.Constant;
import com.gitee.pristine.autoconfigure.constant.SecretOrigin;
import com.gitee.pristine.autoconfigure.constant.ShieldAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置属性
 * @author Pristine Xu
 * @date 2022/3/23 17:51
 * @description:
 */
@Component
@ConfigurationProperties(prefix = Constant.CONFIG_PROPERTIES_PREFIX)
public class ShieldProperties {

    /**
     * 算法类型
     */
    private ShieldAlgorithm algorithm = ShieldAlgorithm.AES;

    /**
     * 秘钥来源
     */
    private SecretOrigin secretOrigin = SecretOrigin.APPLICATION_CONF_FILE;

    /**
     * 外部存储秘钥的文件，支持properties格式，请确文件可以正常访问
     */
    private String externalSecretFile = "";

    /**
     * 是否要在密钥文件读取之后进行删除操作，默认：false
     */
    private Boolean deleteExternalSecretFileAfterRead = false;

    /**
     * 配置文件字符集类型，默认：UTF-8
     */
    private String charset = "UTF-8";

    /**
     * 加密前缀标识
     */
    private String prefix = "SED[";

    /**
     * 加密后缀标识
     */
    private String suffix = "]";

    /**
     * 秘钥
     */
    private String secret = "";

    /**
     * 是否输出加密属性详情
     */
    private Boolean enableDetails = false;

    /**
     * 是否开启风险预警
     */
    private Boolean enableRisking = false;

    /**
     * 风险预警关键词，以逗号隔开，如：password,secret,xxx ......
     */
    private String riskingKeywords = "";

    public ShieldAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(ShieldAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public SecretOrigin getSecretOrigin() {
        return secretOrigin;
    }

    public void setSecretOrigin(SecretOrigin secretOrigin) {
        this.secretOrigin = secretOrigin;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Boolean getEnableDetails() {
        return enableDetails;
    }

    public void setEnableDetails(Boolean enableDetails) {
        this.enableDetails = enableDetails;
    }

    public Boolean getEnableRisking() {
        return enableRisking;
    }

    public void setEnableRisking(Boolean enableRisking) {
        this.enableRisking = enableRisking;
    }

    public String getRiskingKeywords() {
        return riskingKeywords;
    }

    public void setRiskingKeywords(String riskingKeywords) {
        this.riskingKeywords = riskingKeywords;
    }

    public String getExternalSecretFile() {
        return externalSecretFile;
    }

    public void setExternalSecretFile(String externalSecretFile) {
        this.externalSecretFile = externalSecretFile;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Boolean getDeleteExternalSecretFileAfterRead() {
        return deleteExternalSecretFileAfterRead;
    }

    public void setDeleteExternalSecretFileAfterRead(Boolean deleteExternalSecretFileAfterRead) {
        this.deleteExternalSecretFileAfterRead = deleteExternalSecretFileAfterRead;
    }
}
