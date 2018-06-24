package com.testkart.exam.testkart.notifications;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.testkart.exam.R;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

/**
 * Created by elfemo on 1/8/17.
 */

public class PushDialog extends DialogFragment {

    public static final String KEY_NOTIFICATION_DATA = "notification_data";

    private TextView pushTitle, pushTime, pushMessage;
    private ImageView notificationImage;

    private DilatingDotsProgressBar mDilatingDotsProgressBar;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.item_notification, null);

        initViews(rootView);

        builder.setView(rootView);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog dialog = builder.create();

        return dialog;
    }

    private void initViews(View rootView) {

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) rootView.findViewById(R.id.progress);
        mDilatingDotsProgressBar.setDotRadius(5f);
        mDilatingDotsProgressBar.setGrowthSpeed(650);
        mDilatingDotsProgressBar.setDotSpacing(30);
        mDilatingDotsProgressBar.showNow();

        pushTitle = (TextView) rootView.findViewById(R.id.notificationTitle);
        pushTime = (TextView) rootView.findViewById(R.id.notificationTime);
        pushMessage = (TextView) rootView.findViewById(R.id.notificationMessage);
        notificationImage = (ImageView) rootView.findViewById(R.id.notificationImage);

        DataNotification dataNotification = (DataNotification) getArguments().getSerializable(KEY_NOTIFICATION_DATA);

        if(dataNotification != null){

            pushTitle.setText(dataNotification.getTitle());
            pushTime.setText(dataNotification.getDateTime());
            pushMessage.setText(dataNotification.getMessage());




                if(dataNotification.isIncludeImage()){

                    Log.v("Push DIalog", dataNotification.getImageUrl());

                    mDilatingDotsProgressBar.setVisibility(View.VISIBLE);
                   notificationImage.setVisibility(View.VISIBLE);

                    //load profile image
                    Glide.with(getContext())
                            .load(dataNotification.getImageUrl())
                            // .placeholder(R.raw.default_profile)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    mDilatingDotsProgressBar.hideNow();
                                    notificationImage.setImageResource(R.drawable.no_image_available_150);

                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    mDilatingDotsProgressBar.hideNow();
                                    return false;
                                }
                            })
                            .into(notificationImage);

                }else{

                    Log.v("Push DIalog", "No image url");

                    mDilatingDotsProgressBar.setVisibility(View.GONE);

                    notificationImage.setVisibility(View.GONE);

                }

        }
    }
}
