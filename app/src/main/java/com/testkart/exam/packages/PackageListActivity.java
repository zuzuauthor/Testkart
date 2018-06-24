package com.testkart.exam.packages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.packages.model.DataOrderSummary;
import com.testkart.exam.packages.model.Exam;
import com.testkart.exam.packages.model.PackageDataModel;
import com.testkart.exam.packages.model.PackageResponse;
import com.testkart.exam.testkart.notifications.NotificationActivity;
import com.thefinestartist.utils.content.Ctx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import berlin.volders.badger.BadgeShape;
import berlin.volders.badger.Badger;
import berlin.volders.badger.CountBadge;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by testkart on 8/6/17.
 */

public class PackageListActivity extends AppCompatActivity implements PackageFilterDialog.FilterListener, PackageDetailDialog.OnPackageActionListener, SimpleGestureFilter.SimpleGestureListener {

    public static boolean KEY_CART_CHANGED = false;
    public static ArrayList<String> checkRemoveIds = new ArrayList<>();

    private Context context = this;

    private ProgressDialog progressDialog;

    private ArrayList<DataOrderSummary> dataSet = new ArrayList<>();
    private int counter = 0;
    private GridView actGridPackages;
    private ImageView cartCounter;
    private FrameLayout backButton;

    private CountBadge.Factory circleFactory;
    private Drawable cartDrawable;

    private DBHelper dbHelper;

    private SessionManager sessionManager;

    public static PackageListActivity act;

    private ImageView notificationBell;

    private TextView itemCount;

    private FrameLayout filter;

    private SimpleGestureFilter detector;

    private FrameLayout view1, view2;
    private HorizontalScrollView horizontalView;
    private LinearLayout bottomBar;

    private TextView allTextView;

    ImageView featureExam1, featureExam2, featureExam3;

    LinearLayout viewEmpty;

    private TextView viewActionTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_package_list);
        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //initialization
        initialization();

    }

    private void initialization() {

        act = this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        viewActionTitle = (TextView) findViewById(R.id.viewActionTitle);
        viewActionTitle.setText("Exams");

        view1 = (FrameLayout)findViewById(R.id.view1);
        view2 = (FrameLayout)findViewById(R.id.view2);
        horizontalView = (HorizontalScrollView)findViewById(R.id.horizontalView);
        bottomBar = (LinearLayout)findViewById(R.id.bottomBar);

        viewEmpty = (LinearLayout)findViewById(R.id.viewEmpty);
        viewEmpty.setVisibility(View.GONE);

        featureExam1 = (ImageView)findViewById(R.id.featureExam1);
        featureExam2 = (ImageView)findViewById(R.id.featureExam2);
        featureExam3 = (ImageView)findViewById(R.id.featureExam3);
        featureExam1Listener();
        featureExam2Listener();
        featureExam3Listener();


        Animation RightSwipe = AnimationUtils.loadAnimation(this, R.anim.left_slide);
        horizontalView.startAnimation(RightSwipe);

        // Detect touched area
       // detector = new SimpleGestureFilter(this,this);

        dbHelper = new DBHelper(this);

        sessionManager = new SessionManager(this);

        notificationBell = (ImageView)findViewById(R.id.notificationBell);
        if(sessionManager.isLoggedIn()){

            notificationBell.setVisibility(View.VISIBLE);
            notificationBellListener();

        }else{

            notificationBell.setVisibility(View.GONE);
        }

        backButton = (FrameLayout) findViewById(R.id.backButton);

        filter = (FrameLayout) findViewById(R.id.filter);
        filterListener();

       // cartDrawable = getResources().getDrawable(R.drawable.ic_shopping_cart_white_24dp);
        circleFactory = new CountBadge.Factory(this, BadgeShape.circle(.6f, Gravity.END | Gravity.TOP));
        cartCounter = (ImageView) findViewById(R.id.cartCounter);

        actGridPackages = (GridView) findViewById(R.id.actGridPackages);

        actGridPackagesListener();
        actGridPackagesScrollListener();

        allTextView = (TextView)findViewById(R.id.allTextView);
        allTextViewListener();

        itemCount = (TextView)findViewById(R.id.itemCount);

        cartCounterListener();
        backButtonListener();

        //get all packages
        getAllPakages(-1, "", false);
    }

    private void actGridPackagesScrollListener() {

        actGridPackages.setOnTouchListener(new View.OnTouchListener() {

            int scrollEventListSize = 5;
            float lastY;
            // Used to correct for occasions when user scrolls down(/up) but the onTouchListener detects it incorrectly. We will store detected up-/down-scrolls with -1/1 in this list and evaluate later which occured more often
            List<Integer> downScrolledEventsHappened;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float diff = 0;
                if(event.getAction() == event.ACTION_DOWN){
                    lastY = event.getY();
                    downScrolledEventsHappened = new LinkedList<Integer>();
                }
                else if(event.getAction() == event.ACTION_MOVE){
                    diff = event.getY() - lastY;
                    lastY = event.getY();

                    if(diff>0)
                        downScrolledEventsHappened.add(1);
                    else
                        downScrolledEventsHappened.add(-1);

                    //List needs to be filled with some events, will happen very quickly
                    if(downScrolledEventsHappened.size() == scrollEventListSize+1){
                        downScrolledEventsHappened.remove(0);
                        int res=0;
                        for(int i=0; i<downScrolledEventsHappened.size(); i++){
                            res+=downScrolledEventsHappened.get(i);
                        }

                        if (res > 0)
                            onSwipe(SimpleGestureFilter.SWIPE_DOWN);   //scroll up
                        else
                            onSwipe(SimpleGestureFilter.SWIPE_UP);  //scroll down
                    }
                }
                return false; // don't interrupt the event-chain
            }
        });

    }

    private void featureExam1Listener() {

        featureExam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //6

                getAllPakages(1, "999", true);
            }
        });

    }

    private void featureExam2Listener() {

        featureExam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //no data found 999

                getAllPakages(1, "4", true);

            }
        });

    }


    private void featureExam3Listener() {

        featureExam3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //7
                getAllPakages(1, "7", true);

               // getAllPakages(-1, "", false);

            }
        });

    }

    private void allTextViewListener() {

        allTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAllPakages(-1, "", false);
            }
        });
    }

    private void filterListener() {

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PackageFilterDialog fd = new PackageFilterDialog();

                Bundle args = new Bundle();
                args.putSerializable("PL", packageDataSet);
                fd.setArguments(args);

                fd.show(getSupportFragmentManager(), "FD");
            }
        });
    }

    private void notificationBellListener() {

        notificationBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Ctx.startActivity( new Intent(context, NotificationActivity.class));
            }
        });
    }

    private void backButtonListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAllPakages(int filterType, String filterValue, boolean isFilter) {

        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            progressDialog.show();

            makeAPiCall(filterType, filterValue, isFilter);

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }


    private PackageGridAdapter adapter;

    private ArrayList<PackageDataModel> packageDataSet = new ArrayList<>();

    boolean justOncePack = false;

    private void makeAPiCall(final int filterType, final String filterValue, final boolean isFilter) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<PackageResponse> call = null;

        if(isFilter){

            //apply filters
            call = apiService.getAllPackages(filterType+"", filterValue);

        }else{

            call = apiService.getAllPackages("http://www.testkart.com/rest/Packages/index.json");
        }


        call.enqueue(new Callback<PackageResponse>() {
            @Override
            public void onResponse(Call<PackageResponse> call, Response<PackageResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if (response.body() != null) {

                        if (response.body().getStatus()) {

                            if (!response.body().getResponse().isEmpty()) {

                                viewEmpty.setVisibility(View.GONE);
                                actGridPackages.setVisibility(View.VISIBLE);

                                dataSet.clear();

                               // packageDataSet.clear();

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

                                    if(!justOncePack){
                                        packageDataSet.add(pdm);

                                    }

                                }

                                justOncePack = true;


                                if(filterType == 5){


                                    Log.v("Filter_test", "Filter Val: "+filterValue);

                                    switch (filterValue){


                                        case "1":

                                            Collections.sort(dataSet, new Comparator<DataOrderSummary>()
                                                    {

                                                        public int compare(DataOrderSummary o1, DataOrderSummary o2)
                                                        {
                                                            String sa = o1.getPackageId();
                                                            String sb = o2.getPackageId();

                                                            int v = sa.compareTo(sb);

                                                            return v;

                                                            // it can also return 0, and 1
                                                        }
                                                    }
                                            );

                                            break;

                                        case "2":

                                            Collections.sort(dataSet, new Comparator<DataOrderSummary>()
                                                    {

                                                        public int compare(DataOrderSummary o1, DataOrderSummary o2)
                                                        {
                                                            String sa = o1.getPackageOrdering();
                                                            String sb = o2.getPackageOrdering();

                                                            int v = sa.compareTo(sb);

                                                            return v;

                                                            // it can also return 0, and 1
                                                        }
                                                    }
                                            );


                                            Collections.reverse(dataSet);

                                            break;


                                        case "3":

                                            Collections.sort(dataSet, new Comparator<DataOrderSummary>()
                                                    {

                                                        public int compare(DataOrderSummary o1, DataOrderSummary o2)
                                                        {
                                                            String sa = o1.getPackageAmount();
                                                            String sb = o2.getPackageAmount();

                                                            int v = sa.compareTo(sb);

                                                            return v;

                                                            // it can also return 0, and 1
                                                        }
                                                    }
                                            );

                                            Collections.reverse(dataSet);

                                            break;


                                        case "4":

                                            Collections.sort(dataSet, new Comparator<DataOrderSummary>()
                                                    {

                                                        public int compare(DataOrderSummary o1, DataOrderSummary o2)
                                                        {
                                                            String sa = o1.getPackageAmount();
                                                            String sb = o2.getPackageAmount();

                                                            int v = sa.compareTo(sb);

                                                            return v;

                                                            // it can also return 0, and 1
                                                        }
                                                    }
                                            );


                                            break;

                                    }
                                }else{

                                    Log.v("Filter_test", "I am not in");
                                }



                                //build adapter
                                adapter = new PackageGridAdapter(context, dataSet);

                                ArrayList<DataOrderSummary> dsm = dbHelper.getTotalCartItems();

                                ArrayList<String> cil = new ArrayList<String>();

                                for (DataOrderSummary a:
                                     dsm) {

                                    cil.add(a.getPackageId());
                                }

                                adapter.notifyCartItems(cil);

                                actGridPackages.setAdapter(adapter);

                                itemCount.setText(dataSet.size()+" items");

                            } else {

                                //show dialog ... leader board is empty
                                /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Packages");
                                builder.setMessage("There is no packages available right now.");
                                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        getAllPakages(filterType, filterValue, isFilter);
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


                            itemCount.setText(0+" items");
                            actGridPackages.setVisibility(View.GONE);
                            viewEmpty.setVisibility(View.VISIBLE);

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

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });


    }

    private void cartCounterListener() {

        cartCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter == 0) {

                    Intent emptyCart = new Intent(context, EmptyCartActivity.class);
                    startActivity(emptyCart);

                    finish();

                } else {

                    Intent cartActivity = new Intent(context, ShoppingCartActivity.class);
                    startActivity(cartActivity);

                }

            }
        });
    }

    private void actGridPackagesListener() {

        actGridPackages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataOrderSummary ci = (DataOrderSummary) actGridPackages.getItemAtPosition(position);

                if(!ci.isItemAddedToCart()){

                    progressDialog.show();

                    Intent packageDetailActivity = new Intent(context, PackageDetailActivity.class);
                    packageDetailActivity.putExtra(PackageDetailActivity.KEY_PACKAGE_DETAILS, ci);

                    progressDialog.dismiss();

                    startActivityForResult(packageDetailActivity, 1002);

                    /*PackageDetailDialog pdd = new PackageDetailDialog();
                    Bundle args = new Bundle();
                    args.putSerializable(PackageDetailDialog.KEY_PACKAGE_DETAILS, ci);
                    pdd.setArguments(args);
                    pdd.show(getSupportFragmentManager(), PackageDetailDialog.KEY_PACKAGE_DETAIL_DIALOG_NAME);*/

                }else{

                    showCartActionDialog(ci);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1002){

            ArrayList<DataOrderSummary> cartItemsList = dbHelper.getTotalCartItems();
            counter = cartItemsList.size();
            Badger.sett(cartCounter, circleFactory).setCount(counter);

            updateList();
        }
    }

    private void showCartActionDialog(final DataOrderSummary ci) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("My Cart");
        builder.setMessage(ci.getPackageName()+" has already been added to your cart.");
        builder.setCancelable(false);

        builder.setPositiveButton("Remove from cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //remove item from cart
                //update dataSet
                //refresh list

                dbHelper.removeCartItem(ci.getPackageId());

                //update item count
                if(counter > 0 ){

                    counter--;
                }
                Badger.sett(cartCounter, circleFactory).setCount(counter);

                //ci.setItemAddedToCart(false);
                //adapter.notifyDataSetChanged();
                updateList();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //do nothing

            }
        });


        builder.create().show();
    }


    @Override
    public void onItemAddedToCart(DataOrderSummary item) {

        counter++;

        Badger.sett(cartCounter, circleFactory).setCount(counter);

        //add item to cart
        dbHelper.addCartItem(item);

        //refresh list
        /*item.setItemAddedToCart(true);
        adapter.notifyDataSetChanged();*/
        updateList();

    }

    @Override
    public void onItemBuyNow(DataOrderSummary item) {

        //add item to cart
        dbHelper.addCartItem(item);
        /*item.setItemAddedToCart(true);

        //refresh list
        adapter.notifyDataSetChanged();*/

        updateList();

        Intent cartActivity = new Intent(context, ShoppingCartActivity.class);
        startActivity(cartActivity);

    }


    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<DataOrderSummary> cartItemsList = dbHelper.getTotalCartItems();
        counter = cartItemsList.size();
        Badger.sett(cartCounter, circleFactory).setCount(counter);

        //check if cart change
        if(KEY_CART_CHANGED){

            KEY_CART_CHANGED = false;

        updateList();

            /*if(!checkRemoveIds.isEmpty()){

                for (int i = 0; i <dataSet.size(); i++) {

                    if(dataSet.get(i).getPackageId().contains(checkRemoveIds)){

                        dataSet.get(i).setItemAddedToCart(false);

                    }
                }
            }*/


        }

    }


    private void updateList(){

        ArrayList<DataOrderSummary> cartItemsList = dbHelper.getTotalCartItems();
        ArrayList<String> cil = new ArrayList<>();

        for (DataOrderSummary d:
                cartItemsList) {

            cil.add(d.getPackageId());
        }

        if(adapter != null){

            adapter.notifyCartItems(cil);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {

            if(!sessionManager.isLoggedIn()){

               // if(!sessionManager.isLoggedIn()){

                if(!ShoppingCartActivity.cartRetain){

                    dbHelper.clearCart();
                }

               // }

            }

            dbHelper.close();

        }

    }



    String DEBUG_TAG = "SWIPE LISTENER";

    boolean hideLock = false;
    boolean showLock = false;

    @Override
    public void onSwipe(int direction) {

        String str = "";

        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);


        Animation slide_down_view = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down_view);

        slide_down_view.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                view2.setBackgroundColor(Color.parseColor("#0cc6a1"));
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view2.setBackgroundColor(Color.parseColor("#0cc6a1"));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Animation slide_up_view = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_view);


        slide_up_view.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                view2.setBackgroundColor(Color.parseColor("#0cc6a1"));
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view2.setBackgroundResource(R.drawable.bg_semi_round_view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";

               // horizontalView.startAnimation(slide_up);
               // view1.startAnimation(slide_down);

                if(!showLock){

                    horizontalView.setVisibility(View.VISIBLE);
                    horizontalView.startAnimation( slide_up );

                    view1.setVisibility(View.VISIBLE);
                    view1.startAnimation( slide_down_view );

                    //  horizontalView.setVisibility(View.VISIBLE);
                    // view1.setVisibility(View.VISIBLE);
                    bottomBar.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    showLock = true;
                    hideLock = false;
                }

                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";

             //   horizontalView.startAnimation(slide_down);
             //   view1.startAnimation(slide_up);

                if(!hideLock){

                    horizontalView.startAnimation( slide_down );
                    horizontalView.setVisibility(View.INVISIBLE);

                    view1.startAnimation( slide_up_view );
                    view1.setVisibility(View.INVISIBLE);

                    // horizontalView.setVisibility(View.INVISIBLE);
                    // view1.setVisibility(View.INVISIBLE);
                    bottomBar.setBackgroundColor(0);

                    hideLock = true;
                    showLock = false;

                }

                break;

        }
       // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDoubleTap() {

        //Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class

       // this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onFilter(int filterType, String filterValue) {

       // getAllPakages(filterType, filterValue, false);

        Log.v("Filter_data", "Filter Type: "+filterType+" Filter Value: "+filterValue);
       // Toast.makeText(context, "Filter Type: "+filterType+" Filter Value: "+filterValue, Toast.LENGTH_LONG).show();

        if(filterType == 5){

            reArrangeList(filterType, filterValue);

        }else{

            getAllPakages(filterType, filterValue, true);

        }

    }

    private void reArrangeList(int filterType, String filterValue) {

        getAllPakages(filterType, filterValue, false);

    }
}
