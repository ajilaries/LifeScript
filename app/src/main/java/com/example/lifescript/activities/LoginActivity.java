package com.example.lifescript.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lifescript.R;
import com.example.lifescript.database.MemoryDatabase;
import com.example.lifescript.models.User;
import com.example.lifescript.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private MemoryDatabase database;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = MemoryDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError(getString(R.string.email_required));
                return;
            }
            if (password.isEmpty()) {
                etPassword.setError(getString(R.string.password_required));
                return;
            }

            User user = database.userDao().login(email, password);
            if (user != null) {
                sessionManager.createLoginSession(user.getId(), user.getEmail());
                startActivity(new Intent(LoginActivity.this, AddMemoryActivity.class));
                finish();
            } else {
                Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
