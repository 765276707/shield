package com.gitee.pristine.autoconfigure.desensitiser;

public abstract class AbstractPropertyDesensitiser implements PropertyDesensitiser {

    protected String secret;

    @Override
    public void setSecret(String secret) {
        this.secret = secret;
    }

}

