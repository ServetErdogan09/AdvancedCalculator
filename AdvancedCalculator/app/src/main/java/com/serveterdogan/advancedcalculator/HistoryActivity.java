package com.serveterdogan.advancedcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.serveterdogan.advancedcalculator.databinding.ActivityHistoryBinding;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;

    ArrayList<History> arrayList;

    HistoryAdaptor adaptor;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayList = new ArrayList<>();

        adaptor = new HistoryAdaptor(arrayList);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adaptor);

        getData();



    }
    public void getData(){


        try {

            database = this.openOrCreateDatabase("History",MODE_PRIVATE,null);

            Cursor cursor = database.rawQuery("SELECT * FROM history",null);

            int resultIx = cursor.getColumnIndex("result");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()){

                String result = cursor.getString(resultIx);
                int id = Integer.parseInt(cursor.getString(idIx));
                History history = new History(result,id);

                arrayList.add(history);
            }
            adaptor.notifyDataSetChanged();
            cursor.close();

        }catch (Exception e){

            e.printStackTrace();
        }

    }
}