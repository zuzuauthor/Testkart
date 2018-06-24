package com.testkart.exam.banking_digest.my_magazines;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.testkart.exam.R;
import com.testkart.exam.banking_digest.my_magazines.model.my_subscription.Datum;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by elfemo on 12/2/18.
 */

public class SubscriptionListAdapter extends BaseAdapter {

    private int subType = 0; // 0 my sub, 1 expired sub
    private List<Datum> dataSet;
    private LayoutInflater inflater;
    private Context context;


    private SubscriptionActionListener mListener;

    public interface SubscriptionActionListener{

        void readMagazine(String magazineId);
        void viewAllMagazine();
    }


    public SubscriptionListAdapter(Context context, List<Datum> dataSet, int subType) {

        this.inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
        this.context = context;
        this.subType = subType;
        this.mListener = (SubscriptionActionListener)context;
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

            view = inflater.inflate(R.layout.row_subscription1, viewGroup, false);
            mHolder = new ViewHolder();

            mHolder.viewPackageName = (TextView) view.findViewById(R.id.viewPackageName);
            mHolder.btnViewAll = (TextView) view.findViewById(R.id.btnViewAll);
            mHolder.viewMagazineCount = (TextView) view.findViewById(R.id.viewMagazineCount);
            mHolder.viewMagazineCount.setVisibility(View.GONE);

            mHolder.mag1 = view.findViewById(R.id.mag1);
            mHolder.mag2 = view.findViewById(R.id.mag2);
            mHolder.mag3 = view.findViewById(R.id.mag3);
            mHolder.mag4 = view.findViewById(R.id.mag4);

            view.setTag(mHolder);


        }else{

            mHolder = (ViewHolder) view.getTag();

        }


        Datum data = (Datum) getItem(i);
        if(data != null){

            final List<com.testkart.exam.banking_digest.my_magazines.model.my_subscription.Magazine> magas = data.getMagazines();

            mHolder.viewPackageName.setText(data.getPackageName());

            mHolder.btnViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mListener != null){

                        mListener.viewAllMagazine();

                    }


                }
            });

            if(magas != null && !magas.isEmpty()){


                //add listeners
                mHolder.mag1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        loadMagazine(magas.get(0).getMagazineId());

                    }
                });



                mHolder.mag2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        loadMagazine(magas.get(1).getMagazineId());

                    }
                });


                mHolder.mag3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        loadMagazine(magas.get(2).getMagazineId());

                    }
                });


                mHolder.mag4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        loadMagazine(magas.get(3).getMagazineId());

                    }
                });


                RequestOptions requestOptions = new RequestOptions();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                int magCount = magas.size();
                if(magCount > 4){

                    mHolder.btnViewAll.setVisibility(View.VISIBLE);
                    mHolder.viewMagazineCount.setVisibility(View.VISIBLE);
                    mHolder.viewMagazineCount.setText((magCount - 4) +" more");

                    //mag1
                    Glide.with(context)
                            .load(magas.get(0).getMagazineImage())
                            .apply(requestOptions)
                            .into(mHolder.mag1);

                    //mag2
                    Glide.with(context)
                            .load(magas.get(1).getMagazineImage())
                            .apply(requestOptions)
                            .into(mHolder.mag2);


                    //mag3
                    Glide.with(context)
                            .load(magas.get(2).getMagazineImage())
                            .apply(requestOptions)
                            .into(mHolder.mag3);

                    //mag4
                    Glide.with(context)
                            .load(magas.get(3).getMagazineImage())
                            .apply(requestOptions)
                            .into(mHolder.mag4);

                }else{

                    mHolder.btnViewAll.setVisibility(View.GONE);
                    mHolder.viewMagazineCount.setVisibility(View.GONE);


                       if(magas.size() == 1){

                           mHolder.mag1.setVisibility(View.VISIBLE);
                           mHolder.mag2.setVisibility(View.GONE);
                           mHolder.mag3.setVisibility(View.GONE);
                           mHolder.mag4.setVisibility(View.GONE);

                           //mag1
                           Glide.with(context)
                                   .load(magas.get(0).getMagazineImage())
                                   .apply(requestOptions)
                                   .into(mHolder.mag1);
                       }else




                        if(magas.size() == 2) {

                            mHolder.mag1.setVisibility(View.VISIBLE);
                            mHolder.mag2.setVisibility(View.VISIBLE);
                            mHolder.mag3.setVisibility(View.GONE);
                            mHolder.mag4.setVisibility(View.GONE);

                            //mag1
                            Glide.with(context)
                                    .load(magas.get(0).getMagazineImage())
                                    .apply(requestOptions)
                                    .into(mHolder.mag1);

                            //mag2
                            Glide.with(context)
                                    .load(magas.get(1).getMagazineImage())
                                    .apply(requestOptions)
                                    .into(mHolder.mag2);

                        }else

                        if(magas.size() == 3) {

                            mHolder.mag1.setVisibility(View.VISIBLE);
                            mHolder.mag2.setVisibility(View.VISIBLE);
                            mHolder.mag3.setVisibility(View.VISIBLE);
                            mHolder.mag4.setVisibility(View.GONE);

                            //mag1
                            Glide.with(context)
                                    .load(magas.get(0).getMagazineImage())
                                    .apply(requestOptions)
                                    .into(mHolder.mag1);

                            //mag2
                            Glide.with(context)
                                    .load(magas.get(1).getMagazineImage())
                                    .apply(requestOptions)
                                    .into(mHolder.mag2);


                            //mag3
                            Glide.with(context)
                                    .load(magas.get(2).getMagazineImage())
                                    .apply(requestOptions)
                                    .into(mHolder.mag3);


                        }else

                        if(magas.size() == 4){

                            mHolder.mag1.setVisibility(View.VISIBLE);
                            mHolder.mag2.setVisibility(View.VISIBLE);
                            mHolder.mag3.setVisibility(View.VISIBLE);
                            mHolder.mag4.setVisibility(View.VISIBLE);

                            //mag1
                            Glide.with(context)
                                    .load(magas.get(0).getMagazineImage())
                                    .apply(requestOptions)
                                    .into(mHolder.mag1);

                            //mag2
                            Glide.with(context)
                                    .load(magas.get(1).getMagazineImage())
                                    .apply(requestOptions)
                                    .into(mHolder.mag2);


                            //mag3
                            Glide.with(context)
                                    .load(magas.get(2).getMagazineImage())
                                    .apply(requestOptions)
                                    .into(mHolder.mag3);

                            //mag4
                            Glide.with(context)
                                    .load(magas.get(3).getMagazineImage())
                                    .apply(requestOptions)
                                    .into(mHolder.mag4);


                    }

                }


                //show all magazines


            }


        }

        return view;
    }

    private void loadMagazine(String magazineId) {

        if(mListener != null){

            mListener.readMagazine(magazineId);

        }

    }


    class ViewHolder{

        ImageView mag1, mag2, mag3, mag4;
        TextView  viewPackageName, btnViewAll, viewMagazineCount;

        AutofitTextView adTxtPackageName;

    }

}
