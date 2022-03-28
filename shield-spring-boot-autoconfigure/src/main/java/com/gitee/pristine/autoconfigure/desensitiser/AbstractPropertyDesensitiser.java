package com.gitee.pristine.autoconfigure.desensitiser;

/**
 * 抽象脱敏器
 * @author Pristine Xu
 */
public abstract class AbstractPropertyDesensitiser implements PropertyDesensitiser {

    /**
     * 脱敏器所需的配置参数值，包含密钥和编码类型
     */
    protected ConfigParam configParam;

    @Override
    public void setConfigParam(ConfigParam configParam) {
        this.configParam = configParam;
    }

}

