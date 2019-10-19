package com.ryanthomasburke.www.searchtheword;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
    }

    public void goToGame(View view){
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
    }

}
