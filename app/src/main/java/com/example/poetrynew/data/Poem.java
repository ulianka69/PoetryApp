package com.example.poetrynew.data;

import com.google.firebase.firestore.PropertyName;

public class Poem {
    private String id;
    private String title;
    private String author;
    private String content;
    private String preview;
    private boolean isFavorite;

    public Poem() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getPreview() { return preview; }
    public void setPreview(String preview) { this.preview = preview; }


    @PropertyName("isFavorite")
    public boolean isFavorite() { return isFavorite; }

    @PropertyName("isFavorite")
    public void setFavorite(boolean favorite) { this.isFavorite = favorite; }
}