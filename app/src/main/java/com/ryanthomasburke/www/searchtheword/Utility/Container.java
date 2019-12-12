// written by: Ryan
// tested by:// Jesse
// debugged by: N/A it worked out just fine.
package com.ryanthomasburke.www.searchtheword.Utility;

public class Container {

    private int difficulty;
    private int totalScore;
    private int currentLevel;

    public Container(){
        this.difficulty = 1;
        this.totalScore = 0;
        this.currentLevel = 1;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
}
