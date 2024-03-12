package com.serveterdogan.advancedcalculator;


import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.mariuszgromada.math.mxparser.*; // matematiksel işlemlerin sonucunu bu kütpahaneden alacağız kendi kütpahanemize kaydetik

import com.serveterdogan.advancedcalculator.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int flag = 0;


    String result;

    SQLiteDatabase database;

    int id;

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        database = this.openOrCreateDatabase("History",MODE_PRIVATE,null);




        // otomatik klavye çıkmasın diye
       binding.display.setShowSoftInputOnFocus(false);


       Intent intent = getIntent();
       id = intent.getIntExtra("id",0);
       String write = intent.getStringExtra("geri");
       
       if (write != null){
           if (write.equals("old")){
               try {
                   Cursor cursor = database.rawQuery("SELECT * FROM history WHERE id = ?", new String[]{String.valueOf(id)});
                   // id aldık
                   int  resultId = cursor.getColumnIndex("result");
                   if (cursor.moveToFirst()){
                       String result = cursor.getString(resultId);
                       binding.display.setText(result);
                   }
                   cursor.close();
               }catch (Exception e){
                   e.printStackTrace();
               }
           }
       }

       //  Cursor cursor = database.rawQuery("SELECT*FROM arts WHERE id = ?",new String[]{String.valueOf(id)});


    }

    public void anyButton(View view){

       if (view.getId() == R.id.Clear){
           binding.display.setText("");
       }else if (view.getId() == R.id.brackets){
           addBrackets();
       }else if (view.getId() == R.id.square){
          updateDisplay("^");
       }else if (view.getId() == R.id.divide){
          updateDisplay("/");
       }else if (view.getId() == R.id.seven){
           updateDisplay("7");
       }else if (view.getId() == R.id.eight){
           updateDisplay("8");
       }else if (view.getId() == R.id.nine){
           updateDisplay("9");
       }else if (view.getId() == R.id.impact){
           updateDisplay("x");
       }else if (view.getId() == R.id.four){
           updateDisplay("4");
       }else if (view.getId() == R.id.five){
           updateDisplay("5");
       }else if (view.getId() == R.id.six){
           updateDisplay("6");
       }else if (view.getId() == R.id.extraction){
           updateDisplay("-");
       }else if (view.getId() == R.id.one){
           updateDisplay("1");
       }else if (view.getId() == R.id.two){
           updateDisplay("2");
       }else if (view.getId() == R.id.three){
           updateDisplay("3");
       }else if (view.getId() == R.id.collection){
           updateDisplay("+");
       }else if (view.getId() == R.id.zeros){
          //000
           updateDisplayTheZeros();
       }else if (view.getId() == R.id.zero){
           updateDisplay("0");
       }else if (view.getId() == R.id.point){
           updateDisplay(".");
       }else if (view.getId() == R.id.result){
           flag = 1;
          result();
       }else if (view.getId() == R.id.delete){
           delete();
       }
    }

    private  void result() {

        String textDisplay = binding.display.getText().toString();

        // x gördüğün her yere * yaz
        String newTextDisplay = textDisplay.replaceAll("x","*");
        Expression values = new Expression(newTextDisplay); // Epression values nesnesini verecek bize bizde onu String'e çevirdik

         result = String.valueOf(values.calculate());

        // Nan değilse hata yoksa yazdır
        if (!result.equalsIgnoreCase("NaN")){
            binding.display.setText(result);
            binding.display.setSelection(result.length());
            // Sonucu veritabanına ekle

        }else {
            binding.display.setText("Error");
            Toast.makeText(MainActivity.this,"Lütfen İşlemleri Kontrol Ediniz!! ",Toast.LENGTH_LONG).show();
        }

        try{

            String result = binding.display.getText().toString();
            database.execSQL("CREATE TABLE IF NOT EXISTS history(id INTEGER PRIMARY KEY ,result VARCHAR )");
            String sqlString = "INSERT INTO history(result) VALUES(?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1,result);
            sqLiteStatement.execute();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateDisplayTheZeros() {

        if (getString(R.string.displayZero).equals(binding.display.getText().toString())) {
            binding.display.setText("000");
        }else {
            // imleçin nerde olduğunu verecek bize
            int cursor = binding.display.getSelectionStart();

            String oldDisplay = binding.display.getText().toString();
            // 12345x12-234

            String lefSidDisplay = oldDisplay.substring(0,cursor);
            String rightSidDisplay = oldDisplay.substring(cursor);
            String newDisplay = lefSidDisplay + "000" + rightSidDisplay;
            binding.display.setText(newDisplay);
            binding.display.setSelection(cursor+3);
        }

    }

    private void updateDisplay(String addCharTODisplay) {

        int cursor = binding.display.getSelectionStart();
        if (getString(R.string.displayZero).equals(binding.display.getText().toString())){
            binding.display.setText(addCharTODisplay);

        }else {
            // imleçin nerde olduğunu verecek bize

            String oldDisplay = binding.display.getText().toString();
            // 12345x12-234
            String lefSidDisplay = oldDisplay.substring(0,cursor);
            String rightSidDisplay = oldDisplay.substring(cursor);
            String newDisplay = lefSidDisplay + addCharTODisplay + rightSidDisplay;
            binding.display.setText(newDisplay);
        }
        binding.display.setSelection(cursor+1);
    }

    private void addBrackets() {

        // (1234-(-123)x23
        String textDisplay = binding.display.getText().toString();
        int cursorPos = binding.display.getSelectionStart();
        int countBrackets = 0;
        for (int i = 0; i < textDisplay.length(); i++) {
           if (textDisplay.substring(i,i+1).equalsIgnoreCase("(")) countBrackets++;
           if (textDisplay.substring(i,i+1).equalsIgnoreCase(")")) countBrackets--;

        }

        String lastCharOfTextDisplay = textDisplay.substring(textDisplay.length()-1);

        if (countBrackets == 0 || lastCharOfTextDisplay.equals("(")) updateDisplay("(");
        else if (countBrackets > 0 && !lastCharOfTextDisplay.equals(")")) updateDisplay(")");

    }
    private void delete(){

        String textDisplay = binding.display.getText().toString();
        int cursorPos = binding.display.getSelectionStart();


        if (getString(R.string.displayZero).equals(binding.display.getText().toString())){
            binding.display.setText("0");

        }else if (cursorPos > 0) {

            String lefDisplay = textDisplay.substring(0,cursorPos-1);
            String rightDisplay = textDisplay.substring(cursorPos);
            String newDisplay = lefDisplay +  rightDisplay;
            binding.display.setText(newDisplay);
            binding.display.setSelection(cursorPos-1);
        }

        }

    public void historyText(View view){

        Intent intent = new Intent(this,HistoryActivity.class);
        startActivity(intent);
    }


}

