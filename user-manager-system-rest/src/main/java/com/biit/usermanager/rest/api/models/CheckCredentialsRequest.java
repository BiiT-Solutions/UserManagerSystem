package com.biit.usermanager.rest.api.models;

import jakarta.validation.constraints.NotBlank;

public class CheckCredentialsRequest {

    private String email;

    private String username;

    @NotBlank
    private String password;

    public CheckCredentialsRequest() {
    }

    public CheckCredentialsRequest(String username, String email, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
