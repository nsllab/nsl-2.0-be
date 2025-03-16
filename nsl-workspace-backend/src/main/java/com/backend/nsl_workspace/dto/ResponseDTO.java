package com.backend.nsl_workspace.dto;

/**
 * Generic Response Data Transfer Object
 */
public class ResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    // Default constructor
    public ResponseDTO() {
    }

    // Success Response
    public static <T> ResponseDTO<T> success(String message, T data) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    // Success Response without data
    public static <T> ResponseDTO<T> success(String message) {
        return success(message, null);
    }

    // Error Response
    public static <T> ResponseDTO<T> error(String message) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}