package com.testkart.exam.payment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.banking_digest.my_magazines.MyMagazinesActivity;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.exam.examlist.ExamListActivity1;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.payment.model.PaymentResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by testkart on 21/6/17.
 */

public class PaymentHistoryActivity extends AppCompatActivity implements PurchaseHistoryAdapter.PaymentDetailListener {

    private ListView viewMyResult;
    private ProgressDialog progressDialog;
    private Context context = this;
    private SessionManager session;

    private TextView blance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myresult);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Payment History");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        //initialization
        initialization();
    }

    private void initialization() {

        session = new SessionManager(this);

        progressDialog = new ProgressDialog(this);
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

        Call<PaymentResponse> call = apiService.getPaymentResponse(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, retrofit2.Response<PaymentResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if (response.body().getStatus()) {

                        //build adapter

                        if(!response.body().getResponse().isEmpty()){

                            ArrayList<DataPaymentDetails> paymentList = buildData(response.body().getResponse());
                            PurchaseHistoryAdapter adapter = new PurchaseHistoryAdapter(context, paymentList);

                            viewMyResult.setAdapter(adapter);

                        }else{

                            Intent intentEmptyPayment = new Intent(context, EmptyPaymentsActivity.class);
                            startActivity(intentEmptyPayment);
                            finish();

                            //show dialog ... leader board is empty
                            /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Payment History");
                            builder.setMessage("There is not payment history at this moment,");
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
                            d.show();*/

                        }


                    } else {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {

                //hide progress dialog
                progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });
    }

    private ArrayList<DataPaymentDetails> buildData(List<com.testkart.exam.payment.model.Response> response) {

        if(!response.isEmpty()){

            ArrayList<DataPaymentDetails> paymentData = new ArrayList<>();

            for (com.testkart.exam.payment.model.Response r:
                 response) {

                DataPaymentDetails dpd = new DataPaymentDetails();
                dpd.setPayment(r.getPayment());
                dpd.setPackageList(r.getPackage());

                paymentData.add(dpd);
            }

            return paymentData;

        }else{

            return null;
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewDetails(final DataPaymentDetails dpd) {

        if(dpd.getPackageList().isEmpty()){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("View Details");
            builder.setMessage("Package Details not Available.");

            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //do nothing

                }
            });

            builder.create().show();

        }else{


            if(dpd.getPackageList().size() > 1){

                String[] packageList = new String[dpd.getPackageList().size()];

                int count = 0;
                for (com.testkart.exam.payment.model.Package pack: dpd.getPackageList()) {

                    packageList[count] = pack.getName();

                    count++;
                }



                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Purchased Packages")
                        .setItems(packageList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item

                                PaymentDetailsDialog pdd = new PaymentDetailsDialog();

                                Bundle args = new Bundle();
                                args.putSerializable(PaymentDetailsDialog.KEY_PAYMENT_DETAILS, dpd);
                                args.putInt(PaymentDetailsDialog.KEY_PACKAGE_ID, which);
                                pdd.setArguments(args);

                                pdd.show(getSupportFragmentManager(), "PDD");
                            }
                        });

                builder.create().show();

            }else{


                PaymentDetailsDialog pdd = new PaymentDetailsDialog();

                Bundle args = new Bundle();
                args.putSerializable(PaymentDetailsDialog.KEY_PAYMENT_DETAILS, dpd);
                args.putInt(PaymentDetailsDialog.KEY_PACKAGE_ID, 0);
                pdd.setArguments(args);

                pdd.show(getSupportFragmentManager(), "PDD");

            }

        }

    }

    @Override
    public void onTakeExam(DataPaymentDetails dpd) {

        Intent myExam = new Intent(this, ExamListActivity1.class);
        startActivity(myExam);

        finish();
    }

    @Override
    public void readMagazine(DataPaymentDetails dpd) {

        Intent myExam = new Intent(this, MyMagazinesActivity.class);
        startActivity(myExam);

        finish();    }
}
