package com.project.agroworldapp.chatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.project.agroworldapp.R;

import java.util.ArrayList;

public class ChatBotAdapter extends RecyclerView.Adapter<ChatBotAdapter.MessageViewHolder> {
    private final ArrayList<ChatBotModel> chatBotModels;

    public ChatBotAdapter(ArrayList<ChatBotModel> chatBotModels) {
        this.chatBotModels = chatBotModels;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String message = chatBotModels.get(position).getMessage();
        boolean type = chatBotModels.get(position).getType();

        if (type) {
            //If a message is sent
            holder.sentLayout.setVisibility(LinearLayout.VISIBLE);
            holder.sentText.setText(message);
            // Set visibility as GONE to remove the space taken up
            holder.receivedLayout.setVisibility(LinearLayout.GONE);
        } else {
            //Message is received
            holder.receivedLayout.setVisibility(LinearLayout.VISIBLE);
            holder.receivedText.setText(message);
            // Set visibility as GONE to remove the space taken up
            holder.sentLayout.setVisibility(LinearLayout.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatBotModels.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout sentLayout;
        private final LinearLayout receivedLayout;
        private final TextView sentText;
        private final TextView receivedText;

        public MessageViewHolder(final View itemView) {
            super(itemView);
            sentLayout = itemView.findViewById(R.id.sentLayout);
            receivedLayout = itemView.findViewById(R.id.receivedLayout);
            sentText = itemView.findViewById(R.id.sentTextView);
            receivedText = itemView.findViewById(R.id.receivedTextView);
        }
    }
}
