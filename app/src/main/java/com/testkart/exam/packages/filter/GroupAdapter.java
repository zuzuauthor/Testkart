package com.testkart.exam.packages.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.testkart.exam.R;
import com.testkart.exam.edu.register.GroupModel;

import java.util.ArrayList;

/**
 * Created by elfemo on 8/9/17.
 */

public class GroupAdapter extends BaseAdapter {

    private ArrayList<GroupModel> dataSet;
    private LayoutInflater inflater;


    public GroupAdapter(Context context, ArrayList<GroupModel> dataSet){

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

        if(view == null){

            view = inflater.inflate(R.layout.row_filter_package, viewGroup, false);

        }

        CheckBox packView = view.findViewById(R.id.pack1);

        final GroupModel pdm = (GroupModel) getItem(i);

        if(pdm != null){

            packView.setText(pdm.getGroupName());

            packView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        pdm.setGroupChecked(true);

                }
            });

        }


        return view;
    }
}
