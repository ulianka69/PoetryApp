package com.example.poetrynew.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.poetrynew.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvAuthor = findViewById(R.id.tvDetailAuthor);
        TextView tvContent = findViewById(R.id.tvDetailContent);
        Button btnBack = findViewById(R.id.btnBack);


        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String content = getIntent().getStringExtra("content");

        tvTitle.setText(title != null ? title : "Без названия");
        tvAuthor.setText(author != null ? author : "Неизвестный автор");
        tvContent.setText(content != null ? content : "Текст отсутствует");

        btnBack.setOnClickListener(v -> finish());
    }
}