package com.example.mcnotes;

public class Note {
    private String title;
    private String content;
    public int ID;

    public Note() {

    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        this.ID=ID;
    }

}
