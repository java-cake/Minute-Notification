package com.example.minutenotification;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    private TextView txtTime;
    private Button btnStart;
    private Button btnStop;
    private String formattedDate;
    private Thread myThread = null;
    private Runnable runnable = null;
    private ToneGenerator toneGenerator = null;
    private int volumeLevel = 1000;
    private int counter = 0;
    private int counterBeep = 0;
    private int counterTemp = 0;
    private int minute = 600;
    private int minuteRepeat = 600;
    private int duration = 500;
    private int delay1 = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findId();
        runnable = new CountDownRunner();
        myThread = new Thread(runnable);
        myThread.start();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimer(86400000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        counter++;
                        if (counter == minute){
                            minute += minuteRepeat;
                            ++counterBeep;
                            counterTemp = counterBeep;
                            while (counterBeep-- != 0){
                                toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, volumeLevel);
                                toneGenerator.startTone(ToneGenerator.TONE_DTMF_S, duration);
                                try {
                                    sleep(delay1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            counterBeep = counterTemp;
                        }
                    }
                    @Override
                    public void onFinish() {
                        counter = 0;
                    }
                }.start();

            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
    private void findId(){
        txtTime = findViewById(R.id.txtTime);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
    }
    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try
                {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                    formattedDate = sdf.format(date);
                    txtTime.setText(formattedDate);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    class CountDownRunner implements Runnable{
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    sleep(delay1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

}