package com.testkart.exam.packages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.testkart.exam.R;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.MknUtils;
import com.testkart.exam.packages.model.DataOrderSummary;
import com.tuyenmonkey.textdecorator.TextDecorator;

import java.util.ArrayList;

import berlin.volders.badger.BadgeShape;
import berlin.volders.badger.Badger;
import berlin.volders.badger.CountBadge;

/**
 * Created by elfemo on 25/8/17.
 */

public class PackageDetailActivity extends AppCompatActivity {

    public static final String KEY_PACKAGE_DETAILS = "pack_details";

    private DBHelper dbHelper;

    private Context context = this;

    private ImageView shoppingBagView;
    private ImageView toolbarImage;
    private ImageView shareIcon;

    private TextView dgTxtPackageName, dgTxtAmount, dgTxtPackageExpiry, dgTxtExams;
   // private TextView info_text1;
    private WebView productDescription;

    private Button addToCartBtn, buyNowBtn;
    private DataOrderSummary ds;

    private ProgressBar progressBar;

    private FrameLayout backButton;


    private ShareDialog shareDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_package_details1);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Package Details");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }*/


        initViews();

    }

    private void initViews() {

        dbHelper = new DBHelper(this);

        shareDialog = new ShareDialog(this);

        ds = (DataOrderSummary) getIntent().getSerializableExtra(KEY_PACKAGE_DETAILS);

        circleFactory = new CountBadge.Factory(this, BadgeShape.circle(.6f, Gravity.END | Gravity.TOP));

        dgTxtPackageName = (TextView) findViewById(R.id.dgTxtPackageName);
        dgTxtAmount = (TextView) findViewById(R.id.dgTxtAmount);
        dgTxtPackageExpiry = (TextView) findViewById(R.id.dgTxtPackageExpiry);
        dgTxtExams = (TextView) findViewById(R.id.dgTxtExams);

        shoppingBagView = (ImageView) findViewById(R.id.shoppingBagView);
        shoppingBagViewListener();

        toolbarImage = (ImageView) findViewById(R.id.toolbarImage);
        toolbarImage.setDrawingCacheEnabled(true);
        shareIcon = (ImageView) findViewById(R.id.shareIcon);
        shareIconListener();

        productDescription = (WebView)findViewById(R.id.productDescription);
        loadSetting();

        //info_text1 = (TextView) findViewById(R.id.info_text1);

        addToCartBtn = (Button) findViewById(R.id.addToCartBtn);
        buyNowBtn = (Button) findViewById(R.id.buyNowBtn);


        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        backButton = (FrameLayout)findViewById(R.id.backButton);
        backButtonListener();

        addToCartBtnListener();
        buyNowBtnListener();

        //updateViews
        updateViews(ds);


    }

    private void backButtonListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }



    private void shareViaFacebook(){

        String descreption = "Check this out \n"+ds.getPackageName()+" "+
                "http://www.testkart.com/Packages/singleproduct/"+ds.getPackageId()+"  on Testkart";

        //share on fb
        if(ShareDialog.canShow(ShareLinkContent.class)){


            String openWebUrl = "http://www.testkart.com/Packages/singleproduct/"+ds.getPackageId();
            Log.v("OPEN_URL", openWebUrl);

            String imageUrl = "http://www.testkart.com/img/package/" + ds.getPackageImage();
            Log.v("OPEN_URL", imageUrl);


            // Create an object
            /*ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                    .putString("og:url", openWebUrl)
                    .putString("og:type", "website")
                    .putString("og:title", ds.getPackageName())
                    .putString("og:description", descreption)
                    .putString("og:image", imageUrl)
                    .build();


            // Create an action
            ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                    .setActionType("og.likes")
                    .putObject("book", object)
                    .build();

            // Create the content
            ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                    .setPreviewPropertyName("book")
                    .setAction(action)
                    .build();

            ShareDialog.show(PackageDetailActivity.this, content);*/




            /*ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Testkart")
                    .setContentDescription(descreption)
                     .setContentUrl(Uri.parse("http://www.testkart.com/Packages/singleproduct/"+ds.getPackageId()))
                    .setImageUrl(Uri.parse("http://www.testkart.com/img/package/" + ds.getPackageImage()))
                    .build();*/

            Log.v("De_Bug", "http://www.testkart.com/Packages/singleproduct/5");

            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://www.testkart.com/Packages/singleproduct/5"))
                    .build();

            shareDialog.show(content);

        }else{

            Toast.makeText(context, "Unable to share Image", Toast.LENGTH_SHORT).show();
        }

    }


    private void shareViaWhatsApp(){

        try{

            Bitmap bmap = toolbarImage.getDrawingCache();

            String descreption = "Check this out \n"+ds.getPackageName()+" "+
                    " http://www.testkart.com/Packages/singleproduct/"+ds.getPackageId()+"  on Testkart";

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, descreption);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bmap, "", null);
            Uri screenshotUri = Uri.parse(path);

            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image*//*");
            intent.setPackage("com.whatsapp");
            //startActivity(Intent.createChooser(intent, "Share"));
            startActivity(intent);

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    private void shareIconListener() {

        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] shareOptions = {"WhatsApp",
                "Facebook"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Share via...")
                        .setItems(shareOptions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                if(which == 0){
                                    //whatsapp
                                    shareViaWhatsApp();

                                }else{

                                    //facebook
                                    shareViaFacebook();
                                }

                            }
                        });

                builder.create().show();

            }
        });
    }

    private void buyNowBtnListener() {

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add item to cart
                dbHelper.addCartItem(ds);

                Intent cartActivity = new Intent(context, ShoppingCartActivity.class);
                startActivity(cartActivity);
                finish();
            }
        });
    }

    private void addToCartBtnListener() {

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                counter++;

                Badger.sett(shoppingBagView, circleFactory).setCount(counter);

                //add item to cart
                dbHelper.addCartItem(ds);

                showCountinueShoppingDialog();
            }
        });

    }

    private void showCountinueShoppingDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("My Cart");
        builder.setMessage("Exam "+ds.getPackageName()+" added successfully!");
        builder.setPositiveButton("Continue Shopping", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                PackageListActivity.KEY_CART_CHANGED = true;

                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);

               finish();
            }
        });

        builder.setNegativeButton("Visit Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent cartActivity = new Intent(context, ShoppingCartActivity.class);
                startActivity(cartActivity);
                finish();

            }
        });

        AlertDialog d = builder.create();
        d.setCancelable(false);
        d.show();
    }

    private void shoppingBagViewListener() {

        shoppingBagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int counter = dbHelper.getTotalCartItems().size();

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

    private void loadSetting() {

        WebSettings webSettings = productDescription.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // webSettings.setTextZoom(webSettings.getTextZoom() + 10);

        productDescription.setWebChromeClient(new WebChromeClient());

    }

    private void updateViews(DataOrderSummary ds) {

        if (ds != null) {

            Log.v("Pack_details", "http://www.testkart.com/img/package/" + ds.getPackageImage());

            Glide.with(context)
                        /*.load(orderItem.getPackageImage())*/
                    .load("http://www.testkart.com/img/package/" + ds.getPackageImage())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            //mDilatingDotsProgressBar.hideNow();
                            toolbarImage.setImageResource(R.drawable.no_image_available_150);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);

                            return false;
                        }
                    })
                    .apply(new RequestOptions().fitCenter())
                    .into(toolbarImage);


            dgTxtPackageName.setText(ds.getPackageName());

            if ((int) Float.parseFloat(ds.getPackageSHowAmount()) == 0) {

                TextDecorator
                        .decorate(dgTxtAmount, /*packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount()+" "+*/ds.getPackageCurrencyCode() + ds.getPackageAmount())
                        //.setTextColor(R.color.red, packItem.getPackageSHowAmount())
                        //.strikethrough(packItem.getPackageCurrencyCode()+packItem.getPackageSHowAmount())
                        .setTextColor(R.color.positive_green, ds.getPackageCurrencyCode() + ds.getPackageAmount()).build();


            } else {

                TextDecorator
                        .decorate(dgTxtAmount, ds.getPackageCurrencyCode() + ds.getPackageSHowAmount() + " " + ds.getPackageCurrencyCode() + ds.getPackageAmount())
                        .setTextColor(R.color.red, ds.getPackageSHowAmount())
                        .strikethrough(ds.getPackageCurrencyCode() + ds.getPackageSHowAmount())
                        .setTextColor(R.color.positive_green, ds.getPackageCurrencyCode() + ds.getPackageAmount()).build();

            }

            // dgTxtAmount.setText(orderItem.getPackageAmount());

            String date = MknUtils.getFormatDate(ds.getPackageExpiryDays(), "yyyy-MM-dd", "dd-MM-yyyy");

            dgTxtPackageExpiry.setText((date != null)? "Expires On: "+date:"");
            dgTxtExams.setText(ds.getPackageExamName());

            String q = MknUtils.changedHeaderHtml(ds.getPackageDescription());

            productDescription.loadUrl("");
            productDescription.loadData(q, "text/html", "UTF-8");

            //info_text1.setText(Html.fromHtml(ds.getPackageDescription()));


        }else{

            //unable to load product details. please go back and try again
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Unable to load product details. please go back and try again!");
            builder.setCancelable(false);
            builder.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finish();

                }
            });

            builder.create().show();
        }

    }


    private int counter = 0;
    private CountBadge.Factory circleFactory;

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<DataOrderSummary> cartItemsList = dbHelper.getTotalCartItems();
        counter = cartItemsList.size();
        Badger.sett(shoppingBagView, circleFactory).setCount(counter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
