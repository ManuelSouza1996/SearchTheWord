package com.group4.searchtheword;

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

    public void goToScreen1(View v) {
        Intent intent = new Intent ( this, Screen1Activity.class);
        this.startActivity(intent);
    }
}
