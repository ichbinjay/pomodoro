package com.example.pomodoro;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextView timerTextView;
    private Button PomoradoButton;
    private Button ShortBreakButton;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;
    private ImageView imageView;
    private Vibrator vibrator;
    private boolean isVibrating =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set status and bottom bar color to #8AF8AA1A
        getWindow().setStatusBarColor(getResources().getColor(R.color.customColor));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.customColor));

        //remove action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_main);

        PomoradoButton = findViewById(R.id.PomodoroButton);
        ShortBreakButton = findViewById(R.id.ShortBreakButton);
        timerTextView = findViewById(R.id.Timer);

        PomoradoButton.setOnClickListener(v -> {
            if(mediaPlayer != null){
                mediaPlayer.stop();
            }
            PomoradoButton.setEnabled(false);
            ShortBreakButton.setEnabled(false);
            startTimer(25*60*1000);
            addTomato();
        });
        ShortBreakButton.setOnClickListener(v -> {
            if(mediaPlayer != null){
                mediaPlayer.stop();
            }
            PomoradoButton.setEnabled(false);
            ShortBreakButton.setEnabled(false);
            startTimer(5*60*1000);
        });

        Button stopButton = findViewById(R.id.StopButton);
        stopButton.setOnClickListener(v -> {
            if(isVibrating){
                vibrator.cancel();
                isVibrating=false;
            }
            if(countDownTimer != null){
                countDownTimer.cancel();
            }
            if(mediaPlayer != null){
                mediaPlayer.stop();
            }
            PomoradoButton.setEnabled(true);
            ShortBreakButton.setEnabled(true);
            String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", 0, 0);
            timerTextView.setText(timeLeft);
        });
        }
    private void startTimer(long time){
        countDownTimer= new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished/1000)/60;
                int seconds = (int) (millisUntilFinished/1000)%60;
                String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timerTextView.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                PomoradoButton.setEnabled(true);
                ShortBreakButton.setEnabled(true);
                String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", 0, 0);
                timerTextView.setText(timeLeft);
                imageView.setAlpha(1f);


                // Play default alarm sound
                try {
                    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator != null && vibrator.hasVibrator()) {
                        long[] pattern = {0, 10000}; // Vibration pattern: 0ms OFF, 1000ms ON
                        int indexInPatternToRepeat = -1; // Repeat at the start of the pattern
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createWaveform(pattern, indexInPatternToRepeat));
                        } else {
                            vibrator.vibrate(pattern, indexInPatternToRepeat);
                        }
                        isVibrating=true;
                    }
                    /*Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(getApplicationContext(), defaultSoundUri);
                    mediaPlayer.prepare();
                    //mediaPlayer.start();*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void addTomato(){
        //create an image view and add it to the linear layout
        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.tomato);
        imageView.setAdjustViewBounds(true);
        imageView.setMaxHeight(280);
        imageView.setMaxWidth(280);
        imageView.setAlpha(0.5f);
        GridLayout gridLayout = findViewById(R.id.TomatoContainer);
        gridLayout.addView(imageView);
    }
}