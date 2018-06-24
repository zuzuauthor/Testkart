package com.testkart.exam.edu.exam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.testkart.exam.edu.exam.ibps.datamodel.DataSubject;

import java.util.ArrayList;

/**
 * Created by elfemo on 30/8/17.
 */

public class SubjectAdaapter extends BaseAdapter {

    private LayoutInflater inflater;

    private ArrayList<DataSubject> dataSet;

    public SubjectAdaapter(Context context, ArrayList<DataSubject> dataSet){

        this.inflater = LayoutInflater.from(context);
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

        if(view == null){

            view = inflater.inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);

        }

        TextView subjectName = (TextView)view.findViewById(android.R.id.text1);

        DataSubject ds = (DataSubject) getItem(i);

        if(ds != null){

            subjectName.setText(ds.getSubjectName());

        }

        return view;
    }


}
