package com.testkart.exam.banking_digest.my_magazines;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.testkart.exam.R;
import com.testkart.exam.banking_digest.my_magazines.model.Response;
import com.testkart.exam.edu.helper.MknUtils;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by elfemo on 27/2/18.
 */

public class SubscriptionListAdapter1 extends BaseAdapter {

    private int subType = 0; // 0 my sub, 1 expired sub
    private List<Response> dataSet;
    private LayoutInflater inflater;
    private Context context;


    public SubscriptionListAdapter1(Context context, List<Response> dataSet, int subType, LayoutInflater inflater) {

       // Log.v("size1112", dataSet.size()+"");

        this.inflater = LayoutInflater.from(context);//inflater;
        this.dataSet = dataSet;
        this.context = context;
        this.subType = subType;

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

            view = inflater.inflate(R.layout.row_subscription, viewGroup, false);
            mHolder = new ViewHolder();

            mHolder.adTxtAmount = (TextView) view.findViewById(R.id.adTxtAmount);
            mHolder.adTxtPackageName = (AutofitTextView) view.findViewById(R.id.adTxtPackageName);
            mHolder.adImgPackage = (ImageView) view.findViewById(R.id.adImgPackage);

            view.setTag(mHolder);


        }else{

            mHolder = (ViewHolder) view.getTag();

        }


        Response res = (Response) getItem(i);
        com.testkart.exam.banking_digest.my_magazines.model.Magazine mag = res.getMagazine();

        if(mag != null){

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(context)
                    .load("http://www.testkart.com/img/magazines/" + mag.getPhoto())
                    .apply(requestOptions)
                    .into(mHolder.adImgPackage);

            mHolder.adTxtPackageName.setText(mag.getTitle());
            mHolder.adTxtAmount.setText(mag.getFexpiryDate());

        }

        return view;
    }


    class ViewHolder{

        ImageView adImgPackage;
        TextView  adTxtAmount;
        AutofitTextView adTxtPackageName;

    }

}