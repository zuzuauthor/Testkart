package com.testkart.exam.packages;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.packages.model.coupon.CouponResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by testkart on 12/6/17.
 */

public class CouponCodeDialog extends DialogFragment {

    public static final String KEY_AMOUNT = "amouont";

    private EditText input;

    private SessionManager session;

    private ProgressDialog progressDialog;

    private ApplyCouponListener mListener;
    public interface ApplyCouponListener{

        void onCouponApplied(String coupon, boolean status, int payAmount, float discountAmount);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (ApplyCouponListener)context;

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Apply Coupon");
        //builder.setMessage("Enter coupon code to avail extra discount.");

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_coupon_code, null);

        initView(rootView);

        builder.setView(rootView);

        /*LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(15,15,15,15);

        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewParams.gravity = Gravity.CENTER;
        viewParams.bottomMargin = 15;

        TextView message = new TextView(getContext());
        message.setText("Enter coupon code to avail extra discount.");
        message.setLayoutParams(viewParams);


        input = new EditText(getContext());
        input.setHint("Coupon Code");
        input.setGravity(Gravity.CENTER);
        input.setLayoutParams(viewParams);

        container.addView(message);
        container.addView(input);

        builder.setView(container);

        input.requestFocus();*/
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //do nothing we going to override this

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

            }});

        return builder.create();
    }

    private void initView(View rootView) {

        session = new SessionManager(getContext());

        input = (EditText)rootView.findViewById(R.id.input);
    }


    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check network connection
                    //get coupon <code></code>
                    //make api request

                    if (ApiClient.isNetworkAvailable(getContext())) {

                        //show progress dialog
                        progressDialog.show();

                        String coupon = input.getText().toString().trim();
                        String amount = getArguments().getString(KEY_AMOUNT);

                        makeAPiCall(input, coupon, amount);

                    } else {

                        Toast.makeText(getContext(), Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void makeAPiCall(final EditText input, final String coupon, String amount) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CouponResponse> call = apiService.checkCoupon(coupon, amount,
                session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        /*String URL = "http://silversyclops.net/envato/demos/edu-ibps/rest/Checkouts/coupon.json";

        Call<CouponResponse> call = apiService.checkCoupon1(URL,
                coupon, amount,
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

                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                            if(mListener != null){

                                mListener.onCouponApplied(coupon, true, response.body().getResponse().getCoupon().getFinalAmount(),
                                        Float.parseFloat(response.body().getResponse().getCoupon().getDiscountRate()));
                            }

                            dismiss();

                        }else{

                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{


                        Toast.makeText(getContext(), "COUPON RESPONSE BODY NULL", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(getContext(), "ERROR RESPONSE CODE: "+code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });

    }

}
