package com.example.ai.model;

/**
 * Represents a user's chat message request.
 */
public class ChatRequest {

    private String message;
    private String systemPrompt;

    public ChatRequest() {
    }

    public ChatRequest(String message) {
        this.message = message;
    }

    public ChatRequest(String message, String systemPrompt) {
        this.message = message;
        this.systemPrompt = systemPrompt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }
}

