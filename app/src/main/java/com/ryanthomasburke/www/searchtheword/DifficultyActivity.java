package com.ryanthomasburke.www.searchtheword;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ryanthomasburke.www.searchtheword.Utility.Container;

public class DifficultyActivity extends AppCompatActivity {

    public static Container container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
        container = new Container();
    }

    public void easyButton(View view){
        container.setDifficulty(1);
        container.setTotalScore(0);
        container.setCurrentLevel(1);
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
    }

    public void mediumButton(View view){
        container.setDifficulty(2);
        container.setTotalScore(0);
        container.setCurrentLevel(1);
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
    }

    public void hardButton(View view){
        container.setDifficulty(3);
        container.setTotalScore(0);
        container.setCurrentLevel(1);
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
    }



}
