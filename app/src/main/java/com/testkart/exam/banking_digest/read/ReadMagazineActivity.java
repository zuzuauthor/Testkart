package com.testkart.exam.banking_digest.read;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.testkart.exam.R;
import com.testkart.exam.banking_digest.read.model.MagazineContentResponse;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.helper.BaseActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by elfemo on 24/2/18.
 */

public class ReadMagazineActivity extends BaseActivity implements ReadFragment.ReadBookListener {

    public static final String KEY_INTENT_MAGID = "mag_id";
    public static final String KEY_INTENT_MAGNAME = "mag_name";
    public static final String KEY_INTENT_COVER_PAGE = "cover_page";

    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private String magId = "-1";
    private String coverImage = "";
    private DrawerLayout mDrawer;

    private ViewPager viewPager;
    private ImageView  btnNightMode, btnOverFlow;
    private FrameLayout btnDrawer, btnBack;
    private TextView viewChapterName;
   // private FloatingActionButton btnGoTop;
    private FloatingActionMenu btnGoTop;

    private FloatingActionButton fabCheeta;
    private FloatingActionButton fabRabbit;
    private FloatingActionButton fabTurtle;

    private FrameLayout topBar;

    private Animation slide_down ;

    private Animation slide_up ;

    private boolean hideTopbar = true;

    private TextView viewMagazineName;
    private ListView listChapters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_read_magazine);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        initViews();

    }

    private void initViews() {

        //init views
        //get magazine contents from api, update data set
        // set up drawer , viewpager

        slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.topbar_down);

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.topbar_up);

        magId = getIntent().getStringExtra(KEY_INTENT_MAGID);
        coverImage = getIntent().getStringExtra(KEY_INTENT_COVER_PAGE);

        sessionManager = new SessionManager(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        topBar = findViewById(R.id.topBar);
        mDrawer = findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(GravityCompat.START);
        btnDrawer = findViewById(R.id.btnDrawer);
       // btnNightMode = findViewById(R.id.btnNightMode);
        btnBack = findViewById(R.id.btnBack);
       // btnOverFlow = findViewById(R.id.btnOverFlow);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(0);
        viewChapterName = findViewById(R.id.viewChapterName);
        //btnGoTop = findViewById(R.id.btnGoTop);
        listChapters =  findViewById(R.id.listChapters);
        viewMagazineName = findViewById(R.id.viewMagazineName);

        btnGoTop = findViewById(R.id.btnGoTop);
        menuYellowListener();

        fabCheeta = findViewById(R.id.fabCheeta);
        fabRabbit = findViewById(R.id.fabRabbit);
        fabTurtle = findViewById(R.id.fabTurtle);
        fabTurtleListener();
        fabRabbitListener();
        fabCheetaListener();

        viewMagazineName.setText((getIntent().getStringExtra(KEY_INTENT_MAGNAME) != null)? getIntent().getStringExtra(KEY_INTENT_MAGNAME):"NA");

        viewPagerListener();
        listChaptersListener();
        //btnGoTopListener();
        btnDrawerListener();
        getMagazineContent();
        btnBackListener();
        mDrawerListener();


    }

    private void fabCheetaListener() {

        fabCheeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapter.updateData(1);
                btnGoTop.close(true);

            }
        });
    }

    private void fabRabbitListener() {

        fabRabbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapter.updateData(2);
                btnGoTop.close(true);
            }
        });
    }

    private void fabTurtleListener() {

        fabTurtle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapter.updateData(3);
                btnGoTop.close(true);
            }
        });
    }

    private void menuYellowListener() {

        btnGoTop.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                String text;
                if (opened) {
                    text = "Menu opened";
                } else {
                    text = "Menu closed";
                }

               // Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mDrawerListener() {

        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

                btnGoTop.setVisibility(View.GONE);

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

                btnGoTop.setVisibility(View.VISIBLE);

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    private void btnBackListener() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void viewPagerListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ReadFragment fragment = (ReadFragment) getSupportFragmentManager().findFragmentById(R.id.viewPager);
                fragment.onScrollTopPage();
            }

            @Override
            public void onPageSelected(int position) {

                viewChapterName.setText(chapterArrayList.get(position));
                /*listChapters.getChildAt(position).setBackgroundColor(
                        Color.parseColor("#00743D"));*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void listChaptersListener() {

        listChapters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                checkDrawer();
                viewPager.setCurrentItem(i, true);

            }
        });
    }

    /*private void btnGoTopListener() {

        btnGoTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapter.updateData();

            }
        });

    }*/


    private void checkDrawer(){

        if(mDrawer.isDrawerOpen(GravityCompat.START)){

            mDrawer.closeDrawer(GravityCompat.START);

             btnGoTop.setVisibility(View.VISIBLE);

        }else{

            mDrawer.openDrawer(GravityCompat.START);
            btnGoTop.setVisibility(View.GONE);

        }
    }

    private void btnDrawerListener() {

        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkDrawer();

            }
        });

    }

    private void getMagazineContent() {

        if (ApiClient.isNetworkAvailable(this)) {

            if(!magId.equalsIgnoreCase("-1")){

                //show progress dialog
                progressDialog.show();

                makeAPiCall();
            }


        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }



    ArrayList<String> chapterArrayList = new ArrayList<>();
    private MagazineContentPager adapter;
    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Log.v("magId", magId);

        Call<MagazineContentResponse> call = apiService.getMagazineContent(sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC),sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC),  magId);

        call.enqueue(new Callback<MagazineContentResponse>() {
            @Override
            public void onResponse(Call<MagazineContentResponse> call, Response<MagazineContentResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){

                            //check if packages are empty are empty
                            if(!response.body().getResponse().isEmpty()){

                                hideTopbar = true;
                                topBar.startAnimation(slide_up);
                                topBar.setVisibility(View.GONE);
                                btnGoTop.setVisibility(View.VISIBLE);

                                //update chapters
                                com.testkart.exam.banking_digest.read.model.Response  res = response.body().getResponse().get(0);

                                for (com.testkart.exam.banking_digest.read.model.Response  page : response.body().getResponse()){

                                    chapterArrayList.add(page.getTitle());
                                }

                                chapterArrayList.add(0, "Cover Page");

                                viewChapterName.setText(chapterArrayList.get(0));

                                ArrayAdapter<String> chapterAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, chapterArrayList);
                                listChapters.setAdapter(chapterAdapter);

                                //setup view pager
                                adapter = new MagazineContentPager(getSupportFragmentManager(), (ArrayList<com.testkart.exam.banking_digest.read.model.Response>) response.body().getResponse(), context, coverImage);
                                viewPager.setAdapter(adapter);

                            }else {


                            }


                        }else{

                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{

                        Toast.makeText(context, "POResponse body is null", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(context, "ERROR RESPONSE CODE: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MagazineContentResponse> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });

    }


    @Override
    public void onBackPressed() {

        if(mDrawer.isDrawerOpen(GravityCompat.START)){

            mDrawer.closeDrawer(GravityCompat.START);

        }else{

            super.onBackPressed();
        }
    }

    @Override
    public void onSingleTap() {

        if(hideTopbar){

            hideTopbar = false;
            topBar.setVisibility(View.VISIBLE);
            topBar.startAnimation(slide_down);


        }else{

            hideTopbar = true;
            topBar.startAnimation(slide_up);
            topBar.setVisibility(View.GONE);
        }



    }


    /*@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mDrawer.isDrawerOpen(mRightDrawerListView)) {

                View content = findViewById(R.id.right_drawer);
                int[] contentLocation = new int[2];
                content.getLocationOnScreen(contentLocation);
                Rect rect = new Rect(contentLocation[0],
                        contentLocation[1],
                        contentLocation[0] + content.getWidth(),
                        contentLocation[1] + content.getHeight());

                View toolbarView = findViewById(R.id.toolbar);
                int[] toolbarLocation = new int[2];
                toolbarView.getLocationOnScreen(toolbarLocation);
                Rect toolbarViewRect = new Rect(toolbarLocation[0],
                        toolbarLocation[1],
                        toolbarLocation[0] + toolbarView.getWidth(),
                        toolbarLocation[1] + toolbarView.getHeight());


                if (!(rect.contains((int) event.getX(), (int) event.getY())) && !toolbarViewRect.contains((int) event.getX(), (int) event.getY())) {
                    isOutSideClicked = true;
                } else {
                    isOutSideClicked = false;
                }

            } else {
                return super.dispatchTouchEvent(event);
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN && isOutSideClicked) {
            isOutSideClicked = false;
            return super.dispatchTouchEvent(event);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE && isOutSideClicked) {
            return super.dispatchTouchEvent(event);
        }

        if (isOutSideClicked) {
            //make http call/db request
            Toast.makeText(this, "Hello..", Toast.LENGTH_SHORT).show();
        }
        return super.dispatchTouchEvent(event);
    }*/


}
