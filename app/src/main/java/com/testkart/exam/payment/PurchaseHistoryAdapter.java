package com.testkart.exam.payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.MknUtils;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.payment.model.Payment;

import java.util.ArrayList;

/**
 * Created by testkart on 21/6/17.
 */

public class PurchaseHistoryAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<DataPaymentDetails> dataSet;
    private SessionManager session;


    private PaymentDetailListener mListener;
    public interface PaymentDetailListener{

        void onViewDetails(DataPaymentDetails dpd);
        void onTakeExam(DataPaymentDetails dpd);
        void readMagazine(DataPaymentDetails dpd);
    }

    public PurchaseHistoryAdapter(Context context, ArrayList<DataPaymentDetails> dataSet){

        this.dataSet = dataSet;
        this.inflater = LayoutInflater.from(context);
        this.session = new SessionManager(context);

        this.mListener = (PaymentDetailListener)context;
    }


    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataSet.indexOf(dataSet.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.row_item_payment, parent, false);
            mHolder = new ViewHolder();

            mHolder.remark = (TextView)convertView.findViewById(R.id.remark);
            mHolder.paymentMode = (TextView)convertView.findViewById(R.id.paymentMode);
            mHolder.dateTime = (TextView)convertView.findViewById(R.id.dateTime);
            mHolder.netAmount = (TextView)convertView.findViewById(R.id.netAmount);
            mHolder.couponDiscount = (TextView)convertView.findViewById(R.id.couponDiscount);
            mHolder.amount = (TextView)convertView.findViewById(R.id.amount);
            mHolder.tranxId = (TextView)convertView.findViewById(R.id.tranxId);
            mHolder.viewDetails = (Button)convertView.findViewById(R.id.viewDetails);
            mHolder.viewLine = (View)convertView.findViewById(R.id.viewLine);
            mHolder.readMagazine = (Button)convertView.findViewById(R.id.readMagazine);
            mHolder.takeExam = (Button)convertView.findViewById(R.id.takeExam);
            mHolder.takeExam.setVisibility(View.GONE);
            mHolder.readMagazine.setVisibility(View.GONE);

            convertView.setTag(mHolder);


        }else{

            mHolder = (ViewHolder) convertView.getTag();

        }


        final DataPaymentDetails dpd = (DataPaymentDetails) getItem(position);

        if(dpd != null){

            Payment p = dpd.getPayment();

            if(p.getStatus().equalsIgnoreCase("Approved")){

                //Success
                mHolder.remark.setText("Success");
                mHolder.remark.setBackgroundResource(R.drawable.bg_success);

                mHolder.takeExam.setVisibility(View.VISIBLE);
                mHolder.readMagazine.setVisibility(View.VISIBLE);

                mHolder.viewDetails.setVisibility(View.VISIBLE);
                mHolder.viewLine.setVisibility(View.VISIBLE);

            }else if(p.getStatus().equalsIgnoreCase("Pending")){

                //Pending
                mHolder.remark.setText("Pending");
                mHolder.remark.setBackgroundResource(R.drawable.bg_pending);

                mHolder.takeExam.setVisibility(View.GONE);
                mHolder.readMagazine.setVisibility(View.GONE);

                mHolder.viewDetails.setVisibility(View.GONE);
                mHolder.viewLine.setVisibility(View.GONE);

            }else{

                //Pending
                mHolder.remark.setText("Failed");
                mHolder.remark.setBackgroundResource(R.drawable.bg_fail);

                mHolder.takeExam.setVisibility(View.GONE);
                mHolder.readMagazine.setVisibility(View.GONE);

                mHolder.viewDetails.setVisibility(View.GONE);
                mHolder.viewLine.setVisibility(View.GONE);

            }


            if (p.getPaymentsFor().equalsIgnoreCase("0")) {

                mHolder.takeExam.setVisibility(View.VISIBLE);
                mHolder.readMagazine.setVisibility(View.GONE);

            }else if(p.getPaymentsFor().equalsIgnoreCase("1")){

                mHolder.takeExam.setVisibility(View.GONE);
                mHolder.readMagazine.setVisibility(View.VISIBLE);
            }



            mHolder.paymentMode.setText((p.getName() != null)? p.getName() : "");

            if(p.getDate() != null){

                String tranxDate = MknUtils.getFormatDate(p.getDate(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss");
                mHolder.dateTime.setText((p.getDate() != null)? tranxDate : "");
            }


            if(p.getAmount() != null){

                String amnt = MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+p.getAmount();

                mHolder.netAmount.setText((p.getAmount() != null)? amnt : "");
                mHolder.amount.setText((p.getAmount() != null)? amnt : "");

            }


            mHolder.couponDiscount.setText((p.getCouponAmount() != null)? (String)p.getCouponAmount() : "NA");

            mHolder.tranxId.setText((p.getTransactionId() != null)? p.getTransactionId() : "");


            mHolder.viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mListener != null){

                        mListener.onViewDetails(dpd);

                    }

                }
            });

            mHolder.takeExam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mListener != null){

                        mListener.onTakeExam(dpd);

                    }
                }
            });


            mHolder.readMagazine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mListener != null){

                        mListener.readMagazine(dpd);

                    }
                }
            });

        }

        return convertView;
    }

    class ViewHolder{

        TextView remark, paymentMode, dateTime, netAmount, couponDiscount, amount, tranxId;
        Button viewDetails, takeExam, readMagazine;
        View viewLine;

    }

}
