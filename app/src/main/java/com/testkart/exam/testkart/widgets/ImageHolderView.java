package com.testkart.exam.testkart.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.freegeek.android.materialbanner.holder.Holder;
import com.testkart.exam.R;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

/**
 * Created by testkart on 21/7/17.
 */

public class ImageHolderView implements Holder<BannerData> {
    private ImageView imageView;
    private TextView title;
    //private ProgressBar progressBar;
    private DilatingDotsProgressBar progressBar;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner,null);
        imageView = view.findViewById(R.id.imageView);
        title = view.findViewById(R.id.txt_title);
        progressBar = view.findViewById(R.id.progress);
        progressBar.showNow();
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerData data) {
        title.setText(data.getTitle());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(context).load(data.getUrl())
                .thumbnail(0.5f)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.hideNow();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.hideNow();
                        return false;
                    }
                })
                .into(imageView);

    }

}