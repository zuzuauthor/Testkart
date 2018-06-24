package com.testkart.exam.edu.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.testkart.exam.R;
import com.testkart.exam.edu.login.model.TestkartFeature;

import java.util.ArrayList;

/**
 * Created by elfemo on 24/8/17.
 */

public class TestkartFeatureAdapter extends BaseAdapter {

    private ArrayList<TestkartFeature> dataSet;
    private LayoutInflater inflater;

    public TestkartFeatureAdapter(Context context, ArrayList<TestkartFeature> dataSet){

        this.dataSet = dataSet;
        this.inflater = LayoutInflater.from(context);
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

            view = inflater.inflate(R.layout.layout_testkart_board, viewGroup, false);

            mHolder = new ViewHolder();

            mHolder.viewFeature = (TextView)view.findViewById(R.id.viewFeature);
            mHolder.featureIcons = (ImageView)view.findViewById(R.id.featureIcons);

            view.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) view.getTag();

        }

        TestkartFeature tf = (TestkartFeature) getItem(i);

        if(tf != null){

            mHolder.viewFeature.setText(tf.getFeature());
            mHolder.featureIcons.setImageResource(tf.getImageResource());
        }

        return view;
    }

    class ViewHolder{

        TextView viewFeature;
        ImageView featureIcons;
    }
}
