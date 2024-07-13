package com.example.mynotesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mynotesapp.Adapters.MyAdapterClassForCategories;
import com.example.mynotesapp.FireBase.FireBaseFunctions;

import com.example.mynotesapp.Fragments.All_Nots_Fragment;
import com.example.mynotesapp.Fragments.Fitness_notes_fragment;
import com.example.mynotesapp.Fragments.Important_notes_fragment;
import com.example.mynotesapp.Fragments.Improvement_notes_fragment;
import com.example.mynotesapp.Fragments.Other_noter_fragment;
import com.example.mynotesapp.Fragments.Work_notes_Fragment;
import com.example.mynotesapp.Interfaces.RecyclerViewInterface_Buttons;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;

public class MyNotesMain extends AppCompatActivity implements RecyclerViewInterface_Buttons {

    private RecyclerView  recyclerCategories;
    private ImageView profile_Image, addNewnote;
    private TextView myNotes, hiUsername;
    private FirebaseAuth auth;
    private FireBaseFunctions fireBaseFunctions;
    Button colored;
    private MyAdapterClassForCategories myAdapterClassForCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_notes_main);
        auth=FirebaseAuth.getInstance();
        set_ids();

        String[] array = getResources().getStringArray(R.array.categories);
        ArrayList<String> categories = new ArrayList<>(Arrays.asList(array));

        myAdapterClassForCategories = new MyAdapterClassForCategories(this, categories, this);
        recyclerCategories.setAdapter(myAdapterClassForCategories);
        addNewnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyNotesMain.this, AddNewNote.class);
                startActivity(intent);
            }
        });
        profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyNotesMain.this,UpdateProfile.class);
                intent.putExtra("username", FireBaseFunctions.CurrentUser.getUsername());
                intent.putExtra("profilePick", FireBaseFunctions.CurrentUser.getImageUrl());
                startActivity(intent);
            }
        });

    }


    protected void onStart() {
        super.onStart();
        fireBaseFunctions = new FireBaseFunctions(auth, FirebaseDatabase.getInstance().getReference("user"), this);
        fireBaseFunctions.getUserData(new FireBaseFunctions.CallBack_User() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onUserRetrieved(User user) {
                hiUsername.setText("Hi, " + user.getUsername());
                Glide.with(MyNotesMain.this).load(user.getImageUrl()).into(profile_Image);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MyNotesMain.this, "Failed to retrieve user data: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        All_Nots_Fragment allNotesFragment = new All_Nots_Fragment();
        fragmentTransaction.replace(R.id.fragment_container, allNotesFragment);
        fragmentTransaction.addToBackStack("name").setReorderingAllowed(true).commit();


    }
    public void set_ids() {
        myNotes = findViewById(R.id.My_Notes_text_view);
        addNewnote = findViewById(R.id.plus);
        profile_Image = findViewById(R.id.profile_image);
        recyclerCategories = findViewById(R.id.categories);
        hiUsername = findViewById(R.id.current_user_name);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressLint("ResourceAsColor")
    @Override

    public void onItemClick(Button button) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String buttonText = button.getText().toString();
        switch (buttonText) {
            case "All":
                All_Nots_Fragment allNotesFragment = new All_Nots_Fragment();
                fragmentTransaction.replace(R.id.fragment_container, allNotesFragment);
                break;
            case "Fitness":
                Fitness_notes_fragment fitnessNotesFragment = new Fitness_notes_fragment();
                fragmentTransaction.replace(R.id.fragment_container, fitnessNotesFragment);
                break;
            case "Important":
                Important_notes_fragment importantNotesFragment = new Important_notes_fragment();
                fragmentTransaction.replace(R.id.fragment_container, importantNotesFragment);
                break;
            case "Work":
                Work_notes_Fragment workNotesFragment=new Work_notes_Fragment();
                fragmentTransaction.replace(R.id.fragment_container,workNotesFragment);
                break;
            case "Improvement":
                Improvement_notes_fragment improvementNotesFragment=new Improvement_notes_fragment();
            fragmentTransaction.replace(R.id.fragment_container,improvementNotesFragment);
                break;
            case "Others":
                Other_noter_fragment otherNoterFragment=new Other_noter_fragment();
                fragmentTransaction.replace(R.id.fragment_container,otherNoterFragment);
                break;
            default:
                Toast.makeText(this, "Unknown category", Toast.LENGTH_SHORT).show();
                return;
        }

        fragmentTransaction.addToBackStack("name").setReorderingAllowed(true).commit();
    }


}
