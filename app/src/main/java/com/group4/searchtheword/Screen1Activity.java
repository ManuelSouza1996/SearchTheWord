package com.group4.searchtheword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Screen1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);
    }

    public void goToScreen2(View v) {
        Intent intent = new Intent(this, Screen2Activity.class);
        this.startActivity(intent);
    }
    public void back(View v) {
        this.finish();
    }
}
