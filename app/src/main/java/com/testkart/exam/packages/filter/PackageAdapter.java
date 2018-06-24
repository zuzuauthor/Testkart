package com.testkart.exam.packages.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.testkart.exam.R;
import com.testkart.exam.packages.model.PackageDataModel;

import java.util.ArrayList;

/**
 * Created by elfemo on 8/9/17.
 */

public class PackageAdapter extends BaseAdapter {

    private ArrayList<PackageDataModel> dataSet;
    private LayoutInflater inflater;


    public PackageAdapter(Context context, ArrayList<PackageDataModel> dataSet){

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

        final PackageDataModel pdm = (PackageDataModel) getItem(i);

        if(pdm != null){

            packView.setText(pdm.getPackageName());

            packView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    pdm.setPackageChecked(b);
                }
            });
        }


        return view;
    }
}
