package com.jacky.virtualdummy;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private TextView mText;

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mText = findViewById(R.id.text);
        Typeface pacific = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        mText.setTypeface(pacific);



        new Handler().postDelayed(new Runnable() { //handler schedules messages and runnables to be executed at some point in the future 2. enques an action to be performed on diff threads than
            @Override
            public void run() {
                Intent mainIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}