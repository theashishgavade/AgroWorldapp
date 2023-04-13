package com.project.agroworldapp.chatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.project.agroworldapp.R;

import java.util.ArrayList;

public class ChatBotAdapter extends RecyclerView.Adapter<ChatBotViewHolder> {
    private final ArrayList<ChatBotModel> chatBotModels;

    public ChatBotAdapter(ArrayList<ChatBotModel> chatBotModels) {
        this.chatBotModels = chatBotModels;
    }

    @NonNull
    @Override
    public ChatBotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new ChatBotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBotViewHolder holder, int position) {
        String message = chatBotModels.get(position).getMessage();
        boolean type = chatBotModels.get(position).getType();
        holder.bindMessageData(message, type);
    }

    @Override
    public int getItemCount() {
        return chatBotModels.size();
    }
}
