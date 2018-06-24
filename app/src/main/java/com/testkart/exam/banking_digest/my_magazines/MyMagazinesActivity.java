package com.testkart.exam.banking_digest.my_magazines;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.myresult.MyResultActivity1;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.helper.BaseActivity;

import java.io.File;

/**
 * Created by elfemo on 12/2/18.
 */

public class MyMagazinesActivity extends BaseActivity {

    public static String FILE_PATH = "";

    private Context context = this;
    private TabLayout tabLayout;

    private String resultId = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_magazines);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        checkInternet();

    }

    public void checkInternet(){

        if (ApiClient.isNetworkAvailable(context)) {

            //initialization
            initialization();

        } else {

            //show dialog ... leader board is empty
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Network");
            builder.setMessage("Please check your internet connection and try again");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    checkInternet();
                }
            });

            builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }
            });

            AlertDialog d = builder.create();
            d.setCancelable(false);
            d.show();

        }

    }

    private void initialization() {

        resultId = getIntent().getStringExtra(MyResultActivity1.KEY_RESULT_ID);

        createDirectory();
        setupToolbar();
        setupTablayout();

    }

    //create directory to save pdf files
    private void createDirectory() {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Testkart");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");

                FILE_PATH = mediaStorageDir.getAbsolutePath();
                Log.v("File Absolute", FILE_PATH);

                FILE_PATH = mediaStorageDir.getPath();
                Log.v("File Path", FILE_PATH);
            }
        }else{

            FILE_PATH = mediaStorageDir.getAbsolutePath();
            //Log.v("File Absolute", FILE_PATH);

            //FILE_PATH = mediaStorageDir.getPath();
            //Log.v("File Path", FILE_PATH);
        }

    }


    private void setupToolbar() {
        // TODO Auto-generated method stub
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);}

        toolbar.setTitle("My Magazines");
    }



    private void setupTablayout() {

        // TODO Auto-generated method stub
        tabLayout = findViewById(R.id.tabs);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("My Subscriptions"));
        tabLayout.addTab(tabLayout.newTab().setText("Expired Subscriptions"));

        final ViewPager viewPager = findViewById(R.id.viewpager);
        final SubscriptionsPagerAdapter adapter = new SubscriptionsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), resultId);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
