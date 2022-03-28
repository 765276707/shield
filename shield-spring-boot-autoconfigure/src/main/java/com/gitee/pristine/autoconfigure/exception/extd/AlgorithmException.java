package com.gitee.pristine.autoconfigure.exception.extd;

import com.gitee.pristine.autoconfigure.exception.ShieldException;

/**
 * 算法异常
 * @author Pristine Xu
 */
public class AlgorithmException extends ShieldException {

    public AlgorithmException() {
    }

    public AlgorithmException(String message, Object... args) {
        super(message, args);
    }

    public AlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }

}
