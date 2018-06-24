package com.testkart.exam.packages;

import android.content.Context;
import android.graphics.Typeface;
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
import com.testkart.exam.banking_digest.buy.model.DataMagazines;
import com.testkart.exam.packages.model.DataOrderSummary;
import com.tuyenmonkey.textdecorator.TextDecorator;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;

/**
 * Created by testkart on 8/6/17.
 */

public class PackageGridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<DataOrderSummary> dataSet;

    public PackageGridAdapter(Context context, ArrayList<DataOrderSummary> dataSet){

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;

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

            convertView = inflater.inflate(R.layout.grid_package, parent, false);
            mHolder = new ViewHolder();

            mHolder.adImgPackage = (ImageView)convertView.findViewById(R.id.adImgPackage);
            mHolder.adTxtAmount = (TextView)convertView.findViewById(R.id.adTxtAmount);
            mHolder.adTxtPackageName = (AutofitTextView)convertView.findViewById(R.id.adTxtPackageName);
            mHolder.indicator = (ImageView)convertView.findViewById(R.id.indicator);
            mHolder.indicator.setVisibility(View.GONE);

            //set viewHolder
            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();
        }

        DataOrderSummary packItem = (DataOrderSummary) getItem(position);

        if(packItem != null){

            mHolder.adTxtPackageName.setText(packItem.getPackageName());

            if(packItem.isItemAddedToCart()){

                mHolder.indicator.setVisibility(View.VISIBLE);
            }else{

                mHolder.indicator.setVisibility(View.GONE);
            }

            if(cartItemList.contains(packItem.getPackageId())){

                mHolder.indicator.setVisibility(View.VISIBLE);
                packItem.setItemAddedToCart(true);

            }else{

                mHolder.indicator.setVisibility(View.GONE);
                packItem.setItemAddedToCart(false);
            }

            if((int)Float.parseFloat(packItem.getPackageSHowAmount()) == 0){

                TextDecorator
                        .decorate(mHolder.adTxtAmount, /*packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount()+" "+*/packItem.getPackageCurrencyCode()+packItem.getPackageAmount())

                        //.setTextColor(R.color.red, packItem.getPackageSHowAmount())
                        //.strikethrough(packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount())
                        .setTextStyle(Typeface.BOLD, packItem.getPackageCurrencyCode()+packItem.getPackageAmount())
                        .setTextColor(R.color.cart_buy_price, packItem.getPackageCurrencyCode()+packItem.getPackageAmount()).build();


            }else{

                TextDecorator
                        .decorate(mHolder.adTxtAmount, packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount()+" "+packItem.getPackageCurrencyCode()+packItem.getPackageAmount())
                        .setTextColor(R.color.cart_cut_price, packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount())
                        .strikethrough(packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount())
                        .setTextStyle(Typeface.BOLD, packItem.getPackageCurrencyCode()+packItem.getPackageAmount())
                        .setTextColor(R.color.cart_buy_price, packItem.getPackageCurrencyCode()+packItem.getPackageAmount()).build();

            }


            //load package image
            if(!packItem.getPackageImage().isEmpty()){

                Log.v("Image", "http://www.testkart.com/img/package/"+packItem.getPackageImage());

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                Glide.with(context)
                        .load("http://www.testkart.com/img/package/"+packItem.getPackageImage())
                        .apply(requestOptions)
                        .into(mHolder.adImgPackage);

            }

        }

        return convertView;
    }

    class ViewHolder{

        private ImageView adImgPackage, indicator;
        private AutofitTextView adTxtPackageName;
        private TextView adTxtAmount;

    }

    private ArrayList<String> cartItemList;
    public void notifyCartItems(ArrayList<String> cartItemList){

        this.cartItemList = cartItemList;

    }
}
