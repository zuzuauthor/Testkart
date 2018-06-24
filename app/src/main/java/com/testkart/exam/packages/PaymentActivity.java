package com.testkart.exam.packages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.MyBrowser;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.packages.model.payu.POResponse;
import com.testkart.exam.packages.model.payu.PaymentResponse;
import com.testkart.exam.packages.oderconfirmation.OrderConfirmationActivity;
import com.testkart.exam.payment.PaymentHistoryActivity;

import id.arieridwan.lib.PageLoader;

/**
 * Created by testkart on 12/6/17.
 */

public class PaymentActivity extends AppCompatActivity implements MyBrowser.MyBrowserListener {

    private String URL = "";

    public static final String KEY_SUBMIT_ORDER = "submit_order";
    public static final String KEY_SUBMIT_COUPON = "coupon_code";
    public static final String KEY_PAYMENT_GATEWAY = "payment_gateway";
    public static final String KEY_FREE_CHECKOUT = "free_checkout";
    public static final String KEY_PLACE_ORDER = "place_order";

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

        if (TestKartApp.drmON) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Payment");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        //initialization
        checkInternet();
    }


    public void checkInternet() {

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

        session = new SessionManager(context);

        String packIds = getIntent().getStringExtra(KEY_SUBMIT_ORDER);

        packIds = packIds.replaceAll(",$", "");
        String couponCode = getIntent().getStringExtra(KEY_SUBMIT_COUPON);
        String paymentGateway = getIntent().getStringExtra(KEY_PAYMENT_GATEWAY);



        //http://elfemo.com/envato/demos/edu-ibps/Checkouts/paymentMethod
        URL = "http://www.testkart.com/Checkouts/placeOrder?public_key=" + session.getUserDetails().get(SessionManager.KEY_PUBLIC)
                + "&private_key=" + session.getUserDetails().get(SessionManager.KEY_PRIVATE) + "&responses=" + packIds + "&couponCode=" + couponCode + "&payment_gateway=" + paymentGateway;

       /* URL = "http://silversyclops.net/envato/demos/edu-ibps/Checkouts/placeOrder?public_key="+"BF7F08BB4F8274F"
                +"&private_key="+"43f5689f2b97ef02abcc5fe30ee12efd30417ee894bad713efc0dbfd40279c00"+"&responses="+packIds+"&couponCode=DEMO10";
        */

        Log.v("Payment Url", URL);

        myWebView = (WebView) findViewById(R.id.webView);

        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        // Configure related browser settings
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
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

        if (item.getItemId() == android.R.id.home) {

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

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showStatus(String status) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Payment Status");
            builder.setCancelable(false);


            if (status.equals("Success")) {
                //exit to dashboard

                /*builder.setMessage("Exam purchased successfully.");

                builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });*/


                //
               // DBHelper dbHelper = new DBHelper(context);
               // dbHelper.clearCart();

                ShoppingCartActivity.act.finish();

                FinalReviewActivity.act.finish();
                PaymentOptionsActivity.act.finish();


                if (PackageListActivity.act != null) {

                    PackageListActivity.act.finish();
                    //finish();
                }


                PaymentResponse paymentResponse = getPaymentResponse();



                Intent iConfirm = new Intent(context, OrderConfirmationActivity.class);
                iConfirm.putExtra(OrderConfirmationActivity.KEY_PAYMENT_DETAILS, paymentResponse);
                iConfirm.putExtra(PaymentActivity.KEY_SUBMIT_ORDER, getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_ORDER));
                iConfirm.putExtra(PaymentActivity.KEY_SUBMIT_COUPON, getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_COUPON));
                iConfirm.putExtra(PaymentActivity.KEY_FREE_CHECKOUT, getIntent().getBooleanExtra(PaymentActivity.KEY_FREE_CHECKOUT, false));
                startActivity(iConfirm);

                /*Intent myPaymentActivity = new Intent(PaymentActivity.this, PaymentHistoryActivity.class);
                startActivity(myPaymentActivity);*/

                PaymentActivity.this.finish();


            } else if (status.equals("Pending")) {
                //go to payment screen

                builder.setMessage("Thank you for purchasing with us. We will keep you posted regarding the status of your order through e-mail.");

                builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DBHelper dbHelper = new DBHelper(context);
                        dbHelper.clearCart();

                        ShoppingCartActivity.act.finish();

                        FinalReviewActivity.act.finish();
                        PaymentOptionsActivity.act.finish();

                        if (PackageListActivity.act != null) {

                            PackageListActivity.act.finish();
                            //finish();
                        }


                        Intent myPaymentActivity = new Intent(PaymentActivity.this, PaymentHistoryActivity.class);
                        startActivity(myPaymentActivity);

                        PaymentActivity.this.finish();
                    }
                });


                builder.create().show();


            } else if (status.equals("Failed")) {

                builder.setMessage("Thank you for purchasing with us. However, the transaction has been declined");

                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                myWebView.clearCache(true);

                                checkInternet();
                            }
                        });

                    }
                });


                builder.setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ShoppingCartActivity.act.finish();

                        FinalReviewActivity.act.finish();
                        PaymentOptionsActivity.act.finish();

                        if (PackageListActivity.act != null) {

                            PackageListActivity.act.finish();
                            //finish();
                        }


                        Intent myPaymentActivity = new Intent(PaymentActivity.this, PaymentHistoryActivity.class);
                        startActivity(myPaymentActivity);

                        PaymentActivity.this.finish();

                    }
                });


                builder.create().show();

            }


        }

    }

    private PaymentResponse getPaymentResponse() {

        PaymentResponse paymentResponse = new PaymentResponse();

        POResponse placeOrder = (POResponse) getIntent().getSerializableExtra(PaymentActivity.KEY_PLACE_ORDER);

        paymentResponse.setMessage("Your order is placed successfully. Thank you for shopping at Testkart.com .");
        paymentResponse.setStatus("success");
        paymentResponse.setAmount("0");
        paymentResponse.setMode("FREE");


        long tx = System.currentTimeMillis();
        if(placeOrder != null){

            paymentResponse.setTxnid((placeOrder.getTxnid() != null)? placeOrder.getTxnid() : tx+"");

        }else{

            paymentResponse.setTxnid((tx+""));
        }

        return paymentResponse;
    }
}