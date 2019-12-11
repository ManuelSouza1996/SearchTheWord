package com.ryanthomasburke.www.searchtheword.Views;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ryanthomasburke.www.searchtheword.DifficultyActivity;
import com.ryanthomasburke.www.searchtheword.GameActivity;
import com.ryanthomasburke.www.searchtheword.MainActivity;
import com.ryanthomasburke.www.searchtheword.R;

public class SettingsActivity extends AppCompatActivity {
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        seekFunction();

    }
    public void seekFunction()
    {
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        final TextView Status = (TextView)findViewById(R.id.volume);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                Status.setText("Volume:");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Status.setText("Volume:"+ Status);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {



            }
        });
    }

    public void goToMain(View view) {
        try {
            Intent intent = new Intent(this, MainActivity.class);
            System.out.println(intent.toString());
            this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            setContentView(R.layout.activity_main);
        }
    }

    public void goToDifficulty(View view) {
        try {
            Intent intent = new Intent(this, DifficultyActivity.class);
            System.out.println(intent.toString());
            this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            setContentView(R.layout.activity_difficulty);
        }
    }

    public void goToGame(View view) {
        try {
            Intent intent = new Intent(this, GameActivity.class);
            System.out.println(intent.toString());
            this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            setContentView(R.layout.activity_game);
        }

    }
}


