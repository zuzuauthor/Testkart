package com.testkart.exam.edu.helper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.testkart.exam.R;
import com.testkart.exam.edu.models.Consts;

import id.arieridwan.lib.PageLoader;

/**
 * Created by testkart on 20/4/17.
 */

public class BlankActivity extends AppCompatActivity {

    public PageLoader pageLoader;
    public int AnimationMode;
    public int LoadMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        initView();
    }

    public void initView() {
        Intent i = getIntent();
        LoadMode = i.getIntExtra(Consts.LOAD_MODE,0);
        AnimationMode = i.getIntExtra(Consts.ANIMATION_MODE,0);
        pageLoader = (PageLoader) findViewById(R.id.pageloader);
        pageLoader.setOnRetry(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageLoader.startProgress();
            }
        });
        switch (LoadMode){
            case 1:
                pageLoader.startProgress();
                break;
            case 2:
                pageLoader.stopProgress();
                break;
            case 3:
                pageLoader.stopProgressAndFailed();
                break;
            default:
                pageLoader.startProgress();
                break;
        }
        switch (AnimationMode){
            case 1:
                pageLoader.setLoadingAnimationMode(pageLoader.ROTATE_MODE);
                break;
            case 2:
                pageLoader.setLoadingAnimationMode(pageLoader.FLIP_MODE);
                break;
            case 3:
                pageLoader.setLoadingAnimationMode(pageLoader.VIBRATE_MODE);
                break;
            case 4:
                pageLoader.setLoadingAnimationMode(pageLoader.SHAKE_MODE);
                break;
            case 5:
                pageLoader.setLoadingAnimationMode(pageLoader.BOUNCE_MODE);
                break;
            default:
                pageLoader.setLoadingAnimationMode(pageLoader.FLIP_MODE);
                break;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }
}