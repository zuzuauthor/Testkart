package com.testkart.exam.packages.oderconfirmation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.MainActivity;
import com.testkart.exam.edu.help.HelpActivity;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.helper.DialogHelper;
import com.testkart.exam.packages.CartListAdapter;
import com.testkart.exam.packages.FinalReviewActivity;
import com.testkart.exam.packages.PackageDetailActivity;
import com.testkart.exam.packages.PaymentActivity;
import com.testkart.exam.packages.model.DataOrderSummary;
import com.testkart.exam.packages.model.Exam;
import com.testkart.exam.packages.model.PackageDataModel;
import com.testkart.exam.packages.model.PackageResponse;
import com.testkart.exam.packages.model.payu.PaymentResponse;
import com.thefinestartist.utils.content.Ctx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by elfemo on 14/9/17.
 */

public class OrderConfirmationActivity extends AppCompatActivity implements View.OnClickListener,
        PurchaseOrderAdapter.PurcahseOrderListener, CartListAdapter.CartItemListener {

    public static final String KEY_PAYMENT_DETAILS = "p_details";
    public static final String KEY_ODER_DETAILS = "o_details";

    private final int MY_PERMISSIONS_REQUEST_MAKE_CALL = 9999;
    private SessionManager session;
    private Context context = this;

    private ImageView oderConfirm;

    private TextView confirmationMessage, rateUs, tranxId, details, confirmationTitle, totalAmount, paymentMode, userName, userAddress, call, email, faq;
    private android.support.v7.widget.RecyclerView recommendItems;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Button continueShopping;

    private ListView purchasedList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_confirmation);
        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Order Confirmation");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        //initialization
        initViews();

    }

    private void initViews() {

        session = new SessionManager(context);

        confirmationMessage = (TextView)findViewById(R.id.confirmationMessage);
        rateUs = (TextView)findViewById(R.id.rateUs);
        tranxId = (TextView)findViewById(R.id.tranxId);
        details = (TextView)findViewById(R.id.details);
        confirmationTitle = (TextView)findViewById(R.id.confirmationTitle);
        totalAmount = (TextView)findViewById(R.id.totalAmount);
        paymentMode = (TextView)findViewById(R.id.paymentMode);
        userName = (TextView)findViewById(R.id.userName);
        userAddress = (TextView)findViewById(R.id.userAddress);
        call = (TextView)findViewById(R.id.call);
        email = (TextView)findViewById(R.id.email);
        faq = (TextView)findViewById(R.id.faq);
        continueShopping = (Button) findViewById(R.id.continueShopping);
        oderConfirm = (ImageView)findViewById(R.id.oderConfirm);

        recommendItems = (android.support.v7.widget.RecyclerView)findViewById(R.id.recommendItems);
        recommendItems.setHasFixedSize(true);

        purchasedList = (ListView) findViewById(R.id.purchasedList);
        purchasedListListener();

        call.setOnClickListener(this);
        email.setOnClickListener(this);
        faq.setOnClickListener(this);
        rateUs.setOnClickListener(this);
        details.setOnClickListener(this);
        continueShopping.setOnClickListener(this);


        //update views
        updateViews();

        //get recommends
        getAllPakages();
    }

    private void purchasedListListener() {

        purchasedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                DataOrderSummary ci = (DataOrderSummary) purchasedList.getItemAtPosition(i);

               // if(!ci.isItemAddedToCart()){

                    //progressDialog.show();

                    Intent packageDetailActivity = new Intent(context, PackageDetailActivity.class);
                    packageDetailActivity.putExtra(PackageDetailActivity.KEY_PACKAGE_DETAILS, ci);

                   // progressDialog.dismiss();

                    startActivity(packageDetailActivity);
            }
        });

    }

    private String getDate(long time) {

        Date date = new Date(time); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        return sdf.format(date);
    }

    private void updateViews() {

        userName.setText(session.getUserDetails().get(SessionManager.KEY_NAME));
        String address = session.getUserDetails().get(SessionManager.KEY_ADDRESS)+"\n"+
                "Mobile: "+session.getUserDetails().get(SessionManager.KEY_PHONE_NUMBER)+"\n"+
                "Email: "+session.getUserDetails().get(SessionManager.KEY_EMAIL);

        userAddress.setText(address);

        //update order confirm image, success, fail or pending text, title
        //get payment details
        PaymentResponse paymentResponse = (PaymentResponse) getIntent().getSerializableExtra(KEY_PAYMENT_DETAILS);
        updatePaymentResponse(paymentResponse);


        String purchaseDate = "Purchase On: "+getDate(System.currentTimeMillis());

        //populate listView
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<DataOrderSummary> cartItemsList = dbHelper.getTotalCartItems();

        PurchaseOrderAdapter adapter = new PurchaseOrderAdapter(context, cartItemsList, false, purchaseDate);
        purchasedList.setAdapter(adapter);
        FinalReviewActivity.setListViewHeightBasedOnChildren(purchasedList);

    }

    private void updatePaymentResponse(PaymentResponse paymentResponse) {

        if(paymentResponse.getStatus().equals("failure")){

            confirmationTitle.setText("SORRY");
            oderConfirm.setImageResource(R.drawable.o_fail);
            confirmationMessage.setText(paymentResponse.getMessage());

        }else if(paymentResponse.getStatus().equals("success")){

            confirmationTitle.setText("CONGRATULATIONS");
            oderConfirm.setImageResource(R.drawable.o_confirm);
            confirmationMessage.setText(paymentResponse.getMessage());



        }else if(paymentResponse.getStatus().equals("pending")){

            confirmationTitle.setText("CONGRATULATIONS");
            oderConfirm.setImageResource(R.drawable.o_confirm);
            confirmationMessage.setText(paymentResponse.getMessage());
            totalAmount.setText("Rs. "+paymentResponse.getAmount());

        }


        totalAmount.setText("Rs. "+paymentResponse.getAmount());

        tranxId.setText(paymentResponse.getTxnid());

        if(paymentResponse.getMode().equalsIgnoreCase("CC")){

            paymentMode.setText("Credit Card");

        }else if(paymentResponse.getMode().equalsIgnoreCase("NB")){

            paymentMode.setText("Net Banking");

        }else if(paymentResponse.getMode().equalsIgnoreCase("FREE")){

            paymentMode.setText("FREE");

        }else{

            paymentMode.setText(paymentResponse.getMode());
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.rateUs:

                DialogHelper.openPlayStoreToRate(context);

                break;

            case R.id.details:

                PaymentDetailsDailog pd = new PaymentDetailsDailog();

                Bundle args = new Bundle();
                args.putSerializable(OrderConfirmationActivity.KEY_PAYMENT_DETAILS, getIntent().getSerializableExtra(KEY_PAYMENT_DETAILS));
                args.putString(PaymentActivity.KEY_SUBMIT_ORDER, getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_ORDER));
                args.putString(PaymentActivity.KEY_SUBMIT_COUPON, getIntent().getStringExtra(PaymentActivity.KEY_SUBMIT_COUPON));
                //args.putBoolean(PaymentActivity.KEY_FREE_CHECKOUT, getIntent().getBooleanExtra(PaymentActivity.KEY_FREE_CHECKOUT, false));
                pd.setArguments(args);
                pd.show(getSupportFragmentManager(), "PD");

                break;

            case R.id.call:

                checkPermissionAndMakeCall();

                break;

            case R.id.email:

                String to = "admin@testkart.com";
                String subject = "Testkart Payment";

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to });
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, "");

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));

                break;

            case R.id.faq:

                Ctx.startActivity( new Intent(context, HelpActivity.class));
                finish();

                break;

            case R.id.continueShopping:

                Ctx.startActivity( new Intent(context, MainActivity.class));
                finish();

                break;

        }
    }



    private void checkPermissionAndMakeCall() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            //check permission for 6.0
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE);

            if(permissionCheck == PackageManager.PERMISSION_GRANTED){

                makeCall("tel:08693098001");

            }else{

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_MAKE_CALL);
            }

        }else{

            makeCall("tel:08693098001"); //
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_MAKE_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "Now we are ready to make calls.", Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void makeCall(String phoneNo) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(phoneNo));

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);

    }



    private void getAllPakages() {

        if (ApiClient.isNetworkAvailable(context)) {

            makeAPiCall();

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }


    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<PackageResponse> call = null;

        call = apiService.getAllPackages("http://www.testkart.com/rest/Packages/index.json");

        call.enqueue(new Callback<PackageResponse>() {
            @Override
            public void onResponse(Call<PackageResponse> call, Response<PackageResponse> response) {

                int code = response.code();

                if (code == 200) {

                    if (response.body() != null) {

                        if (response.body().getStatus()) {

                            if (!response.body().getResponse().isEmpty()) {

                                // packageDataSet.clear();

                                ArrayList<DataOrderSummary> dataSet = new ArrayList<DataOrderSummary>();

                                for (com.testkart.exam.packages.model.Response r :
                                        response.body().getResponse()) {

                                    DataOrderSummary carttem = new DataOrderSummary();

                                    PackageDataModel pdm = new PackageDataModel();

                                    pdm.setPackageId(r.getPackage().getId());
                                    pdm.setPackageName((r.getPackage().getName() != null) ? r.getPackage().getName() : "");

                                    carttem.setPackageId(r.getPackage().getId());
                                    carttem.setPackageName((r.getPackage().getName() != null) ? r.getPackage().getName() : "");
                                    carttem.setPackageDescription((r.getPackage().getDescription() != null) ? r.getPackage().getDescription() : "");
                                    carttem.setPackageAmount((r.getPackage().getAmount() != null) ? r.getPackage().getAmount() : "");
                                    carttem.setPackageSHowAmount((r.getPackage().getShowAmount() != null) ? r.getPackage().getShowAmount() : "");
                                    carttem.setPackageExpiryDays((r.getPackage().getExpiryDays() != null) ? r.getPackage().getExpiryDays() : "");
                                    carttem.setPackageStatus((r.getPackage().getStatus() != null) ? r.getPackage().getStatus() : "");
                                    carttem.setPackageCreated((r.getPackage().getCreated() != null) ? r.getPackage().getCreated() : "");
                                    carttem.setPackageModify((r.getPackage().getModified() != null) ? r.getPackage().getModified() : "");
                                    carttem.setPackageImage((r.getPackage().getPhoto() != null) ? (String) r.getPackage().getPhoto() : "");
                                    carttem.setItemAddedToCart(false);

                                    carttem.setPackageOrdering((r.getPackage().getOrdering() != null) ? (String) r.getPackage().getOrdering() : "0");

                                    String examIds = "";

                                    String examName = "";

                                    for (Exam exam :
                                            r.getExam()) {

                                        examIds += exam.getId() + ", ";
                                        examName += exam.getName() + ", ";

                                    }

                                    carttem.setPackageExamIds(examIds.replaceAll(", $", ""));
                                    carttem.setPackageExamName(examName.replaceAll(", $", ""));
                                    carttem.setPackageCurrencyCode("₹");

                                    dataSet.add(carttem);

                                }

                                // The number of Columns
                                mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recommendItems.setLayoutManager(mLayoutManager);

                                mAdapter = new RecommendedAdapter(context, dataSet);
                                recommendItems.setAdapter(mAdapter);

                            } else {

                                //show dialog ... leader board is empty
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Packages");
                                builder.setMessage("There is no packages available right now.");
                                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        getAllPakages();
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

                        Toast.makeText(context, "POResponse body is null", Toast.LENGTH_SHORT).show();

                    }


                } else {

                    Toast.makeText(context, "ERROR RESPONSE CODE: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PackageResponse> call, Throwable t) {

                t.printStackTrace();

            }
        });


    }

    @Override
    public void onSelectPurchaseItem(DataOrderSummary removedItem) {



    }

    @Override
    public void onItemRemovedFromCart(DataOrderSummary removedItem) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.clearCart();
    }
}
