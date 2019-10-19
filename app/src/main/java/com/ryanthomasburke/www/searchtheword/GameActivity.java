package com.ryanthomasburke.www.searchtheword;


import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ryanthomasburke.www.searchtheword.Utility.Word;
import com.ryanthomasburke.www.searchtheword.Views.WordGrid;

public class GameActivity extends AppCompatActivity {

    private WordGrid wordGrid;

    private char[][] letterGrid = {
            //0   1   2   3   4   5   6   7   8   9
            {'A','B','C','D','E','P','G','H','Z','A'}, // 0
            {'N','D','D','H','H','F','G','H','Y','B'}, // 1
            {'T','A','E','D','E','Y','G','H','S','C'}, // 2
            {'O','J','C','W','H','F','G','H','F','D'}, // 3
            {'N','E','W','D','O','F','G','R','G','E'}, // 4
            {'I','S','E','G','E','R','Y','H','J','F'}, // 5
            {'A','S','C','D','E','A','D','H','K','G'}, // 6
            {'A','E','M','A','N','U','E','L','I','H'}, // 7
            {'A','G','G','S','O','M','E','O','I','I'}, // 8
            {'A','B','C','D','E','F','G','H','I','J'}  // 9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        wordGrid = findViewById(R.id.wordsGrid);
        wordGrid.setLetters(letterGrid);
        wordGrid.setWords(
                new Word("ANTONIA", false, 0, 0, 0, 6),
                new Word("JESSE", false, 1, 3, 1, 7),
                new Word("MANUEL", false, 2, 7, 7, 7),
                new Word("LO", false, 7, 7, 7, 8),
                new Word("RYAN", false, 7, 4, 4, 7));
        wordGrid.setOnWordSearchedListener(new WordGrid.OnWordSearchedListener() {
            @Override
            public void wordFound(String string) {
                Toast.makeText(GameActivity.this, string + " found", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
