package com.testkart.exam.packages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.testkart.exam.R;
import com.testkart.exam.banking_digest.buy.MagazinesListActivity;
import com.testkart.exam.edu.TestKartApp;

/**
 * Created by elfemo on 25/8/17.
 */

public class EmptyCartActivity extends AppCompatActivity {

    private Context context = this;
    private Button startShoppingButton, startShopMagazineButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_empty_cart);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Cart");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        initViews();

    }

    private void initViews() {

        startShoppingButton = (Button)findViewById(R.id.startShoppingButton);
        startShoppingButtonListener();

        startShopMagazineButton = (Button)findViewById(R.id.startShopMagazineButton);
        startShopMagazineButtonListener();

    }

    private void startShopMagazineButtonListener() {

        startShopMagazineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shoppingListActivity = new Intent(context, MagazinesListActivity.class);
                startActivity(shoppingListActivity);
                finish();
            }
        });

    }

    private void startShoppingButtonListener() {

        startShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shoppingListActivity = new Intent(context, PackageListActivity.class);
                startActivity(shoppingListActivity);
                finish();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
