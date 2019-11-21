package com.ryanthomasburke.www.searchtheword;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Highscores {
    static int[] easyScores = new int[3];
    static int[] mediumScores = new int[3];
    static int[] hardScores = new int[3];
    static int[] allScores = new int[9];
    static String path = "";

    public static void main(String[] args) {
        readHighScores(path);
        replaceScore(170, easyScores);
        updateFile(path);
    }

    /*
     * @author Ryan
     * @param path
     */
    public static void readHighScores(String path) {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(path));
            for (int i = 0; scanner.hasNextLine(); i++) {
                int line = Integer.parseInt(scanner.nextLine());
                allScores[i] = line;
                System.out.println(i + ":" + line);
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
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
    }

    public static void replaceScore(int score, int[] scores) {

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

    public static void updateFile(String path) {
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
