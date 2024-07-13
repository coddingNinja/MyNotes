package com.example.mynotesapp.FireBase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mynotesapp.EachNotes;
import com.example.mynotesapp.MyNotesMain;
import com.example.mynotesapp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class FireBaseFunctions {
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private Context context;
    public static User CurrentUser=  new User();;
    public static HashMap<String,ArrayList<EachNotes>> hashMap_notes=new HashMap<>();

    public FireBaseFunctions(FirebaseAuth auth, DatabaseReference databaseReference, Context context) {
        this.auth = auth;
        this.databaseReference = databaseReference;
        this.context = context;
    }

    public void add_Note_To_FireBase(EachNotes note, String category) {
        databaseReference.child(auth.getCurrentUser().getUid()).child("notes").child(category)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    ArrayList<EachNotes> noteList = new ArrayList<>();

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                                String title=noteSnapshot.child("title").getValue(String.class);
                                String discription=noteSnapshot.child("discription").getValue(String.class);
                                EachNotes existingNote = new EachNotes(title,discription);
                                noteList.add(existingNote);
                            }
                        }
                        noteList.add(note);

                        if (category.equals("All"))
                        {
                           Add_Every_Notes_To_All_Category(note);

                        }
                        else {
                            add_list_to_fireBase(noteList,category);
                            Add_Every_Notes_To_All_Category(note);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to add note", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void Add_Every_Notes_To_All_Category(EachNotes note) {
        databaseReference.child(auth.getCurrentUser().getUid()).child("notes").child("All")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    ArrayList<EachNotes> noteList = new ArrayList<>();

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                                String title=noteSnapshot.child("title").getValue(String.class);
                                String discription=noteSnapshot.child("discription").getValue(String.class);
                                EachNotes existingNote = new EachNotes(title,discription);
                                noteList.add(existingNote);
                            }
                        }
                        noteList.add(note);
                        add_list_to_fireBase(noteList,"All");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to add note", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void add_list_to_fireBase(ArrayList<EachNotes> notesArrayList, String category) {
        databaseReference.child(auth.getCurrentUser().getUid()).child("notes").child(category).setValue(notesArrayList);

    }

    public interface CallBack_User {
        void onUserRetrieved(User user);
        void onError(String error);
    }



    public void getUserData(final CallBack_User callback) {
        databaseReference.child(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {

                        if (snapshot.hasChild("username")) {
                            CurrentUser.setUsername(snapshot.child("username").getValue(String.class));
                        }
                        if (snapshot.hasChild("imageUrl")) {
                            CurrentUser.setImageUrl(snapshot.child("imageUrl").getValue(String.class));
                        }
                        if (snapshot.hasChild("notes"))
                        {

                        }
                        callback.onUserRetrieved(CurrentUser);



                    } else {
                        Log.e("FirebaseFunctions", "Snapshot is null or does not exist");
                        callback.onError("Snapshot is null or does not exist");
                    }
                } else {
                    Log.e("FirebaseFunctions", "Failed to read user data", task.getException());
                    callback.onError("Failed to read user data");
                }
            }
        });
    }

    public interface Callback_note {
        void onSuccess(ArrayList<EachNotes> notes);
        void onFailure(String error);
    }


    public void getNotes(final Callback_note callbackNote,String note_category) {
       DatabaseReference db= databaseReference.child(auth.getCurrentUser().getUid()).child("notes").child(note_category);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<EachNotes> new_list = new ArrayList<>();

                if (snapshot.exists()) {

                    for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                        String title=noteSnapshot.child("title").getValue(String.class);
                        String discription=noteSnapshot.child("discription").getValue(String.class);
                        EachNotes note=new EachNotes(title,discription);
                        new_list.add(note);
                    }
                    callbackNote.onSuccess(new_list);
                } else {

                    callbackNote.onFailure("Snapshot is null or doesn't exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FirebaseFunctions", "Task failed: " + databaseError.getMessage());
                callbackNote.onFailure("Task failed: " + databaseError.getMessage());
            }
        });
    }


    public void searchNoteByTitle(EachNotes eachNotes, String currentCategory, String originalTitle, String newCategory) {
        databaseReference.child(auth.getCurrentUser().getUid()).child("notes").child(currentCategory)
                .orderByChild("title").equalTo(originalTitle).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                                String noteKey = noteSnapshot.getKey();
                                if (noteKey != null) {
                                    if (currentCategory.equals(newCategory)) {
                                        Update_Note(eachNotes, currentCategory, noteKey,newCategory);
                                    } else {
                                        delete_Updated_notes(originalTitle,currentCategory,noteKey);
                                        add_Note_To_FireBase(eachNotes,newCategory);
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(context, "Note not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Failed to update note", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private   void Update_Note(EachNotes eachNotes,String category,String note_Key,String NextCtegory)
    {

        if (category.equals(NextCtegory))
        {
            databaseReference.child(auth.getCurrentUser().getUid()).child("notes").child(category).child(note_Key).setValue(eachNotes);
        }
     else
        {
            databaseReference.child(auth.getCurrentUser().getUid()).child("notes").child(category).child(note_Key).setValue(eachNotes);
            add_Note_To_FireBase(eachNotes,NextCtegory);
        }
    }

    public void delete_Updated_notes(String title,String category,String note_Key)
    {
            databaseReference.child(auth.getCurrentUser().getUid()).child("notes").child(category).child(note_Key).removeValue();
    }
    public void Delete_selected_Notes(String title,String category)
    {
        databaseReference.child(auth.getCurrentUser().getUid()).child("notes").child(category).orderByChild("title").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists())
               {
                   for (DataSnapshot noteSnapshot : snapshot.getChildren())
                   {
                       String noteKey = noteSnapshot.getKey();
                       databaseReference.child(auth.getCurrentUser().getUid()).child("notes").child(category).child(noteKey).removeValue();

                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static User getCurrentUser() {
        return CurrentUser;
    }

    public static void setCurrentUser(User currentUser) {
        CurrentUser = currentUser;
    }
}
