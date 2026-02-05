package com.example.lifescript.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.lifescript.dao.MemoryDao;
import com.example.lifescript.dao.UserDao;
import com.example.lifescript.models.Memory;
import com.example.lifescript.models.User;

@Database(entities = {Memory.class, User.class}, version = 2, exportSchema = false)
public abstract class MemoryDatabase extends RoomDatabase {

    public abstract MemoryDao memoryDao();
    public abstract UserDao userDao();

    private static volatile MemoryDatabase INSTANCE;

    public static MemoryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MemoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MemoryDatabase.class,
                            "lifescript_db"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // For simplicity in this example
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
