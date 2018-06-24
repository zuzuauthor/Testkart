package com.testkart.exam.edu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.testkart.exam.R;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.get_start.GetStartActivity;
import com.testkart.exam.testkart.Hello;
import com.testkart.exam.testkart.home.HomePageResponse;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by testkart on 10/4/17.
 */

public class SplashActivity extends AppCompatActivity {

    //http://www.testkart.com/rest/Apis/home.json
    //private SmileyLoadingView smileView;
    //Splash screen timer

    private static int SPLASH_TIME_OUT = 3000;

    private Context context = this;
    private SessionManager sessionManager;
    private TextView getStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){

            if(TestKartApp.drmON){

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            }

            sessionManager = new SessionManager(this);
            FirebaseMessaging.getInstance().subscribeToTopic("global");

            if(sessionManager.isLoggedIn()){

                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.putExtras(getIntent());

                startActivity(i);

                finish();

            }else{

                setContentView(R.layout.testkart_splash);

                //initialization
                initialization();

            }

        } else{


            // do something for phones running an SDK before lollipop
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Testkart Online Examination");
            builder.setMessage("This app does not run on pre lollipop devices. Please update your os to lollipop. Thank you");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finish();
                }
            });

            builder.create().show();

        }

    }

    private void initialization() {

        //sessionManager = new SessionManager(this);

        getStart = (TextView)findViewById(R.id.getStart);
        //getStartListenet();

        getLeaderListData();


       // smileView = (SmileyLoadingView) findViewById(R.id.loading_view);
       // smileView.start();

        //final DilatingDotsProgressBar mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);

       /* mDilatingDotsProgressBar.setDotRadius(5f);
        mDilatingDotsProgressBar.setGrowthSpeed(650);
        mDilatingDotsProgressBar.setDotSpacing(30);

        // show progress bar and start animating
        mDilatingDotsProgressBar.showNow();*/

        /*new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                //smileView.stop();

                if(sessionManager.isLoggedIn()){

                    // This method will be executed once the timer is over
                    // Start your app main activity

                    //mDilatingDotsProgressBar.hide();

                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putExtras(getIntent());
                    startActivity(i);

                }else{

                    // This method will be executed once the timer is over
                    // Start your app main activity

                    //mDilatingDotsProgressBar.hide();

                    Intent i = new Intent(SplashActivity.this, Hello.class);
                    i.putExtras(getIntent());
                    startActivity(i);
                }



                // close this activity
                finish();


            }
        }, SPLASH_TIME_OUT);*/
    }

    private void getStartListenet() {

        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.isLoggedIn()){

                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);

                }else{

                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashActivity.this, GetStartActivity.class);
                    startActivity(i);
                }



                // close this activity
                finish();

            }
        });
    }


    //working with leader board
    private void getLeaderListData() {

        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            //progressDialog.show();

            makeAPiCall();

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        // progressDialog.show();

        Call<HomePageResponse> call = apiService.getHomePageResponse("http://www.testkart.com/rest/Apis/home.json");

        call.enqueue(new Callback<HomePageResponse>() {
            @Override
            public void onResponse(Call<HomePageResponse> call, retrofit2.Response<HomePageResponse> response) {

                //progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if (response.body().getStatus()) {

                        if(sessionManager.isLoggedIn()){

                            // This method will be executed once the timer is over
                            // Start your app main activity

                            //mDilatingDotsProgressBar.hide();

                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            i.putExtras(getIntent());
                            startActivity(i);

                        }else{

                            // This method will be executed once the timer is over
                            // Start your app main activity

                            //mDilatingDotsProgressBar.hide();

                            Intent i = new Intent(SplashActivity.this, Hello.class);
                            i.putExtras(getIntent());
                            i.putExtra("IS_FROM_SPLASH", true);
                            i.putExtra("HOME_DATA", response.body().getHome());
                            startActivity(i);
                        }


                        // close this activity
                        finish();


                    } else {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HomePageResponse> call, Throwable t) {

                //hide progress dialog
                // progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });

    }



}
