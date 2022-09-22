package com.biit.usermanager.core.controller.models;


import com.biit.server.exceptions.ValidateBadRequestException;

public interface Validates<T> {
    void validate(T dto) throws ValidateBadRequestException;
}
