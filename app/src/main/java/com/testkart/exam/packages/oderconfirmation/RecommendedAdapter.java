package com.testkart.exam.packages.oderconfirmation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.testkart.exam.R;
import com.testkart.exam.packages.PackageDetailActivity;
import com.testkart.exam.packages.model.DataOrderSummary;
import com.tuyenmonkey.textdecorator.TextDecorator;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;

/**
 * Created by elfemo on 14/9/17.
 */

public class RecommendedAdapter  extends RecyclerView.Adapter<RecommendedAdapter.ViewHolder>{

    private Context context;
    private ArrayList<DataOrderSummary> dataSet;

    public RecommendedAdapter(Context context, ArrayList<DataOrderSummary> dataSet) {
        super();
        this.context = context;
        this.dataSet = dataSet;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recommended_grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.tvSpecies.setText(dataSet.get(i).getPackageName());

        if((int)Float.parseFloat(dataSet.get(i).getPackageSHowAmount()) == 0){

            TextDecorator
                    .decorate(viewHolder.tvPrice, /*packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount()+" "+*/dataSet.get(i).getPackageCurrencyCode()+dataSet.get(i).getPackageAmount())

                    //.setTextColor(R.color.red, packItem.getPackageSHowAmount())
                    //.strikethrough(packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount())
                    .setTextStyle(Typeface.BOLD, dataSet.get(i).getPackageCurrencyCode()+dataSet.get(i).getPackageAmount())
                    .setTextColor(R.color.cart_buy_price, dataSet.get(i).getPackageCurrencyCode()+dataSet.get(i).getPackageAmount()).build();


        }else{

            TextDecorator
                    .decorate(viewHolder.tvPrice, dataSet.get(i).getPackageCurrencyCode()+dataSet.get(i).getPackageSHowAmount()+" "+dataSet.get(i).getPackageCurrencyCode()+dataSet.get(i).getPackageAmount())
                    .setTextColor(R.color.cart_cut_price, dataSet.get(i).getPackageCurrencyCode()+dataSet.get(i).getPackageSHowAmount())
                    .strikethrough(dataSet.get(i).getPackageCurrencyCode()+dataSet.get(i).getPackageSHowAmount())
                    .setTextStyle(Typeface.BOLD, dataSet.get(i).getPackageCurrencyCode()+dataSet.get(i).getPackageAmount())
                    .setTextColor(R.color.cart_buy_price, dataSet.get(i).getPackageCurrencyCode()+dataSet.get(i).getPackageAmount()).build();

        }


        //load package image
        if(!dataSet.get(i).getPackageImage().isEmpty()){

            Log.v("Image", "http://www.testkart.com/img/package_thumb/"+dataSet.get(i).getPackageImage());


            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(context)
                    .load("http://www.testkart.com/img/package_thumb/"+dataSet.get(i).getPackageImage())
                    .apply(requestOptions)
                    .into( viewHolder.imgThumbnail);

        }


        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onRecommendedClick(View view, int position, boolean isLongClick) {

                if (isLongClick) {

                    //Toast.makeText(context, "#" + position + " - " + dataSet.get(position).getPackageName() + " (Long click)", Toast.LENGTH_SHORT).show();
                    //context.startActivity(new Intent(context, MainActivity.class));
                } else {

                    DataOrderSummary ci = (DataOrderSummary) dataSet.get(i);

                    // if(!ci.isItemAddedToCart()){

                    //progressDialog.show();

                    Intent packageDetailActivity = new Intent(context, PackageDetailActivity.class);
                    packageDetailActivity.putExtra(PackageDetailActivity.KEY_PACKAGE_DETAILS, ci);

                    // progressDialog.dismiss();

                    context.startActivity(packageDetailActivity);

                    ((OrderConfirmationActivity)context).finish();


                   // Toast.makeText(context, "#" + position + " - " + dataSet.get(position).getPackageName(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public AutofitTextView tvSpecies;
        public TextView tvPrice;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.adImgPackage);
            tvSpecies = (AutofitTextView) itemView.findViewById(R.id.adTxtPackageName);
            tvPrice = (TextView)itemView.findViewById(R.id.adTxtAmount);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onRecommendedClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onRecommendedClick(view, getPosition(), true);
            return true;
        }
    }
}
