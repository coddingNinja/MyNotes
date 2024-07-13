package com.example.mynotesapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotesapp.EachNotes;
import com.example.mynotesapp.FireBase.FireBaseFunctions;
import com.example.mynotesapp.Adapters.MyAdapterClassForEachRow;
import com.example.mynotesapp.Interfaces.RecyclerViewInterFaceForEachRow;
import com.example.mynotesapp.NoteExpention;
import com.example.mynotesapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class All_Nots_Fragment extends Fragment implements RecyclerViewInterFaceForEachRow {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FireBaseFunctions fireBaseFunctions;
    private RecyclerView recyclerView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public All_Nots_Fragment() {

    }

    public static All_Nots_Fragment newInstance(String param1, String param2) {
        All_Nots_Fragment fragment = new All_Nots_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all__nots_, container, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        recyclerView = rootView.findViewById(R.id.All_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        fireBaseFunctions = new FireBaseFunctions(auth, databaseReference, this.requireContext());

        loadNotes();


        return rootView;
    }

    private void loadNotes() {
        fireBaseFunctions.getNotes(new FireBaseFunctions.Callback_note() {
            @Override
            public void onSuccess(ArrayList<EachNotes> notes) {
                if (isAdded()) {
                    if (notes.isEmpty()) {
                        Toast.makeText(requireContext(), "No notes found", Toast.LENGTH_SHORT).show();
                    } else {
                        MyAdapterClassForEachRow adapter = new MyAdapterClassForEachRow(requireContext(), notes,All_Nots_Fragment.this);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    Log.d("Tag", "Error: " + error);
                    Toast.makeText(requireContext(), "Failed to load notes: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        },"All");
    }

    @Override
    public void onNotesClick(EachNotes eachNotes) {
        Intent intent= new Intent(requireActivity(), NoteExpention.class);
        intent.putExtra("title", eachNotes.getTitle());
        intent.putExtra("description",eachNotes.getDiscription());
        intent.putExtra("category","All");
        startActivity(intent);
    }
}
