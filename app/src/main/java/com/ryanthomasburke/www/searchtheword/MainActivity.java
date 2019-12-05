package com.ryanthomasburke.www.searchtheword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creates the highscore files if it doesn't exist yet.
        createHighScoreFiles();
    }

    private void createHighScoreFiles(){
        try {

            File myFile = new File(getFilesDir()+"/highscores.txt");
            if (!myFile.exists()){
                myFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
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

    public void goToInstructions(View view){
        try{
            Intent intent = new Intent(this, InstructionsActivity.class);
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
