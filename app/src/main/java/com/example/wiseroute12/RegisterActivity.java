package com.example.wiseroute12;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quartetsproject.helpers.DatabaseHelper;
import com.example.wiseroute12.R;

public class RegisterActivity extends AppCompatActivity {

    EditText editUsername, editEmail, editPassword;
    Button btnRegister;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "נא למלא את כל השדות", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean success = dbHelper.addUser(username, email, password);
                if (success) {
                    Toast.makeText(RegisterActivity.this, "הרשמה הצליחה!", Toast.LENGTH_SHORT).show();
                    // כאן אפשר לעבור למסך ההתחברות או למסך הראשי
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "שם משתמש או אימייל כבר קיימים", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
