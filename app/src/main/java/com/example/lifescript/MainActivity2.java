package com.example.lifescript;
import com.example.lifescript.database.MemoryDatabase;
import com.example.lifescript.models.Memory;
import com.example.lifescript.utils.SessionManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    EditText etJournal;
    Button btnSave;

    MemoryDatabase database;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etJournal = findViewById(R.id.etJournal);
        btnSave = findViewById(R.id.btnSave);
        database = MemoryDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String journalText = etJournal.getText().toString().trim();

                if(journalText.isEmpty()){
                    Toast.makeText(MainActivity2.this,
                            "Please write something",Toast.LENGTH_SHORT).show();

                }else{
                    int userId = sessionManager.getUserId();
                    String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                    Memory memory = new Memory(
                            userId,
                            journalText,
                            null,
                            currentTime
                    );
                    database.memoryDao().insertMemory(memory);
                    Toast.makeText(MainActivity2.this,
                            "Entry saved to database",Toast.LENGTH_SHORT).show();
                    etJournal.setText("");

                }
            }
        });
    }
}
