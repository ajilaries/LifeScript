package com.example.lifescript.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "memories")
public class Memory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;         // Associated user ID for local auth (int to match User.id)
    private String promptText;  // The written memory
    private String imagePath;   // URI/Path to the attached image
    private String createdAt;   // Auto-generated timestamp

    public Memory(int userId, String promptText, String imagePath, String createdAt) {
        this.userId = userId;
        this.promptText = promptText;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
