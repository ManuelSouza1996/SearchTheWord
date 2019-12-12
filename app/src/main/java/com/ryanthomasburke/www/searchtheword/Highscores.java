// written by: Antonia created the original file when creating the GUI.
// Ryan created all the functions
//Jesse created created the 2 test function to get proper reads and write to internal files. Added a few lines to Ryan's code to read from internal files instead of Asset files.
// tested by:// Everyone via play the game
// debugged by: Jesse

package com.ryanthomasburke.www.searchtheword;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileReader;


public class Highscores extends AppCompatActivity {
    int[] easyScores = new int[3];
    int[] mediumScores = new int[3];
    int[] hardScores = new int[3];
    int[] allScores = new int[9];
    String path = "highscores.txt";
    private TextView hard3;
    private TextView hard2;
    private TextView hard1;





    @Override
    protected void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        // TODO THESE TEXTVIEWS ARE THROWING ERRORS, NO IDEA WHY
        setContentView(R.layout.activity_highscores);
        TextView easy1 = findViewById(R.id.easy1);
        TextView easy2 = findViewById(R.id.easy2);
        TextView easy3 = findViewById(R.id.easy3);
        TextView medium1 = findViewById(R.id.medium1);
        TextView medium2 = findViewById(R.id.medium2);
        TextView medium3 = findViewById(R.id.medium3);
        hard1 = findViewById(R.id.hard1);
        hard2 = findViewById(R.id.hard2);
        hard3 = findViewById(R.id.hard3);
        read();
        easy1.setText(Integer.toString(easyScores[0]));
        easy2.setText(Integer.toString(easyScores[1]));
        easy3.setText(Integer.toString(easyScores[2]));
        medium1.setText(Integer.toString(mediumScores[0]));
        medium2.setText(Integer.toString(mediumScores[1]));
        medium3.setText(Integer.toString(mediumScores[2]));
        hard1.setText(Integer.toString(hardScores[0]));
        hard2.setText(Integer.toString(hardScores[1]));
        hard3.setText(Integer.toString(hardScores[2]));



    }


    public void read(){
        BufferedReader br;
        try {
            File myFile = new File(getFilesDir()+"/highscores.txt");
            FileReader fr =new FileReader(myFile);
            br = new BufferedReader(fr);

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

    //A Working example of Writing from file on android studio
    //Source to read and write to files :https://developer.android.com/training/data-storage
    private void test(){
        try {

            File myFile = new File(getFilesDir()+"/highscores.txt");
            if (!myFile.exists()){
                myFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append("30\n");
                myOutWriter.append("20\n");
                myOutWriter.append("10\n");
                myOutWriter.append("60\n");
                myOutWriter.append("50\n");
                myOutWriter.append("40\n");
                myOutWriter.append("90\n");
                myOutWriter.append("80\n");
                myOutWriter.append("70\n");
                myOutWriter.close();
                fOut.close();
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    //A Working example of reading from file on android studio
    private void test2() {
        BufferedReader br;
        try {
            File myFile = new File(getFilesDir()+"/highscores.txt");
            FileReader fr =new FileReader(myFile);
            br = new BufferedReader(fr);

            hard3.setText(br.readLine().toString());
        }
        catch (IOException e){
            //
        }


    }
}
