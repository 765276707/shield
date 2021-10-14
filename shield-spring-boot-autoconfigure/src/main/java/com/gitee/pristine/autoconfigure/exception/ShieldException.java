package com.gitee.pristine.autoconfigure.exception;

/**
 * Shield异常
 * @author xzb
 */
public class ShieldException extends RuntimeException {

    public ShieldException() {
    }

    public ShieldException(String message) {
    }

    public ShieldException(String message, Object args) {
        super(String.format(message, args));
    }

    public ShieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShieldException(Throwable cause) {
        super(cause);
    }

    public ShieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
