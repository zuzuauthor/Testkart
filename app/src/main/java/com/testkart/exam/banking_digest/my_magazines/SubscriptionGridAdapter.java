package com.testkart.exam.banking_digest.my_magazines;

import android.content.Context;
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
import com.testkart.exam.banking_digest.my_magazines.model.Magazine;
import com.testkart.exam.banking_digest.my_magazines.model.Response;
import com.testkart.exam.edu.helper.MknUtils;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by elfemo on 22/2/18.
 */

public class SubscriptionGridAdapter extends BaseAdapter {

    private int subType = 0; // 0 my sub, 1 expired sub
    private List<Response> dataSet;
    private LayoutInflater inflater;
    private Context context;


    public SubscriptionGridAdapter(Context context, List<Response> dataSet, int subType) {

        this.inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
        this.subType = subType;

        this.context = context;
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

        if (view == null) {

            view = inflater.inflate(R.layout.row_subscription, viewGroup, false);
            mHolder = new ViewHolder();

            mHolder.adImgPackage = (ImageView) view.findViewById(R.id.adImgPackage);
            mHolder.adTxtPackageName = (AutofitTextView) view.findViewById(R.id.adTxtPackageName);
            mHolder.adTxtAmount = (TextView) view.findViewById(R.id.adTxtAmount);


            view.setTag(mHolder);


        } else {

            mHolder = (ViewHolder) view.getTag();

        }


        Response res = (Response) getItem(i);

        Magazine magazine = res.getMagazine();

        if (magazine != null) {

            //check for expiry

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(context)
                    .load("http://www.testkart.com/img/magazines/" + magazine.getPhoto())
                    .apply(requestOptions)
                    .into(mHolder.adImgPackage);

            String fExpiry = "";

            if (magazine.getFexpiryDate() != null) {

                fExpiry = MknUtils.getFormatDate(magazine.getFexpiryDate(), "yyy-MM-dd", "dd-MM-yyyy");


            } else {

                fExpiry = "Unlimited";
            }

            mHolder.adTxtAmount.setText(fExpiry);

            mHolder.adTxtPackageName.setText(magazine.getTitle());
        }

        return view;
    }


    class ViewHolder {

        ImageView adImgPackage;
        TextView adTxtAmount;

        AutofitTextView adTxtPackageName;

    }

}