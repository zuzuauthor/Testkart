package com.testkart.exam.edu.exam.ibps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.testkart.exam.R;

import java.util.ArrayList;

/**
 * Created by testkart on 5/7/17.
 */

public class LanguageAdapter extends BaseAdapter{

    LayoutInflater inflater;
    ArrayList<String> dataSet;

    /*************  CustomAdapter Constructor *****************/
    public LanguageAdapter(Context context, ArrayList<String> dataSet) {

        this.dataSet = dataSet;
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
    public View getView(int position, View convertView, android.view.ViewGroup parent) {

        ViewGroup mHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.row_language, parent, false);
            mHolder = new ViewGroup();

            mHolder.langName = (TextView)convertView.findViewById(R.id.langName);

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewGroup) convertView.getTag();

        }


        String dl = (String) getItem(position);

        if(dl != null){

            mHolder.langName.setText(dl);
        }

        return convertView;
    }



    class ViewGroup{

        private TextView langName;

    }


}