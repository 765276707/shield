package com.gitee.pristine.autoconfigure.exception;

public class ExternalSecretFileLoadException extends ShieldException {

    public ExternalSecretFileLoadException(String message) {
        super(message);
    }

    public ExternalSecretFileLoadException(String message, Object args) {
        super(message, args);
    }

}
