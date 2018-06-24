package com.testkart.exam.packages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.testkart.exam.edu.login.LoginActivity1;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.packages.model.DataOrderSummary;
import com.testkart.exam.packages.model.coupon.CouponResponse;
import com.testkart.exam.packages.model.place_order.DataPlaceOrder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by testkart on 12/6/17.
 */

public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener,
        CartListAdapter.CartItemListener,
        CouponCodeDialog.ApplyCouponListener,
        OrderSummaryDialog.OnOrderPlaceListener{

    private Context context = this;
    private SessionManager session;

    private String ccCode = "";

    private ProgressDialog progressDialog;

    private int count = 0;

    private FrameLayout backButton;
    private LinearLayout editCoupon;
    private TextView placeOrder, totalPrice, couponCode, itemCount;

    private ListView cartItems;


    private DBHelper dbHelper;

    private int payableAmount = 0;

    public static ShoppingCartActivity act;

    private ExpandableLayout expandableView;

    private ImageView couponIndicator;

    private TextView hideDetails;

    private TextView price, handlingCharges, coupon, total;
    private LinearLayout couponContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_cart);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //init views
        initialization();
    }

    private void initialization() {

        dbHelper = new DBHelper(this);
        session = new SessionManager(this);

        act = this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        cartItems = (ListView)findViewById(R.id.cartItems);

        placeOrder = (TextView)findViewById(R.id.placeOrder);
        totalPrice = (TextView)findViewById(R.id.totalPrice);
        couponCode = (TextView)findViewById(R.id.couponCode);
        itemCount = (TextView)findViewById(R.id.itemCount);

        couponContainer = (LinearLayout)findViewById(R.id.couponContainer);

        editCoupon = (LinearLayout)findViewById(R.id.editCoupon);
        editCoupon.setTag("edit");

        couponIndicator = (ImageView)findViewById(R.id.couponIndicator);
        expandableView = (ExpandableLayout)findViewById(R.id.expandableView);

        hideDetails = (TextView)findViewById(R.id.hideDetails);
        hideDetails.setTag("SHOW");
        hideDetailsListener();

       // editCoupon = (LinearLayout)findViewById(R.id.editCoupon);
        price = (TextView)findViewById(R.id.price);
        coupon = (TextView)findViewById(R.id.coupon);
        total = (TextView)findViewById(R.id.total);


        placeOrder.setOnClickListener(this);
        totalPrice.setOnClickListener(this);
        editCoupon.setOnClickListener(this);

        backButton = (FrameLayout)findViewById(R.id.backButton);

        backButtonListener();

        updateViews();

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


    int baseAmount =0;

    private void updateViews() {

        payableAmount = 0;

        ArrayList<DataOrderSummary> cartItemsList = dbHelper.getTotalCartItems();

        for (DataOrderSummary d:
             cartItemsList) {

            payableAmount += Float.parseFloat(d.getPackageAmount());
        }


        baseAmount = payableAmount;

        count = cartItemsList.size();

        itemCount.setText(count+" Items");

        totalPrice.setText("₹"+payableAmount+"");

       // if(session.isLoggedIn()){

            if(payableAmount > 0){

                editCoupon.setVisibility(View.VISIBLE);


            }else{

                editCoupon.setVisibility(View.GONE);
            }

        /*}else{

                //change to gone
                couponContainer.setVisibility(View.VISIBLE);

        }*/



        CartListAdapter adapter = new CartListAdapter(context, cartItemsList, true);
        cartItems.setAdapter(adapter);

        setListViewHeightBasedOnChildren(cartItems);


        updateOrderSummary();
    }



    private void updateOrderSummary() {

        if(couponApplied){

            couponContainer.setVisibility(View.VISIBLE);
            coupon.setText("Yes "+"( -₹"+discountAmount+" )");

            price.setText("₹"+baseAmount);

        }else{

            couponContainer.setVisibility(View.GONE);

            price.setText("₹"+baseAmount);
        }

    }



    private void backButtonListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backPressed = true;

                finish();
            }
        });
    }


    public static boolean cartRetain = false;
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.placeOrder:

                //check if cart is not empty
                if(count > 0){

                    //check if user login
                    if(session.isLoggedIn()){

                        /*// This method will be executed once the timer is over
                        // Start your app main activity
                        Intent cartActivity = new Intent(context, PaymentActivity.class);
                        startActivity(cartActivity);*/

                        /*OrderSummaryDialog sd = new OrderSummaryDialog();

                        Bundle args = new Bundle();
                        args.putInt(OrderSummaryDialog.KEY_ORDER_PRICE, payableAmount);
                        args.putBoolean(OrderSummaryDialog.KEY_COUPON_APPLIED, couponApplied);
                        args.putFloat(OrderSummaryDialog.KEY_COUPON_AMOUNT, discountAmount);
                        args.putString(OrderSummaryDialog.KEY_ITEM_COUNT, count+"" );

                        sd.setArguments(args);
                        sd.show(getSupportFragmentManager(), "OSD");*/

                        onOrderPlace();

                    }else{

                        // This method will be executed once the timer is over
                        // Start your app main activity

                        Intent i = new Intent(context, LoginActivity1.class);
                        i.putExtra(LoginActivity1.KEY_RETAIN_CART, true);
                        cartRetain = true;

                        startActivity(i);

                        //finishAffinity();
                    }


                }else{

                    //show alert dialog
                    showACartEmptyAlert();

                }

                break;

            case R.id.totalPrice:

                //nothing to call

                break;

            case R.id.editCoupon:

                if(((String)editCoupon.getTag()).equalsIgnoreCase("edit")){

                    CouponCodeDialog ccd = new CouponCodeDialog();

                    Bundle args = new Bundle();
                    args.putString(CouponCodeDialog.KEY_AMOUNT, payableAmount+"");
                    ccd.setArguments(args);
                    ccd.show(getSupportFragmentManager(), "CCD");

                }else if(((String)editCoupon.getTag()).equalsIgnoreCase("remove")){

                    //make api call to remove coupon
                    if (ApiClient.isNetworkAvailable(context)) {

                        //show progress dialog
                        progressDialog.show();

                        makeAPiCall();

                    } else {

                        Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

                    }


                }

                break;
        }
    }

    private void makeAPiCall() {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CouponResponse> call = apiService.deleteCoupon(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        /*String URL = "http://silversyclops.net/envato/demos/edu-ibps/rest/Checkouts/couponDelete.json";


        Call<CouponResponse> call = apiService.deleteCoupon1(URL,
                "BF7F08BB4F8274F",
                "43f5689f2b97ef02abcc5fe30ee12efd30417ee894bad713efc0dbfd40279c00");*/

        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){

                            couponApplied = false;

                            ccCode = "";

                            couponCode.setText("APPLY COUPONS");
                            updateViews();

                            couponIndicator.setImageResource(R.drawable.ic_navigate_next_black_24dp);
                            editCoupon.setTag("edit");


                        }else{

                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{


                        Toast.makeText(context, "COUPON RESPONSE BODY NULL", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(context, "ERROR RESPONSE CODE: "+code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });

    }

    private void showACartEmptyAlert() {


        Intent emptyCart = new Intent(context, EmptyCartActivity.class);
        startActivity(emptyCart);
        finish();


        /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("My Cart");
        builder.setMessage("Looks like you don't have any exam in your cart. Let's do some shopping");

        builder.setPositiveButton("Continue Shopping", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing just dismiss

                if(session.isLoggedIn()){

                    Intent packageList = new Intent(context, PackageListActivity.class);
                    startActivity(packageList);

                }

                finish();
            }
        });

        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                *//*if(!session.isLoggedIn()){

                    Intent packageList = new Intent(context, PackageListActivity.class);
                    startActivity(packageList);

                }

                finish();*//*
                finish();

            }
        });

        builder.setCancelable(false);

        builder.create().show();*/
    }

    @Override
    public void onItemRemovedFromCart(DataOrderSummary removedItem) {

        dbHelper.removeCartItem(removedItem.getPackageId());

        PackageListActivity.KEY_CART_CHANGED = true;
        PackageListActivity.checkRemoveIds.add(removedItem.getPackageId());

        //update item count
        updateViews();

        if(count == 0){

            //show alert dialog
            showACartEmptyAlert();

        }

    }


    private boolean couponApplied = false;
    private float discountAmount;
    @Override
    public void onCouponApplied(String coupon, boolean status, int payAmount, float discountAmount) {

        if(status){

            //change edit icon to remove icon
            //change text add coupon to Coupon code "DEMO10" is applied
            couponApplied = status;
            this.discountAmount = discountAmount;

            editCoupon.setTag("remove");
            couponIndicator.setImageResource(R.drawable.ic_close_black_24dp);

            //Coupon 'Demo 10' Applied
            couponCode.setText("Coupon '"+coupon+"' Applied");
            ccCode = coupon;

            payableAmount = payAmount;
            totalPrice.setText("₹"+payableAmount+"");

            updateOrderSummary();
        }

    }

    @Override
    public void onOrderPlace() {

        //check network connection
        // get all cart items
        // make api call

        //make api call to remove coupon
        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            //progressDialog.show();

            submitOrder();

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void submitOrder() {


        ArrayList<DataOrderSummary> cartList = dbHelper.getTotalCartItems();

        List<Integer> packIds = new ArrayList<>();
        DataPlaceOrder dPlaceOrder = new DataPlaceOrder();

        String pids = "";

        for (DataOrderSummary d:
                cartList) {

            packIds.add(new Integer(d.getPackageId()));

            pids += d.getPackageId()+",";
        }


        //check if order is free checkout
        if(payableAmount == 0){

            //free checkout
            //bundle details
            Intent paymentActivity = new Intent(context, FinalReviewActivity.class);
            paymentActivity.putExtra(FinalReviewActivity.KEY_PRICE, price.getText().toString());
            paymentActivity.putExtra(FinalReviewActivity.KEY_TOTAL_PRICE, totalPrice.getText().toString());
            paymentActivity.putExtra(FinalReviewActivity.KEY_COUPON, coupon.getText().toString());
            paymentActivity.putExtra(FinalReviewActivity.KEY_COUPON_APPLYED, couponApplied);
            paymentActivity.putExtra(FinalReviewActivity.KEY_BASE_AMOUNT, baseAmount+"");
            paymentActivity.putExtra(FinalReviewActivity.KEY_PAYABLE_AMOUNT, payableAmount+"");
            paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_ORDER, pids);
            paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_COUPON, ccCode);
            startActivity(paymentActivity);

        }else{

            //bundle details
            Intent paymentActivity = new Intent(context, FinalReviewActivity.class);
            paymentActivity.putExtra(FinalReviewActivity.KEY_PRICE, price.getText().toString());
            paymentActivity.putExtra(FinalReviewActivity.KEY_TOTAL_PRICE, totalPrice.getText().toString());
            paymentActivity.putExtra(FinalReviewActivity.KEY_COUPON, coupon.getText().toString());
            paymentActivity.putExtra(FinalReviewActivity.KEY_COUPON_APPLYED, couponApplied);
            paymentActivity.putExtra(FinalReviewActivity.KEY_PAYABLE_AMOUNT, payableAmount+"");
            paymentActivity.putExtra(FinalReviewActivity.KEY_BASE_AMOUNT, baseAmount+"");
            paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_ORDER, pids);
            paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_COUPON, ccCode);
            startActivity(paymentActivity);


        }



        /*ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        ArrayList<DataOrderSummary> cartList = dbHelper.getTotalCartItems();

        List<Integer> packIds = new ArrayList<>();
        DataPlaceOrder dPlaceOrder = new DataPlaceOrder();

        String pids = "";

        for (DataOrderSummary d:
        cartList) {

            packIds.add(new Integer(d.getPackageId()));

            pids += d.getPackageId()+",";
        }

        dPlaceOrder.setPrivateKey(session.getUserDetails().get(SessionManager.KEY_PRIVATE));
        dPlaceOrder.setPublicKey(session.getUserDetails().get(SessionManager.KEY_PUBLIC));
        dPlaceOrder.setResponses(packIds);

        Gson gson = new Gson();
        String j = gson.toJson(dPlaceOrder);

        Log.v("My_Json_Placeorder", j);

        pids = pids.replaceAll(",$", "");

        Intent paymentActivity = new Intent(context, PaymentActivity.class);
        paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_ORDER, pids);
        paymentActivity.putExtra(PaymentActivity.KEY_SUBMIT_COUPON, ccCode);
        startActivity(paymentActivity);*/

        /*Call<CouponResponse> call = apiService.placeOrder(dPlaceOrder);

        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, POResponse<CouponResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){

                            //go to payment
                            Intent cartActivity = new Intent(context, PaymentActivity.class);
                            startActivity(cartActivity);


                        }else{

                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{


                        Toast.makeText(context, "COUPON RESPONSE BODY NULL", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(context, "ERROR RESPONSE CODE: "+code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });*/

    }


    private boolean backPressed = false;
    @Override
    public void onBackPressed() {

        backPressed = true;

        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();

        PackageListActivity.KEY_CART_CHANGED = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {

            if(backPressed){

                //do nothing

            }else{

                if(!session.isLoggedIn()){
                    if(!cartRetain){

                        dbHelper.clearCart();
                    }
                }

            }

            dbHelper.close();

        }

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
}
