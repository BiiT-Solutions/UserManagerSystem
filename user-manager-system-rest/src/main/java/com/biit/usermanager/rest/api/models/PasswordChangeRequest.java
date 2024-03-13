package com.biit.usermanager.rest.api.models;

public class PasswordChangeRequest {
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
