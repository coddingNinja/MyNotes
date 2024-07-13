package com.example.mynotesapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotesapp.EachNotes;
import com.example.mynotesapp.Interfaces.RecyclerViewInterFaceForEachRow;
import com.example.mynotesapp.R;

import java.util.ArrayList;
import java.util.Random;

public class MyAdapterClassForEachRow extends RecyclerView.Adapter<MyAdapterClassForEachRow.ViewHolderClass> {

    Context context;
    ArrayList<EachNotes> eachNotes;
    private RecyclerViewInterFaceForEachRow recyclerViewInterFaceForEachRow;
    private int[] colors;

    public MyAdapterClassForEachRow(Context context, ArrayList<EachNotes> eachNotes, RecyclerViewInterFaceForEachRow recyclerViewInterFaceForEachRow) {
        this.context = context;
        this.eachNotes = eachNotes;
        this.recyclerViewInterFaceForEachRow = recyclerViewInterFaceForEachRow;
        this.colors = new int[]{
                ContextCompat.getColor(context, R.color.orange),
                ContextCompat.getColor(context, R.color.yellow),
                ContextCompat.getColor(context, R.color.skyBlue),
                ContextCompat.getColor(context, R.color.brown)
        };
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.eachrow, parent, false);
        return new ViewHolderClass(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, @SuppressLint("RecyclerView") int position) {
        EachNotes note = eachNotes.get(position);
        holder.title.setText(note.getTitle());
        holder.discription.setText(note.getDiscription());

        int random= new Random().nextInt(colors.length);
        holder.cardView.setCardBackgroundColor(colors[random]);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterFaceForEachRow.onNotesClick(note);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eachNotes.size();
    }

    class ViewHolderClass extends RecyclerView.ViewHolder {
        TextView title, discription;
        CardView cardView;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.each_row_cardview);
            title = itemView.findViewById(R.id.title);
            discription = itemView.findViewById(R.id.description);
        }

    }
}
