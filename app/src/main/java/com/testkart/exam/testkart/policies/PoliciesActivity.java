package com.testkart.exam.testkart.policies;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.MyBrowser;
import com.testkart.exam.edu.rest.ApiClient;

import id.arieridwan.lib.PageLoader;

/**
 * Created by elfemo on 22/7/17.
 */

public class PoliciesActivity extends AppCompatActivity implements MyBrowser.MyBrowserListener{

    public static final String KEY_POLICY_NAME = "policy_name";
    public static final String KEY_POLICY_URL = "policy_url";

    private Context context = this;
    private WebView myWebView;
    public PageLoader pageLoader;
    public int AnimationMode;
    public int LoadMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_leaderboard);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //initialization
        checkInternet();
    }



    public void checkInternet(){

        if (ApiClient.isNetworkAvailable(context)) {

            //initialization
            initialization();

        } else {

            //show dialog ... leader board is empty
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Network");
            builder.setMessage("Please check your internet connection and try again");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    checkInternet();
                }
            });

            builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }
            });

            AlertDialog d = builder.create();
            d.setCancelable(false);
            d.show();

        }

    }



    private void initialization() {

        String policyName = getIntent().getStringExtra(KEY_POLICY_NAME);
        final String policyUrl = getIntent().getStringExtra(KEY_POLICY_URL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(policyName);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }




        myWebView = (WebView) findViewById(R.id.webView);
        // Configure related browser settings
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        myWebView.setWebViewClient(new MyBrowser(this));

        // Enable responsive layout
        myWebView.getSettings().setUseWideViewPort(true);
        // Zoom out if the content width is greater than the width of the viewport
        myWebView.getSettings().setLoadWithOverviewMode(true);


        pageLoader = (PageLoader) findViewById(R.id.pageloader);
        pageLoader.setImageLoading(R.raw.test_logo_progress_64);
        pageLoader.setLoadingImageHeight(57);
        pageLoader.setLoadingImageWidth(100);
        pageLoader.setOnRetry(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Load the initial URL
                myWebView.loadUrl(policyUrl);
            }
        });

        // Load the initial URL
        myWebView.loadUrl(policyUrl);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    //my browser listeners
    @Override
    public void onEduPageStarted(WebView view, String url, Bitmap favicon) {

        pageLoader.startProgress();
        LoadMode = 1;

    }

    @Override
    public void onEduPageFinished(WebView view, String url) {

        pageLoader.stopProgress();
        LoadMode = 2;

    }

    @Override
    public void onEduReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

        pageLoader.stopProgressAndFailed();
        LoadMode = 3;
    }
}