package com.testkart.exam.packages;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.testkart.exam.R;
import com.testkart.exam.packages.model.DataOrderSummary;
import com.tuyenmonkey.textdecorator.TextDecorator;

import java.util.ArrayList;

/**
 * Created by testkart on 12/6/17.
 */

public class CartListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<DataOrderSummary> dataSet;
    private Context context;
    private CartItemListener mListener;
    private boolean showDeleteIcon;

    public interface CartItemListener{

        void onItemRemovedFromCart(DataOrderSummary removedItem);
    }

    public CartListAdapter(Context context, ArrayList<DataOrderSummary> dataSet, boolean showDeleteIcon){

        this.inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
        this.mListener = (CartItemListener)context;
        this.context = context;

        this.showDeleteIcon = showDeleteIcon;
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

            convertView = inflater.inflate(R.layout.row_cart_item, parent, false);

            mHolder = new ViewHolder();

            mHolder.packageImage = convertView.findViewById(R.id.packageImage);
            mHolder.removeItem  = convertView.findViewById(R.id.removeItem);
            mHolder.packageName = convertView.findViewById(R.id.packageName);
            mHolder.packageAmount = convertView.findViewById(R.id.packageAmount);
            mHolder.discountPrice = convertView.findViewById(R.id.discountPrice);
            mHolder.percentageOff = convertView.findViewById(R.id.percentageOff);

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();

        }

        final DataOrderSummary item = (DataOrderSummary) getItem(position);

        if(item != null){

            if(showDeleteIcon){

                mHolder.removeItem.setVisibility(View.VISIBLE);
            }else{

                mHolder.removeItem.setVisibility(View.GONE);

            }

            mHolder.removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showConfirmationDialog(item);

                }
            });


            if(!item.getPackageImage().isEmpty()){

                Glide.with(context)
                        .load("http://www.testkart.com/img/package_thumb/"+item.getPackageImage())
                        // .placeholder(R.raw.default_profile)
                        .into(mHolder.packageImage);

            }

            mHolder.packageName.setText(item.getPackageName());

            if((int)Float.parseFloat(item.getPackageSHowAmount()) == 0){

                mHolder.percentageOff.setVisibility(View.GONE);


                mHolder.packageAmount.setText(item.getPackageCurrencyCode()+item.getPackageAmount());

                mHolder.discountPrice.setVisibility(View.GONE
                );
                /*
                TextDecorator
                        .decorate(mHolder.packageAmount, *//*packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount()+" "+*//*item.getPackageCurrencyCode()+item.getPackageAmount())
                        //.setTextColor(R.color.red, packItem.getPackageSHowAmount())
                        //.strikethrough(packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount())
                        .setTextColor(R.color.positive_green, item.getPackageCurrencyCode()+item.getPackageAmount()).build();
*/

            }else{

                mHolder.percentageOff.setVisibility(View.GONE);

                mHolder.packageAmount.setText(item.getPackageCurrencyCode()+item.getPackageAmount());
                mHolder.discountPrice.setVisibility(View.VISIBLE);

                TextDecorator
                        .decorate(mHolder.discountPrice, item.getPackageCurrencyCode()+item.getPackageSHowAmount())
                        .strikethrough(item.getPackageCurrencyCode()+item.getPackageSHowAmount()).build();

            }

        }


        return convertView;
    }

    private void showConfirmationDialog(final DataOrderSummary item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure?");
        builder.setMessage("Do you want to remove package "+item.getPackageName()+" from your cart.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(mListener != null){

                    mListener.onItemRemovedFromCart(item);

                }

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing just dismiss

            }
        });


        builder.create().show();

    }


    class ViewHolder{

        private ImageView packageImage, removeItem;
        private TextView packageName, packageAmount, discountPrice, percentageOff;
    }

}
