package com.javaproject.topjava.error;

import com.javaproject.topjava.error.AppException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

public class NotAllowedException extends AppException {
    public NotAllowedException(String msg) {
        super(HttpStatus.FORBIDDEN, msg, ErrorAttributeOptions.of(MESSAGE));
    }
}
