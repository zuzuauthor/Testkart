package com.testkart.exam.helper;

//import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by testkart on 7/6/17.
 */

public class BaseActivity extends AppCompatActivity {

    public Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
