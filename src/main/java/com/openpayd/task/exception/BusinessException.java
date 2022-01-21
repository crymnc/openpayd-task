package com.openpayd.task.exception;

public final class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 51514312562L;

    private String code;

    public BusinessException() {
        super();
    }

    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BusinessException(final String message) {
        super(message);
    }

    public BusinessException(String code, final String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(final Throwable cause) {
        super(cause);
    }

    public String getCode(){
        return this.code;
    }
}
