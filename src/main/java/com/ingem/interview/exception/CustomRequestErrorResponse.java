package com.ingem.interview.exception;

import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class CustomRequestErrorResponse {

    Map<String, List<String>> errors;
    private Date timestamp;
    private int status;

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
