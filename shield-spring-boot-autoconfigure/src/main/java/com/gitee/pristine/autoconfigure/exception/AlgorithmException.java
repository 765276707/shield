package com.gitee.pristine.autoconfigure.exception;

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
