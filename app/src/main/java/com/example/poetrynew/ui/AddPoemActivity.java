package com.example.poetrynew.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.poetrynew.R;
import java.util.HashMap;
import java.util.Map;

public class AddPoemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poem);

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etAuthor = findViewById(R.id.etAuthor);
        EditText etContent = findViewById(R.id.etContent);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String author = etAuthor.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Заполни название и текст!", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSave.setEnabled(false);
            btnSave.setText("Сохранение...");

            Map<String, Object> poem = new HashMap<>();
            poem.put("title", title);
            poem.put("author", author.isEmpty() ? "Неизвестный автор" : author);
            poem.put("content", content);
            poem.put("preview", content.length() > 60 ? content.substring(0, 60) + "..." : content);
            poem.put("isFavorite", false);

            FirebaseFirestore.getInstance().collection("poems")
                    .add(poem)
                    .addOnSuccessListener(doc -> {
                        Toast.makeText(this, " Стих добавлен!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, " Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        btnSave.setEnabled(true);
                        btnSave.setText("Сохранить в библиотеку");
                    });
        });
    }
}