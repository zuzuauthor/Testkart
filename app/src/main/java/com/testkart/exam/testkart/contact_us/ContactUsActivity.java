package com.testkart.exam.testkart.contact_us;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.Toast;

import com.testkart.exam.R;

import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.MyBrowser;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.rest.ApiClient;


import id.arieridwan.lib.PageLoader;

/**
 * Created by elfemo on 24/7/17.
 */

public class ContactUsActivity extends AppCompatActivity implements MyBrowser.MyBrowserListener{

    private String URL = "http://www.testkart.com/Contactsm";

    final int SELECT_PHOTO = 1;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact us");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        session = new SessionManager(context);

        myWebView = (WebView) findViewById(R.id.webView);

        myWebView.addJavascriptInterface(new ContactUsActivity.WebAppInterface(this), "Android");

        // Configure related browser settings
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new ContactUsActivity.WebAppInterface(this), "Android");
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

        /*app:setLoadingImage="@raw/test_logo_progress_64"
        app:setLoadingImageWidth="64"
        app:setLoadingImageHeight="36"*/

        pageLoader.setOnRetry(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Load the initial URL
                myWebView.loadUrl(URL);
            }
        });

        // Load the initial URL
        myWebView.loadUrl(URL);

        //showInfoDialog(URL);
    }

    private void showInfoDialog(String url) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Calling URL");
        builder.setMessage(url);

        builder.create().show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }*/

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



    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public String showGallery(boolean toast) {

            //make action picker intent

            Toast.makeText(mContext, "make action picker intent", Toast.LENGTH_SHORT).show();

            String file = "test";
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            return file;

        }


        @JavascriptInterface
        public void makeCall(String number) {

            //make calling intent
            //Toast.makeText(mContext, "make calling intent", Toast.LENGTH_SHORT).show();

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:0377778888"));

            if (ActivityCompat.checkSelfPermission(ContactUsActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    Uri selectedImage = data.getData();
                    myWebView.loadUrl("javascript:setFileUri('" + selectedImage.toString() + "')");
                    String path = getRealPathFromURI(this, selectedImage);
                    myWebView.loadUrl("javascript:setFilePath('" + path + "')");
                }
        }

    }


    public String getRealPathFromURI(Context context, Uri contentUri)
    {
        Cursor cursor = null;
        try
        {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }


    /*

    <script type="text/javascript">
        function showAndroidGallery(toast) {
            Android.showGallery(toast);
        }
        function setFilePath(file) {
            document.getElementById('lblpath').innerHTML = file;
            Android.showToast(file);
        }
        function setFileUri(uri) {
            document.getElementById('lbluri').innerHTML = uri;
            Android.showToast(uri);
        }
        function choosePhoto() {
            var file = Android.choosePhoto();
            window.alert("file = " + file);
        }
    </script>

     */

}