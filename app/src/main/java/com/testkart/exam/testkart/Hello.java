package com.testkart.exam.testkart;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.freegeek.android.materialbanner.MaterialBanner;
import com.freegeek.android.materialbanner.holder.ViewHolderCreator;
import com.freegeek.android.materialbanner.view.indicator.CirclePageIndicator;
import com.google.firebase.messaging.FirebaseMessaging;
import com.testkart.exam.R;
import com.testkart.exam.banking_digest.buy.MagazinesListActivity;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.leaderboard.LeaderListResponse;
import com.testkart.exam.edu.leaderboard.Leaderboard;
import com.testkart.exam.edu.leaderboard.LeaderboardActivity1;
import com.testkart.exam.edu.login.LoginActivity1;
import com.testkart.exam.edu.login.TestkartGetStartActivity;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.helper.DialogHelper;
import com.testkart.exam.packages.EmptyCartActivity;
import com.testkart.exam.packages.PackageListActivity;
import com.testkart.exam.packages.ShoppingCartActivity;
import com.testkart.exam.testkart.contact_us.ContactUsNativeActivity;
import com.testkart.exam.testkart.home.Home;
import com.testkart.exam.testkart.home.HomePageResponse;
import com.testkart.exam.testkart.home.Testimonial;
import com.testkart.exam.testkart.home.testimonalis.TestimonaliPagerAdapter;
import com.testkart.exam.testkart.notifications.Config;
import com.testkart.exam.testkart.notifications.DataNotification;
import com.testkart.exam.testkart.notifications.NotificationActivity;
import com.testkart.exam.testkart.notifications.NotificationUtils;
import com.testkart.exam.testkart.notifications.PushDialog;
import com.testkart.exam.testkart.policies.PoliciesActivity;
import com.testkart.exam.testkart.study_material.StudyMaterialActivity;
import com.testkart.exam.testkart.widgets.BannerData;
import com.testkart.exam.testkart.widgets.ImageHolderView;
import com.testkart.exam.testkart.widgets.NestingViewPager;
import com.thefinestartist.utils.content.Ctx;

import java.util.ArrayList;
import java.util.List;

import berlin.volders.badger.BadgeShape;
import berlin.volders.badger.Badger;
import berlin.volders.badger.CountBadge;
import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by testkart on 18/7/17.
 */

public class Hello extends AppCompatActivity {

    private Context context = this;
    private DBHelper dbHelper;

    private static final String TAG = Hello.class.getSimpleName();

    //private BannerSlider bannerSlider;
    private String[] images = {"http://www.testkart.com/img/slides_thumb/6d998cabd577c46e56df60e4b997ebad.jpg",
            "http://www.testkart.com/img/slides_thumb/be40f179542b9f0975c683bb4a3edcbb.jpg",
            "http://www.testkart.com/img/slides_thumb/bfb8cb27749a062147e9eda4fd8313b0.jpg"
    };

    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private FrameLayout drawerIcon;

    private ImageView shoppingBagView;
    private FrameLayout shopNowView,leaderboardView;

    private ImageView featureExam1, featureExam2, featureExam3;

    private LinearLayout featureExamContainer;

    //private AutoScrollViewPager testimonaliesPager;
    private NestingViewPager testimonaliesPager;

    private LinearLayout testimonalieContainer;

    private ImageView notificationBell;

    CountBadge.Factory circleFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testkart_getstart_drawer);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //initialization
        //initialization();
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

        checkPermission();

        dbHelper = new DBHelper(context);

        circleFactory = new CountBadge.Factory(this, BadgeShape.circle(.6f, Gravity.END | Gravity.TOP));

        testimonalieContainer = (LinearLayout) findViewById(R.id.testimonalieContainer);
        //testimonaliesPager = (AutoScrollViewPager)findViewById(R.id.pager);
        testimonaliesPager = (NestingViewPager) findViewById(R.id.pager);

        notificationBell = (ImageView)findViewById(R.id.notificationBell);
        notificationBellListener();

        setupDrawer();

        shoppingBagView = (ImageView)findViewById(R.id.shoppingBagView);
        shoppingBagViewListener();

        shopNowView = (FrameLayout)findViewById(R.id.shopNowView);
        shopNowViewListener();

        leaderboardView = (FrameLayout) findViewById(R.id.leaderboardView);
        leaderboardViewListener();


        featureExamContainer = (LinearLayout)findViewById(R.id.featureExamContainer);
        featureExamContainerListener();

        getLeaderListData();

        initBroadcastReceiver();

        showPushDialog(getIntent());


        if(getIntent().getBooleanExtra("IS_FROM_SPLASH", false)){

            setupMaterialBannerSlider(1, null);

        }else{

            getHomeData();
        }


        /*testimonalieContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        testimonaliesPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                testimonaliesPager.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });*/


    }

    private void notificationBellListener() {

        notificationBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent notiIntent = new Intent(context, NotificationActivity.class);
                notiIntent.putExtra("IS_GUEST", true);
                startActivity(notiIntent);

                //Ctx.startActivity( new Intent(context, NotificationActivity.class));
            }
        });
    }

    private void featureExamContainerListener() {

        featureExamContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(permissionsOK){

                    Intent cartActivity = new Intent(context, PackageListActivity.class);
                    startActivity(cartActivity);

                }else{

                    checkPermission();
                }


            }
        });
    }

    private void leaderboardViewListener() {

        leaderboardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent leaderboardIntent = new Intent(context, LeaderboardActivity1.class);
                startActivity(leaderboardIntent);


            }
        });
    }

    private void shopNowViewListener() {

        shopNowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(permissionsOK){

                    Intent cartActivity = new Intent(context, PackageListActivity.class);
                    startActivity(cartActivity);

                }else{

                    checkPermission();
                }

            }
        });
    }


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private void initBroadcastReceiver() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    //displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    showPushDialog(intent);

                    updateNotificationCount();

                    //String message = intent.getStringExtra("message");

                    //Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);

                }
            }
        };
    }

    private void showPushDialog(Intent intent) {


        if(getIntent() != null){

            if(getIntent().getBooleanExtra("push", false)){

                DataNotification dataNoti = new DataNotification();
                dataNoti.setDateTime(getIntent().getStringExtra("time"));
                dataNoti.setImageUrl(getIntent().getStringExtra("image_url"));
                dataNoti.setMessage(getIntent().getStringExtra("message"));

                if (TextUtils.isEmpty(getIntent().getStringExtra("image_url"))) {
                    dataNoti.setIncludeImage(false);


                } else {
                    // image is present, show notification with image
                    dataNoti.setIncludeImage(true);

                }

                dataNoti.setTitle(getIntent().getStringExtra("title"));

                PushDialog pd = new PushDialog();

                Bundle bundle = new Bundle();
                bundle.putSerializable(PushDialog.KEY_NOTIFICATION_DATA, dataNoti);

                pd.setArguments(bundle);

                pd.show(getSupportFragmentManager(), "PUSH_DIALOG");


                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(Config.NOTIFICATION_ID_BIG_IMAGE);


            }else{

                //do nothing
                Log.v("Notification", "No new notification keep inactive");
            }
        }

    }


    private void updateBag(){

        if(dbHelper != null){

         //   int notiCount = dbHelper.getUnreadMessageCount(session.getUserDetails().get(SessionManager.KEY_STUDENT_ID));

         //   Badger.sett(shoppingBagView, circleFactory).setCount(notiCount);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();


       // updateBag();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

        updateNotificationCount();
    }


    private void  updateNotificationCount(){

        int notiCount = dbHelper.getUnreadMessageCount("\'guest\'");

        Badger.sett(notificationBell, circleFactory).setCount(notiCount);
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    private void shoppingBagViewListener() {

        shoppingBagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(permissionsOK){

                    int counter = dbHelper.getTotalCartItems().size();

                    if (counter == 0) {

                        Intent emptyCart = new Intent(context, EmptyCartActivity.class);
                        startActivity(emptyCart);

                    } else {

                        Intent cartActivity = new Intent(context, ShoppingCartActivity.class);
                        startActivity(cartActivity);

                    }

                }else{

                    checkPermission();
                }

            }
        });
    }

    private void setupMaterialBannerSlider(int status, Home hd) {

       // if(getIntent() != null){

            Home homeData = null;

            if(status == 1){

                if(getIntent() != null){

                    homeData = (Home) getIntent().getSerializableExtra("HOME_DATA");

                }else{

                    //default
                    setupBanners(null, images, false);
                }

            }else if(status == 2){

                homeData = hd;
            }

            if(homeData != null){

                setupBanners(homeData,images, true);

                setupTestimonolies(homeData.getTestimonials());

                featureExam1 = (ImageView)findViewById(R.id.featureExam1);
                featureExam2 = (ImageView)findViewById(R.id.featureExam2);
                featureExam3 = (ImageView)findViewById(R.id.featureExam3);

                Glide.with(context)
                        .load(homeData.getFeatureExamUrls().get(0))
                        // .placeholder(R.raw.default_profile)
                        .into(featureExam1);

                Glide.with(context)
                        .load(homeData.getFeatureExamUrls().get(1))
                        // .placeholder(R.raw.default_profile)
                        .into(featureExam2);

                Glide.with(context)
                        .load(homeData.getFeatureExamUrls().get(2))
                        // .placeholder(R.raw.default_profile)
                        .into(featureExam3);

            }else{

                //default
                setupBanners(null, images, false);

            }


        /*}else{

            //default
            setupBanners(null, images, false);

        }*/

    }

    private void setupTestimonolies(List<Testimonial> testimonials) {

        if(testimonials != null && !testimonials.isEmpty()){

            testimonalieContainer.setVisibility(View.VISIBLE);

            TestimonaliPagerAdapter adapter = new TestimonaliPagerAdapter(context, testimonials);
            testimonaliesPager.setAdapter(adapter);

            testimonaliesPager.startAutoScroll();

        }else{

            // hide testimonalies
            testimonalieContainer.setVisibility(View.GONE);
        }
    }


    private void setupBanners(Home homeData, String[] images, boolean fromApi){

        //push notification end
        List<BannerData> bannerData = new ArrayList<>();

        if(fromApi){

            for (int i = 0; i < homeData.getHomeData().size(); i++) {
                BannerData data = new BannerData();
                data.setTitle("Country road " + (i + 1));
                data.setUrl(homeData.getHomeData().get(i).getSlidesPhoto());
                bannerData.add(data);
            }

        }else{

            for (int i = 0; i < images.length; i++) {
                BannerData data = new BannerData();
                data.setTitle("Country road " + (i + 1));
                data.setUrl(images[i]);
                bannerData.add(data);
            }
        }


        MaterialBanner materialBanner = (MaterialBanner) findViewById(R.id.material_banner);
        materialBanner.setPages(new ViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new ImageHolderView();
            }
        },bannerData);

        //set indicator
        CirclePageIndicator cIndicator = new CirclePageIndicator(this);
        cIndicator.setFillColor(Color.parseColor("#FF4081"));
        cIndicator.setStrokeColor(Color.parseColor("#FF4081"));

        materialBanner.setIndicator(cIndicator);
        materialBanner.startTurning(3000);

    }


    private void setupDrawer() {

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        View headerLayout = nvDrawer.getHeaderView(0);

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawer.closeDrawer(GravityCompat.START);

                if(permissionsOK){

                    Intent loginIntent = new Intent(context, TestkartGetStartActivity.class);
                    //loginIntent.putExtra(LoginActivity1.KEY_RETAIN_CART, false);
                    startActivity(loginIntent);

                }else{

                    checkPermission();
                }

            }
        });

        drawerIcon = (FrameLayout) findViewById(R.id.drawerIcon);
        drawerIconListener();
    }

    private void drawerIconListener() {

        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDrawer.isDrawerOpen(GravityCompat.START)){

                    mDrawer.closeDrawer(GravityCompat.START);

                }else{

                    mDrawer.openDrawer(GravityCompat.START);

                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Testkart");
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finishAffinity();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //do nothing
                }
            });

            builder.create().show();

        }
    }



    /*@Override
    public void onBackPressed() {

        if(mDrawer.isDrawerOpen(GravityCompat.START)){

            mDrawer.closeDrawer(GravityCompat.START);
        }else{

            super.onBackPressed();
        }

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        switch(menuItem.getItemId()) {
            case R.id.nav_home:

                //Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_exam:

                if(permissionsOK){

                    // Toast.makeText(this, "Exam", Toast.LENGTH_SHORT).show();
                    gotoActivity(PackageListActivity.class, null);
                }else{

                    checkPermission();
                }

                break;
            case R.id.nav_study_material:

                //DialogHelper.showInfoDialog(context, "In Progress", "The following module is under construction.");

                Intent studyMaterialActivity = new Intent(this, StudyMaterialActivity.class);
                startActivity(studyMaterialActivity);

                break;

            case R.id.nav_register:

                if(permissionsOK){

                    // Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();
                    Intent registerIntent = new Intent(this, com.testkart.exam.edu.SignupActivity.class);
                    startActivity(registerIntent);

                }else{

                    checkPermission();
                }


                break;


            case R.id.nav_login:


                if(permissionsOK){

                    // Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(this, LoginActivity1.class);
                    loginIntent.putExtra(LoginActivity1.KEY_RETAIN_CART, false);
                    startActivity(loginIntent);

                }else{

                    checkPermission();
                }

                break;



            //sub menus
            case R.id.nav_contact_us:

               // Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
                Intent contactUsIntent = new Intent(this, ContactUsNativeActivity.class);
                startActivity(contactUsIntent);

                break;

            case R.id.nav_about_us:

                callPolicies("About Us", "http://www.testkart.com/Contents/pages/aboutusm");
                //Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();

                break;


            case R.id.nav_live_chat:

                DialogHelper.showInfoDialog(context, "In Progress", "The following module is under construction.");

                break;

            case R.id.nav_terms_of_use:

                callPolicies("Terms of Use", "http://www.testkart.com/Contents/pages/TermsOfUseM");
               // Toast.makeText(this, "Terms of Use", Toast.LENGTH_SHORT).show();

                break;


            case R.id.nav_rate_app:

                DialogHelper.openPlayStoreToRate(context);

                break;


            case R.id.nav_more_apps:

                /*Intent moreApps = new Intent(this, MoreAppActivity.class);
                startActivity(moreApps);*/

                final String appPackageName = "Testkart";//getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id="+appPackageName)));
                }

                break;


            case R.id.nav_user_policy:

                callPolicies("User Policy", "http://www.testkart.com/Contents/pages/UserPolicyM");
                //Toast.makeText(this, "User Policy", Toast.LENGTH_SHORT).show();

                break;

            case R.id.nav_privacy_policy:

                callPolicies("Privacy Policy", "http://www.testkart.com/Contents/pages/PrivacyPolicyM");
                //Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show();

                break;

            case R.id.nav_refund_policy:

                callPolicies("Refund Policy", "http://www.testkart.com/Contents/pages/RefundPolicyM");
                //Toast.makeText(this, "Refund Policy", Toast.LENGTH_SHORT).show();

                break;

            case R.id.nav_disclaimer:

                callPolicies("Disclaimer", "http://www.testkart.com/Contents/pages/DisclaimerM");
                //Toast.makeText(this, "Disclaimer", Toast.LENGTH_SHORT).show();

                break;


            case R.id.nav_banking_digest:

                if(permissionsOK){

                    Ctx.startActivity( new Intent(context, MagazinesListActivity.class));

                }else{

                    checkPermission();
                }

                break;


            default:
                Toast.makeText(this, "This is your home", Toast.LENGTH_SHORT).show();
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }



    private void callPolicies(String policyName, String policyUrl){

        Intent policyIntent = new Intent(context, PoliciesActivity.class);
        policyIntent.putExtra(PoliciesActivity.KEY_POLICY_NAME, policyName);
        policyIntent.putExtra(PoliciesActivity.KEY_POLICY_URL, policyUrl);
        startActivity(policyIntent);

        //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }


    private void gotoActivity(Class c, Bundle bundle){

        Intent policyIntent = new Intent(context, c);
        startActivity(policyIntent);

        //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }




    //working with leader board
    private void getLeaderListData() {

        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            //progressDialog.show();

            makeAPiCall();

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
       // progressDialog.show();

        Call<LeaderListResponse> call = apiService.getLeaderList("http://www.testkart.com/rest/Leaderboards.json", "", "");

        call.enqueue(new Callback<LeaderListResponse>() {
            @Override
            public void onResponse(Call<LeaderListResponse> call, retrofit2.Response<LeaderListResponse> response) {

                //progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if (response.body().getStatus()) {

                        //build adapter

                        if(response.body().getLeaderboard() != null && !response.body().getLeaderboard().isEmpty()){

                            initLeaderViews();

                            for (int i = 0; i < response.body().getLeaderboard().size(); i++) {

                                Leaderboard leader = response.body().getLeaderboard().get(i);

                                if(i == 0){

                                    if (leader != null) {

                                        studentName1.setText(leader.getStudentName());
                                        examName1.setText(leader.getExamName());
                                        percentage1.setText(leader.getResultPercent()+"%");
                                        groupName1.setText(leader.getGroup());

                                        //load profile image
                                        Glide.with(context)
                                                .load(leader.getStudentPhoto())
                                                // .placeholder(R.raw.default_profile)
                                                .into(viewProfilePic1);

                                        trophy1.setImageResource(R.drawable.trophy1);

                                        TextDrawable drawable4 = TextDrawable.builder()
                                                .beginConfig()
                                                .textColor(Color.parseColor("#ffffff"))
                                                .useFont(Typeface.DEFAULT)
                                                .endConfig()
                                                .buildRound("1", Color.parseColor("#05c1ff"));

                                        viewRank1.setImageDrawable(drawable4);

                                    }

                                }else if(i ==1){

                                    studentName2.setText(leader.getStudentName());
                                    examName2.setText(leader.getExamName());
                                    percentage2.setText(leader.getResultPercent()+"%");
                                    groupName2.setText(leader.getGroup());

                                    //load profile image
                                    Glide.with(context)
                                            .load(leader.getStudentPhoto())
                                            // .placeholder(R.raw.default_profile)
                                            .into(viewProfilePic2);

                                    trophy2.setImageResource(R.drawable.trophy2);
                                    TextDrawable drawable4 = TextDrawable.builder()
                                            .beginConfig()
                                            .textColor(Color.parseColor("#ffffff"))
                                            .useFont(Typeface.DEFAULT)
                                            .endConfig()
                                            .buildRound("2", Color.parseColor("#05c1ff"));

                                    viewRank2.setImageDrawable(drawable4);

                                }else if(i == 2){

                                    studentName3.setText(leader.getStudentName());
                                    examName3.setText(leader.getExamName());
                                    percentage3.setText(leader.getResultPercent()+"%");
                                    groupName3.setText(leader.getGroup());

                                    //load profile image
                                    Glide.with(context)
                                            .load(leader.getStudentPhoto())
                                            // .placeholder(R.raw.default_profile)
                                            .into(viewProfilePic3);

                                    trophy3.setImageResource(R.drawable.trophy3);
                                    TextDrawable drawable4 = TextDrawable.builder()
                                            .beginConfig()
                                            .textColor(Color.parseColor("#ffffff"))
                                            .useFont(Typeface.DEFAULT)
                                            .endConfig()
                                            .buildRound("3", Color.parseColor("#05c1ff"));

                                    viewRank3.setImageDrawable(drawable4);

                                }else{

                                    break;
                                }
                            }

                            //update leader view 1,2,3


                        }else{

                            //getLeaderListData();

                            /*//show dialog ... leader board is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Leader Board");
                            builder.setMessage("Leader board is empty");
                            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


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
            public void onFailure(Call<LeaderListResponse> call, Throwable t) {

                //hide progress dialog
               // progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });

    }


    CircleImageView viewProfilePic1, viewProfilePic2, viewProfilePic3;
    TextView studentName1, studentName2, studentName3;
    TextView examName1, examName2, examName3;
    TextView groupName1, groupName2, groupName3;
    ImageView viewRank1, viewRank2, viewRank3;
    ImageView trophy1, trophy2, trophy3;
    AutofitTextView percentage1, percentage2, percentage3;

    private void initLeaderViews() {

        //student1
        viewProfilePic1 = (CircleImageView)findViewById(R.id.viewProfilePic1);
        studentName1 = (TextView)findViewById(R.id.studentName1);
        examName1 = (TextView)findViewById(R.id.examName1);
        trophy1 = (ImageView)findViewById(R.id.trophy1);
        percentage1 = (AutofitTextView)findViewById(R.id.percentage1);
        viewRank1 = (ImageView)findViewById(R.id.viewRank1);
        groupName1 = (TextView)findViewById(R.id.groupName1);

        //student1
        viewProfilePic2 = (CircleImageView)findViewById(R.id.viewProfilePic2);
        studentName2 = (TextView)findViewById(R.id.studentName2);
        examName2 = (TextView)findViewById(R.id.examName2);
        trophy2 = (ImageView)findViewById(R.id.trophy2);
        percentage2 = (AutofitTextView)findViewById(R.id.percentage2);
        viewRank2 = (ImageView)findViewById(R.id.viewRank2);
        groupName2 = (TextView)findViewById(R.id.groupName2);

        //student3
        viewProfilePic3 = (CircleImageView)findViewById(R.id.viewProfilePic3);
        studentName3 = (TextView)findViewById(R.id.studentName3);
        examName3 = (TextView)findViewById(R.id.examName3);
        trophy3 = (ImageView)findViewById(R.id.trophy3);
        percentage3 = (AutofitTextView)findViewById(R.id.percentage3);
        viewRank3 = (ImageView)findViewById(R.id.viewRank3);
        groupName3 = (TextView)findViewById(R.id.groupName3);

    }







    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 9999;
    private final int MY_PERMISSIONS_REQUEST_GET_ACCOUNT = 1111;

    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 2222;
    private final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 3333;
    private final int MY_PERMISSIONS_REQUEST_RECEVIE_SMS = 4444;
    private final int MY_PERMISSIONS_REQUEST_READ_SMS = 5555;

    private boolean permissionsOK = false;

    private void checkPermission() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            //check permission for 6.0
            int permissionAccountCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.GET_ACCOUNTS);

            int permissionReadContacts = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE);

            int permissionReadExternalStorage = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            int permissionWriteExternalStorage = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            int permissionReceiveSms = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECEIVE_SMS);

            int permissionReadSms = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_SMS);

            if(permissionAccountCheck != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        MY_PERMISSIONS_REQUEST_GET_ACCOUNT);

            }else if(permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED){

               ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);

            }else if(permissionWriteExternalStorage != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);

            }else if(permissionReceiveSms != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_RECEVIE_SMS);

            }else if(permissionReadSms != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        MY_PERMISSIONS_REQUEST_READ_SMS);

            }else if(permissionReadContacts != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }else{

                //ready to go
                // all permissions granted
                permissionsOK = true;

            }

        }else{

            // no need of permission call
            permissionsOK = true;
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_GET_ACCOUNT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                   // Toast.makeText(this, "Now we are ready to make calls.", Toast.LENGTH_SHORT).show();

                    checkPermission();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permissionsOK = false;
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Toast.makeText(this, "Now we are ready to make calls.", Toast.LENGTH_SHORT).show();

                    checkPermission();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permissionsOK = false;
                }
                return;
            }


            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Toast.makeText(this, "Now we are ready to make calls.", Toast.LENGTH_SHORT).show();

                    checkPermission();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permissionsOK = false;
                }
                return;
            }


            case MY_PERMISSIONS_REQUEST_RECEVIE_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Toast.makeText(this, "Now we are ready to make calls.", Toast.LENGTH_SHORT).show();

                    checkPermission();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permissionsOK = false;
                }
                return;
            }


            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Toast.makeText(this, "Now we are ready to make calls.", Toast.LENGTH_SHORT).show();

                    checkPermission();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permissionsOK = false;
                }
                return;
            }


            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "Now we are ready to Register / Login.", Toast.LENGTH_SHORT).show();
                    permissionsOK = true;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permissionsOK = false;
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

            // other 'case' lines to check for other
            // permissions this app might request
        }


        private ProgressDialog progressDialog;
    private void getHomeData() {

        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();

            makeHomeDataAPiCall();

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void makeHomeDataAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
         progressDialog.show();

        Call<HomePageResponse> call = apiService.getHomePageResponse("http://www.testkart.com/rest/Apis/home.json");

        call.enqueue(new Callback<HomePageResponse>() {
            @Override
            public void onResponse(Call<HomePageResponse> call, retrofit2.Response<HomePageResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if (response.body() != null) {

                        if(response.body().getStatus()){

                            setupMaterialBannerSlider(2, response.body().getHome());

                        }else{


                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }



                    } else {

                        Toast.makeText(context, "Home Data POResponse Body Null", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HomePageResponse> call, Throwable t) {

                //hide progress dialog
                 progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });

    }

}
