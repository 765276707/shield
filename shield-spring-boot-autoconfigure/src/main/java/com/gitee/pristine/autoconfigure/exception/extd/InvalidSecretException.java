package com.gitee.pristine.autoconfigure.exception.extd;

import com.gitee.pristine.autoconfigure.exception.ShieldException;

/**
 * 无效的密钥异常
 * @author Pristine Xu
 * @date 2022/3/26 20:49
 * @description: 密钥无效
 */
public class InvalidSecretException extends ShieldException {

    public InvalidSecretException() {
    }

    public InvalidSecretException(String message) {
        super(message);
    }

}
