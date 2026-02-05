package com.example.lifescript.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifescript.R;
import com.example.lifescript.database.MemoryDatabase;
import com.example.lifescript.models.Memory;
import com.example.lifescript.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    private RecyclerView recyclerMemories;
    private Button btnAdd;
    private MemoryDatabase database;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        database = MemoryDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        recyclerMemories = findViewById(R.id.recyclerMemories);
        btnAdd = findViewById(R.id.btnAdd);

        recyclerMemories.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(TimelineActivity.this, AddMemoryActivity.class));
        });

        loadMemories();
    }

    private void loadMemories() {
        int userId = sessionManager.getUserId();
        List<Memory> memories = database.memoryDao().getMemoriesByUser(userId);
        // You would typically set an adapter here
        // For now, this loads the data for the timeline
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadMemories();
    }
}
