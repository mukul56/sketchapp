package com.example.sketchapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sketchapp.authentication.Authen;
import com.example.sketchapp.util.sharePrefer;

import static java.lang.Thread.sleep;

public class splash extends AppCompatActivity {
    TextView splashtv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final sharePrefer my = new sharePrefer(this);
        splashtv = (TextView) findViewById(R.id.text);
        Animation myAnim1 = AnimationUtils.loadAnimation(this, R.anim.sliding);
        splashtv.startAnimation(myAnim1);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                        if (!my.isLogged()) {
                        Intent i = new Intent(splash.this, Authen.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(splash.this, MainActivity.class);
                        startActivity(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}