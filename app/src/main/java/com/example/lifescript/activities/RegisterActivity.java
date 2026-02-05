package com.example.lifescript.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lifescript.R;
import com.example.lifescript.database.MemoryDatabase;
import com.example.lifescript.models.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private MemoryDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = MemoryDatabase.getInstance(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError(getString(R.string.email_required));
                return;
            }
            if (password.isEmpty()) {
                etPassword.setError(getString(R.string.password_required));
                return;
            }
            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError(getString(R.string.passwords_dont_match));
                return;
            }

            // Check if user already exists
            if (database.userDao().getUserByEmail(email) != null) {
                Toast.makeText(this, R.string.email_exists, Toast.LENGTH_SHORT).show();
                return;
            }

            // Register user
            User user = new User(email, password);
            database.userDao().registerUser(user);

            Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }
}
