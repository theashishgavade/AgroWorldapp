package com.project.agroworld.chatbot;

public interface ChatBotListener {
    void onError(String message);

    void onResponse(String reply);
}

