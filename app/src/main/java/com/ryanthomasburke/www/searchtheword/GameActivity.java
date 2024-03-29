
// written by: Antonia created the original file,
// Ryan created the word grid and the event handler for the wordgrid for highlight word event, he also originally had a sample code to display a static word game with a few working static words. Ryan also started container code and left code example to be adopted.
//Jesse created get word from file, put word in wordgrid, timer, scores, and added to the listner ryan created to handle the win lose game over conditions. Created remaining code in file.
// tested by:// Everyone via play the game
// debugged by: Jesse

package com.ryanthomasburke.www.searchtheword;

//Implementing timer Source: https://www.youtube.com/watch?v=MDuGwI6P-X8  +  https://codinginflow.com/tutorials/android/countdowntimer/part-1-countdown-timer
// Some of the logic in this class is loosely based on the
// code of the projects listed below.
// https://github.com/rjbasitali/word-search-custom-view-android
// https://github.com/abdularis/Word-Search-Game


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ryanthomasburke.www.searchtheword.Utility.Container;
import com.ryanthomasburke.www.searchtheword.Utility.Word;
import com.ryanthomasburke.www.searchtheword.Views.WordGrid;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.InputStreamReader;
import android.os.CountDownTimer; //used for timer
import java.util.Locale;          //used for timer
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class GameActivity extends AppCompatActivity {

    private WordGrid wordGrid;
    private int currentLevel;
    private long currentTimer;
    private double difficulty;
    private int numWords;
    private int pointsPerWord;
    private int totalScore;
    private TextView wordBank;
    private TextView wordFound;
    private TextView timerView;
    private TextView scoreView;
    private TextView levelView;
    private Button nextLevelButton;
    private CountDownTimer theCountDownTimer;
    private boolean timeRunning;
    private long timeRemaining;
    private ArrayList<String[]> wordList = new ArrayList<>();
    private char[][] letterGrid;
    private boolean gameover = false;
    Container container;




    //This is where the MAIN PROGRAM starts when Game Activity Loads.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // This creates a container that "clones"
        // the container in Difficulty Activity.
        // Just to check, there is a Sys.out...
        // to verify it has correct data.
        container = DifficultyActivity.container;
        System.out.println("Dif: " + container.getDifficulty());
        wordGrid = findViewById(R.id.wordsGrid);
        wordBank = findViewById(R.id.wordBank);
        wordFound = findViewById(R.id.wordFound);
        currentLevel = container.getCurrentLevel();
        difficulty = container.getDifficulty();
        numWords =5 + Math.min((int)Math.ceil(difficulty*currentLevel),5);
        pointsPerWord = 5 + (int)(10*difficulty);
        totalScore = container.getTotalScore();


        //setup timer variables and display score, timer, and current level
        currentTimer = (long)(Math.max(((int)(90/difficulty)) - (int)(currentLevel*difficulty), 10))*1000;
        timeRemaining = currentTimer;
        timeRunning = false;
        timerView = findViewById(R.id.gameTimer);
        scoreView = findViewById(R.id.gameScore);
        levelView = findViewById(R.id.gameLevel);
        scoreView.setText(String.valueOf(totalScore));
        nextLevelButton = findViewById(R.id.btnNextLevel);
        nextLevelButton.setVisibility(View.INVISIBLE);
        levelView.setText(String.valueOf(currentLevel));

        //get words, place word in char array, and place words in wordGrid.
        final String[] words = getWords(numWords);
        letterGrid = makeLetterGrid(words);
        Word[] newWordList = new Word[numWords];
        for (int i = 0; i < words.length; i ++){
            newWordList[i] = new Word(wordList.get(i)[0],false,Integer.valueOf(wordList.get(i)[2]),Integer.valueOf(wordList.get(i)[3]), Integer.valueOf(wordList.get(i)[4]),Integer.valueOf(wordList.get(i)[5]));
        }
        wordGrid.setWords(newWordList);
        wordGrid.setLetters(letterGrid);


        //Starts Timer
        startTimer();
        updateCountDownText();


        //Event Handler for when a word is found
        wordGrid.setOnWordSearchedListener(new WordGrid.OnWordSearchedListener() {
            @Override
            public void wordFound(String string) {
                if (gameover == false) {
                    Toast.makeText(GameActivity.this, string + " found", Toast.LENGTH_SHORT).show();
                    updatePoints(pointsPerWord);  //adds score
                    addToTimer(4000);

                    checkOffWord(string);
                    playerWins();

                    if (playerWins()){
                        pauseTimer();
                        wordBank.setText("You Win\n");
                        wordBank.append("Time Bonus Points:" + (timeRemaining/1000/2) +"\n");
                        wordBank.append("Current Points:" + totalScore +"\n");
                        totalScore = totalScore + (int)(timeRemaining/1000/2);
                        wordBank.append("Total Points:" + totalScore +"\n");
                        nextLevelButton.setVisibility(View.VISIBLE);

                    }

                }




            }
        });


    }

    public void NextLevel(View view){
        container.setCurrentLevel(container.getCurrentLevel() +1);
        container.setTotalScore(totalScore);
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
        finish();
    }

    //Starts Timer for the game
    private void startTimer() {
        theCountDownTimer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long timeUntilFinished) {
                timeRemaining = timeUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeRunning = false;
                //Handles win and lose condition
                gameover = true;
                wordGrid.setGameOver(true);
                wordBank.setText("GAME OVER \n");
                wordBank.append("Final Score:" + totalScore +"\n");

                if(checkHighScore()){
                    wordBank.append("CONGRATULATIONS\n New Record\n");
                }


            }
        }.start();

        timeRunning = true;
    }


    //Adds extra time to current timer
    private void addToTimer(long extraTimeInMilliSeconds){
        pauseTimer();
        timeRemaining = timeRemaining + extraTimeInMilliSeconds;
        startTimer();
    }
    //Pause Game Timer
    private void pauseTimer() {
        theCountDownTimer.cancel();
        timeRunning = false;
        //Code below may need to be modified for settings



    }

    //Checks High Score
    private boolean checkHighScore(){

        String[] highScore = new String[10];  //There are only 9 highscores
        int wordInFileCount =0;
        //reads HighScore File

        BufferedReader br = null;
        try {
            File myFile = new File(getFilesDir()+"/highscores.txt");
            FileReader fr =new FileReader(myFile);
            br = new BufferedReader(fr);

            String mLine;
            while ((mLine = br.readLine()) != null) {
                highScore[wordInFileCount] = mLine;
                wordInFileCount++;

            }
            //

            Boolean newHighScore = false;
            int tempTotalScore = totalScore;
            //Easy
            if (difficulty == 1.0) {
                for (int i =0; i < 3; i++){
                    int tempInt = Integer.parseInt(highScore[i].toString());
                    if (tempTotalScore > tempInt){
                        highScore[i] = String.valueOf(tempTotalScore);
                        newHighScore = true;
                        tempTotalScore = tempInt;


                    }
                }
            }

            //Medium
            else if (difficulty == 2) {
                for (int i =0; i < 3; i++){
                    int tempInt = Integer.parseInt(highScore[i+3].toString());
                    if (tempTotalScore > tempInt){
                        highScore[i+3] = String.valueOf(tempTotalScore);
                        newHighScore = true;
                        tempTotalScore = tempInt;


                    }
                }
            }
            //Hard
            else if (difficulty == 3) {
                for (int i =0; i < 3; i++){
                    int tempInt = Integer.parseInt(highScore[i+6].toString());
                    if (tempTotalScore > tempInt){
                        highScore[i+6] = String.valueOf(tempTotalScore);
                        newHighScore = true;
                        tempTotalScore = tempInt;


                    }
                }
            }


            //Writes new High Score


            if (newHighScore) {
                //Method to Write goes Below Here:
                try {

                    File myFile2 = new File(getFilesDir()+"/highscores.txt");

                        FileOutputStream fOut = new FileOutputStream(myFile2);
                        OutputStreamWriter myOutWriter =
                                new OutputStreamWriter(fOut);
                        myOutWriter.write(highScore[0].toString()+"\n");
                        myOutWriter.write(highScore[1].toString()+"\n");
                        myOutWriter.write(highScore[2].toString()+"\n");
                        myOutWriter.write(highScore[3].toString()+"\n");
                        myOutWriter.write(highScore[4].toString()+"\n");
                        myOutWriter.write(highScore[5].toString()+"\n");
                        myOutWriter.write(highScore[6].toString()+"\n");
                        myOutWriter.write(highScore[7].toString()+"\n");
                        myOutWriter.write(highScore[8].toString()+"\n");
                        myOutWriter.close();
                        fOut.close();


                } catch (Exception e) {
                    e.getMessage();
                }


                return newHighScore;
            }
            else{
                return false;
            }

        } catch (IOException e) {
            //log the exception
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
        }

        return false;
    }




    //Resets Game Timer
    private void resetTimer() {
        timeRemaining = currentTimer;
        updateCountDownText();
    }

    //Update Timer to display time in the game
    private void updateCountDownText() {
        int minutes = (int) (timeRemaining / 1000) / 60;
        int seconds = (int) (timeRemaining / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timerView.setText(timeLeftFormatted);
    }

    private void updatePoints(int points){
        totalScore += points;
        scoreView.setText(String.valueOf(totalScore));
    }

    /* Function Name: getWords
     * Written By: Jesse Vang
     * Inputs: The number of words one wants to retrieve from a file
     * Outputs:  The requested number of words pulled from file is returned as a String Array.
     * Objective: The methods pulls some amount of a word file and returns those words in a string array.
     * CALLERS: OnCreate()
     * CALLEES:
     * Resource: Loading a file to bufferedStream code layout idea comes from: https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
     *           Reading from a file in Android Studio Code example from Here:  https://stackoverflow.com/questions/9544737/read-file-from-assets
     * Algorithm:
     * 			1.Creates N number of random numbers from the range( 0 to number of words in the wordlistFile  )
     * 			2.Repeat step 1 until all random numbers are unique.
     * 			3.The N random numbers are sorted in ascending order
     * 			4. Reads the word list from the word list File until the Nth row is read and stores nth row word into string Array
     * 			5. Repeat Step 4 until all Nth numbers from N has been read from file and stored into String Array.
     * 			6. String Array is returned
     */
    private String[] getWords(int numberOfWords){
        String[] tmpWordList = new String[numberOfWords];
        int[] wordIndex = new int[numberOfWords];  //holds which word to pull from file
        int wordInFileCount =0;
        //reads Word File to get the length of the file to determine max number of word to later use be used as max randomize number.
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(getAssets().open("wordlist.txt")));

            String mLine;
            while ((mLine = br.readLine()) != null) {
                wordInFileCount++;

            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
        }





        //1.Creates N number of random numbers from 0 to number of words in the wordlistFile
        for (int i = 0; i < wordIndex.length;i++){
            int numToAdd;
            boolean sameNumber = true;
            //2.Repeat step 1 until all random numbers are the different.
            while (sameNumber == true) {
                sameNumber = false;
                numToAdd = ((int) (Math.random() * wordInFileCount)+1);
                System.out.println("Random Number Generated to Add:" + numToAdd +"\n");
                for (int j = 0; j < wordIndex.length; j++) {
                    if ((numToAdd) == wordIndex[j]){
                        sameNumber = true;
                    }
                }
                if (!sameNumber){
                    wordIndex[i] = numToAdd;


                }
            }

        }

        //3.The N numbers are sorted in ascending order
        Arrays.sort(wordIndex, 0, wordIndex.length);


        for (int i =0; i < wordIndex.length; i ++){
            System.out.println("index I = "+  i + " Value = " + wordIndex[i]);
        }

        BufferedReader br2 = null;
        try {
            br2 = new BufferedReader(
                    new InputStreamReader(getAssets().open("wordlist.txt")));
            int counter =0;
            String currentLine ="";

            for (int i = 0; i< wordIndex.length;i++) {

                //4. Reads the word list from the word list File until the Nth row is read and stores nth row word into string Array\
                //5. Repeat Step 4 until all N numbers has been read and stored into String Array.
                while(counter < wordIndex[i]) {
                    currentLine = br2.readLine();
                    counter++;
                    if (counter == wordIndex[i]) {
                        tmpWordList[i] = currentLine.toUpperCase();
                        wordBank.append(currentLine.toUpperCase() + "\n");
                        String[] strAdd =new String[7];

                        strAdd[0] = currentLine.toUpperCase();
                        strAdd[1] = "false";
                        strAdd[2] = "0";
                        strAdd[3] = "0";
                        strAdd[4] = "0";
                        strAdd[5] = "0";
                        strAdd[6] = "false";

                        wordList.add(strAdd);
                    }
                }

            }
            for (int i =0; i < tmpWordList.length; i ++){
                System.out.println("wordIndex= "+  i + " Word = " + tmpWordList[i]);
            }
            br2.close();
        } catch (IOException e) {
            //log the exception
        } finally {
            if (br2 != null) {
                try {
                    br2.close();
                } catch (IOException e) {

                }
            }
        }


        //6. String Array is returned
        return tmpWordList;
    }


    //cross off a word once it's been found in the wordList and remove from wordbank to add to wordFoundList
    private void checkOffWord(String inputWordFound){
        wordBank.setText("");
        wordFound.setText("");
        for (int i = 0;i < wordList.size(); i ++){
            if (wordList.get(i)[0].equals(inputWordFound)){
                String[] tempInput = new String[7];
                tempInput[0] =wordList.get(i)[0].toString();
                tempInput[1] =wordList.get(i)[1].toString();
                tempInput[2] =wordList.get(i)[2].toString();
                tempInput[3] =wordList.get(i)[3].toString();
                tempInput[4] =wordList.get(i)[4].toString();
                tempInput[5] =wordList.get(i)[5].toString();
                tempInput[6] = "true";
                wordList.set(i,tempInput);
            }

            if (wordList.get(i)[6].equals("false")){
                wordBank.append(wordList.get(i)[0].toString() + "\n");

            }
            else {
                wordFound.append(wordList.get(i)[0].toString() + "\n");
            }



        }



    }

    //Checks to see if All words has been checked off
    private boolean playerWins(){
        boolean wins = true;
        for(int i = 0; i < wordList.size(); i ++){
            if (wordList.get(i)[6].equals("false")){
                wins = false;
                break;
            }
        }
        return wins;
    }



    /* Function Name: makeLetterGrid
     * Written By: Jesse Vang
     *
     * Inputs: Takes in a list of word that needs to be placed in the word Search.
     * Outputs:  A word Search grid 10 by 10 will be output as a char[][]
     * Objective: The methods inserts some amount of a words into a 2d array and fills remaining spots with letters.
     * CALLERS: onCreate()
     * CALLEES:
     * Resources: N/A
     * Algorithm:
     * 			1. Creates a grid( 2-d char array with empty characters)
     * 			2.  2a.For Each word to be placed in the grid, generate a random spot(index) on the 2d char grid to try to place current Word in
     * 				2b. Checks Random Index of rows and columns to ensure it is valid (empty or contains same letter as first letter of word to put in), if already repeat 2a-2b.
     * 			3. Test to see if Top,Bottom,Left,Right,diagonal from current spot would fit the length of the Word
     * 			4. Test to see if if either top bottom right left areas for the word to be place is valid
     * 			5. Choose a direction that meets test from step 3&4, otherwise repeat Steps 2-4
     * 			6. Repeats steps 2 to 5 until all words from list has been placed on grid.
     * 			7. Fill in remaining Grid spots with random letter
     * 			8. Returns grid (2-d Char Array)
     *
     *Resource: Loading a file idea comes from: https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
     *			Case statement examples from: https://www.geeksforgeeks.org/switch-statement-in-java/
     */
    private char[][] makeLetterGrid(String[] strWordlist){

        int placementAttempts =0;
        //1. Creates a grid( 2-d char array with empty characters)
        char[][] tmpLetterGrid = new char[10][10];
        for (int i =0; i < tmpLetterGrid.length;i++){
            for (int j = 0; j < tmpLetterGrid[i].length;j++) {
                tmpLetterGrid[i][j] = ' ';
            }

        }

        //Inserts each Words from list into letterGrid
        for(int i =0; i < strWordlist.length;i++) {
            int row=0,column=0;
            int wordlength = strWordlist[i].length();

            boolean chosenSpot = false;
            ArrayList<String> validChoice = new ArrayList<String>();

            while (chosenSpot == false){
                //2. For Each word to be placed in the grid, generate a random spot(row and column index)
                //on the grid to try to place the current Word from word list.
                row = (int)((Math.random()*10) );
                column = (int)((Math.random()*10) );
                if (tmpLetterGrid[row][column] == ' ') {
                    chosenSpot = true;

                }
            }

            boolean top,bottom,left, right, topRight, topLeft, bottomRight,bottomLeft;
            //3. Test to see if Top,Bottom,Left,Right from current spot would fit the length of the Word
            //4. Test to see if if either top bottom right left areas for the word to be place is valid

            //test Top
            if ((row+1)-wordlength>=0){
                top = false;
                int strCount =0;
                for(int j =0; j< wordlength;j++) {
                    if ((tmpLetterGrid[row-j][column] == Character.toUpperCase((char)(strWordlist[i].charAt(j))))||(tmpLetterGrid[row-j][column] == ' ')) {

                        strCount++;
                    }
                    if (strCount == wordlength) {
                        top= true;
                        validChoice.add("top");
                    }
                }

            }
            else {top = false;}

            //test Bottom
            if ((row-1)+wordlength<tmpLetterGrid.length) {
                bottom = false;
                int strCount =0;
                for(int j =0; j< wordlength;j++) {
                    if ((tmpLetterGrid[row+j][column] == Character.toUpperCase((char)(strWordlist[i].charAt(j))))||(tmpLetterGrid[row+j][column] == ' ')) {
                        strCount++;
                    }
                    if (strCount == wordlength) {
                        bottom= true;
                        validChoice.add("bottom");
                    }
                }
            }
            else {bottom = false;}



            //test Left or "backwards"
            if ((column+1)-wordlength>=0){
                left = false;
                int strCount =0;
                for(int j =0; j< wordlength;j++) {
                    if ((tmpLetterGrid[row][column-j] == Character.toUpperCase((char)(strWordlist[i].charAt(j))))||(tmpLetterGrid[row][column-j] == ' ')) {
                        strCount++;
                    }
                    if (strCount == wordlength) {
                        left= true;
                        validChoice.add("left");
                    }
                }
            }


            else {left = false;}

            //test Right
            if ((column-1)+wordlength<tmpLetterGrid.length){
                right = false;
                int strCount =0;
                for(int j =0; j< wordlength;j++) {
                    if ((tmpLetterGrid[row][column+j] == Character.toUpperCase((char)(strWordlist[i].charAt(j))))||(tmpLetterGrid[row][column+j] == ' ')) {
                        strCount++;
                    }
                    if (strCount == wordlength) {
                        right= true;
                        validChoice.add("right");
                    }
                }

            }
            else {right = false;}

            //Test Top Right

            if (((column-1)+wordlength<tmpLetterGrid.length)&&((row+1)-wordlength>=0)){
                topRight = false;
                int strCount =0;
                for(int j =0; j< wordlength;j++) {
                    if ((tmpLetterGrid[row-j][column+j] == Character.toUpperCase((char)(strWordlist[i].charAt(j))))||(tmpLetterGrid[row-j][column+j] == ' ')) {
                        strCount++;
                    }
                    if (strCount == wordlength) {
                        topRight= true;
                          validChoice.add("topRight");
                    }
                }

            }
            else {topRight = false;}


            //Test Top Left

            if (((column+1)-wordlength>=0)&&(((row+1)-wordlength>=0))){
                topLeft = false;
                int strCount =0;
                for(int j =0; j< wordlength;j++) {
                    if ((tmpLetterGrid[row-j][column-j] == Character.toUpperCase((char)(strWordlist[i].charAt(j))))||(tmpLetterGrid[row-j][column-j] == ' ')) {
                        strCount++;
                    }
                    if (strCount == wordlength) {
                        topLeft= true;
                           validChoice.add("topLeft");
                    }
                }
            }


            else {topLeft = false;}

            //Test Bottom Right

            if (((column-1)+wordlength<tmpLetterGrid.length)&&((row-1)+wordlength<tmpLetterGrid.length)){
                bottomRight = false;
                int strCount =0;
                for(int j =0; j< wordlength;j++) {
                    if ((tmpLetterGrid[row+j][column+j] == Character.toUpperCase((char)(strWordlist[i].charAt(j))))||(tmpLetterGrid[row+j][column+j] == ' ')) {
                        strCount++;
                    }
                    if (strCount == wordlength) {
                        bottomRight= true;
                        validChoice.add("bottomRight");
                    }
                }

            }
            else {topRight = false;}


            //Test Bottom Left
            if (((row-1)+wordlength<tmpLetterGrid.length)&&((column+1)-wordlength>=0)){
                bottomLeft = false;
                int strCount =0;
                for(int j =0; j< wordlength;j++) {
                    if ((tmpLetterGrid[row+j][column-j] == Character.toUpperCase((char)(strWordlist[i].charAt(j))))||(tmpLetterGrid[row+j][column-j] == ' ')) {
                        strCount++;
                    }
                    if (strCount == wordlength) {
                        bottomLeft= true;
                        validChoice.add("bottomLeft");
                    }
                }

            }
            else {topRight = false;}


            //5. Choose a random valid direction that meets test from step 3&4 and place word in grid

            int randomChoice = 0;
            if (!validChoice.isEmpty()){
                randomChoice = (int)(Math.random() *validChoice.size());
                String choice = validChoice.get(randomChoice).toString();
                switch(choice) {
                    case "right":
                        for (int j = 0; j < strWordlist[i].length();j++) {
                            tmpLetterGrid[row][column+j] = Character.toUpperCase((char)(strWordlist[i].charAt(j)));
                        }
                        System.out.println("Word: " + strWordlist[i].toString() + " rowColum:"+ row +""+column + " Goes Right");
                        String[] strAdd = wordList.get(i);
                        strAdd[2]= column+"";
                        strAdd[3]= row+"";
                        strAdd[4]= column-1+strWordlist[i].length()+"";
                        strAdd[5]= row+"";
                        wordList.set(i,strAdd);
                        break;
                    case "left":
                        for (int j = 0; j < strWordlist[i].length();j++) {
                            tmpLetterGrid[row][column-j] = Character.toUpperCase((char)(strWordlist[i].charAt(j)));
                        }
                        System.out.println("Word: " + strWordlist[i].toString() + " rowColum:"+ row +""+column + " Goes Left");
                        String[] strAdd2 = wordList.get(i);
                        strAdd2[2]= column+"";
                        strAdd2[3]= row+"";
                        strAdd2[4]= column+1-strWordlist[i].length()+"";
                        strAdd2[5]= row+"";
                        wordList.set(i,strAdd2);
                        break;

                    case "top":
                        for (int j = 0; j < strWordlist[i].length();j++) {
                            tmpLetterGrid[row-j][column] = Character.toUpperCase((char)(strWordlist[i].charAt(j)));

                        }
                        System.out.println("Word: " + strWordlist[i].toString() + " rowColum:"+ row +""+column + " Goes Top");
                        String[] strAdd3 = wordList.get(i);
                        strAdd3[2]= column+"";
                        strAdd3[3]= row+"";
                        strAdd3[4]= column+"";
                        strAdd3[5]= row+1-strWordlist[i].length()+"";
                        wordList.set(i,strAdd3);
                        break;
                    case "bottom":
                        for (int j = 0; j < strWordlist[i].length();j++) {
                            tmpLetterGrid[row+j][column] = Character.toUpperCase((char)(strWordlist[i].charAt(j)));
                        }
                        System.out.println("Word: " + strWordlist[i].toString() + " rowColum:"+ row +""+column + " Goes Bottom");
                        String[] strAdd4 = wordList.get(i);
                        strAdd4[2]= column+"";
                        strAdd4[3]= row+"";
                        strAdd4[4]= column+"";
                        strAdd4[5]= row-1+strWordlist[i].length()+"";
                        wordList.set(i,strAdd4);
                        break;

                    case "topRight":
                        for (int j = 0; j < strWordlist[i].length();j++) {
                            tmpLetterGrid[row-j][column+j] = Character.toUpperCase((char)(strWordlist[i].charAt(j)));
                        }
                        System.out.println("Word: " + strWordlist[i].toString() + " rowColum:"+ row +""+column + " Goes Top Right");
                        String[] strAdd5 = wordList.get(i);
                        strAdd5[2]= column+"";
                        strAdd5[3]= row+"";
                        strAdd5[4]= column-1+strWordlist[i].length()+"";
                        strAdd5[5]= row+1-strWordlist[i].length()+"";
                        wordList.set(i,strAdd5);
                        break;
                    case "topLeft":
                        for (int j = 0; j < strWordlist[i].length();j++) {
                            tmpLetterGrid[row-j][column-j] = Character.toUpperCase((char)(strWordlist[i].charAt(j)));
                        }
                        System.out.println("Word: " + strWordlist[i].toString() + " rowColum:"+ row +""+column + " Goes Top Left");
                        String[] strAdd6 = wordList.get(i);
                        strAdd6[2]= column+"";
                        strAdd6[3]= row+"";
                        strAdd6[4]= column+1-strWordlist[i].length()+"";
                        strAdd6[5]= row+1-strWordlist[i].length()+"";
                        wordList.set(i,strAdd6);
                        break;
                    case "bottomRight":
                        for (int j = 0; j < strWordlist[i].length();j++) {
                            tmpLetterGrid[row+j][column+j] = Character.toUpperCase((char)(strWordlist[i].charAt(j)));
                        }
                        System.out.println("Word: " + strWordlist[i].toString() + " rowColum:"+ row +""+column + " Goes Bottom Right");
                        String[] strAdd7 = wordList.get(i);
                        strAdd7[2]= column+"";
                        strAdd7[3]= row+"";
                        strAdd7[4]= column-1+strWordlist[i].length()+"";
                        strAdd7[5]= row-1+strWordlist[i].length()+"";
                        wordList.set(i,strAdd7);
                        break;
                    case "bottomLeft":
                        for (int j = 0; j < strWordlist[i].length();j++) {
                            tmpLetterGrid[row+j][column-j] = Character.toUpperCase((char)(strWordlist[i].charAt(j)));
                        }
                        System.out.println("Word: " + strWordlist[i].toString() + " rowColum:"+ row +""+column + " Goes Bottom Left");
                        String[] strAdd8 = wordList.get(i);
                        strAdd8[2]= column+"";
                        strAdd8[3]= row+"";
                        strAdd8[4]= column+1-strWordlist[i].length()+"";
                        strAdd8[5]= row-1+strWordlist[i].length()+"";
                        wordList.set(i,strAdd8);
                        break;

                    default:
                        //6. Repeats steps 2 to 5 until all words from list has been placed on grid.
                        //This forces to recheck a new spot since the word was not place in gird.
                        // if over 100,000 attempts to put words on list fails, reset puzzle and try again.
                        if (placementAttempts <100) {
                            i--;
                            placementAttempts++;
                        }
                        else {
                            placementAttempts = 0;
                            i =-1;
                            for (int j =0; j < tmpLetterGrid.length;j++){
                                for (int k = 0; k < tmpLetterGrid[j].length;k++) {
                                    tmpLetterGrid[j][k] = ' ';
                                }

                            }
                        }
                }
            }
            else {
                // if over 100,000 attempts to put words on list fails, reset puzzle and try again.
                if (placementAttempts <100) {
                    i--;
                    placementAttempts++;
                }
                else {
                    placementAttempts = 0;
                    i =-1;
                    for (int j =0; j < tmpLetterGrid.length;j++){
                        for (int k = 0; k < tmpLetterGrid[j].length;k++) {
                            tmpLetterGrid[j][k] = ' ';
                        }

                    }
                }

            }





        }

        //7. Fill in remaining Grid spots with random letter
        char[] letter = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        for (int i =0; i < tmpLetterGrid.length; i ++) {
            for (int j =0; j< tmpLetterGrid[i].length;j++) {
                if (tmpLetterGrid[i][j] == ' ') {
                    tmpLetterGrid[i][j] = letter[(int)(Math.random()*26)];
                }
            }

        }




        //8. Returns grid (2-d Char Array)
        return tmpLetterGrid;


    }


}
