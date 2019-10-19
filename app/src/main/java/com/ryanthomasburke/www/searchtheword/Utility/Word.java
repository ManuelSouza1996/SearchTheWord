package com.ryanthomasburke.www.searchtheword.Utility;

public class Word {

    private String word;
    private boolean highlighted;
    private int fromRow, fromColumn, toRow, toColumn;

    public Word(String word, boolean highlighted, int fromColumn, int fromRow, int toColumn, int toRow) {
        this.word = word;
        this.highlighted = highlighted;
        this.fromRow = fromRow;
        this.fromColumn = fromColumn;
        this.toRow = toRow;
        this.toColumn = toColumn;
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getFromColumn() {
        return fromColumn;
    }

    public int getToRow() {
        return toRow;
    }

    public int getToColumn() {
        return toColumn;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}
