package com.example.mynotesapp;

public class EachNotes {
    String title;
    String Discription;


    public EachNotes(String title, String discription ) {
        this.title = title;
        Discription = discription;

    }





    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }
}
