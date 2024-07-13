package com.example.mynotesapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fitness_notes_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fitness_notes_fragment extends Fragment implements RecyclerViewInterFaceForEachRow {
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FireBaseFunctions fireBaseFunctions;
    private RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fitness_notes_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fitness_notes_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fitness_notes_fragment newInstance(String param1, String param2) {
        Fitness_notes_fragment fragment = new Fitness_notes_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
                        MyAdapterClassForEachRow adapter = new MyAdapterClassForEachRow(requireContext(), notes,Fitness_notes_fragment.this);
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
        },"Fitness");
    }

    @Override
    public void onNotesClick(EachNotes eachNotes) {
        Intent intent= new Intent(requireActivity(), NoteExpention.class);
        intent.putExtra("title", eachNotes.getTitle());
        intent.putExtra("description",eachNotes.getDiscription());
        intent.putExtra("category","Fitness");
        startActivity(intent);
    }
}