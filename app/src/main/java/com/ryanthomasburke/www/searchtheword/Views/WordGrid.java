package com.ryanthomasburke.www.searchtheword.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;

import com.ryanthomasburke.www.searchtheword.GameActivity;
import com.ryanthomasburke.www.searchtheword.R;
import com.ryanthomasburke.www.searchtheword.Utility.Word;

import java.util.Random;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

public class WordGrid extends View {

    private int rows, columns, width, height, rectWH;

    private char[][] letters;
    private Word[] words;

    private Cell[][] cells;
    private Cell cellDragStart, cellDragEnd;

    private Paint textPainter;
    private Paint highlighter;
    private Paint gridPainter;
    private boolean gameover = false;
    private Context context;


    private OnWordSearchedListener onWordSearchedListener;
    private int wordsSearched = 0;



    public WordGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize();
    }

    private void initialize() {

        textPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPainter.setSubpixelText(true);
        textPainter.setColor(0xcc000000);
        textPainter.setTextSize(70);
        textPainter.setTextAlign(Paint.Align.LEFT);

        highlighter = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlighter.setStyle(Paint.Style.STROKE);
        highlighter.setStrokeWidth(110);
        highlighter.setStrokeCap(Paint.Cap.ROUND);
        highlighter.setColor(0x4400649C);

        gridPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPainter.setStyle(Paint.Style.STROKE);
        gridPainter.setStrokeWidth(4);
        gridPainter.setStrokeCap(Paint.Cap.SQUARE);
        gridPainter.setColor(0x11000000);


    }

    public void setGameOver(boolean input){

        this.gameover = input;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        /*TODO: CREATE RANDOM HIGHLIGHT COLOR
        Random rnd = new Random();
        highlighter.setARGB(50, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
         */

        if(rows <= 0 || columns <= 0) {
            return;
        }

        // This block of code draws the grid
        for(int i = 0; i < rows - 1; i++) {
            canvas.drawLine(0, cells[i][0].getCellRectangle().bottom, width, cells[i][0].getCellRectangle().bottom, gridPainter);
        }
        for(int i = 0; i < columns - 1; i++) {
            canvas.drawLine(cells[0][i].getCellRectangle().right, cells[0][0].getCellRectangle().top, cells[0][i].getCellRectangle().right, cells[columns-1][0].getCellRectangle().bottom, gridPainter);
        }

        // This block of code creates the letters
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                String letter = String.valueOf(cells[i][j].getLetter());
                Rect textBounds = new Rect();
                textPainter.getTextBounds(letter, 0, 1, textBounds);
                canvas.drawText(letter, cells[i][j].getCellRectangle().centerX() - (textPainter.measureText(letter) / 2),
                        cells[i][j].getCellRectangle().centerY() + (textBounds.height() / 2), textPainter);
            }
        }

        // This block of code controls the highlighter
        if(cellDragStart != null && cellDragEnd != null && isFromToValid(cellDragStart, cellDragEnd)) {
            canvas.drawLine(cellDragStart.getCellRectangle().centerX(), cellDragStart.getCellRectangle().centerY(),
                    cellDragEnd.getCellRectangle().centerX() + 1, cellDragEnd.getCellRectangle().centerY(), highlighter);
        }

        for(Word word : words) {
            if(word.isHighlighted()) {
                canvas.drawLine(
                        cells[word.getFromRow()][word.getFromColumn()].getCellRectangle().centerX(),
                        cells[word.getFromRow()][word.getFromColumn()].getCellRectangle().centerY(),
                        cells[word.getToRow()][word.getToColumn()].getCellRectangle().centerX() + 1,
                        cells[word.getToRow()][word.getToColumn()].getCellRectangle().centerY(), highlighter);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);
        int d = Math.min(measuredWidth, measuredHeight);
        setMeasuredDimension(d, d);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;

        initializeCells();
    }

    private void initializeCells() {
        if(rows <= 0 || columns <= 0) {
            return;
        }
        cells = new Cell[rows][columns];
        rectWH = width/rows;

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                cells[i][j] = new Cell(new Rect(j*rectWH,i*rectWH,(j+1)*rectWH,(i+1)*rectWH),letters[i][j],i,j);
            }
        }
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = 100;
        } else {
            result = specSize;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int pointerIndex = MotionEventCompat.getActionIndex(event);
        final float x = MotionEventCompat.getX(event, pointerIndex);
        final float y = MotionEventCompat.getY(event, pointerIndex);

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            try {
                Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(50);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            cellDragStart = getCell(x,y);
            cellDragEnd = cellDragStart;
            invalidate();

        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            Cell cell = getCell(x,y);
            if(cellDragStart != null && cell != null && isFromToValid(cellDragStart, cell)) {
                cellDragEnd = cell;
                invalidate();
            }
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            try {
                Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(50);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            String word = getWord(cellDragStart, cellDragEnd);
            highlightIfString(word);
            cellDragStart = null;
            cellDragEnd = null;
            invalidate();
            return false;
        }
        return true;
    }

    private Cell getCell(float x, float y) {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(cells[i][j].getCellRectangle().contains((int)x,(int)y)) {
                    return cells[i][j];
                }
            }
        }
        return null;
    }

    public void setLetters(char[][] letters) {
        this.letters = letters;
        rows = letters.length;
        columns = letters[0].length;
        initializeCells();
        invalidate();
    }

    private boolean isFromToValid(Cell cellDragFrom, Cell cellDragTo) {
        return (Math.abs(cellDragFrom.getRow() - cellDragTo.getRow()) == Math.abs(cellDragFrom.getColumn() - cellDragTo.getColumn()))
                || cellDragFrom.getRow() == cellDragTo.getRow() || cellDragFrom.getColumn() == cellDragTo.getColumn();
    }

    private class Cell {
        private Rect cellRectangle;
        private char letter;
        private int rowIndex, columnIndex;

        public Cell(Rect rect, char letter, int rowIndex, int columnIndex) {
            this.cellRectangle = rect;
            this.letter = letter;
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
        }

        public Rect getCellRectangle() {
            return cellRectangle;
        }

        public char getLetter() {
            return letter;
        }

        public int getRow() {
            return rowIndex;
        }

        public int getColumn() {
            return columnIndex;
        }

    }

    public interface OnWordSearchedListener {
        void wordFound(String word);
    }

    public void setOnWordSearchedListener(OnWordSearchedListener onWordSearchedListener) {
        this.onWordSearchedListener = onWordSearchedListener;
    }

    public void setWords(Word... words) {
        this.words = words;
    }


    private String getWord(Cell fromCell, Cell toCell) {
        String word = "";
        if(fromCell.getRow() == toCell.getRow()) {
            int column = fromCell.getColumn() < toCell.getColumn() ? fromCell.getColumn() : toCell.getColumn();
            for(int i = 0; i < Math.abs(fromCell.getColumn() - toCell.getColumn()) + 1; i++) {
                word += String.valueOf(cells[fromCell.getRow()][i+column].getLetter());
            }
        } else if(fromCell.getColumn() == toCell.getColumn()) {
            int row = fromCell.getRow() < toCell.getRow() ? fromCell.getRow() : toCell.getRow();
            for(int i = 0; i < Math.abs(fromCell.getRow() - toCell.getRow()) + 1; i++) {
                word += String.valueOf(cells[i+row][toCell.getColumn()].getLetter());
            }
        } else {
            if(fromCell.getRow() > toCell.getRow()) {
                Cell cell = fromCell;
                fromCell = toCell;
                toCell = cell;
            }
            for(int i = 0; i < Math.abs(fromCell.getRow() - toCell.getRow()) + 1; i++) {
                int temp = fromCell.getColumn() < toCell.getColumn() ? i : -i;
                word += String.valueOf(cells[fromCell.getRow()+i][fromCell.getColumn()+temp].getLetter());
            }
        }
        return word;
    }

    private void highlightIfString(String string) {
        if(gameover == false) {
            for (Word word : words) {
                if ((word.getWord().equals(string)) || (word.getWord().equals(reverseString(string)))) {
                    if (onWordSearchedListener != null) {
                        if (word.getWord().equals(string)) {
                            onWordSearchedListener.wordFound(string);
                        } else if (word.getWord().equals(reverseString(string))) {
                            onWordSearchedListener.wordFound(reverseString(string));
                        }
                    }

                    try {
                        TextView wordFound;
                        wordFound = findViewById(R.id.wordFound);
                        //wordFound.append(string + "\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    word.setHighlighted(true);
                    wordsSearched++;
                    break;
                }
            }
        }
    }

    /*Function Name: Reverse String
     *Written By: Jesse Vang
     * Purpose: reverse String.
     */
    //reverse function
    private static String reverseString(String strReverse) {
        char[] tmpS = strReverse.toCharArray();
        String newString = "";
        for (int i =tmpS.length-1; i >= 0; i --) {
            newString += tmpS[i];
        }
        return newString;
    }

}
