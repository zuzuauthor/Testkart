package com.testkart.exam.edu.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.testkart.exam.R;

import java.util.List;

/**
 * Created by elfemo on 10/8/17.
 */

public class TopExamAdapter extends BaseAdapter {

    private List<TodaysExam> todaysExams;
    private LayoutInflater inflater;

    public TopExamAdapter(Context context, List<TodaysExam> todaysExams){

        this.todaysExams = todaysExams;

        this.inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return todaysExams.size();
    }

    @Override
    public Object getItem(int i) {
        return todaysExams.get(i);
    }

    @Override
    public long getItemId(int i) {
        return todaysExams.indexOf(todaysExams.get(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder mHolder;

        if(view ==  null){

            view = inflater.inflate(R.layout.item_top_exam, viewGroup, false);

            mHolder = new ViewHolder();

            mHolder.examName = (TextView)view.findViewById(R.id.examName);


            view.setTag(mHolder);


        }else{


            mHolder = (ViewHolder) view.getTag();

        }


        TodaysExam tE = (TodaysExam) getItem(i);

        if(tE != null){


            mHolder.examName.setText(tE.getExamName());
        }

        return view;
    }



    class ViewHolder{

        private TextView examName;

    }

}
