package com.testkart.exam.edu.transaction;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.testkart.exam.R;
import com.testkart.exam.edu.helper.MknUtils;
import com.testkart.exam.edu.helper.SessionManager;

import java.util.ArrayList;

/**
 * Created by testkart on 23/5/17.
 */

public class TranxHistoryAdapter extends BaseAdapter {

    private ArrayList<Transactionhistory> dataSet;
    private LayoutInflater inflater;
    private SessionManager session;

    public TranxHistoryAdapter(Context context, ArrayList<Transactionhistory> dataSet){

        this.dataSet = dataSet;

        session = new SessionManager(context);

        this.inflater = LayoutInflater.from(context);
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

            convertView = inflater.inflate(R.layout.row_tranx, parent, false);

            mHolder = new ViewHolder();

            mHolder.circularTextView = (ImageView)convertView.findViewById(R.id.circularTextView);
            mHolder.blance = (TextView)convertView.findViewById(R.id.blance);
            mHolder.paymentMode = (TextView)convertView.findViewById(R.id.paymentMode);
            mHolder.date = (TextView)convertView.findViewById(R.id.date);
            mHolder.remark = (TextView)convertView.findViewById(R.id.remark);
            mHolder.up_down = (TextView)convertView.findViewById(R.id.up_down);

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();

        }


        Transactionhistory t = (Transactionhistory) getItem(position);

        if(t != null){

            if(t.getInAmount() != null){

                TextDrawable drawable4 = TextDrawable.builder()
                        .beginConfig()
                        .fontSize(28)
                        .textColor(Color.parseColor("#f5f5f5"))
                        .useFont(Typeface.DEFAULT)
                        .endConfig()
                        .buildRound((t.getInAmount() != null)? MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+t.getInAmount():MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+"0", Color.parseColor("#008744"));

                mHolder.circularTextView.setImageDrawable(drawable4);

                mHolder.up_down.setText("Credit");

            }else if(t.getOutAmount() != null){

                TextDrawable drawable4 = TextDrawable.builder()
                        .beginConfig()
                        .fontSize(28)
                        .textColor(Color.parseColor("#f5f5f5"))
                        .useFont(Typeface.DEFAULT)
                        .endConfig()
                        .buildRound((t.getOutAmount() != null)? MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+t.getOutAmount(): MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+"0", Color.parseColor("#d62d20"));

                mHolder.circularTextView.setImageDrawable(drawable4);

                mHolder.up_down.setText("Debit");

            }


            if(t.getType().equalsIgnoreCase("AD")){

                //Administrator
                mHolder.paymentMode.setText("Administrator");

            }else if(t.getType().equalsIgnoreCase("EM")){

                //Pay Exam
                mHolder.paymentMode.setText("Pay Exam");



            }else if(t.getType().equalsIgnoreCase("PG")){

                //Payment Gateway
                mHolder.paymentMode.setText("Payment Gateway");

            }

            String tranxDate = MknUtils.getFormatDate(t.getDate(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss");
            mHolder.date.setText(tranxDate);
            mHolder.blance.setText(MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+t.getBalance());
            mHolder.remark.setText(t.getRemarks());

        }

        return convertView;
    }


    class ViewHolder{

        private ImageView circularTextView ;
        private TextView up_down, paymentMode, date, remark, blance;
    }

}
