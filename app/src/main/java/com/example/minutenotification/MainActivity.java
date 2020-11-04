package com.example.minutenotification;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView txtTime;
    private Button btnStart;
    private String formattedDate;
    private Thread myThread = null;
    private Runnable runnable = null;
    private int counter = 0;
    private int counterBip = 0;
    private int minute = 600;
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
                            minute *= 2;
                            ++counterBip;
                            while (--counterBip != 0){
                                // Bip
                            }
                        }
                    }
                    @Override
                    public void onFinish() {

                    }
                }.start();

            }
        });
    }
    private void findId(){
        txtTime = findViewById(R.id.txtTime);
        btnStart = findViewById(R.id.btnStart);
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
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

}