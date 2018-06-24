package com.testkart.exam.edu.myresult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.testkart.exam.R;
import com.testkart.exam.edu.helper.MknUtils;

import java.util.ArrayList;

/**
 * Created by testkart on 18/5/17.
 */

public class MyResultAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<ResultModel> dataSet;


    private OnMyresultListener mListener;
    public interface OnMyresultListener{

        void onViewResult(ResultModel result);
        void onPrintCertificate(ResultModel result);
        void onPrintResult(ResultModel resul);
    }

    public MyResultAdapter(Context context, ArrayList<ResultModel> dataSet){

        this.dataSet = dataSet;

        this.inflater = LayoutInflater.from(context);
        this.mListener = (OnMyresultListener)context;

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

            convertView = inflater.inflate(R.layout.row_myresult, parent, false);

            mHolder = new ViewHolder();

            mHolder.progress = (CircularProgressBar)convertView.findViewById(R.id.progress);
            mHolder.examName = (TextView)convertView.findViewById(R.id.examName);
            mHolder.date = (TextView)convertView.findViewById(R.id.date);
            mHolder.examResult = (TextView)convertView.findViewById(R.id.examResult);
            mHolder.score = (TextView)convertView.findViewById(R.id.score);
            mHolder.viewdetails = (TextView)convertView.findViewById(R.id.viewdetails);
            mHolder.print = (TextView)convertView.findViewById(R.id.print);
            mHolder.percentage = (TextView)convertView.findViewById(R.id.percentage);
            mHolder.certificate = (TextView)convertView.findViewById(R.id.certificate);

            convertView.setTag(mHolder);
        }else {

            mHolder = (ViewHolder) convertView.getTag();
        }

       final  ResultModel r = (ResultModel) getItem(position);

        if(r != null){

            mHolder.examName.setText(r.getExamName());

            String resultDate = MknUtils.getFormatDate(r.getDate(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss");

            mHolder.date.setText((r.getDate() != null)? /*r.getDate().split(" ")[0]*/ resultDate: "");
            mHolder.examResult.setText("Result: "+r.getExamResult());
            mHolder.score.setText("Marks: "+r.getScore());

            mHolder.percentage.setText((r.getPercentage() != null)? r.getPercentage()+"%" : "0"+"%");
            mHolder.progress.setProgress((r.getPercentage() != null)? Float.parseFloat(r.getPercentage()) : 0);

            if(r.isCertificate() && r.getExamResult().equalsIgnoreCase("Pass")){

                mHolder.certificate.setVisibility(View.GONE); //previous visible

            }else {

                mHolder.certificate.setVisibility(View.GONE);
            }

            if(r.getResultDeclear().equalsIgnoreCase("yes")){

                mHolder.print.setVisibility(View.VISIBLE);
                mHolder.viewdetails.setVisibility(View.VISIBLE);
            }else{

                mHolder.print.setVisibility(View.GONE);
                mHolder.viewdetails.setVisibility(View.GONE);
            }


            //click listeners
            mHolder.certificate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mListener != null){

                        mListener.onPrintCertificate(r);
                    }
                }
            });


            mHolder.viewdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mListener != null){

                        mListener.onViewResult(r);
                    }
                }
            });

            mHolder.print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mListener != null){

                        mListener.onPrintResult(r);
                    }
                }
            });


            //change when needed
            mHolder.print.setVisibility(View.GONE);

        }

        return convertView;
    }


    class ViewHolder{

        private CircularProgressBar progress;
        private TextView examName, date, examResult, score, viewdetails, print, percentage, certificate;
    }

}
