package com.testkart.exam.packages.oderconfirmation;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.packages.CartListAdapter;
import com.testkart.exam.packages.FinalReviewActivity;
import com.testkart.exam.packages.model.payu.PaymentResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by elfemo on 15/9/17.
 */

public class PaymentDetailsDailog extends DialogFragment  {

    private SessionManager session;

    private TextView placeON, deliverOn, itemsCount, userName, userAddress, orderNo;
    private LinearLayout couponContainer;
    private TextView paymentMode, price, coupon, paybaleAmount;
    private ListView packageList;

    private FrameLayout backButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Fullscreen);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_payment_details1, null);

        initViews(rootView);

        builder.setView(rootView);

        return builder.create();
    }

    private void initViews(View rootView) {

        session = new SessionManager(getContext());

       // placeON = (TextView)rootView.findViewById(R.id.placeON);
        deliverOn = (TextView)rootView.findViewById(R.id.deliverOn);
        itemsCount = (TextView)rootView.findViewById(R.id.itemsCount);
        userName = (TextView)rootView.findViewById(R.id.userName);
        userAddress = (TextView)rootView.findViewById(R.id.userAddress);
        orderNo = (TextView)rootView.findViewById(R.id.orderNo);
        paymentMode = (TextView) rootView.findViewById(R.id.paymentMode);
        price = (TextView) rootView.findViewById(R.id.price);
        coupon = (TextView) rootView.findViewById(R.id.coupon);
        paybaleAmount = (TextView) rootView.findViewById(R.id.paybaleAmount);

        couponContainer = (LinearLayout)rootView.findViewById(R.id.couponContainer);

        packageList = (ListView)rootView.findViewById(R.id.packageList);
        backButton = (FrameLayout)rootView.findViewById(R.id.backButton);
        backButtonListener();

        updateViews();

    }

    private void updateViews() {

        PaymentResponse paymentResponse = (PaymentResponse) getArguments().getSerializable(OrderConfirmationActivity.KEY_PAYMENT_DETAILS);

        DBHelper dbHelper = new DBHelper(getContext());
        ArrayList<com.testkart.exam.packages.model.DataOrderSummary> cartItemsList = dbHelper.getTotalCartItems();
        CartListAdapter adapter = new CartListAdapter(getContext(), cartItemsList, false);
        packageList.setAdapter(adapter);
        FinalReviewActivity.setListViewHeightBasedOnChildren(packageList);

        updatePaymentDetails(paymentResponse, cartItemsList.size());

    }

    private void updatePaymentDetails(PaymentResponse paymentResponse, int size) {

        if(paymentResponse != null){

            orderNo.setText("Order #"+paymentResponse.getTxnid());
            deliverOn.setText(getDate(System.currentTimeMillis()));
            itemsCount.setText("(items "+size+")");

            //update your details
            userName.setText(session.getUserDetails().get(SessionManager.KEY_NAME));
            String address = session.getUserDetails().get(SessionManager.KEY_ADDRESS)+"\n"+
                    "Mobile: "+session.getUserDetails().get(SessionManager.KEY_PHONE_NUMBER)+"\n"+
                    "Email: "+session.getUserDetails().get(SessionManager.KEY_EMAIL);

            userAddress.setText(address);


            if(paymentResponse.getMode().equalsIgnoreCase("CC")){

                paymentMode.setText("Credit Card");

            }else if(paymentResponse.getMode().equalsIgnoreCase("NB")){

                paymentMode.setText("Net Banking");

            }else if(paymentResponse.getMode().equalsIgnoreCase("FREE")){

                paymentMode.setText("FREE");

            }else{

                paymentMode.setText(paymentResponse.getMode());
            }


            /*if(getArguments().getString("PaymentActivity.KEY_SUBMIT_COUPON") != null){

                couponContainer.setVisibility(View.VISIBLE);
                coupon.setText("Yes "+"( -₹"+discountAmount+" )");

                price.setText("₹"+baseAmount);

            }else{

                couponContainer.setVisibility(View.GONE);

                price.setText("₹"+baseAmount);
            }

            price.setText();
            coupon.setText();*/
            paybaleAmount.setText("Rs. "+paymentResponse.getAmount());

        }
    }


    private String getDate(long time) {

        Date date = new Date(time); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        return sdf.format(date);
    }

    private void backButtonListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
    }

}
