package com.example.wiseroute12;



import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OpenAIService {

    public interface ChatCallback {
        void onSuccess(String content);
        void onError(String message);
    }

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private String apiKey = "";
    // אופציה 1: להכניס מפתח בזמן יצירה
    public OpenAIService(String apiKey) {
        this.apiKey = apiKey;
    }

    // אופציה 2: או להגדיר/להחליף מפתח אחר כך
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void sendMessage(String userMessage, ChatCallback callback) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            callback.onError("חסר API KEY (OpenAIService).");
            return;
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-4o-mini");

            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "system").put("content", "You are a helpful assistant."));
            messages.put(new JSONObject().put("role", "user").put("content", userMessage));

            jsonBody.put("messages", messages);
            jsonBody.put("temperature", 0.7);
        } catch (Exception e) {
            callback.onError("שגיאה בבניית הבקשה");
            return;
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + apiKey.trim())
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("שגיאת רשת: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String raw = (response.body() != null) ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    callback.onError("HTTP " + response.code() + " " + response.message() + "\n" + raw);
                    return;
                }

                try {
                    JSONObject json = new JSONObject(raw);
                    String content = json.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");

                    callback.onSuccess(content.trim());
                } catch (Exception e) {
                    callback.onError("שגיאה בעיבוד התשובה");
                }
            }
        });
    }
}