package com.example.provatennispadel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity{
        //implements AdapterView.OnItemClickListener {

    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        b2 = findViewById(R.id.btn2);
        b2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }
/*
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }*/

}
