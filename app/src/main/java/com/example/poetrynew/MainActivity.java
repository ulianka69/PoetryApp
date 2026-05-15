package com.example.poetrynew;

import com.example.poetrynew.ui.AddPoemActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.poetrynew.ui.AuthActivity;
import com.example.poetrynew.ui.PoemAdapter;
import com.example.poetrynew.data.Poem;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private RecyclerView recycler;
    private PoemAdapter adapter;
    private SearchView searchView;
    private List<Poem> allPoems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        setupListeners();
        loadPoems();
    }

    private void initViews() {
        recycler = findViewById(R.id.recycler);
        searchView = findViewById(R.id.searchView);
        Button btnLogout = findViewById(R.id.btnLogout);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PoemAdapter();
        recycler.setAdapter(adapter);

        adapter.setOnPoemClickListener(poem ->
                Toast.makeText(this, poem.getTitle(), Toast.LENGTH_SHORT).show()
        );
    }

    private void setupListeners() {
        findViewById(R.id.btnLogout).setOnClickListener(v -> logout());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPoems(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterPoems(newText);
                return true;
            }
        });

        findViewById(R.id.fabAdd).setOnClickListener(v -> {
            startActivity(new Intent(this, AddPoemActivity.class));
        });
    }

    private void loadPoems() {
        db.collection("poems")
                .orderBy("title")
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Ошибка загрузки: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    allPoems.clear();
                    if (snapshots != null) {
                        for (com.google.firebase.firestore.DocumentSnapshot doc : snapshots.getDocuments()) {
                            Poem poem = doc.toObject(Poem.class);
                            if (poem != null) {
                                poem.setId(doc.getId());
                                allPoems.add(poem);
                            }
                        }
                    }
                    adapter.setPoems(allPoems);
                });
    }

    private void filterPoems(String query) {
        List<Poem> filtered = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Poem poem : allPoems) {
            if (poem.getTitle().toLowerCase().contains(lowerQuery) ||
                    poem.getAuthor().toLowerCase().contains(lowerQuery)) {
                filtered.add(poem);
            }
        }
        adapter.setPoems(filtered);
    }

    private void logout() {
        auth.signOut();
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }
    }
}