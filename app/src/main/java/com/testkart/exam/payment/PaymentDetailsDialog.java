package com.testkart.exam.payment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.MknUtils;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.payment.model.Package;
import com.testkart.exam.payment.model.PackagesPayment;
import com.testkart.exam.payment.model.Payment;

import java.util.List;

/**
 * Created by testkart on 21/6/17.
 */

public class PaymentDetailsDialog extends DialogFragment {

    public static final String KEY_PAYMENT_DETAILS = "payment_details";
    public static final String KEY_PACKAGE_ID = "payment_id";

    private TextView packageName, exams, price, quantity, amount, date, expiryDate, total;
    private SessionManager session;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_payment_details, null);

        initViews(rootView);

        builder.setView(rootView);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //do nothing
            }
        });

        return builder.create();
    }

    private void initViews(View rootView) {

        session = new SessionManager(getContext());

        DataPaymentDetails dpd = (DataPaymentDetails) getArguments().getSerializable(KEY_PAYMENT_DETAILS);

        packageName = (TextView)rootView.findViewById(R.id.packageName);
        exams = (TextView)rootView.findViewById(R.id.exams);
        price = (TextView)rootView.findViewById(R.id.price);
        quantity = (TextView)rootView.findViewById(R.id.quantity);
        amount = (TextView)rootView.findViewById(R.id.amount);
        date = (TextView)rootView.findViewById(R.id.date);
        expiryDate = (TextView)rootView.findViewById(R.id.expiryDate);
        total = (TextView)rootView.findViewById(R.id.total);


        quantity.setText("1");

        if(dpd != null){

            int pid = getArguments().getInt(KEY_PACKAGE_ID);

            Package p = dpd.getPackageList().get(pid);

            Payment pp = dpd.getPayment();

            PackagesPayment ppp = p.getPackagesPayment();

            List<com.testkart.exam.payment.model.Exam> examList = dpd.getPackageList().get(pid).getExam();

            if(p != null){

                if(p.getExpiryDays() != null){

                    if(p.getExpiryDays().equals(pid)){

                        expiryDate.setText("Unlimited");
                    }else{

                        String tranxDate = MknUtils.getFormatDate(p.getExpiryDays(), "yyyy-MM-dd", "dd-MM-yyyy");
                        expiryDate.setText((p.getExpiryDays() != null) ? tranxDate : "");
                    }

                }


                packageName.setText((p.getName() != null) ? p.getName() : "");
                amount.setText((ppp.getAmount() != null)? MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+ppp.getAmount():"");
                price.setText((ppp.getPrice() != null)? MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+ppp.getPrice():"");


            }


            if(pp != null){

                if(pp.getDate() != null){

                    String tranxDate = MknUtils.getFormatDate(pp.getDate(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy");
                    date.setText((pp.getDate() != null)? tranxDate : "");

                }

            }


            if(!examList.isEmpty()){

                StringBuilder sb = new StringBuilder();

                for (com.testkart.exam.payment.model.Exam e:
                examList) {

                    sb.append(e.getName()+", ");
                }


                String eStr = sb.toString().replaceAll(", $", "");

                exams.setText(eStr);

            }


            total.setText(MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+ppp.getAmount());

        }

    }
}
