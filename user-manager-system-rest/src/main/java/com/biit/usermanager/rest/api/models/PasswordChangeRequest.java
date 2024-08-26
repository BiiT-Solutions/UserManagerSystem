package com.biit.usermanager.rest.api.models;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public class PasswordChangeRequest {

    @NotBlank
    private String newPassword;

    public PasswordChangeRequest() {
    }

    public PasswordChangeRequest(String newPassword) {
        setNewPassword(newPassword);
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
