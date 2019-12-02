package com.ryanthomasburke.www.searchtheword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToDifficulty(View view){
        try{
            Intent intent = new Intent(this, DifficultyActivity.class);
            System.out.println(intent.toString());
            this.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void goToHighscore(View view){
        try{
            Intent intent = new Intent(this, Highscores.class);
            System.out.println(intent.toString());
            this.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
