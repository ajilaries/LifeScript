package com.example.lifescript;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    EditText etJournal;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etJournal = findViewById(R.id.etJournal);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {   // ✅ correct method name
                String journalText = etJournal.getText().toString().trim();

                if (journalText.isEmpty()) {
                    Toast.makeText(MainActivity2.this,
                            "Please write something", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity2.this,
                            "Entry saved", Toast.LENGTH_SHORT).show();
                    etJournal.setText("");
                }
            }
        });
    }
}
