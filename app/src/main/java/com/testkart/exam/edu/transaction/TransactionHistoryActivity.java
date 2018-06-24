package com.testkart.exam.edu.transaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by testkart on 23/5/17.
 */

public class TransactionHistoryActivity extends AppCompatActivity {

    public static final String KEY_WALLET = "wallet";

    private ListView viewMyResult;
    private ProgressDialog progressDialog;
    private Context context = this;
    private SessionManager session;

    private TextView blance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myresult);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Transaction History");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        //initialization
        initialization();
    }

    private void initialization() {

        String amount = getIntent().getStringExtra(KEY_WALLET);

        session = new SessionManager(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Transaction History");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        blance = (TextView)findViewById(R.id.blance);
        //blance.setText("Balance: ₹"+amount);

        viewMyResult = (ListView) findViewById(R.id.viewMyResult);

        getTransactionData();
    }

    private void getTransactionData() {

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

        Call<TransationResponse> call = apiService.getTransactionData(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<TransationResponse>() {
            @Override
            public void onResponse(Call<TransationResponse> call, retrofit2.Response<TransationResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if (response.body().getStatus()) {

                        //build adapter

                        if(!response.body().getResponse().isEmpty()){

                            ArrayList<Transactionhistory> dataSet = new ArrayList<>();

                            for (Response res
                                    :
                                    response.body().getResponse()) {

                                dataSet.add(res.getTransactionhistory());
                            }


                            TranxHistoryAdapter adapter = new TranxHistoryAdapter(context, dataSet);

                            viewMyResult.setAdapter(adapter);

                        }else{


                            //show dialog ... leader board is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Transaction History");
                            builder.setMessage("Transaction History");
                            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    getTransactionData();
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

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransationResponse> call, Throwable t) {

                //hide progress dialog
                progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
