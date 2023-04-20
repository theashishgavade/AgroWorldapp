package com.project.agroworldapp.chatbot;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.agroworldapp.BuildConfig;
import com.project.agroworldapp.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class PerformRequest {
    private final RequestQueue queue;
    private String reply;
    private final char[] illegalChars = {'#', '<', '>', '$', '+', '%', '!', '`', '&',
            '*', '\'', '\"', '|', '{', '}', '/', '\\', ':', '@'};

    public PerformRequest(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    private String formatMessage(String message) {

        message = message.replace(' ', '-');
        for (char illegalChar : illegalChars) {
            message = message.replace(illegalChar, '-');
        }
        return message;
    }

    public void getResponse(String message, final ChatBotListener volleyResponseListener) {
        message = formatMessage(message);
        String url = "http://api.brainshop.ai/get?bid=" + BuildConfig.BRAIN_ID + "&key=" + BuildConfig.CHAT_BOT_API + "&uid=1&msg=" + message;
        Log.d("URL", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            reply = response.getString("cnt");
                            Log.d("RESPONSE", reply);
                            volleyResponseListener.onResponse(reply);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            volleyResponseListener.onError("JSON Exception");
                        }

                    }
                },
                error -> {
                    error.printStackTrace();
                    volleyResponseListener.onError("Volley Error");
                });
        queue.add(request);
    }
}
