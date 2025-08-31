package com.inventryService.model;

public class Error_Response {
    private String message;
    private String path;

    public Error_Response(String message, String requestURI) {
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Error_Response() {
    }

}
