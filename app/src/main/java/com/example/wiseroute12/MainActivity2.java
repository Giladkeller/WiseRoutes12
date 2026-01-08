package com.example.wiseroute12;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{
    private Button btAddPost , btcomment1 , btcomment2 ,btcomment3 , btcomment4 , btcomment5;
    private Dialog dialog;
    private Dialog currentDialog;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btAddPost =(Button)findViewById(R.id.addPostBtn);
        btAddPost.setOnClickListener(this);
        findViewById(R.id.addPostBtn).setOnClickListener(v -> createDialog());

    }



    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId()==btAddPost.getId()){
            createDialog();
        }

    }

    public void createDialog() {
        currentDialog = new Dialog(this);
        currentDialog.setContentView(R.layout.newpost);

        // ⬅️ מגדיל את הדיאלוג
        if (currentDialog.getWindow() != null) {
            currentDialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        EditText etTrailName = currentDialog.findViewById(R.id.etTrailName);
        EditText etDescription = currentDialog.findViewById(R.id.etDescription);
        Button btnAdd = currentDialog.findViewById(R.id.btnPublish);
        Button btnChooseImage = currentDialog.findViewById(R.id.btnChooseImage);
        ImageView ivPostImage = currentDialog.findViewById(R.id.ivPostImage);

        LinearLayout postsContainer = findViewById(R.id.postsContainer);

        // הכפתור לבחור תמונה
        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnAdd.setOnClickListener(v -> {
            String trailName = etTrailName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (trailName.isEmpty() || description.isEmpty()) return;

            // ===== יצירת פוסט חדש =====
            LinearLayout post = new LinearLayout(this);
            post.setOrientation(LinearLayout.VERTICAL);
            post.setPadding(16, 16, 16, 16);

            TextView title = new TextView(this);
            title.setText(trailName);
            title.setTextSize(18);
            title.setTypeface(null, Typeface.BOLD);

            ImageView image = new ImageView(this);
            if (selectedImageUri != null) {
                image.setImageURI(selectedImageUri); // משתמש בתמונה שבחר המשתמש
            } else {
                image.setImageResource(R.drawable.ic_add_photo); // ברירת מחדל
            }
            LinearLayout.LayoutParams imgParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            400
                    );
            image.setLayoutParams(imgParams);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            TextView desc = new TextView(this);
            desc.setText(description);

            // ===== שורת לייק =====
            LinearLayout likesRow = new LinearLayout(this);
            likesRow.setOrientation(LinearLayout.HORIZONTAL);
            likesRow.setPadding(0, 12, 0, 0);

            ImageView heart = new ImageView(this);
            heart.setImageResource(R.drawable.ic_heart);
            heart.setLayoutParams(new LinearLayout.LayoutParams(80, 80));

            TextView likesCount = new TextView(this);
            likesCount.setText("0");
            likesCount.setTextSize(16);
            likesCount.setPadding(16, 0, 0, 0);

            likesRow.addView(heart);
            likesRow.addView(likesCount);

            // ===== הוספה לפוסט =====
            post.addView(title);
            post.addView(image);
            post.addView(desc);
            post.addView(likesRow);

            // מוסיף למעלה
            postsContainer.addView(post, 0);

            // איפוס משתנה התמונה לדיאלוג הבא
            selectedImageUri = null;
            ivPostImage.setImageResource(R.drawable.ic_add_photo);

            currentDialog.dismiss();
        });

        currentDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (currentDialog != null) {
                ImageView ivPostImage = currentDialog.findViewById(R.id.ivPostImage);
                if (ivPostImage != null) {
                    ivPostImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}