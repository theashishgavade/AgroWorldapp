package com.project.agroworldapp.chatbot;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.R;

public class ChatBotViewHolder extends RecyclerView.ViewHolder {
    private final LinearLayout sentLayout;
    private final LinearLayout receivedLayout;
    private final TextView sentText;
    private final TextView receivedText;

    public ChatBotViewHolder(final View itemView) {
        super(itemView);
        sentLayout = itemView.findViewById(R.id.sentLayout);
        receivedLayout = itemView.findViewById(R.id.receivedLayout);
        sentText = itemView.findViewById(R.id.sentTextView);
        receivedText = itemView.findViewById(R.id.receivedTextView);
    }

    public void bindMessageData(String message, boolean type) {
        if (type) {
            //If a message is sent
            sentLayout.setVisibility(LinearLayout.VISIBLE);
            sentText.setText(message);
            // Set visibility as GONE to remove the space taken up
            receivedLayout.setVisibility(LinearLayout.GONE);
        } else {
            //Message is received
            receivedLayout.setVisibility(LinearLayout.VISIBLE);
            receivedText.setText(message);
            // Set visibility as GONE to remove the space taken up
            sentLayout.setVisibility(LinearLayout.GONE);
        }
    }
}
