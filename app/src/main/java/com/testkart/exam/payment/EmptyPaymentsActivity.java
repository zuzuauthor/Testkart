package com.testkart.exam.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.testkart.exam.R;

/**
 * Created by mukesh on 16/9/17.
 */

public class EmptyPaymentsActivity extends AppCompatActivity {

    private Button goBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_empty_payments);

        initViews();
    }

    private void initViews() {

        goBack = (Button)findViewById(R.id.goBack);
        goBackListener();
    }

    private void goBackListener() {

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
