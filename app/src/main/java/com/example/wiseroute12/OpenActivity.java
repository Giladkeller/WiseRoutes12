package com.example.wiseroute12;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OpenActivity extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog.Builder builder;

    private ImageView imgContact;

    private Button btnStart;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_open);

        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

        }

        // Initialize views first (avoid using imgContact before findViewById)
        imgContact = (ImageView) findViewById(R.id.imgContact);
        imgContact.setOnClickListener(this);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = sp.getBoolean("isLoggedIn", false);

        // Always read username (use empty string as default) and guard before using substring
        username = sp.getString("username", "");
        if (isLoggedIn && username != null && !username.isEmpty()) {
            String firstLetter = username.substring(0, 1).toUpperCase();
            imgContact.setImageBitmap(createLetterBitmap(firstLetter));
        }

    }

    private Bitmap createLetterBitmap(String letter) {

        int size = 200;

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // ציור עיגול
        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(0xFF3F51B5); // כחול

        canvas.drawCircle(size / 2f, size / 2f, size / 2f, circlePaint);

        // ציור האות
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(96);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float x = size / 2f;
        float y = size / 2f - (fm.ascent + fm.descent) / 2;

        canvas.drawText(letter, x, y, textPaint);

        return bitmap;
    }




    public void onClick(View v) {
            SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
            boolean isLoggedIn = sp.getBoolean("isLoggedIn", false);

            if (v.getId() == btnStart.getId()) {

                if (!isLoggedIn) {
                    Toast.makeText(this, "עליך להתחבר כדי להמשיך", Toast.LENGTH_SHORT).show();
                    return; // עוצר כאן – אין מעבר מסך
                }

                // אם כן מחובר
                startActivity(new Intent(this, MainActivity.class));
            }

        if (v.getId() == imgContact.getId()) {
            if (isLoggedIn) {
                builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("stop");
                builder.setMessage("you login you sure you want change user");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(OpenActivity.this, RegisterActivity.class));
                            }
                }).setNegativeButton("no", null).show();
                return;
            }
            startActivity(new Intent(this, RegisterActivity.class));
        }

    }

    }