package com.project.agroworldapp.chatbot;

public interface ChatBotListener {
    void onError(String message);

    void onResponse(String reply);
}

