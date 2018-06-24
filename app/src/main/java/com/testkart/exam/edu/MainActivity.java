package com.testkart.exam.edu;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.testkart.exam.R;
import com.testkart.exam.banking_digest.buy.MagazinesListActivity;
import com.testkart.exam.banking_digest.my_magazines.MyMagazinesActivity;
import com.testkart.exam.edu.dashboard.MyDashboardResponse;
import com.testkart.exam.edu.dashboard.MyExamStatus;
import com.testkart.exam.edu.dashboard.TodaysExam;
import com.testkart.exam.edu.dashboard.TopExamAdapter;
import com.testkart.exam.edu.exam.ExamListAdapter;
import com.testkart.exam.edu.exam.examlist.Exam;
import com.testkart.exam.edu.exam.examlist.ExamListActivity;
import com.testkart.exam.edu.exam.examlist.ExamListActivity1;
import com.testkart.exam.edu.exam.examlist.ExamListResponse;
import com.testkart.exam.edu.exam.examlist.Response;
import com.testkart.exam.edu.exam.model.CheckExamResponse;
import com.testkart.exam.edu.help.HelpActivity;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.MknUtils;
import com.testkart.exam.edu.helper.MyBrowser;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.leaderboard.LeaderboardActivity1;
import com.testkart.exam.edu.login.LoginActivity1;
import com.testkart.exam.edu.mailbox.MailCountResponse;
import com.testkart.exam.edu.mailbox.MyCurrency;
import com.testkart.exam.edu.mailbox.WalletResponse;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.myresult.GroupPerformanceActivity;
import com.testkart.exam.edu.myresult.MyResultActivity1;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.edu.transaction.TransactionHistoryActivity;
import com.testkart.exam.helper.DialogHelper;
import com.testkart.exam.packages.EmptyCartActivity;
import com.testkart.exam.packages.PackageListActivity;
import com.testkart.exam.packages.ShoppingCartActivity;
import com.testkart.exam.payment.PaymentHistoryActivity;
import com.testkart.exam.testkart.Hello;
import com.testkart.exam.testkart.contact_us.ContactUsNativeActivity;
import com.testkart.exam.testkart.home.HomeCopyActivity;
import com.testkart.exam.testkart.home.HomePageResponse;
import com.testkart.exam.testkart.notifications.Config;
import com.testkart.exam.testkart.notifications.DataNotification;
import com.testkart.exam.testkart.notifications.MyFirebaseInstanceIDService;
import com.testkart.exam.testkart.notifications.NotificationActivity;
import com.testkart.exam.testkart.notifications.NotificationUtils;
import com.testkart.exam.testkart.notifications.PushDialog;
import com.testkart.exam.testkart.study_material.StudyMaterialActivity;
import com.thefinestartist.utils.content.Ctx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import berlin.volders.badger.BadgeShape;
import berlin.volders.badger.Badger;
import berlin.volders.badger.CountBadge;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MyBrowser.MyBrowserListener {

    public static MainActivity mainActivity;

    private String URL = "";

    private SessionManager session;
    private Context context = this;
    //private  WebView myWebView;

    //public PageLoader pageLoader;
    public int AnimationMode;
    public int LoadMode;

    private CircleImageView viewProfilePic;
    private TextView viewUserName, viewEmail;
    private HashMap<String, String> user;


    private ListView topfive;
    private TextView topFiveHeading;
    CountBadge.Factory circleFactory;

    private DBHelper dbHelper;

    private ProgressDialog progressDialog;

    private boolean isLogoutDialogVisible = false;


    //new layout views
    private TextView examGiven, absentExams, bestScoreIn, onDate, failIn, avgPercentage, rank;
    private ListView topExams;

    private LinearLayout topExamsContainer;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // setContentView(R.layout.activity_dashboard);
        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        mainActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Dashboard");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //
        ///if(LoginActivity1.gotoCart){

       // }else{

            ///LoginActivity1.gotoCart = false;

            checkInternet();

            initBroadcastReceiver();

            updateUserToken();

       // }

    }

    private void updateUserToken() {

        // check if user need to update
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);

        if(pref.getBoolean(MyFirebaseInstanceIDService.KEY_FCM_UPDATE_NEEDED, false)){

            //get user token
            String token = pref.getString(MyFirebaseInstanceIDService.KEY_FCM_TOKEN, null);

            if(token != null){

                MyFirebaseInstanceIDService.sendRegistrationToServer(token, context);

            }else{

                Log.v("MyFirebaseInstanceID", "Token is null cannot update");
            }

        }else{

            Log.v("MyFirebaseInstanceID", "Token already updated");
        }

    }


    //comment from here


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

                    //String message = intent.getStringExtra("message");

                    //Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);

                    showPushDialog(getIntent());

                    if(menu != null){

                        checkUnreadMessages(menu);

                    }

                }
            }
        };
    }

    private void deleteExtraNotification() {

        //get notification count
        String userId = session.getUserDetails().get(SessionManager.KEY_STUDENT_ID);
        int notificationCount = dbHelper.getNotificationCount(userId);

        if(notificationCount>5){

            //get all the notification
            ArrayList<DataNotification> notificationsList = dbHelper.getNotifications(userId, -1);

            //un select latest 5 notification
            for (int i = 0; i < 5; i++) {

                notificationsList.remove(i);
            }

            //lets delete the garbage notifications
            dbHelper.deleteOldNotifications(notificationsList);

        }else{

            Log.v("Notification Checkup", "Nothing to clean");
        }

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

        dbHelper = new DBHelper(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        session = new SessionManager(context);
        user = session.getUserDetails();
        Log.v("User", session.getUserDetails().toString());

        http://testkart.com/envato/demos/edu-com.testkart.exam
       /*URL = "http://www.testkart.com/crm/Dashboardsm";"http://elfemo.com/envato/demos/edu-ibps/rest/Dashboards/index?public_key="+session.getUserDetails().get(SessionManager.KEY_PUBLIC)
                +"&private_key="+session.getUserDetails().get(SessionManager.KEY_PRIVATE);
*/
        Log.v("URL", URL);


        //clear extra notification
        //deleteExtraNotification();


        fab = (FloatingActionButton)findViewById(R.id.fab);
        fabListener();


        topfive = (ListView)findViewById(R.id.topfive);
        topFiveHeading = (TextView)findViewById(R.id.topFiveHeading);

        circleFactory = new CountBadge.Factory(this, BadgeShape.circle(.6f, Gravity.END | Gravity.TOP));

        topExamsContainer = (LinearLayout)findViewById(R.id.topExamsContainer);
        topExamsContainer.setVisibility(View.GONE);

        examGiven = (TextView)findViewById(R.id.examGiven);
        absentExams = (TextView)findViewById(R.id.absentExams);
        bestScoreIn = (TextView)findViewById(R.id.bestScoreIn);
        onDate = (TextView)findViewById(R.id.onDate);
        failIn = (TextView)findViewById(R.id.failIn);
        avgPercentage = (TextView)findViewById(R.id.avgPercentage);
        rank = (TextView)findViewById(R.id.rank);

        topExams = (ListView) findViewById(R.id.topExams);
        topExams.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        MknUtils.setListViewHeightBasedOnChildren(topExams);


        topExamsListener();


        getMyDashboardData();





        // myWebView = (WebView) findViewById(R.id.webView);
        // Configure related browser settings
       // myWebView.getSettings().setLoadsImagesAutomatically(true);
       // myWebView.getSettings().setJavaScriptEnabled(true);
       // myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
       // myWebView.setWebViewClient(new MyBrowser(this));

        // Enable responsive layout
       // myWebView.getSettings().setUseWideViewPort(true);
        // Zoom out if the content width is greater than the width of the viewport
       // myWebView.getSettings().setLoadWithOverviewMode(true);


        if(user != null){

            View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);

            if(user.get(SessionManager.KEY_PROFILE_IMAGE) != null && !user.get(SessionManager.KEY_PROFILE_IMAGE).isEmpty()){

                //load profile image
                Glide.with(context)
                        .load(user.get(SessionManager.KEY_PROFILE_IMAGE))
                        // .placeholder(R.raw.default_profile)
                        .into(((CircleImageView) header.findViewById(R.id.hpic)));

            }

            ((TextView) header.findViewById(R.id.fName)).setText(user.get(SessionManager.KEY_NAME));
            ((TextView) header.findViewById(R.id.email)).setText(user.get(SessionManager.KEY_EMAIL));

        }


        /*pageLoader = (PageLoader) findViewById(R.id.pageloader);
        pageLoader.setImageLoading(R.raw.test_logo_progress_64);
        pageLoader.setLoadingImageHeight(57);
        pageLoader.setLoadingImageWidth(100);
        pageLoader.setOnRetry(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Load the initial URL


               // myWebView.loadUrl(URL);
            }
        });*/

        // Load the initial URL
      //  myWebView.loadUrl(URL);


        //check remaning exam
        if (ApiClient.isNetworkAvailable(context)) {

            //update menu
            //check configuration and update views
            checkRemaningExam();

        }

        showPushDialog(getIntent()  );

        showCartDialog();

        headwrListener();

    }

    private void headwrListener() {

        ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Ctx.startActivity( new Intent(context, ProfileActivity.class));
            }
        });
    }

    private void showCartDialog() {

        if(LoginActivity1.gotoCart){

            LoginActivity1.gotoCart = false;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("My Cart");
            builder.setMessage("You have items in your cart. Do you want to continue your checkout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent gotoCart = new Intent(context, ShoppingCartActivity.class);
                    startActivity(gotoCart);

                }
            });

            builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //do nothing

                }
            });

            builder.create().show();

        }else{

            //Toast.makeText(context, "cant show", Toast.LENGTH_SHORT).show();
            //do nothing
        }
    }

    private void fabListener() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getHomeData(1);

            }
        });
    }

    private void topExamsListener() {

        topExams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TodaysExam tE = (TodaysExam) topExams.getItemAtPosition(i);

                pickExamAction(null, tE.getExamId());

                //Toast.makeText(context, tE.getExamName(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getMyDashboardData() {

        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            progressDialog.show();

            makeDashboardAPiCall(true);

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }
    }

    private void makeDashboardAPiCall(boolean b) {

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            //show progress dialog
            if(b){

            progressDialog.show();
            }


            Call<MyDashboardResponse> call = apiService.getMyDashboardData(session.getUserDetails().get(SessionManager.KEY_PUBLIC), session.getUserDetails().get(SessionManager.KEY_PRIVATE));

            call.enqueue(new Callback<MyDashboardResponse>() {
                @Override
                public void onResponse(Call<MyDashboardResponse> call, retrofit2.Response<MyDashboardResponse> response) {

                    progressDialog.dismiss();

                    int code = response.code();

                    if (code == 200) {

                        if(response.body() != null){

                            if (response.body().getStatus()) {


                                //update user status
                                updateUserStats(response.body().getDashboardData().getMyExamStatus());

                                //update top exams
                                updateTopExams(response.body().getDashboardData().getTodaysExams());


                            } else {

                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }else{

                            Toast.makeText(context, "POResponse body is null " + code, Toast.LENGTH_SHORT).show();
                        }


                    } else {

                        Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MyDashboardResponse> call, Throwable t) {

                    //hide progress dialog
                    progressDialog.dismiss();

                    Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                    t.printStackTrace();

                }
            });


    }

    private void updateTopExams(List<TodaysExam> todaysExams) {

        if(todaysExams != null && !todaysExams.isEmpty()){

            topExamsContainer.setVisibility(View.VISIBLE);
            topExams.setAdapter(new TopExamAdapter(context, todaysExams));

        }else{

            topExamsContainer.setVisibility(View.GONE);
        }

    }

    private void updateUserStats(MyExamStatus examStats) {

        Log.v("MyExamStatus", examStats.toString());

        examGiven.setText(examStats.getTotalExamGiven().intValue()+"");
        absentExams.setText(examStats.getAbsentExams().intValue()+"");
        bestScoreIn.setText(examStats.getBestScoreIn());
        onDate.setText(examStats.getOn());
        failIn.setText(examStats.getFailedExamCount().intValue()+"");
        avgPercentage.setText(examStats.getAveragePercentage());
        rank.setText(examStats.getRank());

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

                    finish();
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


    boolean inflateMenuJustOnce = false;

    private Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!inflateMenuJustOnce){

            inflateMenuJustOnce = true;
            getMenuInflater().inflate(R.menu.main, menu);

            this.menu = menu;

        }

        updateCat(menu);

        if (ApiClient.isNetworkAvailable(context)) {

            //update menu
            //check configuration and update views
            updateViews(menu);

        }else{

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }


        return true;
    }


    private void updateViews(Menu menu) {

        if(user.get(SessionManager.KEY_PAID_EXAM) != null){

            if(!user.get(SessionManager.KEY_PAID_EXAM).equalsIgnoreCase("1")){

                menu.findItem(R.id.action_wallet)
                        .setVisible(false);

                //also hide side menu items
                ((NavigationView)findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_transation).setVisible(false);
                ((NavigationView)findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_payment).setVisible(false);



            }else{

                //getWalletAmount(menu);

            }
        }

        //get message count
       // getMesageCount(menu);
        checkUnreadMessages(menu);

    }



    private void getWalletAmount(Menu menu) {

        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            progressDialog.show();

            walletAmount(menu);

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }
    }


    private String walletAmount = "";
    private void walletAmount(final Menu menu) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<WalletResponse> call = apiService.getWalletCount(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, retrofit2.Response<WalletResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if (response.body().getStatus()) {

                        //build adapter

                        if(menu != null){

                            MenuItem mi =  menu.findItem(R.id.action_wallet);
                            mi.setTitle(MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+response.body().getBalance());

                            walletAmount = response.body().getBalance();
                        }

                    } else {

                        //check if token is invalid or not
                        if(response.body().getMessage().contains("Invalid Token") && !isLogoutDialogVisible){

                            isLogoutDialogVisible = true;

                            //show logout dialog
                            new MknUtils().invalidTokenLogoutUser(context, session, new MknUtils.Visitor() {
                                @Override
                                public int doJob(Bundle args) {

                                    Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();

                                    finishAffinity();

                                    return 0;
                                }
                            });


                        }

                    }

                } else {

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {


                //hide progress dialog
                progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });

    }

    private void getMesageCount(Menu menu) {

        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            progressDialog.show();

            msgcount(menu);

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }


    private void msgcount(final Menu menu) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<MailCountResponse> call = apiService.getMailCount(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<MailCountResponse>() {
            @Override
            public void onResponse(Call<MailCountResponse> call, retrofit2.Response<MailCountResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if(response.body() != null){

                        if (response.body().getStatus()) {

                            //build adapter

                            if(menu != null){

                                Badger.sett(menu.findItem(R.id.action_message), circleFactory).setCount((response.body().getEmail() != null ? response.body().getEmail() : 0));

                            }

                        } else {

                            //check if token is invalid or not
                            if(response.body().getMessage().contains("Invalid Token") && !isLogoutDialogVisible){

                                isLogoutDialogVisible = true;

                                //show logout dialog
                                new MknUtils().invalidTokenLogoutUser(context, session, new MknUtils.Visitor() {
                                    @Override
                                    public int doJob(Bundle args) {

                                        Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();

                                        finishAffinity();

                                        return 0;
                                    }
                                });


                            }else{

                                if(menu != null){

                                    Badger.sett(menu.findItem(R.id.action_message), circleFactory).setCount(0);
                                }

                            }

                        }

                    }else{

                        Toast.makeText(context, "POResponse body null", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MailCountResponse> call, Throwable t) {


                //hide progress dialog
                progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Testkart");
            builder.setMessage("Are you sure you want to logout?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    // logout session and clear session
                    session.logoutUser(false);
                    finish();

                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //do nothing

                }
            });

            builder.create().show();

            return true;
        }else if(id == R.id.action_message){

            Badger.sett(item, circleFactory).setCount(0);

            Ctx.startActivity( new Intent(context, NotificationActivity.class));

        }else if(id == R.id.action_wallet){

            Ctx.startActivity( new Intent(context, TransactionHistoryActivity.class));

        }else if(id == R.id.action_cart){

            //check if cart empty or not
            if(dbHelper.getTotalCartItems().size() > 0){

                Ctx.startActivity( new Intent(context, ShoppingCartActivity.class));

            }else{


                Intent emptyCart = new Intent(context, EmptyCartActivity.class);
                startActivity(emptyCart);

                //Toast.makeText(context, "Your cart is empty", Toast.LENGTH_SHORT).show();

            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_leaderboard) {

            //go to ProfileActivity
            Ctx.startActivity( new Intent(context, LeaderboardActivity1.class));

        } else if (id == R.id.nav_myexam) {

            //go to MyExamActivity
            Ctx.startActivity( new Intent(context, ExamListActivity1.class));
            //DialogHelper.showInfoDialog(context, "In Progress", "The following module is under construction.");

        } else if (id == R.id.nav_myresult) {

            //go to MyResultActivity
            Ctx.startActivity( new Intent(context, MyResultActivity1.class));

        } else if (id == R.id.nav_payment) {

            //go to PaymentActivity
            Ctx.startActivity( new Intent(context, PaymentActivity.class));

        } else if (id == R.id.nav_transation) {

            //go to TransationHistoryActivity
            Ctx.startActivity( new Intent(context, TransactionHistoryActivity.class));

        } else if (id == R.id.nav_mailbox) {

            //go to MyInboxActivity
            Ctx.startActivity( new Intent(context, MyInboxActivity.class));

        }else if (id == R.id.nav_profile){

            //go to ProfileActivity
            Ctx.startActivity( new Intent(context, ProfileActivity.class));

        }else if(id == R.id.nav_help){

            Ctx.startActivity( new Intent(context, HelpActivity.class));

        }else if(id == R.id.nav_group){

            Ctx.startActivity( new Intent(context, GroupPerformanceActivity.class));

        }else if(id == R.id.nav_buy_packages){

            Ctx.startActivity( new Intent(context, PackageListActivity.class));

        }else if(id == R.id.nav_my_magazines){

            Ctx.startActivity( new Intent(context, MyMagazinesActivity.class));

        }else if(id == R.id.nav_payment_history){

            Ctx.startActivity( new Intent(context, PaymentHistoryActivity.class));

        }else if(id == R.id.nav_study_material){

           // DialogHelper.showInfoDialog(context, "In Progress", "The following module is under construction.");

            Intent studyMaterialActivity = new Intent(this, StudyMaterialActivity.class);
            startActivity(studyMaterialActivity);

        }else if(id == R.id.nav_contact_us){

            Ctx.startActivity( new Intent(context, ContactUsNativeActivity.class));

        }else if(id == R.id.nav_more_apps){

        //Ctx.startActivity( new Intent(context, MoreAppActivity.class));

            final String appPackageName = "Testkart";//getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id="+appPackageName)));
            }

        }else if(id == R.id.nav_home){

            getHomeData(1);

        }else if(id == R.id.nav_rate_app){

            DialogHelper.openPlayStoreToRate(context);

        }else if(id == R.id.nav_banking_digest){

            Ctx.startActivity( new Intent(context, MagazinesListActivity.class));

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        /*if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }*/
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }


    //my browser listeners
    @Override
    public void onEduPageStarted(WebView view, String url, Bitmap favicon) {

        //pageLoader.startProgress();
        LoadMode = 1;

    }

    @Override
    public void onEduPageFinished(WebView view, String url) {

        //pageLoader.stopProgress();
        LoadMode = 2;

    }

    @Override
    public void onEduReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

        //pageLoader.stopProgressAndFailed();
        LoadMode = 3;
    }



    private boolean firstTIme = true;

    @Override
    protected void onResume() {
        super.onResume();

        if(ProfileActivity.updatePic){

            ProfileActivity.updatePic = false;

            View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
            if(session.getUserDetails().get(SessionManager.KEY_PROFILE_IMAGE) != null && !session.getUserDetails().get(SessionManager.KEY_PROFILE_IMAGE).isEmpty()){

                //load profile image
                Glide.with(context)
                        .load(session.getUserDetails().get(SessionManager.KEY_PROFILE_IMAGE))
                        // .placeholder(R.raw.default_profile)
                        .into(((CircleImageView) header.findViewById(R.id.hpic)));

            }

        }


        if(!firstTIme){

            firstTIme = false;

            if (ApiClient.isNetworkAvailable(context)) {

                //update message count and wallete
               // getWalletAmount(menu);
               // getMesageCount(menu);
                updateCat(menu);

                //check any unread notification
                checkUnreadMessages(menu);

            } else {

                Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

            }

        }else {

            firstTIme = false;

        }


        if (ApiClient.isNetworkAvailable(context)) {

            //check if dashboard need to refresh
            if(Consts.KEY_REFRESH_DASHBOARD){

                Consts.KEY_REFRESH_DASHBOARD = false;

                // Load the initial URL
                /*if(myWebView != null){

                    //myWebView.loadUrl(URL);
                }*/

                makeDashboardAPiCall(false);


            }else{

                Consts.KEY_REFRESH_DASHBOARD = false;
            }

        }else{

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }



        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    private void checkUnreadMessages(Menu menu) {

        if(menu != null){

            Log.v("Student Id", "ID=>"+session.getUserDetails().get(SessionManager.KEY_STUDENT_ID));

            int notiCount = dbHelper.getUnreadMessageCount(session.getUserDetails().get(SessionManager.KEY_STUDENT_ID));

            Badger.sett(menu.findItem(R.id.action_message), circleFactory).setCount(notiCount);
        }

    }

    private void updateCat(Menu menu) {

        if(menu != null){

            try{

                int cartItemCount =  dbHelper.getTotalCartItems().size();

                Badger.sett(menu.findItem(R.id.action_cart), circleFactory).setCount(cartItemCount);

            }catch (Exception e){

                Log.v("Error", e.getMessage());
            }

        }
    }


    //----------------------------------Top five exam

    private void todayExam() {

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
        //progressDialog.show();

        Call<ExamListResponse> call = apiService.getTodayExam(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<ExamListResponse>() {
            @Override
            public void onResponse(Call<ExamListResponse> call, retrofit2.Response<ExamListResponse> response) {

                //progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body().getStatus()){

                        if(response.body().getResponse().isEmpty()){

                            // no need to show top five exam
                            //topFiveHeading.setVisibility(View.GONE);
                            topfive.setVisibility(View.GONE);

                        }else{

                            //show top five
                            //topFiveHeading.setVisibility(View.VISIBLE);
                            topfive.setVisibility(View.VISIBLE);

                            ArrayList<Exam> dataSet = new ArrayList<Exam>();

                            for (Response r:
                                    response.body().getResponse()) {

                                Exam  e = r.getExam();
                                e.setExpiry((String)r.getExamOrder().getExpiryDate());
                                //e.setExamExpiry((String)r.getExamOrder().getExpiryDate());

                                dataSet.add(e);
                            }

                            ExamListAdapter adapter = new ExamListAdapter(context, dataSet,0, true);

                            topfive.setAdapter(adapter);

                            MknUtils.getListViewSize(topfive);
                        }


                    }else{

                        //check if token is invalid or not
                        if(response.body().getMessage().contains("Invalid Token") && !isLogoutDialogVisible){

                            isLogoutDialogVisible = true;

                            //show logout dialog
                            new MknUtils().invalidTokenLogoutUser(context, session, new MknUtils.Visitor() {
                                @Override
                                public int doJob(Bundle args) {

                                    Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();

                                    finishAffinity();

                                    return 0;
                                }
                            });


                        }else{

                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }

                }else{

                    Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ExamListResponse> call, Throwable t) {

                //hide progress dialog
                //progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });


    }



    //currency sumbol
    MyCurrency[] MyCurrencyAll ={
            new MyCurrency("$", "dollar sign"),
            new MyCurrency("¢", "cent sign"),
            new MyCurrency("£", "pound sign"),
            new MyCurrency("¤", "currency sign"),
            new MyCurrency("¥", "yen sign"),
            new MyCurrency("ƒ", "latin small letter f with hook"),
            new MyCurrency("", "afghani sign"),
            new MyCurrency("৲", "bengali rupee mark"),
            new MyCurrency("૱", "gujarati rupee sign"),
            new MyCurrency("௹", "tamil rupee sign"),
            new MyCurrency("฿", "thai currency symbol baht"),
            new MyCurrency("¤", "khmer currency symbol riel"),
            new MyCurrency("ℳ", "script capital m"),
            new MyCurrency("元", "cjk unified ideograph-5143"),
            new MyCurrency("円", "cjk unified ideograph-5186"),
            new MyCurrency("圆", "cjk unified ideograph-5706"),
            new MyCurrency("圓", "cjk unified ideograph-5713"),
            new MyCurrency("", "rial sign"),
            new MyCurrency("₠", "EURO-CURRENCY SIGN"),
            new MyCurrency("₡", "COLON SIGN"),
            new MyCurrency("₢", "CRUZEIRO SIGN"),
            new MyCurrency("₣", "FRENCH FRANC SIGN"),
            new MyCurrency("₤", "LIRA SIGN"),
            new MyCurrency("₥", "MILL SIGN"),
            new MyCurrency("₦", "NAIRA SIGN"),
            new MyCurrency("₧", "PESETA SIGN"),
            new MyCurrency("₨", "RUPEE SIGN"),
            new MyCurrency("₩", "WON SIGN"),
            new MyCurrency("₪", "NEW SHEQEL SIGN"),
            new MyCurrency("₫", "DONG SIGN"),
            new MyCurrency("€", "EURO SIGN"),
            new MyCurrency("₭", "KIP SIGN"),
            new MyCurrency("₮", "TUGRIK SIGN"),
            new MyCurrency("₯", "DRACHMA SIGN"),
            new MyCurrency("₰", "GERMAN PENNY SIGN"),
            new MyCurrency("₱", "PESO SIGN"),
            new MyCurrency("₲", "GUARANI SIGN"),
            new MyCurrency("₳", "AUSTRAL SIGN"),
            new MyCurrency("₴", "HRYVNIA SIGN"),
            new MyCurrency("₵", "CEDI SIGN"),
            new MyCurrency("₶", "LIVRE TOURNOIS SIGN"),
            new MyCurrency("₷", "SPESMILO SIGN"),
            new MyCurrency("₸", "TENGE SIGN"),
            new MyCurrency("₹", "INDIAN RUPEE SIGN"),
            new MyCurrency("₺", "TURKISH LIRA SIGN")
    };

















    //check is exam is pending

    private void checkRemaningExam(){

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<CheckExamResponse> call = apiService.checkExam(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<CheckExamResponse>() {
            @Override
            public void onResponse(Call<CheckExamResponse> call, final retrofit2.Response<CheckExamResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){

                            //show dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(response.body().getRemExamName());
                            builder.setCancelable(false);
                            builder.setMessage("Do you want to resume?");
                            //builder.setMessage(response.body().getMessage());

                            builder.setPositiveButton("Take Exam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //start exam activity
                                    Intent examActivity = new Intent(context, ExamListActivity1.class);
                                    examActivity.putExtra(ExamListActivity.KEY_PREVIOUS_EXAM_ID, response.body().getTestId());
                                    examActivity.putExtra(ExamListActivity.KEY_IS_PREVIOUS_EXAM, true);
                                    examActivity.putExtra(ExamListActivity.KEY_PREVIOUS_EXAM_NAME, response.body().getRemExamName());
                                    examActivity.putExtra(ExamListActivity.KEY_PREVIOUS_EXAM_MESSAGE, response.body().getMessage());

                                    startActivity(examActivity);

                                }
                            });


                            builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //dismiss();

                                }
                            });


                            builder.create().show();

                        }else{

                            //check if token is invalid or not
                            if(response.body().getMessage().contains("Invalid Token") && !isLogoutDialogVisible){

                                isLogoutDialogVisible = true;

                                //show logout dialog
                                new MknUtils().invalidTokenLogoutUser(context, session, new MknUtils.Visitor() {
                                    @Override
                                    public int doJob(Bundle args) {

                                        Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();

                                        finishAffinity();

                                        return 0;
                                    }
                                });

                            }

                            // show nothing
                           // Toast.makeText(context, "No pending exam", Toast.LENGTH_SHORT).show();
                        }


                    }else{

                        Toast.makeText(context, "POResponse body null", Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CheckExamResponse> call, Throwable t) {

                progressDialog.dismiss();

                t.printStackTrace();

            }
        });

    }



    public static final String KEY_TAKE_EXAM = "take_exam";
    public static final String KEY_EXAM_FROM_DASHBOARD = "from_dashboard";
    public static final String KEY_EXAM_ID_FROM_DASH = "exam_id_from_dashboard";

    private void pickExamAction(final Exam exam, final String examId) {

        final CharSequence colors[] = new CharSequence[] {"View Details", "Take Exam"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Pick a color");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]

                Intent examActivity = new Intent(context, ExamListActivity1.class);
                examActivity.putExtra(KEY_EXAM_FROM_DASHBOARD, true);
                examActivity.putExtra(KEY_EXAM_ID_FROM_DASH, examId);


                if(which == 0){
                    // details
                    examActivity.putExtra(KEY_TAKE_EXAM, false);

                }else if(which == 1){

                    //take exam
                    examActivity.putExtra(KEY_TAKE_EXAM, true);
                }

               startActivity(examActivity);
            }
        });
        builder.show();
    }


    //get home data

    //working with leader board
    private void getHomeData(int status) {

        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            //progressDialog.show();

            makeHomeDataAPiCall(status);

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void makeHomeDataAPiCall(final int status) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        // progressDialog.show();

        Call<HomePageResponse> call = apiService.getHomePageResponse("http://www.testkart.com/rest/Apis/home.json");

        call.enqueue(new Callback<HomePageResponse>() {
            @Override
            public void onResponse(Call<HomePageResponse> call, retrofit2.Response<HomePageResponse> response) {

                //progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if (response.body() != null) {

                        if(response.body().getStatus()){

                            if(status == 1){
                                //login = true

                                Intent i = new Intent(context, HomeCopyActivity.class);
                                i.putExtras(getIntent());
                                i.putExtra("HOME_DATA", response.body().getHome());
                                startActivity(i);

                            }else if(status == 2){
                                // login = false

                                Intent i = new Intent(context, Hello.class);
                                i.putExtras(getIntent());
                                i.putExtra("HOME_DATA", response.body().getHome());
                                startActivity(i);

                            }

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
                // progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });

    }

}
