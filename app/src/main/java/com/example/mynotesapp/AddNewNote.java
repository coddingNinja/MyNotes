package com.example.mynotesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mynotesapp.FireBase.FireBaseFunctions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class AddNewNote extends AppCompatActivity {
    Spinner category;
    TextView save;
    EditText title,discription;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    String Title,Description,Category;
    DatabaseReference databaseReference;
    HashMap<String , ArrayList<EachNotes>> note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_note);

        setValues();
        String[] array = getResources().getStringArray(R.array.categories);
        ArrayList<String> categories = new ArrayList<>(Arrays.asList(array));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Title=title.getText().toString();
                Description=discription.getText().toString();
               Category=category.getSelectedItem().toString();
                if (!Title.isEmpty() && !Description.isEmpty() && !Category.isEmpty()) {
                    FireBaseFunctions fireBaseFunctions=new FireBaseFunctions(auth,databaseReference,AddNewNote.this);
                    fireBaseFunctions.add_Note_To_FireBase(new EachNotes(Title,Description),Category);
                    Intent intent=new Intent(AddNewNote.this,MyNotesMain.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddNewNote.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void setValues()
    {
        title=findViewById(R.id.editTextTitle);
        discription=findViewById(R.id.editTextDescription);
        category=findViewById(R.id.spinnerCategory);
        save= findViewById(R.id.textViewSave);
        int position=0;
        category.setSelection(position);
        auth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference("user");
    }
}