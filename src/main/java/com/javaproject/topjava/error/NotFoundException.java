package com.javaproject.topjava.error;

import javax.persistence.EntityNotFoundException;

public class NotFoundException extends EntityNotFoundException {
    public NotFoundException(String message) {
        super(message);
    }
}
