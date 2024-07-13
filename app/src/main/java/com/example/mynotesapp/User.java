package com.example.mynotesapp;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    String username;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    HashMap<String, ArrayList<EachNotes> > notes;
   public  User()
    {

    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HashMap<String, ArrayList<EachNotes>> getNotes() {
        return notes;
    }

    public void setNotes(HashMap<String, ArrayList<EachNotes>> notes) {
        this.notes = notes;
    }

    public User( String imageUrl,HashMap<String, ArrayList<EachNotes>> notes, String username ) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.notes = notes;
    }
}
