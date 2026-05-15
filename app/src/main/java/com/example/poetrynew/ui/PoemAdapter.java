package com.example.poetrynew.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.example.poetrynew.R;
import com.example.poetrynew.data.Poem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PoemAdapter extends RecyclerView.Adapter<PoemAdapter.PoemViewHolder> {

    private List<Poem> poems = new ArrayList<>();
    private OnPoemClickListener listener;

    public interface OnPoemClickListener {
        void onPoemClick(Poem poem);
    }

    public void setOnPoemClickListener(OnPoemClickListener listener) {
        this.listener = listener;
    }

    public void setPoems(List<Poem> poems) {
        this.poems = poems != null ? poems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PoemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poem, parent, false);
        return new PoemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoemViewHolder holder, int position) {
        holder.bind(poems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return poems.size();
    }


    class PoemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvPreview;
        ImageButton btnFavorite;

        PoemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvPreview = itemView.findViewById(R.id.tvPreview);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onPoemClick(poems.get(position));
                }
            });


            btnFavorite.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    toggleFavorite(position);
                }
            });
        }

        void bind(Poem poem, int position) {
            tvTitle.setText(poem.getTitle());
            tvAuthor.setText("— " + poem.getAuthor());
            tvPreview.setText(poem.getPreview());
            updateIcon(poem.isFavorite());
        }

        private void toggleFavorite(int position) {
            Poem poem = poems.get(position);
            boolean newStatus = !poem.isFavorite();
            poem.setFavorite(newStatus);
            updateIcon(newStatus);
            notifyItemChanged(position);


            FirebaseFirestore.getInstance()
                    .collection("poems")
                    .document(poem.getId())
                    .set(Collections.singletonMap("isFavorite", newStatus), SetOptions.merge())
                    .addOnFailureListener(e -> {
                        poem.setFavorite(!newStatus);
                        updateIcon(poem.isFavorite());
                        notifyItemChanged(position);
                        Toast.makeText(itemView.getContext(), "Ошибка сети", Toast.LENGTH_SHORT).show();
                    });
        }

        private void updateIcon(boolean isFav) {
            btnFavorite.setImageResource(isFav
                    ? android.R.drawable.btn_star_big_on
                    : android.R.drawable.btn_star);
        }
    }
}