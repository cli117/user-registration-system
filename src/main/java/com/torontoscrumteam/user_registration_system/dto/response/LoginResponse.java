package com.torontoscrumteam.user_registration_system.dto.response;

public class LoginResponse {

    private boolean success;
    private String message;
    private String token; // Add token field

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Constructors
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.token = null; // Initialize token to null for failure cases
    }

    public LoginResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }

    public LoginResponse() {
    }
}