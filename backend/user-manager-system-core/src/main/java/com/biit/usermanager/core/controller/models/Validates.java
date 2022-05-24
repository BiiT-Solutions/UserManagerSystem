package com.biit.usermanager.core.controller.models;


import com.biit.usermanager.core.exceptions.ValidateBadRequestException;

public interface Validates<T> {
    void validate(T dto) throws ValidateBadRequestException;
}
