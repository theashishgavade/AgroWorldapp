package com.project.agroworldapp.chatbot;

public class ChatBotModel {
    // Type 0 for sent, 1 for received
    private final boolean type;
    // Message content
    private String message;

    public ChatBotModel(boolean type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getType() {
        return this.type;
    }

}
