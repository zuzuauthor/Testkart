package com.testkart.exam.testkart.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.testkart.exam.R;

import java.util.ArrayList;

/**
 * Created by elfemo on 27/7/17.
 */

public class NotificationAdapter extends BaseAdapter {

    private ArrayList<DataNotification> dataSet;
    private LayoutInflater inflater;
    private Context context;

    public NotificationAdapter(Context context, ArrayList<DataNotification> dataSet){

        this.dataSet = dataSet;
        this.inflater = LayoutInflater.from(context);
        this.context = context;

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

            convertView = inflater.inflate(R.layout.item_notification, parent, false);
            mHolder = new ViewHolder();

            mHolder.notificationMessage = (TextView)convertView.findViewById(R.id.notificationMessage);
            mHolder.notificationTime = (TextView)convertView.findViewById(R.id.notificationTime);
            mHolder.notificationTitle = (TextView)convertView.findViewById(R.id.notificationTitle);
            mHolder.notificationImage = (ImageView) convertView.findViewById(R.id.notificationImage);

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();

        }

        DataNotification notificationData = (DataNotification) getItem(position);

        if(notificationData != null){

            if(notificationData.isIncludeImage()){

                mHolder.notificationImage.setVisibility(View.VISIBLE);

                //load profile image
                Glide.with(context)
                        .load(notificationData.getImageUrl())
                        // .placeholder(R.raw.default_profile)
                        .into(mHolder.notificationImage);

            }else{

                mHolder.notificationImage.setVisibility(View.GONE);

            }

            mHolder.notificationTitle.setText(notificationData.getTitle());
            mHolder.notificationTime.setText(notificationData.getDateTime());
            mHolder.notificationMessage.setText(notificationData.getMessage());

        }

        return convertView;
    }


    class ViewHolder{

        private ImageView notificationImage;
        private TextView notificationMessage, notificationTime, notificationTitle;

    }

}
