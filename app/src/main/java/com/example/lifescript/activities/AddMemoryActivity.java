package com.example.lifescript.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.lifescript.R;
import com.example.lifescript.database.MemoryDatabase;
import com.example.lifescript.models.Memory;
import com.example.lifescript.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddMemoryActivity extends AppCompatActivity {

    private EditText etMemoryText;
    private Button btnSelectImage, btnSaveMemory, btnViewStory, btnLogout;
    private ImageView ivPreview;
    
    private MemoryDatabase database;
    private SessionManager sessionManager;
    private String selectedImageUri = null;

    // ActivityResultLauncher for picking images
    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri.toString();
                    ivPreview.setVisibility(View.VISIBLE);
                    Glide.with(this).load(uri).into(ivPreview);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        database = MemoryDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        etMemoryText = findViewById(R.id.etMemoryText);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveMemory = findViewById(R.id.btnSaveMemory);
        btnViewStory = findViewById(R.id.btnViewStory);
        btnLogout = findViewById(R.id.btnLogout);
        ivPreview = findViewById(R.id.ivPreview);

        btnSelectImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        btnSaveMemory.setOnClickListener(v -> saveMemory());

        btnViewStory.setOnClickListener(v -> {
            startActivity(new Intent(AddMemoryActivity.this, ViewStoryActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            startActivity(new Intent(AddMemoryActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void saveMemory() {
        String memoryText = etMemoryText.getText().toString().trim();

        if (memoryText.isEmpty()) {
            Toast.makeText(this, R.string.please_write_something, Toast.LENGTH_SHORT).show();
            return;
        }

        // Auto-generate date and time
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        int userId = sessionManager.getUserId();

        Memory memory = new Memory(
                userId,
                memoryText,
                selectedImageUri,
                currentTime
        );

        database.memoryDao().insertMemory(memory);

        Toast.makeText(this, R.string.memory_saved, Toast.LENGTH_SHORT).show();

        // Clear inputs after save
        etMemoryText.setText("");
        ivPreview.setVisibility(View.GONE);
        selectedImageUri = null;
    }
}
