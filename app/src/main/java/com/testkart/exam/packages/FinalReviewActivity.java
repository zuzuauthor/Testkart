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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chuross.library.ExpandableLayout;
import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.HttpHandler;
import com.testkart.exam.packages.model.DataOrderSummary;
import com.testkart.exam.packages.model.payu.POResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by elfemo on 5/9/17.
 */

public class FinalReviewActivity extends AppCompatActivity implements CartListAdapter.CartItemListener {

    public static final String KEY_PRICE = "price";
    public static final String KEY_TOTAL_PRICE = "total_price";
    public static final String KEY_COUPON = "coupon";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_COUPON_APPLYED = "price";
    public static final String KEY_PAYABLE_AMOUNT = "payable_amount";
    public static final String KEY_BASE_AMOUNT = "base_price";

    private ProgressDialog progressDialog;

    private Context context = this;
    private SessionManager session;

    private TextView userName, userAddress;

    private ListView cartItems;

    private TextView hideDetails, price, coupon, total, totalPrice;
    private LinearLayout couponContainer;
    private ExpandableLayout expandableView;
    private Button placeOrder;

    public static FinalReviewActivity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_review);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Review Your Order");
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        act = this;

        userName = (TextView)findViewById(R.id.userName);
        userAddress = (TextView)findViewById(R.id.userAddress);

        cartItems = (ListView)findViewById(R.id.cartItems);

        hideDetails = (TextView)findViewById(R.id.hideDetails);
        hideDetails.setTag("SHOW");
        hideDetailsListener();

        expandableView = (ExpandableLayout)findViewById(R.id.expandableView);
        couponContainer = (LinearLayout)findViewById(R.id.couponContainer);
        price = (TextView)findViewById(R.id.price);
        coupon = (TextView)findViewById(R.id.coupon);
        total = (TextView)findViewById(R.id.total);
        totalPrice = (TextView)findViewById(R.id.totalPrice);

        placeOrder = (Button)findViewById(R.id.placeOrder);
        placeOrderListener();

        updateViews();
    }


    private void updateViews() {

        //update your details
        userName.setText(session.getUserDetails().get(SessionManager.KEY_NAME));
        String address = session.getUserDetails().get(SessionManager.KEY_ADDRESS)+"\n"+
                "Mobile: "+session.getUserDetails().get(SessionManager.KEY_PHONE_NUMBER)+"\n"+
                "Email: "+session.getUserDetails().get(SessionManager.KEY_EMAIL);

        userAddress.setText(address);


        DBHelper dbHelper = new DBHelper(this);

        ArrayList<DataOrderSummary> cartItemsList = dbHelper.getTotalCartItems();

        CartListAdapter adapter = new CartListAdapter(context, cartItemsList, false);
        cartItems.setAdapter(adapter);

        setListViewHeightBasedOnChildren(cartItems);

        //get intent

        boolean couponApplied = getIntent().getBooleanExtra(KEY_COUPON_APPLYED, false);
        String extPrice = getIntent().getStringExtra(KEY_PAYABLE_AMOUNT);
        String extTotalPrice = getIntent().getStringExtra(KEY_TOTAL_PRICE);
        String extCoupon = getIntent().getStringExtra(KEY_COUPON);
        String extTotal = getIntent().getStringExtra(KEY_TOTAL);
        String basePrice = getIntent().getStringExtra(KEY_BASE_AMOUNT);

        totalPrice.setText(extTotalPrice+"");

        placeOrder.setText("PAY Rs. "+extPrice+" SECURELY");

        updateOrderSummary(couponApplied, extCoupon, basePrice);

    }


    private void updateOrderSummary(boolean couponApplied, String couponTxt, String basePrice) {

        if(couponApplied){

            couponContainer.setVisibility(View.VISIBLE);
            coupon.setText(couponTxt);  //"Yes "+"( -₹"+discountAmount+" )"
            price.setText("₹"+basePrice);

        }else{

            couponContainer.setVisibility(View.GONE);
            price.setText("₹"+basePrice);
        }

    }


    private void placeOrderListener() {

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String packIds = getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_ORDER);
                String couponCode = getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_COUPON);

                //place order and get product info
                if (ApiClient.isNetworkAvailable(context)) {

                    if(getIntent().getStringExtra(KEY_PAYABLE_AMOUNT).equals("0")){

                        Intent paymentOption = new Intent(context, PaymentOptionsActivity.class);
                        paymentOption.putExtra(PaymentActivity.KEY_SUBMIT_ORDER, packIds);
                        paymentOption.putExtra(PaymentActivity.KEY_SUBMIT_COUPON, couponCode);
                        paymentOption.putExtra(PaymentActivity.KEY_FREE_CHECKOUT, true);
                        startActivity(paymentOption);

                    }else{

                        //show progress dialog
                        progressDialog.show();
                        makeApiCall(packIds, couponCode, "payu");
                    }



                } else {

                    Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void makeApiCall(String packIds, String couponCode, String paymentGateway) {

        final String URL1 = "http://www.testkart.com/Checkouts/placeOrderMobile?public_key="+session.getUserDetails().get(SessionManager.KEY_PUBLIC)+"&private_key="+session.getUserDetails().get(SessionManager.KEY_PRIVATE)+"&responses="+packIds+"&couponCode="+couponCode+"&payment_gateway="+paymentGateway;

        Log.v("PLACE_ORDER_URL", URL1);

        new AsyncTask<String, String, String>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.show();

            }

            @Override
            protected String doInBackground(String... strings) {

                HttpHandler sh = new HttpHandler();

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(URL1);

               // Log.v("PLACE_ODER", jsonStr);

                return jsonStr;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if(s != null){

                    try {

                        JSONObject jObj = new JSONObject(s);

                        boolean status = jObj.getBoolean("status");
                        String message = jObj.getString("message");

                        if(status){

                            JSONObject j = jObj.getJSONObject("response");

                            String paymentHash = j.getString("hash");
                            String merchantId = j.getString("merchantId");
                            String merchantKey = j.getString("merchantKey");
                            String merchantSalt = j.getString("merchantSalt");
                            String txnid = j.getString("txnid");
                            String amount = j.getString("amount");
                            String productinfo = j.getString("productinfo");
                            String email = j.getString("email");
                            String firstname = j.getString("firstname");
                            String surl = j.getString("surl");
                            String furl = j.getString("furl");
                            String curl = j.getString("curl");
                            String address1 = j.getString("address1");
                            String phone = j.getString("phone");


                            Log.v("PLACE_DATA","paymentHash: "+paymentHash);
                            Log.v("PLACE_DATA","merchantKey: "+merchantKey);
                            Log.v("PLACE_DATA","txnid: "+txnid);
                            Log.v("PLACE_DATA","amount: "+amount);
                            Log.v("PLACE_DATA","productinfo: "+productinfo);
                            Log.v("PLACE_DATA","email: "+email);
                            Log.v("PLACE_DATA","firstname: "+firstname);
                            Log.v("PLACE_DATA","surl: "+surl);
                            Log.v("PLACE_DATA","furl: "+furl);
                            Log.v("PLACE_DATA","phone: "+phone);


                            POResponse placeOrder = new POResponse();
                            placeOrder.setHash(paymentHash);
                            placeOrder.setMerchantId(merchantId);
                            placeOrder.setMerchantKey(merchantKey);
                            placeOrder.setMerchantSalt(merchantSalt);
                            placeOrder.setTxnid(txnid);
                            placeOrder.setProductinfo(productinfo);
                            placeOrder.setEmail(email);
                            placeOrder.setFirstname(firstname);
                            placeOrder.setSurl(surl);
                            placeOrder.setFurl(furl);
                            placeOrder.setCurl(curl);
                            placeOrder.setAddress1(address1);
                            placeOrder.setPhone(phone);
                            placeOrder.setAmount(amount);


                            Intent paymentOption = new Intent(context, PaymentOptionsActivity.class);
                            paymentOption.putExtra(PaymentActivity.KEY_FREE_CHECKOUT, false);
                            paymentOption.putExtra(PaymentActivity.KEY_PLACE_ORDER, placeOrder);
                            startActivity(paymentOption);


                        }else{

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{

                    Toast.makeText(context, "RESPONSE BODY IS NULL", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
        }.execute(null, null, null);

    }


    private void hideDetailsListener() {

        hideDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hideDetails.getTag().equals("SHOW")){

                    hideDetails.setTag("HIDE");
                    hideDetails.setText("SHOW DETAILS");
                    expandableView.collapse();

                }else{

                    hideDetails.setTag("SHOW");
                    hideDetails.setText("HIDE DETAILS");
                    expandableView.expand();

                }

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


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onItemRemovedFromCart(DataOrderSummary removedItem) {

    }
}
