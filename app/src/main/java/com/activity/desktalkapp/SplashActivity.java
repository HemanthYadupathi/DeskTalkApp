package com.activity.desktalkapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private Thread mSplashThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);

        final SplashActivity sPlashScreen = this;

        // The thread to wait for splash screen events
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        // Wait given period of time or exit on touch
                        wait(2000);
                    }
                }
                catch(InterruptedException ex){
                }

                finish();

                // Run next activity
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, LoginActivity.class);
                startActivity(intent);
            }
        };

        mSplashThread.start();
    }
}