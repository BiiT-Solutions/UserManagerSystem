package com.biit.usermanager.rest.api.models;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public class PasswordChangeRequest {

    @NotBlank(message = "Password is not set correctly.")
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
