package com.testkart.exam.packages;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.testkart.exam.R;
import com.testkart.exam.packages.model.DataOrderSummary;
import com.tuyenmonkey.textdecorator.TextDecorator;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;


/**
 * Created by testkart on 8/6/17.
 */

public class PackageDetailDialog extends DialogFragment {

    public static final String KEY_PACKAGE_DETAIL_DIALOG_NAME = "PackageDetailDialog";
    public static final String KEY_PACKAGE_DETAILS = "package_details";

    private ImageButton dgBtnClose;
    private ImageView dgImgPackage;
    private TextView dgTxtDescription, dgTxtPackageName, dgTxtAmount, dgTxtPackageExpiry, dgTxtExams;

    private DataOrderSummary orderItem;

    private DilatingDotsProgressBar mDilatingDotsProgressBar;

    private OnPackageActionListener mListener;
    public interface OnPackageActionListener{

        void onItemAddedToCart(DataOrderSummary item);
        void onItemBuyNow(DataOrderSummary item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (OnPackageActionListener)context;

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_packagedetails, null);

        initViews(rootView);

        builder.setView(rootView);

        builder.setPositiveButton("Add to cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if(mListener != null){

                    mListener.onItemAddedToCart(orderItem);

                }

            }
        });

        builder.setNegativeButton("Buy Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(mListener != null){

                    mListener.onItemBuyNow(orderItem);

                }

            }
        });

        AlertDialog dialog = builder.create();

        return dialog;
    }

    private void initViews(View view) {

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) view.findViewById(R.id.progress);
        mDilatingDotsProgressBar.setDotRadius(5f);
        mDilatingDotsProgressBar.setGrowthSpeed(650);
        mDilatingDotsProgressBar.setDotSpacing(30);

        // initialize views and set defaults
        dgBtnClose = (ImageButton)view.findViewById(R.id.dgBtnClose);
        dgImgPackage = (ImageView)view.findViewById(R.id.dgImgPackage);
        dgTxtDescription = (TextView) view.findViewById(R.id.dgTxtDescription);
        dgTxtPackageName  = (TextView) view.findViewById(R.id.dgTxtPackageName);
        dgTxtAmount  = (TextView) view.findViewById(R.id.dgTxtAmount);
        dgTxtPackageExpiry  = (TextView) view.findViewById(R.id.dgTxtPackageExpiry);
        dgTxtExams  = (TextView) view.findViewById(R.id.dgTxtExams);

        dgBtnCloseListener();

        updateViews();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void updateViews() {

        // show progress bar and start animating
        mDilatingDotsProgressBar.showNow();

        orderItem = (DataOrderSummary) getArguments().getSerializable(KEY_PACKAGE_DETAILS);

        if(orderItem != null){

            //load package image
            if(!orderItem.getPackageImage().isEmpty()){

                Glide.with(getContext())
                        /*.load(orderItem.getPackageImage())*/
                        .load("http://www.testkart.com/img/package_thumb/"+orderItem.getPackageImage())

                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                mDilatingDotsProgressBar.hideNow();
                                dgImgPackage.setImageResource(R.drawable.no_image_available_150);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                mDilatingDotsProgressBar.hideNow();
                                return false;
                            }
                        })
                        .apply(new RequestOptions().override(100, 100).fitCenter())
                        .into(dgImgPackage);

            }

            dgTxtPackageName.setText(orderItem.getPackageName());

            if((int)Float.parseFloat(orderItem.getPackageSHowAmount()) == 0){

                TextDecorator
                        .decorate(dgTxtAmount, /*packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount()+" "+*/orderItem.getPackageCurrencyCode()+orderItem.getPackageAmount())
                        //.setTextColor(R.color.red, packItem.getPackageSHowAmount())
                        //.strikethrough(packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount())
                        .setTextColor(R.color.positive_green, orderItem.getPackageCurrencyCode()+orderItem.getPackageAmount()).build();


            }else{

                TextDecorator
                        .decorate(dgTxtAmount, orderItem.getPackageCurrencyCode()+orderItem.getPackageSHowAmount()+" "+orderItem.getPackageCurrencyCode()+orderItem.getPackageAmount())
                        .setTextColor(R.color.red, orderItem.getPackageSHowAmount())
                        .strikethrough(orderItem.getPackageCurrencyCode()+orderItem.getPackageSHowAmount())
                        .setTextColor(R.color.positive_green, orderItem.getPackageCurrencyCode()+orderItem.getPackageAmount()).build();

            }

           // dgTxtAmount.setText(orderItem.getPackageAmount());
            dgTxtPackageExpiry.setText(orderItem.getPackageExpiryDays());
            dgTxtExams.setText(orderItem.getPackageExamName());
            dgTxtDescription.setText(Html.fromHtml(orderItem.getPackageDescription()));
        }
    }

    private void dgBtnCloseListener() {
        dgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }

}
