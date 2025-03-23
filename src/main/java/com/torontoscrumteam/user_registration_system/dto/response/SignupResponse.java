package com.torontoscrumteam.user_registration_system.dto.response;

public class SignupResponse {

    private boolean success;
    private String message;

    // Default constructor (recommended)
    public SignupResponse() {
    }

    public SignupResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
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
}