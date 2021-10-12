package com.gitee.pristine.autoconfigure.constant;

public enum ShieldAlgorithm {

    AES("com.gitee.pristine.autoconfigure.desensitiser.impl.AesPropertyDesensitiser"),
    DES3("com.gitee.pristine.autoconfigure.desensitiser.impl.Des3PropertyDesensitiser");

    private String value;

    ShieldAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
