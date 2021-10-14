package com.gitee.pristine.autoconfigure.desensitiser;

/**
 * 抽象脱敏器
 * @author xzb
 */
public abstract class AbstractPropertyDesensitiser implements PropertyDesensitiser {

    protected String secret;

    @Override
    public void setSecret(String secret) {
        this.secret = secret;
    }

}

