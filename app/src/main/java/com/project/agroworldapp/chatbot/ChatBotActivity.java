package com.project.agroworldapp.chatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.project.agroworldapp.R;
import com.project.agroworldapp.databinding.ActivityChatbotBinding;

import java.util.ArrayList;

public class ChatBotActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityChatbotBinding binding;
    private ArrayList<ChatBotModel> chatBotModels;
    private PerformRequest request;
    private ChatBotAdapter chatBotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatbot);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        request = new PerformRequest(this);

        // Set RecyclerView layout manager.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        // Set an animation
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        chatBotModels = new ArrayList<>();
        chatBotAdapter = new ChatBotAdapter(chatBotModels);
        binding.recyclerView.setAdapter(chatBotAdapter);
        sendMessage("Hello");
        binding.msgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.msgInput.getText().toString();
                sendMessage(message);
            }
        });

        binding.tvAboutQuery.setOnClickListener(ChatBotActivity.this);
        binding.tvContactQuery.setOnClickListener(ChatBotActivity.this);
        binding.tvLanguageQuery.setOnClickListener(ChatBotActivity.this);
        binding.tvAlarmQuery.setOnClickListener(ChatBotActivity.this);
        binding.tvHistoryQuery.setOnClickListener(ChatBotActivity.this);
        binding.tvWeatherQuery.setOnClickListener(ChatBotActivity.this);
        binding.tvNewsQuery.setOnClickListener(ChatBotActivity.this);
        binding.tvOtherQuery.setOnClickListener(ChatBotActivity.this);

    }

    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            chatBotModels.add(new ChatBotModel(true, message));
            int newPosition = chatBotModels.size() - 1;
            chatBotAdapter.notifyItemInserted(newPosition);
            binding.recyclerView.scrollToPosition(newPosition);
            binding.msgInput.setText("");
            getReply(message);
        }
    }

    private void getReply(String message) {
        request.getResponse(message, new ChatBotListener() {
            @Override
            public void onError(String message) {
                Log.d("REQUEST ERROR", message);
            }

            @Override
            public void onResponse(String reply) {
                chatBotModels.add(new ChatBotModel(false, reply));
                int newPosition = chatBotModels.size() - 1;
                chatBotAdapter.notifyItemInserted(newPosition);
                binding.recyclerView.scrollToPosition(newPosition);
                binding.rlQueries.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onClick(View v) {
        binding.rlQueries.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.tvAboutQuery:
                sendMessage("About");
                break;
            case R.id.tvAlarmQuery:
                sendMessage("Alarm");
                break;
            case R.id.tvContactQuery:
                sendMessage("Contact");
                break;
            case R.id.tvHistoryQuery:
                sendMessage("History");
                break;
            case R.id.tvWeatherQuery:
                sendMessage("Weather");
                break;
            case R.id.tvLanguageQuery:
                sendMessage("Language");
                break;
            case R.id.tvNewsQuery:
                sendMessage("News");
                break;
            case R.id.tvOtherQuery:
                sendMessage("Other");
                break;
            default:

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}