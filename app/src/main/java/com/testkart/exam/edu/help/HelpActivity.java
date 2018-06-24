package com.testkart.exam.edu.help;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.ExpandableHelpAdapter;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by testkart on 18/5/17.
 */

public class HelpActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private ExpandableListView viewHelp;
    private ProgressDialog progressDialog;
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);
        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("FAQ");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        //initialization
        initialization();

    }


    private void initialization() {

        sessionManager = new SessionManager(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Help");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        viewHelp = (ExpandableListView) findViewById(R.id.viewHelp);
        viewHelp.setIndicatorBounds(24, 24);

        getHelptData();
    }

    private void getHelptData() {

        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            progressDialog.show();

            makeAPiCall();

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<HelpResponse> call = apiService.getHelpData(sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC),
                sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<HelpResponse>() {
            @Override
            public void onResponse(Call<HelpResponse> call, retrofit2.Response<HelpResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if (response.body().getStatus()) {


                        if(!response.body().getResponse().isEmpty()){

                            //build adapter

                            HashMap<String, String> childData = new HashMap<String, String>();
                            ArrayList<String> headerData = new ArrayList<String>();

                            for (Response r:
                                    response.body().getResponse()) {

                                headerData.add(r.getHelp().getLinkTitle());
                                childData.put(r.getHelp().getLinkTitle(), r.getHelp().getLinkDesc());

                            }


                            ExpandableHelpAdapter helpAdapter = new ExpandableHelpAdapter(context, headerData, childData);

                            viewHelp.setAdapter(helpAdapter);

                            viewHelp.expandGroup(0);

                        }else{

                            //show dialog ... leader board is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Help");
                            builder.setMessage("No Help Found");
                            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    getHelptData();
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


                    } else {

                        Toast.makeText(HelpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(HelpActivity.this, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HelpResponse> call, Throwable t) {


                //hide progress dialog
                progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();
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
