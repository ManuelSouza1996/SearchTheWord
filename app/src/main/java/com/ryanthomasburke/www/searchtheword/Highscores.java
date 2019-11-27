package com.ryanthomasburke.www.searchtheword;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Highscores extends AppCompatActivity {
    int[] easyScores = new int[3];
    int[] mediumScores = new int[3];
    int[] hardScores = new int[3];
    int[] allScores = new int[9];
    String path = "highscores.txt";


    @Override
    protected void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        // TODO THESE TEXTVIEWS ARE THROWING ERRORS, NO IDEA WHY
        setContentView(R.layout.activity_highscores);
        read();
        System.out.println(easyScores[1]);
        TextView easy1 = findViewById(R.id.easy1);
        TextView easy2 = findViewById(R.id.easy2);
        TextView easy3 = findViewById(R.id.easy3);
        TextView medium1 = findViewById(R.id.medium1);
        TextView medium2 = findViewById(R.id.medium2);
        TextView medium3 = findViewById(R.id.medium3);
        TextView hard1 = findViewById(R.id.hard1);
        TextView hard2 = findViewById(R.id.hard2);
        TextView hard3 = findViewById(R.id.hard3);
        easy1.setText(Integer.toString(easyScores[2]));
        easy2.setText(Integer.toString(easyScores[1]));
        easy3.setText(Integer.toString(easyScores[0]));
        medium1.setText(Integer.toString(mediumScores[2]));
        medium2.setText(Integer.toString(mediumScores[1]));
        medium3.setText(Integer.toString(mediumScores[0]));
        hard1.setText(Integer.toString(hardScores[2]));
        hard2.setText(Integer.toString(hardScores[1]));
        hard3.setText(Integer.toString(hardScores[0]));
    }

    public void read(){
        BufferedReader br;
        try {
            br = new BufferedReader(
                    new InputStreamReader(getAssets().open("highscores.txt")));

            for (int i = 0; i < 9; i++){
                allScores[i] = Integer.parseInt(br.readLine());
                System.out.println(i + ":" + allScores[i]);
            }
            for (int i = 0; i < 3; i ++) {
                easyScores[i] = allScores[i];
            }
            for (int i = 0; i < 3; i ++) {
                mediumScores[i] = allScores[i + 3];
            }
            for (int i = 0; i < 3; i ++) {
                hardScores[i] = allScores[i + 6];
            }
            for (int i = 0; i < 3; i ++) {
                System.out.println(hardScores[i]);
                System.out.println(mediumScores[i]);
                System.out.println(easyScores[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void replaceScore(int score, int[] scores) {

        boolean scoreIsHighEnough = false;

        if(score > scores[0]) {
            System.out.println("Score is greater than [0]");
            scoreIsHighEnough = true;
            if (score > scores[1]) {
                System.out.println("Score is greater than [1]");
                if(score > scores[2]) {
                    System.out.println("Score is greater than [2]");
                    scores[2] = score;
                } else {scores[1] = score;}
            } else {scores[0] = score;}
        } else {System.out.println("Too low");}

    }

    public void updateFile(String path) {
        PrintWriter writer;

        for (int i = 0; i < 3; i++) {
            allScores[i] = easyScores[i];
        }

        for (int i = 0; i < 3; i++) {
            allScores[i + 3] = mediumScores[i];
        }

        for (int i = 0; i < 3; i++) {
            allScores[i + 6] = hardScores[i];
        }

        try {
            writer = new PrintWriter(path, "UTF-8");
            for (int i = 0; i < allScores.length; i ++) {
                if (i < (allScores.length - 1)){
                    writer.println(allScores[i]);
                }
                else {writer.print(allScores[i]);};

            }
            writer.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
