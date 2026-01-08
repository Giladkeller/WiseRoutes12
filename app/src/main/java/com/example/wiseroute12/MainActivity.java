package com.example.wiseroute12;
import android.util.Log;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    private EditText editTextQuestion;
    private TextView textViewResponse;
    private static final String API_KEY = "";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextQuestion = findViewById(R.id.editTextQuestion);
        Button btnSend = findViewById(R.id.btnSend);
        textViewResponse = findViewById(R.id.textViewResponse);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = editTextQuestion.getText().toString().trim();
                if (!question.isEmpty()) {
                    sendMessageToChatGPT(question);
                }
            }
        });
    }

    private void sendMessageToChatGPT(String message) {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo");
            JSONArray messagesArray = new JSONArray();
            messagesArray.put(new JSONObject().put("role", "system").put("content", "You are a helpful assistant."));
            messagesArray.put(new JSONObject().put("role", "user").put("content", message));
            jsonBody.put("messages", messagesArray);
            jsonBody.put("temperature", 0.7);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> textViewResponse.setText("שגיאה: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String chatResponse = jsonResponse.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                        runOnUiThread(() -> textViewResponse.setText(chatResponse.trim()));
                    } catch (Exception e) {
                        runOnUiThread(() -> textViewResponse.setText("שגיאה בעיבוד התשובה"));
                    }
                } else {
                    runOnUiThread(() -> textViewResponse.setText("שגיאת תגובה: " + response.message()+ " "+ response.code()));

                    Log.d("API_RESPONSE", "Response Error: " + response.message());
                    // Log.e("API_RESPONSE", "Response Error: " + response.message());
                    String errorBody = response.body() != null ? response.body().string() : "No response body";
                    Log.e("API_RESPONSE", "Response Error: " + errorBody);

                }
            }
        });
    }
}

