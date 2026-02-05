package com.example.lifescript.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.lifescript.models.Memory;

import java.util.List;

@Dao
public interface MemoryDao {
    @Insert
    void insertMemory(Memory memory);

    @Query("SELECT * FROM memories WHERE userId = :userId ORDER BY createdAt DESC")
    List<Memory> getMemoriesByUser(int userId);
}
