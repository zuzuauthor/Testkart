package com.testkart.exam.packages.cc_avenue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.testkart.exam.R;
import com.testkart.exam.packages.cc_avenue.utility.AvenuesParams;
import com.testkart.exam.packages.cc_avenue.utility.CCAvenueResponse;
import com.testkart.exam.packages.cc_avenue.utility.Constants;
import com.testkart.exam.packages.cc_avenue.utility.LoadingDialog;
import com.testkart.exam.packages.cc_avenue.utility.RSAUtility;
import com.testkart.exam.packages.cc_avenue.utility.ServiceUtility;
import com.testkart.exam.packages.model.payu.POResponse;
import com.testkart.exam.packages.model.payu.PaymentResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class WebViewActivity extends AppCompatActivity {

    Intent mainIntent;
    String encVal;
    String vResponse;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        mainIntent = getIntent();

        //get rsa key method
        get_RSA_key(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
    }

    private class RenderView extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {

                StringBuffer vEncVal = new StringBuffer("");

                POResponse placeOrder = (POResponse) mainIntent.getSerializableExtra(AvenuesParams.PRODUCT_INFO);

                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));

                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_NAME, placeOrder.getFirstname()));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ADDRESS, placeOrder.getAddress1()));
                //vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CITY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                //vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_STATE, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                //vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ZIP, "226002"));
                //vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_TEL, placeOrder.getPhone()));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_EMAIL, placeOrder.getEmail()));

                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  //encrypt amount and currency

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            LoadingDialog.cancelLoading();

            @SuppressWarnings("unused")

            /*
            <script type="text/javascript">
            $(window).on('load', function() {
            showPaymentStatus('<?php echo$mobileStatus;?>');
            });
            function showPaymentStatus(status) {
            Android.showStatus(status);
            }
        </script>


             */

            class MyJavaScriptInterface {
                @JavascriptInterface
                public void showStatus(String html) {
                    // process the html source code to get final status of transaction
                    String status = null;

                    Log.v("Status", "Response: " + html);

                    PaymentResponse paymentResponse = new PaymentResponse();
                    Gson gson = new Gson();

                    if(html != null && !html.isEmpty()){

                        CCAvenueResponse ccRes = gson.fromJson(html, CCAvenueResponse.class);

                        if(ccRes != null){

                            paymentResponse.setId(0d);
                            paymentResponse.setMode(ccRes.getPaymentMode());

                            if (html.indexOf("Success") != -1) {
                                paymentResponse.setStatus("success");
                            } else if (html.indexOf("Failure") != -1) {
                                paymentResponse.setStatus("failure");
                            } else if (ccRes.getOrderStatus().equalsIgnoreCase("Aborted")) {
                                paymentResponse.setStatus("Aborted");
                            } else if(ccRes.getOrderStatus().equalsIgnoreCase("Invalid")){

                                paymentResponse.setStatus("failure");

                            }else {
                                paymentResponse.setStatus("failure");
                            }

                            paymentResponse.setKey(ccRes.getTrackingId());
                            paymentResponse.setTxnid(ccRes.getOrderId());
                            paymentResponse.setTransactionFee("");
                            paymentResponse.setAmount(ccRes.getAmount());
                            paymentResponse.setCardCategory("");
                            paymentResponse.setDiscount("");
                            paymentResponse.setAddedon("");
                            paymentResponse.setAdditionalCharges("");
                            paymentResponse.setProductinfo("");
                            paymentResponse.setFirstname(ccRes.getBillingName());
                            paymentResponse.setEmail(ccRes.getBillingEmail());
                            paymentResponse.setPaymentSource("");
                            paymentResponse.setErrorMessage(ccRes.getFailureMessage());

                        }

                    }

                    Intent intent=new Intent();
                    intent.putExtra("CC_ORDER_STATUS",paymentResponse);
                    setResult(AvenuesParams.CCAVENUE_REQUEST_CODE,intent);
                    finish();//finishing activity
                }
            }

            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "Android");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    LoadingDialog.cancelLoading();

                    //Log.v("Page finish Encrypted", "javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

                    if (url.indexOf("/ccavResponseHandler.jsp") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
                }
            });


            try {

                String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), "UTF-8")
                        + "&" + AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID), "UTF-8")
                        + "&" + AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ORDER_ID), "UTF-8")
                        + "&" + AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL), "UTF-8")
                        + "&" + AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.CANCEL_URL), "UTF-8")
                        +"&" + AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");

                Log.v("Post Data", postData);

                webview.postUrl(Constants.TRANS_URL, postData.getBytes());

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    public void get_RSA_key(final String ac, final String od) {

        LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(WebViewActivity.this,response,Toast.LENGTH_LONG).show();
                        LoadingDialog.cancelLoading();

                        if (response != null && !response.equals("")) {

                            Log.v("RSA", "RSA : "+ response);

                            vResponse = response;     ///save retrived rsa key

                            if (vResponse.contains("!ERROR!")) {

                                show_alert(vResponse);

                            } else {

                                new RenderView().execute();   // Calling async task to get display content

                            }

                        }
                        else
                        {
                            show_alert("No response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LoadingDialog.cancelLoading();
                        //Toast.makeText(WebViewActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(AvenuesParams.ACCESS_CODE, ac);
                params.put(AvenuesParams.ORDER_ID, od);
                return params;

            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void show_alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                WebViewActivity.this).create();

        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");

        alertDialog.setMessage(msg);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.show();
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("CC Avenue");
        builder.setMessage("Are you sure you want to abort this transaction?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                PaymentResponse paymentResponse = new PaymentResponse();
                paymentResponse.setStatus("Aborted");

                Intent intent=new Intent();
                intent.putExtra("CC_ORDER_STATUS",paymentResponse);
                setResult(AvenuesParams.CCAVENUE_REQUEST_CODE,intent);
                finish();//finishing activity
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        builder.create().show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        LoadingDialog.cancelLoading();
    }
}