package com.testkart.exam.edu.exam;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.testkart.exam.R;

import java.util.ArrayList;

/**
 * Created by testkart on 5/5/17.
 */

public class MultipleChoiceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> dataSet;
    private boolean multipleType;
    private LayoutInflater inflater;

    public MultipleChoiceAdapter(Context context, ArrayList<String> dataSet, boolean multipleType){

        this.context = context;
        this.dataSet = dataSet;
        this.multipleType = multipleType;
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

        if(convertView == null){

            convertView = inflater.inflate(R.layout.row_multiple_choice, parent, false);

        }

        //init
        ImageView circularTextView = (ImageView)convertView.findViewById(R.id.circularTextView);
        WebView viewOption = (WebView)convertView.findViewById(R.id.viewOption);
        WebSettings webSettings = viewOption.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String option = (String) getItem(position);

        TextDrawable drawable0 = TextDrawable.builder()
                .beginConfig()
                .fontSize(18)
                .endConfig()
                .buildRound((position+1)+"", Color.parseColor("#999999"));
        circularTextView.setImageDrawable(drawable0);


        viewOption.loadData(option, "text/html", "UTF-8");

        return convertView;
    }
}
