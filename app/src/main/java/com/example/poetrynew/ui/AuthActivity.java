package com.example.poetrynew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.poetrynew.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.example.poetrynew.R;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        auth = FirebaseAuth.getInstance();
        initViews();
        setupListeners();

        // Если уже авторизован — сразу в главное меню
        if (auth.getCurrentUser() != null) {
            goToMain();
        }
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> authenticate(false));
        btnRegister.setOnClickListener(v -> authenticate(true));
    }

    private void authenticate(boolean register) {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (email.isEmpty() || pass.length() < 6) {
            Toast.makeText(this, "Введите email и пароль (≥6 символов)", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        if (register) {
            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> handleAuthResult(task, "Регистрация"));
        } else {
            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> handleAuthResult(task, "Вход"));
        }
    }

    private void handleAuthResult(com.google.android.gms.tasks.Task task, String action) {
        setLoading(false);
        if (task.isSuccessful()) {
            Toast.makeText(this, action + " успешен!", Toast.LENGTH_SHORT).show();
            goToMain();
        } else {
            String msg = task.getException() != null ? task.getException().getMessage() : "Ошибка";
            Toast.makeText(this, action + " не удался: " + msg, Toast.LENGTH_LONG).show();
        }
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? android.view.View.VISIBLE : android.view.View.GONE);
        btnLogin.setEnabled(!loading);
        btnRegister.setEnabled(!loading);
        etEmail.setEnabled(!loading);
        etPassword.setEnabled(!loading);
    }

    private void goToMain() {
        startActivity(new Intent(AuthActivity.this, MainActivity.class));
        finish();
    }
}