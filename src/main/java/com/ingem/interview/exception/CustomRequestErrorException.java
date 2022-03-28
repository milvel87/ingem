package com.ingem.interview.exception;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public class CustomRequestErrorException extends NestedRuntimeException {

    private final HttpStatus status;

    @Nullable
    private final String reason;

    @Nullable
    private final String attribute;

    public CustomRequestErrorException(String msg, HttpStatus status, @Nullable String attribute, @Nullable String reason) {
        super(msg);
        this.status = status;
        this.reason = reason;
        this.attribute = attribute;
    }

    public CustomRequestErrorException(String msg, Throwable cause, HttpStatus status, @Nullable String reason, @Nullable String attribute) {
        super(msg, cause);
        this.status = status;
        this.reason = reason;
        this.attribute = attribute;
    }

    public String getAttribute() {
        return this.attribute;
    }

    public String getReason() {
        return this.reason;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}
