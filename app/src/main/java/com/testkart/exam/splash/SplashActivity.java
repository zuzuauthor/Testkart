package com.testkart.exam.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.testkart.exam.R;
import com.testkart.exam.edu.MainActivity;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.login.LoginActivity1;
import com.testkart.exam.get_start.GetStartActivity;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

/**
 * Created by testkart on 8/6/17.
 */

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    private TextView getStart;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testkart_splash);

        //initialization
        initialization();

    }

    private void initialization() {

        sessionManager = new SessionManager(this);

        getStart = (TextView)findViewById(R.id.getStart);
        //getStartListenet();

        final DilatingDotsProgressBar mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);

// show progress bar and start animating
        mDilatingDotsProgressBar.showNow();

// stop animation and hide

        new Handler().postDelayed(new Runnable() {


             //* Showing splash screen with a timer. This will be useful when you
            // * want to show case your app logo / company


            @Override
            public void run() {


                if(sessionManager.isLoggedIn()){

                    // This method will be executed once the timer is over
                    // Start your app main activity
                    mDilatingDotsProgressBar.hideNow();
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);

                }else{

                    // This method will be executed once the timer is over
                    // Start your app main activity
                    mDilatingDotsProgressBar.hideNow();
                    Intent i = new Intent(SplashActivity.this, LoginActivity1.class);
                    startActivity(i);
                }




            }
        }, SPLASH_TIME_OUT);
    }

    private void getStartListenet() {

        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SplashActivity.this, GetStartActivity.class);
                startActivity(i);

                // close this activity
                finish();

            }
        });
    }

}
