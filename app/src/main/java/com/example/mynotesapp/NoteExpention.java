package com.example.mynotesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotesapp.FireBase.FireBaseFunctions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class NoteExpention extends AppCompatActivity {
    Spinner spinner;
    EditText Title,Description;
    TextView update,delete;
    FirebaseAuth auth;
    FireBaseFunctions fireBaseFunctions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.note_expention);
        spinner=findViewById(R.id.note_expention_spinner);
        Title=findViewById(R.id.note_expention_title);
        Description=findViewById(R.id.note_expention_Description);
        update=findViewById(R.id.Update);
        delete=findViewById(R.id.Delete);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.categories, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        auth=FirebaseAuth.getInstance();
        fireBaseFunctions=new FireBaseFunctions(auth, FirebaseDatabase.getInstance().getReference("user"),this);
        String title=getIntent().getStringExtra("title");
        String description=getIntent().getStringExtra("description");
        String category=getIntent().getStringExtra("category");

        Title.setText(title);
        Description.setText(description);
        int position=adapter.getPosition(category);
        spinner.setSelection(position);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Title.getText().toString().equals(title)||!Description.getText().toString().equals(description)||!spinner.getSelectedItem().toString().equals(category))
                {
                    EachNotes  notes=new EachNotes(Title.getText().toString(),Description.getText().toString());
                    fireBaseFunctions.searchNoteByTitle(notes,category,title, spinner.getSelectedItem().toString());
                    Intent intent= new Intent(NoteExpention.this,MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent= new Intent(NoteExpention.this,MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(NoteExpention.this,"Not Will Not Update Because it Is Same",Toast.LENGTH_LONG).show();
                   }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireBaseFunctions.Delete_selected_Notes(title,category);
                Intent intent= new Intent(NoteExpention.this,MainActivity.class);
                startActivity(intent);
            }
        });







    }

}