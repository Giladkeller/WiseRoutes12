package com.example.wiseroute12;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    private EditText editTextQuestion;
    private TextView textViewResponse;
    private ProgressBar progressBar;

    private static final OpenAIService openAIService = new OpenAIService("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editTextQuestion = findViewById(R.id.editTextQuestion);
        Button btnSend = findViewById(R.id.btnSend);
        textViewResponse = findViewById(R.id.textViewResponse);
        progressBar = findViewById(R.id.progressBar);

        btnSend.setOnClickListener(v -> {
            String question = editTextQuestion.getText().toString().trim();
            if (question.isEmpty()) return;

            setLoading(true);

            openAIService.sendMessage(question, new OpenAIService.ChatCallback() {
                @Override
                public void onSuccess(String content) {
                    runOnUiThread(() -> {
                        setLoading(false);
                        textViewResponse.setText(content);
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        setLoading(false);
                        textViewResponse.setText("שגיאה:\n" + message);
                    });
                }
            });
        });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}