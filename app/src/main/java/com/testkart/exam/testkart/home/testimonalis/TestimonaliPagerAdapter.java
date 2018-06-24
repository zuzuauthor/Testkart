package com.testkart.exam.testkart.home.testimonalis;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.testkart.exam.R;
import com.testkart.exam.testkart.home.Testimonial;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by elfemo on 24/8/17.
 */

public class TestimonaliPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<Testimonial> testimonials;

    public TestimonaliPagerAdapter(Context context, List<Testimonial> testimonials) {

        this.testimonials = testimonials;

        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return testimonials.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        CircleImageView imageView = (CircleImageView) itemView.findViewById(R.id.viewImage);
        TextView clientName = (TextView) itemView.findViewById(R.id.clientName);
        TextView clientComments = (TextView) itemView.findViewById(R.id.clientComments);
        RatingBar ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

        Testimonial t = this.testimonials.get(position);

        Glide.with(mContext)
                .load(t.getImage())
                // .placeholder(R.raw.default_profile)
                .into(imageView);

        clientName.setText(t.getName());
        clientComments.setText(t.getComments());
        ratingBar.setRating(t.getRating());


        ((ViewPager) container).addView(itemView);

        //container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       // container.removeView((LinearLayout) object);

        ((ViewPager) container).removeView((LinearLayout) object);
    }
}