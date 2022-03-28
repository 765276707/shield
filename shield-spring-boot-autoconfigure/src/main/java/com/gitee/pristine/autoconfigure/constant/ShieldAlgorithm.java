package com.gitee.pristine.autoconfigure.constant;

/**
 * 内置加密算法
 * @author Pristine Xu
 */
public enum ShieldAlgorithm {

    /** AES */
    AES("com.gitee.pristine.autoconfigure.desensitiser.impl.AesPropertyDesensitiser"),
    /** DES3 */
    DES3("com.gitee.pristine.autoconfigure.desensitiser.impl.Des3PropertyDesensitiser"),
    /** PBE */
    PBE("com.gitee.pristine.autoconfigure.desensitiser.impl.PbePropertyDesensitiser"),
    /** RC4 */
    RC4("com.gitee.pristine.autoconfigure.desensitiser.impl.Rc4PropertyDesensitiser"),
    /** IDEA */
    IDEA("com.gitee.pristine.autoconfigure.desensitiser.impl.IdeaPropertyDesensitiner"),
    /** SM4 */
    SM4("com.gitee.pristine.autoconfigure.desensitiser.impl.Sm4PropertyDesensitiser");

    private String value;

    ShieldAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
