package com.gitee.pristine.autoconfigure.exception;

/**
 * 算法异常
 * @author xzb
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
