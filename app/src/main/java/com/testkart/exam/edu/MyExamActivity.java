package com.testkart.exam.edu;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.MyBrowser;
import com.testkart.exam.edu.helper.SessionManager;

import id.arieridwan.lib.PageLoader;

/**
 * Created by testkart on 15/4/17.
 */

public class MyExamActivity extends AppCompatActivity implements MyBrowser.MyBrowserListener{

    private String URL = "https://www.slimframework.com/";

    private SessionManager session;
    private Context context = this;
    private WebView myWebView;
    public PageLoader pageLoader;
    public int AnimationMode;
    public int LoadMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_leaderboard);

        //initialization
        initialization();
    }


    private void initialization() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Exam");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        //session = new SessionManager(context);

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
        pageLoader.setOnRetry(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Load the initial URL
                myWebView.loadUrl(URL);
            }
        });




        // Load the initial URL
        myWebView.loadUrl(URL);

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