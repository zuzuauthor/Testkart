package com.testkart.exam.packages;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.testkart.exam.R;


/**
 * Created by testkart on 12/6/17.
 */

public class OrderSummaryDialog extends DialogFragment {

    public static final String KEY_ORDER_PRICE = "order_PRICE";
    public static final String KEY_COUPON_APPLIED = "coupon_applied";
    public static final String KEY_COUPON_AMOUNT = "coupon_amount";
    public static final String KEY_ITEM_COUNT = "item_count";

    private ImageView closeDialog;
    private TextView itemCount, price, handlingCharges, coupon, total;
    private LinearLayout couponContainer;


    private OnOrderPlaceListener mListener;
    public interface OnOrderPlaceListener{

        void onOrderPlace();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (OnOrderPlaceListener)context;

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_order_summary, null);
        initViews(rootView);

        builder.setView(rootView);

        builder.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(mListener != null){

                    mListener.onOrderPlace();
                }

            }
        });


        return builder.create();
    }

    private void initViews(View rootView) {

        couponContainer = (LinearLayout)rootView.findViewById(R.id.couponContainer);
        itemCount = (TextView)rootView.findViewById(R.id.itemCount);
        price = (TextView)rootView.findViewById(R.id.price);
        handlingCharges = (TextView)rootView.findViewById(R.id.handlingCharges);
        coupon = (TextView)rootView.findViewById(R.id.coupon);
        total = (TextView)rootView.findViewById(R.id.total);
        closeDialog = (ImageView)rootView.findViewById(R.id.closeDialog);
        closeDialogListener();

        //get bundle extra
        Bundle args = getArguments();

        if(args != null){

            //updateViews
            updateViews(args);
        }
    }

    private void updateViews(Bundle args) {

        if(args.getBoolean(KEY_COUPON_APPLIED)){

            couponContainer.setVisibility(View.VISIBLE);
            coupon.setText("Yes "+"( -₹"+args.getFloat(KEY_COUPON_AMOUNT)+" )");

            price.setText("$"+(args.getInt(KEY_ORDER_PRICE)+args.getFloat(KEY_COUPON_AMOUNT)));

        }else{

            couponContainer.setVisibility(View.GONE);

            price.setText("₹"+args.getInt(KEY_ORDER_PRICE));
        }

        itemCount.setText("Summary ("+args.getString(KEY_ITEM_COUNT)+" items)");


        total.setText("₹"+(args.getInt(KEY_ORDER_PRICE)/* - args.getFloat(KEY_COUPON_AMOUNT)*/));

    }

    private void closeDialogListener() {

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }
}
