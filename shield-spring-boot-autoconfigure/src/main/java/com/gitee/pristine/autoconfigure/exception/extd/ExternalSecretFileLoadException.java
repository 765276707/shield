package com.gitee.pristine.autoconfigure.exception.extd;

import com.gitee.pristine.autoconfigure.exception.ShieldException;

/**
 * 外部秘钥配置加载异常
 * @author xzb
 */
public class ExternalSecretFileLoadException extends ShieldException {

    public ExternalSecretFileLoadException(String message) {
        super(message);
    }

    public ExternalSecretFileLoadException(String message, Object args) {
        super(message, args);
    }

}
