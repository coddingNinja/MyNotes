package com.example.mynotesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotesapp.Interfaces.RecyclerViewInterface_Buttons;
import com.example.mynotesapp.R;

import java.util.ArrayList;

public class MyAdapterClassForCategories extends RecyclerView.Adapter<MyAdapterClassForCategories.ViewHolderClass> {

private final RecyclerViewInterface_Buttons recyclerViewInterfaceButtons;
    Context context;
    ArrayList<String> categories;
    public static ArrayList<Button> buttons=new ArrayList<>();
    private int[] colors;


    public MyAdapterClassForCategories(Context context, ArrayList<String> categories,RecyclerViewInterface_Buttons recyclerViewInterfaceButtons) {
        this.context = context;
        this.categories = categories;
        this.recyclerViewInterfaceButtons=recyclerViewInterfaceButtons;
        this.colors = new int[]{
                ContextCompat.getColor(context, R.color.white),
                ContextCompat.getColor(context, R.color.black),

        };
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.each_category,parent,false);
        return new ViewHolderClass(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        holder.category.setText(categories.get(position));
        buttons.add(holder.category);
        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               recyclerViewInterfaceButtons.onItemClick(holder.category);

                for (int i = 0; i < buttons.size(); i++) {
                    if (i==position)
                    {
                        Button b=buttons.get(position);
                        b.setTextColor(colors[1]);
                        b.setBackgroundColor(colors[0]);
                    }
                    else
                    {
                        Button b=buttons.get(i);
                        b.setTextColor(colors[0]);
                        b.setBackgroundColor(colors[1]);
                        b.setBackgroundResource(R.drawable.button_each_);
                    }
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class  ViewHolderClass extends RecyclerView.ViewHolder {

        Button category;
        CardView cardView;
        public  ViewHolderClass(@NonNull View itemView) {

            super(itemView);
            category=itemView.findViewById(R.id.each_category);
            cardView=itemView.findViewById(R.id.catecory_button);

        }
    }
}
