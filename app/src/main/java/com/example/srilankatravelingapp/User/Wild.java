package com.example.srilankatravelingapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.srilankatravelingapp.R;
import com.example.srilankatravelingapp.User.WildPlace.SinharajaForest;
import com.example.srilankatravelingapp.User.WildPlace.Yala;

public class Wild extends AppCompatActivity {

    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wild);

        backBtn = findViewById(R.id.back_pre);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Wild.super.onBackPressed();

            }
        });
    }


    public void sinhraja(View view) {
        startActivity(new Intent(this, SinharajaForest.class));

    }

    public void yala(View view) {
        startActivity(new Intent(this, Yala.class));

    }
}