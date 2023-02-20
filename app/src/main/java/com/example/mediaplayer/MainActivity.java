package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    // Declaring Widgets
    // Buttons
    Button forward_btn, backward_btn, play_btn, pause_btn;
    // TextViews
    TextView tv_time, tv_mediaTitle;
    // SeekBar
    SeekBar seekBar;

    // Media Player
    MediaPlayer mediaPlayer;

    // Handlers
    Handler handler = new Handler();

    // Variables
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Defining and Initializing
        play_btn = findViewById(R.id.btn_play);
        pause_btn = findViewById(R.id.btn_pause);
        forward_btn = findViewById(R.id.btn_forward);
        backward_btn = findViewById(R.id.btn_backward);

        tv_time = findViewById(R.id.tv_time_left);
        tv_mediaTitle = findViewById(R.id.tv_media_name);

        seekBar = findViewById(R.id.seekBar);

        // creating media player
        mediaPlayer = MediaPlayer.create(this,
                R.raw.sodankal_ravchim);

        /*tv_mediaTitle.setText(getResources().getIdentifier(
                "SodankalRavchim",
                "raw",
                getPackageName()
        ));*/

        // set seekbar to false
        seekBar.setClickable(false);

        // Button Functionalities
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp + forwardTime) <= finalTime) {
                    startTime += forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Forward not possible", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime -= backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "backward not possible", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void playMusic() {
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        tv_time.setText(String.format(
                "%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))
        ));

        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 100);
    }

    // Creating the Runnable
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tv_time.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));

            seekBar.setProgress((int) startTime);
            handler.postDelayed(this, 100);
        }
    };
}