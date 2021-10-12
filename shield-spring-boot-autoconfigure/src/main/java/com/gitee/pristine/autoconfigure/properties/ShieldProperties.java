package com.gitee.pristine.autoconfigure.properties;

import com.gitee.pristine.autoconfigure.constant.SecretOrigin;
import com.gitee.pristine.autoconfigure.constant.ShieldAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "shield")
public class ShieldProperties {

    // 算法类型
    private ShieldAlgorithm algorithm = ShieldAlgorithm.AES;

    // 秘钥来源
    private SecretOrigin secretOrigin = SecretOrigin.APPLICATION_CONF_FILE;

    // 外部存储秘钥的文件，支持properties格式
    private String externalSecretFile = "";

    // 加密前缀标识
    private String prefix = "SED[";

    // 加密后缀标识
    private String suffix = "]";

    // 秘钥
    private String secret = "";

    // 是否输出加密属性详情
    private Boolean enableDetails = false;

    // 是否开启风险预警
    private Boolean enableRisking = false;

    // 风险预警关键词，以逗号隔开
    private String riskingKeywords = "password,secret";

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
}
