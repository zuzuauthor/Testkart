package com.testkart.exam.testkart.my_result.subject_report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.testkart.exam.R;

import java.util.List;

/**
 * Created by elfemo on 18/8/17.
 */

public class SubjectReportAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<SubjectStat> dataSet;

    public SubjectReportAdapter(Context context, List<SubjectStat> dataSet){

        inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int i) {
        return dataSet.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dataSet.indexOf(dataSet.get(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder mHolder;

        if(view == null){

            view = inflater.inflate(R.layout.item_subject_report, viewGroup, false);
            mHolder = new ViewHolder();

            mHolder.subjectName = (TextView)view.findViewById(R.id.subjectName);
            mHolder.totalQuestions = (TextView)view.findViewById(R.id.totalQuestions);
            mHolder.correctIncorrectQuestions = (TextView)view.findViewById(R.id.correctIncorrectQuestions);
            mHolder.scoreNegative = (TextView)view.findViewById(R.id.scoreNegative);
            mHolder.unatempMarks = (TextView)view.findViewById(R.id.unatempMarks);

            view.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) view.getTag();
        }


        SubjectStat ss = (SubjectStat) getItem(i);


        if(ss != null){

            mHolder.subjectName.setText(ss.getSubjectname());
            mHolder.totalQuestions.setText(ss.getTotalQuestions()+"");
            mHolder.correctIncorrectQuestions.setText(ss.getCorrectIncorrectCount());
            mHolder.scoreNegative.setText(ss.getPositiveNegativeMarks());
            mHolder.unatempMarks.setText(ss.getUnattemptQuestionCountMarks());

        }

        return view;
    }


    class ViewHolder{

        private TextView subjectName, totalQuestions,  correctIncorrectQuestions, scoreNegative, unatempMarks ;
    }

}
