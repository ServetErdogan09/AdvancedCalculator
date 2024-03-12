package com.serveterdogan.advancedcalculator;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serveterdogan.advancedcalculator.databinding.ReceyclerRowBinding;

import java.util.ArrayList;

public class HistoryAdaptor extends RecyclerView.Adapter<HistoryAdaptor.History> {

    ArrayList<com.serveterdogan.advancedcalculator.History> arrayList;

    public HistoryAdaptor(ArrayList<com.serveterdogan.advancedcalculator.History> arrayList) {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public History onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReceyclerRowBinding receyclerRowBinding = ReceyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new History(receyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull History holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.textView.setText(arrayList.get(position).result);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                intent.putExtra("geri","old");
                intent.putExtra("id",arrayList.get(position).id);
                System.out.println("id : " + arrayList.get(position).id);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class History extends RecyclerView.ViewHolder{

        private ReceyclerRowBinding binding;
        public History(ReceyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
