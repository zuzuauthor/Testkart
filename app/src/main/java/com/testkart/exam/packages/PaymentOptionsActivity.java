package com.testkart.exam.packages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.packages.cc_avenue.WebViewActivity;
import com.testkart.exam.packages.cc_avenue.utility.AvenuesParams;
import com.testkart.exam.packages.cc_avenue.utility.Constants;
import com.testkart.exam.packages.cc_avenue.utility.ServiceUtility;
import com.testkart.exam.packages.model.payu.POResponse;
import com.testkart.exam.packages.model.payu.PaymentResponse;
import com.testkart.exam.packages.oderconfirmation.OrderConfirmationActivity;
import com.testkart.exam.payment.PaymentHistoryActivity;
import com.payu.india.CallBackHandler.OnetapCallback;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Extras.PayUSdkDetails;
import com.payu.india.Interfaces.OneClickPaymentListener;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by elfemo on 6/9/17.
 */

public class PaymentOptionsActivity extends AppCompatActivity implements OneClickPaymentListener {

    private Context context = this;
    private SessionManager session;
    private ProgressDialog progressDialog;

    public static PaymentOptionsActivity act;

    private FrameLayout payuBiz, ccAvenu, freeCheckOut;

    private String merchantKey, userCredentials;

    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;

    // This sets the configuration
    private PayuConfig payuConfig;

    // Used when generating hash from SDK
    private PayUChecksum checksum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //TODO Must write this code if integrating One Tap payments
        OnetapCallback.setOneTapCallback(this);

        //TODO Must write below code in your activity to set up initial context for PayU
        Payu.setInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Payment Options");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


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

        session = new SessionManager(this);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");

        act = this;

        payuBiz = (FrameLayout)findViewById(R.id.payuBiz);
        ccAvenu = (FrameLayout)findViewById(R.id.ccAvenu);
        freeCheckOut = (FrameLayout)findViewById(R.id.freeCheckOut);

        payuBizListener();
        ccAvenuListener();
        freeCheckOutListener();

        if(getIntent().getBooleanExtra(PaymentActivity.KEY_FREE_CHECKOUT, false)){

            payuBiz.setVisibility(View.GONE);
            ccAvenu.setVisibility(View.GONE);
            freeCheckOut.setVisibility(View.VISIBLE);

        }else{

            payuBiz.setVisibility(View.VISIBLE);
            ccAvenu.setVisibility(View.VISIBLE);
            freeCheckOut.setVisibility(View.GONE);

        }


        //payu init
        payuInit();

    }

    private void payuInit() {

        // lets tell the people what version of sdk we are using
        PayUSdkDetails payUSdkDetails = new PayUSdkDetails();
        //Toast.makeText(this, "Build No: " + payUSdkDetails.getSdkBuildNumber() + "\n Build Type: " + payUSdkDetails.getSdkBuildType() + " \n Build Flavor: " + payUSdkDetails.getSdkFlavor() + "\n Application Id: " + payUSdkDetails.getSdkApplicationId() + "\n Version Code: " + payUSdkDetails.getSdkVersionCode() + "\n Version Name: " + payUSdkDetails.getSdkVersionName(), Toast.LENGTH_LONG).show();


        /* 0MQaQP is test key in PRODUCTION_ENv just for testing purpose in this app. Merchant should use their
                    * own key in PRODUCTION_ENV
                    */
        //((EditText) findViewById(R.id.editTextMerchantKey)).setText("0MQaQP");

        //set the test key in test environment
        //((EditText) findViewById(R.id.editTextMerchantKey)).setText("gtKFFx");


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {

                /**
                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                 * data.getStringExtra("result") ---> POResponse received from merchant's Surl/Furl
                 *
                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                 * for identifying status of transaction. There are two possible status like, success or failure
                 * */

                final PaymentResponse paymentResponse = new Gson().fromJson(data.getStringExtra("payu_response"),PaymentResponse.class);

                orderStatusAction(paymentResponse);

            } else {
               // Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();

                Toast.makeText(this, "Transaction Abort!", Toast.LENGTH_LONG).show();
            }
        }else if(requestCode == AvenuesParams.CCAVENUE_REQUEST_CODE){

            try{

                PaymentResponse paymentResponse = (PaymentResponse) data.getSerializableExtra("CC_ORDER_STATUS");
                orderStatusAction(paymentResponse);

            }catch (Exception e){

                e.printStackTrace();

            }

        }

    }


    private void orderStatusAction(PaymentResponse paymentResponse){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Payment Status");

        if(paymentResponse.getStatus().equalsIgnoreCase("failure")){

            builder.setMessage("Thank you for purchasing with us. However,the transaction has been declined");

            paymentResponse.setMessage("Thank you for purchasing with us. However, the transaction has been declined");

            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    navigateToBaseActivity((POResponse) getIntent().getSerializableExtra(PaymentActivity.KEY_PLACE_ORDER));

                }
            });

            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    ShoppingCartActivity.act.finish();
                    FinalReviewActivity.act.finish();
                    if (PackageListActivity.act != null) {
                        PackageListActivity.act.finish();
                    }

                    Intent myPaymentActivity = new Intent(context, PaymentHistoryActivity.class);
                    startActivity(myPaymentActivity);

                    PaymentOptionsActivity.act.finish();

                }
            });

            builder.create().show();

        }else if(paymentResponse.getStatus().equalsIgnoreCase("success")){

            builder.setMessage("Package order successfully done.");
            paymentResponse.setMessage("Your order is placed successfully. Thank you for shopping at Testkart.com .");

            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {



                            /*Intent myPaymentActivity = new Intent(context, PaymentHistoryActivity.class);
                            startActivity(myPaymentActivity);*/

                }
            });

            ShoppingCartActivity.act.finish();
            FinalReviewActivity.act.finish();
            if (PackageListActivity.act != null) {
                PackageListActivity.act.finish();
            }

            Intent iConfirm = new Intent(context, OrderConfirmationActivity.class);
            iConfirm.putExtra(OrderConfirmationActivity.KEY_PAYMENT_DETAILS, paymentResponse);
            iConfirm.putExtra(PaymentActivity.KEY_SUBMIT_ORDER, getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_ORDER));
            iConfirm.putExtra(PaymentActivity.KEY_SUBMIT_COUPON, getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_COUPON));
            iConfirm.putExtra(PaymentActivity.KEY_FREE_CHECKOUT, getIntent().getBooleanExtra(PaymentActivity.KEY_FREE_CHECKOUT, false));
            startActivity(iConfirm);

            PaymentOptionsActivity.act.finish();


        }else if(paymentResponse.getStatus().equals("pending")){

            builder.setMessage("Thank you for purchasing with us. We will keep you posted regarding the status of your order through e-mail.");

            paymentResponse.setMessage("Thank you for purchasing with us. We will keep you posted regarding the status of your order through e-mail.");

            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    ShoppingCartActivity.act.finish();
                    FinalReviewActivity.act.finish();
                    if (PackageListActivity.act != null) {
                        PackageListActivity.act.finish();
                    }

                    Intent myPaymentActivity = new Intent(context, PaymentHistoryActivity.class);
                    startActivity(myPaymentActivity);

                    PaymentOptionsActivity.act.finish();

                }
            });

            builder.create().show();

        }else if(paymentResponse.getStatus().equalsIgnoreCase("Aborted")){

            //Toast.makeText(this, "ORDER STATUS : Aborted", Toast.LENGTH_LONG).show();

            builder.setMessage("The transaction process is aborted by you. You can re-initiate payment process again by selecting one payment gateway from given list");

           // paymentResponse.setMessage("Thank you for purchasing with us. We will keep you posted regarding the status of your order through e-mail.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {



                }
            });

            builder.create().show();


        }

    }



    /**
     * This method prepares all the payments params to be sent to PayuBaseActivity.java
     */
    public void navigateToBaseActivity(POResponse placeOrder) {

        String merchantKey = placeOrder.getMerchantKey();
       // String merchantKey = "gtKFFx";

        String amount = placeOrder.getAmount();

        mPaymentParams = new PaymentParams();

        mPaymentParams.setKey(merchantKey);
        mPaymentParams.setAmount(amount);
        mPaymentParams.setProductInfo(placeOrder.getProductinfo());
        mPaymentParams.setFirstName(placeOrder.getFirstname());
        mPaymentParams.setEmail(placeOrder.getEmail());
        mPaymentParams.setTxnId(placeOrder.getTxnid());
        //mPaymentParams.setSurl("https://payu.herokuapp.com/success");
        //mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
        mPaymentParams.setSurl(placeOrder.getSurl());
        mPaymentParams.setFurl(placeOrder.getFurl());
        mPaymentParams.setUdf1("");
        mPaymentParams.setUdf2("");
        mPaymentParams.setUdf3("");
        mPaymentParams.setUdf4("");
        mPaymentParams.setUdf5("");
        mPaymentParams.setUserCredentials(merchantKey+":"+placeOrder.getEmail());

        int environment = PayuConstants.PRODUCTION_ENV;
       // int environment = PayuConstants.STAGING_ENV;// environment = PayuConstants.STAGING_ENV;   PRODUCTION_ENV

        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);

        generateHashFromServer(mPaymentParams);




        /**
         * For Test Environment, merchantKey = "gtKFFx"
         * For Production Environment, merchantKey should be your live key or for testing in live you can use "0MQaQP"
         */
       /* int environment = PayuConstants.PRODUCTION_ENV;  // environment = PayuConstants.STAGING_ENV;

        userCredentials = merchantKey + ":" + email;

        //TODO Below are mandatory params for hash genetation
        mPaymentParams = new PaymentParams();

        mPaymentParams.setKey(merchantKey);
        mPaymentParams.setAmount(amount);
        mPaymentParams.setProductInfo(proInfo);
        mPaymentParams.setFirstName(fName);
        mPaymentParams.setEmail(email);
        mPaymentParams.setTxnId(tnxId);  //Transaction Id should be kept unique for each transaction.


        *//**
         * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
         * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
         *//*
        mPaymentParams.setSurl(sUrl);
        mPaymentParams.setFurl(fUrl);

        *//*
         * udf1 to udf5 are options params where you can pass additional information related to transaction.
         * If you don't want to use it, then send them as empty string like, udf1=""
         * *//*
        mPaymentParams.setUdf1("");
        mPaymentParams.setUdf2("");
        mPaymentParams.setUdf3("");
        mPaymentParams.setUdf4("");
        mPaymentParams.setUdf5("");

        *//**
         * These are used for store card feature. If you are not using it then user_credentials = "default"
         * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
         * here merchant_key = your merchant key,
         * user_id = unique id related to user like, email, phone number, etc.
         * *//*
        mPaymentParams.setUserCredentials(userCredentials);

        //TODO Pass this param only if using offer key
        //mPaymentParams.setOfferKey("cardnumber@8370");

        //TODO Sets the payment environment in PayuConfig object
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);

        //TODO It is recommended to generate hash from server only. Keep your key and salt in server side hash generation code.
        //generateHashFromServer(mPaymentParams);

        *//**
         * Below approach for generating hash is not recommended. However, this approach can be used to test in PRODUCTION_ENV
         * if your server side hash generation code is not completely setup. While going live this approach for hash generation
         * should not be used.
         * *//*
        //String salt = "13p0PXZk";
        //generateHashFromSDK(mPaymentParams, salt);

        Log.v("MY_HASH", paymentHash);
        PayuHashes payuHashes = new PayuHashes();
        payuHashes.setPaymentHash(paymentHash);
       // mPaymentParams.setHash(payuHashes.getPaymentRelatedDetailsForMobileSdkHash());
        launchSdkUI(payuHashes);*/

    }



    /******************************
     * Client hash generation
     ***********************************/
    // Do not use this, you may use this only for testing.
    // lets generate hashes.
    // This should be done from server side..
    // Do not keep salt anywhere in app.
    public void generateHashFromSDK(PaymentParams mPaymentParams, String salt) {
        PayuHashes payuHashes = new PayuHashes();
        PostData postData = new PostData();

        // payment Hash;
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setAmount(mPaymentParams.getAmount());
        checksum.setKey(mPaymentParams.getKey());
        checksum.setTxnid(mPaymentParams.getTxnId());
        checksum.setEmail(mPaymentParams.getEmail());
        checksum.setSalt(salt);
        checksum.setProductinfo("My Exams");
        checksum.setFirstname(mPaymentParams.getFirstName());
        checksum.setUdf1(mPaymentParams.getUdf1());
        checksum.setUdf2(mPaymentParams.getUdf2());
        checksum.setUdf3(mPaymentParams.getUdf3());
        checksum.setUdf4(mPaymentParams.getUdf4());
        checksum.setUdf5(mPaymentParams.getUdf5());

        postData = checksum.getHash();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setPaymentHash(postData.getResult());
        }

        // checksum for payemnt related details
        // var1 should be either user credentials or default
        String var1 = mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials();
        String key = mPaymentParams.getKey();

        if ((postData = calculateHash(key, PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // Assign post data first then check for success
            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(postData.getResult());
        //vas
        if ((postData = calculateHash(key, PayuConstants.VAS_FOR_MOBILE_SDK, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setVasForMobileSdkHash(postData.getResult());

        // getIbibocodes
        if ((postData = calculateHash(key, PayuConstants.GET_MERCHANT_IBIBO_CODES, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setMerchantIbiboCodesHash(postData.getResult());

        if (!var1.contentEquals(PayuConstants.DEFAULT)) {
            // get user card
            if ((postData = calculateHash(key, PayuConstants.GET_USER_CARDS, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // todo rename storedc ard
                payuHashes.setStoredCardsHash(postData.getResult());
            // save user card
            if ((postData = calculateHash(key, PayuConstants.SAVE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setSaveCardHash(postData.getResult());
            // delete user card
            if ((postData = calculateHash(key, PayuConstants.DELETE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setDeleteCardHash(postData.getResult());
            // edit user card
            if ((postData = calculateHash(key, PayuConstants.EDIT_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setEditCardHash(postData.getResult());
        }

        if (mPaymentParams.getOfferKey() != null) {
            postData = calculateHash(key, PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey(), salt);
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                payuHashes.setCheckOfferStatusHash(postData.getResult());
            }
        }

        if (mPaymentParams.getOfferKey() != null && (postData = calculateHash(key, PayuConstants.CHECK_OFFER_STATUS, mPaymentParams.getOfferKey(), salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setCheckOfferStatusHash(postData.getResult());
        }

        // we have generated all the hases now lest launch sdk's ui
        launchSdkUI(payuHashes);
    }


    // deprecated, should be used only for testing.
    private PostData calculateHash(String key, String command, String var1, String salt) {
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setKey(key);
        checksum.setCommand(command);
        checksum.setVar1(var1);
        checksum.setSalt(salt);
        return checksum.getHash();
    }


    /**
     * This method generates hash from server.
     *
     * @param mPaymentParams payments params used for hash generation
     */
    public void generateHashFromServer(PaymentParams mPaymentParams) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));

        // for offer_key
        if (null != mPaymentParams.getOfferKey())
            postParamsBuffer.append(concatParams(PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey()));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }


    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }



    /**
     * This AsyncTask generates hash from server.
     */
    private class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected PayuHashes doInBackground(String... postParams) {
            PayuHashes payuHashes = new PayuHashes();
            try {

                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL("http://www.testkart.com/checkout/getHashes");
              //  URL url = new URL("https://payu.herokuapp.com/get_hash");

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        //TODO Below three hashes are mandatory for payment flow and needs to be generated at merchant server
                        /**
                         * Payment hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_hash -
                         *
                         * sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
                         *
                         */
                        case "payment_hash":
                            payuHashes.setPaymentHash(response.getString(key));
                            break;
                        /**
                         * vas_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating vas_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be "default"
                         *
                         */
                        case "vas_for_mobile_sdk_hash":
                            payuHashes.setVasForMobileSdkHash(response.getString(key));
                            break;
                        /**
                         * payment_related_details_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_related_details_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "payment_related_details_for_mobile_sdk_hash":
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                            break;

                        //TODO Below hashes only needs to be generated if you are using Store card feature
                        /**
                         * delete_user_card_hash is used while deleting a stored card.
                         * Below is formula for generating delete_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "delete_user_card_hash":
                            payuHashes.setDeleteCardHash(response.getString(key));
                            break;
                        /**
                         * get_user_cards_hash is used while fetching all the cards corresponding to a user.
                         * Below is formula for generating get_user_cards_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "get_user_cards_hash":
                            payuHashes.setStoredCardsHash(response.getString(key));
                            break;
                        /**
                         * edit_user_card_hash is used while editing details of existing stored card.
                         * Below is formula for generating edit_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "edit_user_card_hash":
                            payuHashes.setEditCardHash(response.getString(key));
                            break;
                        /**
                         * save_user_card_hash is used while saving card to the vault
                         * Below is formula for generating save_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "save_user_card_hash":
                            payuHashes.setSaveCardHash(response.getString(key));
                            break;

                        //TODO This hash needs to be generated if you are using any offer key
                        /**
                         * check_offer_status_hash is used while using check_offer_status api
                         * Below is formula for generating check_offer_status_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be Offer Key.
                         *
                         */
                        case "check_offer_status_hash":
                            payuHashes.setCheckOfferStatusHash(response.getString(key));
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return payuHashes;
        }

        @Override
        protected void onPostExecute(PayuHashes payuHashes) {
            super.onPostExecute(payuHashes);

            progressDialog.dismiss();
            launchSdkUI(payuHashes);
        }
    }



    /**
     * This method adds the Payuhashes and other required params to intent and launches the PayuBaseActivity.java
     *
     * @param payuHashes it contains all the hashes generated from merchant server
     */
    public void launchSdkUI(PayuHashes payuHashes) {

        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);

        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);

        //Lets fetch all the one click card tokens first
       // fetchMerchantHashes(intent);

    }



    //TODO This method is used if integrating One Tap Payments

    /**
     * This method stores merchantHash and cardToken on merchant server.
     *
     * @param cardToken    card token received in transaction response
     * @param merchantHash merchantHash received in transaction response
     */
    private void storeMerchantHash(String cardToken, String merchantHash) {

        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials + "&card_token=" + cardToken + "&merchant_hash=" + merchantHash;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    //TODO Deploy a file on your server for storing cardToken and merchantHash nad replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/store_merchant_hash");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                    InputStream responseInputStream = conn.getInputStream();
                    StringBuffer responseStringBuffer = new StringBuffer();
                    byte[] byteContainer = new byte[1024];
                    for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                        responseStringBuffer.append(new String(byteContainer, 0, i));
                    }

                    JSONObject response = new JSONObject(responseStringBuffer.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                this.cancel(true);
            }
        }.execute();
    }



    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method fetches merchantHash and cardToken already stored on merchant server.
     */
    private void fetchMerchantHashes(final Intent intent) {
        // now make the api call.
        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials;
        final Intent baseActivityIntent = intent;
        new AsyncTask<Void, Void, HashMap<String, String>>() {

            @Override
            protected HashMap<String, String> doInBackground(Void... params) {
                try {
                    //TODO Replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/get_merchant_hashes");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                    InputStream responseInputStream = conn.getInputStream();
                    StringBuffer responseStringBuffer = new StringBuffer();
                    byte[] byteContainer = new byte[1024];
                    for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                        responseStringBuffer.append(new String(byteContainer, 0, i));
                    }

                    JSONObject response = new JSONObject(responseStringBuffer.toString());

                    HashMap<String, String> cardTokens = new HashMap<String, String>();
                    JSONArray oneClickCardsArray = response.getJSONArray("data");
                    int arrayLength;
                    if ((arrayLength = oneClickCardsArray.length()) >= 1) {
                        for (int i = 0; i < arrayLength; i++) {
                            cardTokens.put(oneClickCardsArray.getJSONArray(i).getString(0), oneClickCardsArray.getJSONArray(i).getString(1));
                        }
                        return cardTokens;
                    }
                    // pass these to next activity

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, String> oneClickTokens) {
                super.onPostExecute(oneClickTokens);

                baseActivityIntent.putExtra(PayuConstants.ONE_CLICK_CARD_TOKENS, oneClickTokens);
                startActivityForResult(baseActivityIntent, PayuConstants.PAYU_REQUEST_CODE);
            }
        }.execute();
    }



    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method deletes merchantHash and cardToken from server side file.
     *
     * @param cardToken cardToken of card whose merchantHash and cardToken needs to be deleted from merchant server
     */
    private void deleteMerchantHash(String cardToken) {

        final String postParams = "card_token=" + cardToken;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    //TODO Replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/delete_merchant_hash");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                this.cancel(true);
            }
        }.execute();
    }

    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method prepares a HashMap of cardToken as key and merchantHash as value.
     *
     * @param merchantKey     merchant key used
     * @param userCredentials unique credentials of the user usually of the form key:userId
     */
    public HashMap<String, String> getAllOneClickHashHelper(String merchantKey, String userCredentials) {

        // now make the api call.
        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials;
        HashMap<String, String> cardTokens = new HashMap<String, String>();

        try {
            //TODO Replace below url with your server side file url.
            URL url = new URL("https://payu.herokuapp.com/get_merchant_hashes");

            byte[] postParamsByte = postParams.getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postParamsByte);

            InputStream responseInputStream = conn.getInputStream();
            StringBuffer responseStringBuffer = new StringBuffer();
            byte[] byteContainer = new byte[1024];
            for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                responseStringBuffer.append(new String(byteContainer, 0, i));
            }

            JSONObject response = new JSONObject(responseStringBuffer.toString());

            JSONArray oneClickCardsArray = response.getJSONArray("data");
            int arrayLength;
            if ((arrayLength = oneClickCardsArray.length()) >= 1) {
                for (int i = 0; i < arrayLength; i++) {
                    cardTokens.put(oneClickCardsArray.getJSONArray(i).getString(0), oneClickCardsArray.getJSONArray(i).getString(1));
                }

            }
            // pass these to next activity

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cardTokens;
    }





    private void freeCheckOutListener() {

        freeCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String packIds = getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_ORDER);
                String couponCode = getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_COUPON);
                String paymentGateway = "free";

                Intent paymentActivity = new Intent(context, PaymentActivity.class);
                paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_ORDER, packIds);
                paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_COUPON, couponCode);
                paymentActivity.putExtra(PaymentActivity.KEY_PAYMENT_GATEWAY, paymentGateway);
                paymentActivity.putExtra(PaymentActivity.KEY_PLACE_ORDER, getIntent().getSerializableExtra(PaymentActivity.KEY_PLACE_ORDER));
                paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_ORDER, getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_ORDER));
                paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_COUPON, getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_COUPON));
                paymentActivity.putExtra(PaymentActivity.KEY_FREE_CHECKOUT, getIntent().getBooleanExtra(PaymentActivity.KEY_FREE_CHECKOUT, false));

                startActivity(paymentActivity);

            }
        });

    }

    private void ccAvenuListener() {

        ccAvenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //bundle data
                //get rsa key
                //call webview
                //dismiss webview, return status on activity result

                POResponse placeOrder = (POResponse) getIntent().getSerializableExtra(PaymentActivity.KEY_PLACE_ORDER);

                //Mandatory parameters. Other parameters can be added if required.
                String vAccessCode = (String) ServiceUtility.chkNull(Constants.ACCESS_CODE.trim());
                String vMerchantId = (String) ServiceUtility.chkNull(Constants.MERCHANT_ID.trim());
                String vCurrency = (String) ServiceUtility.chkNull("INR");
                String vAmount = (String) ServiceUtility.chkNull(placeOrder.getAmount().trim());

                if(!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")){
                    Intent intent = new Intent(context,WebViewActivity.class);
                    intent.putExtra(AvenuesParams.ACCESS_CODE, vAccessCode);
                    intent.putExtra(AvenuesParams.MERCHANT_ID, vMerchantId);
                    intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(placeOrder.getTxnid()).toString().trim());
                    intent.putExtra(AvenuesParams.CURRENCY, vCurrency);
                    intent.putExtra(AvenuesParams.AMOUNT, vAmount);
                    intent.putExtra(AvenuesParams.PRODUCT_INFO, placeOrder);
                    intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(Constants.RETURN_URL).toString().trim());
                    intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(Constants.RETURN_URL).toString().trim());
                    intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(Constants.GET_RSA).toString().trim());

                    startActivityForResult(intent, AvenuesParams.CCAVENUE_REQUEST_CODE);

                }else{
                    Toast.makeText(context, "All parameters are mandatory.", Toast.LENGTH_SHORT).show();
                }








                /*String packIds = getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_ORDER);
                String couponCode = getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_COUPON);
                String paymentGateway = "cc";

                Intent paymentActivity = new Intent(context, PaymentActivity.class);
                paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_ORDER, packIds);
                paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_COUPON, couponCode);
                paymentActivity.putExtra(PaymentActivity.KEY_PAYMENT_GATEWAY, paymentGateway);
                startActivity(paymentActivity);*/

            }
        });
    }

    private void payuBizListener() {

        payuBiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                POResponse placeOrder = (POResponse) getIntent().getSerializableExtra(PaymentActivity.KEY_PLACE_ORDER);
                Log.v("Packagee_Data", placeOrder.toString());
                navigateToBaseActivity(placeOrder);

                /*Intent paymentActivity = new Intent(context, PaymentActivity.class);
                paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_ORDER, packIds);
                paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_COUPON, couponCode);
                paymentActivity.putExtra(PaymentActivity.KEY_PAYMENT_GATEWAY, paymentGateway);
                startActivity(paymentActivity);*/

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    //TODO This method is used only if integrating One Tap Payments

    /**
     * Returns a HashMap object of cardToken and one click hash from merchant server.
     * <p>
     * This method will be called as a async task, regardless of merchant implementation.
     * Hence, not to call this function as async task.
     * The function should return a cardToken and corresponding one click hash as a hashMap.
     *
     * @param userCreds a string giving the user credentials of user.
     * @return the Hash Map of cardToken and one Click hash.
     **/
    @Override
    public HashMap<String, String> getAllOneClickHash(String userCreds) {
        // 1. GET http request from your server
        // GET params - merchant_key, user_credentials.
        // 2. In response we get a
        // this is a sample code for fetching one click hash from merchant server.
        return getAllOneClickHashHelper(merchantKey, userCreds);
    }

    //TODO This method is used only if integrating One Tap Payments
    @Override
    public void getOneClickHash(String cardToken, String merchantKey, String userCredentials) {

    }


    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method will be called as a async task, regardless of merchant implementation.
     * Hence, not to call this function as async task.
     * This function save the oneClickHash corresponding to its cardToken
     *
     * @param cardToken    a string containing the card token
     * @param oneClickHash a string containing the one click hash.
     **/

    @Override
    public void saveOneClickHash(String cardToken, String oneClickHash) {
        // 1. POST http request to your server
        // POST params - merchant_key, user_credentials,card_token,merchant_hash.
        // 2. In this POST method the oneclickhash is stored corresponding to card token in merchant server.
        // this is a sample code for storing one click hash on merchant server.

        storeMerchantHash(cardToken, oneClickHash);

    }

    //TODO This method is used only if integrating One Tap Payments

    /**
     * This method will be called as a async task, regardless of merchant implementation.
     * Hence, not to call this function as async task.
     * This function delete’s the oneClickHash from the merchant server
     *
     * @param cardToken       a string containing the card token
     * @param userCredentials a string containing the user credentials.
     **/

    @Override
    public void deleteOneClickHash(String cardToken, String userCredentials) {

        // 1. POST http request to your server
        // POST params  - merchant_hash.
        // 2. In this POST method the oneclickhash is deleted in merchant server.
        // this is a sample code for deleting one click hash from merchant server.

        deleteMerchantHash(cardToken);

    }

}
