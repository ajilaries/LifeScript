package com.example.lifescript.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lifescript.R;
import com.example.lifescript.database.MemoryDatabase;
import com.example.lifescript.models.Memory;
import com.example.lifescript.utils.SessionManager;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewStoryActivity extends AppCompatActivity {

    private Button btnGenerateStory;
    private TextView tvStoryContent;
    private ProgressBar progressBar;
    
    private MemoryDatabase database;
    private SessionManager sessionManager;

    // Gemini API Key
    private static final String GEMINI_API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);

        database = MemoryDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        btnGenerateStory = findViewById(R.id.btnGenerateStory);
        tvStoryContent = findViewById(R.id.tvStoryContent);
        progressBar = findViewById(R.id.progressBar);

        btnGenerateStory.setOnClickListener(v -> fetchAndProcessMemories());
    }

    private void fetchAndProcessMemories() {
        int userId = sessionManager.getUserId();
        List<Memory> memories = database.memoryDao().getMemoriesByUser(userId);

        if (memories == null || memories.isEmpty()) {
            Toast.makeText(this, R.string.no_memories_found, Toast.LENGTH_SHORT).show();
            return;
        }

        // Sort memories chronologically
        Collections.reverse(memories);

        Map<String, StringBuilder> monthlyMemories = new LinkedHashMap<>();
        for (Memory memory : memories) {
            String date = memory.getCreatedAt();
            if (date == null || date.length() < 7) continue;

            String month = date.substring(0, 7);
            
            if (!monthlyMemories.containsKey(month)) {
                monthlyMemories.put(month, new StringBuilder());
            }
            
            StringBuilder sb = monthlyMemories.get(month);
            sb.append("- On ").append(date).append(": ").append(memory.getPromptText());
            if (memory.getImagePath() != null) {
                sb.append(" (Photo captured)");
            }
            sb.append("\n");
        }

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("You are a personal AI biographer. Write a cohesive, emotional, first-person story ")
                .append("that connects these daily memories into a beautiful narrative. ")
                .append("The story should feel like a reflective journal entry. ")
                .append("Here are my memories grouped by month:\n\n");

        for (Map.Entry<String, StringBuilder> entry : monthlyMemories.entrySet()) {
            promptBuilder.append("Month: ").append(entry.getKey()).append("\n")
                    .append(entry.getValue().toString()).append("\n");
        }

        generateStory(promptBuilder.toString());
    }

    private void generateStory(String fullPrompt) {
        progressBar.setVisibility(View.VISIBLE);
        btnGenerateStory.setEnabled(false);
        tvStoryContent.setText(R.string.story_generating);

        // Updated model name to "gemini-1.5-flash" as "gemini-pro" might be deprecated or unavailable,
        // which often causes 404 ServerException.
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", GEMINI_API_KEY);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(fullPrompt)
                .build();

        Executor executor = Executors.newSingleThreadExecutor();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnGenerateStory.setEnabled(true);
                    tvStoryContent.setText(result.getText());
                });
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("GeminiError", "Error generating story", t);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnGenerateStory.setEnabled(true);
                    tvStoryContent.setText("Error: " + t.getMessage());
                    Toast.makeText(ViewStoryActivity.this, "Check Logcat (GeminiError) for details", Toast.LENGTH_LONG).show();
                });
            }
        }, executor);
    }
}
