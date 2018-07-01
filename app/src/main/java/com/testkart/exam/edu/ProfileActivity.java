package com.testkart.exam.edu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.testkart.exam.R;
import com.testkart.exam.edu.helper.FileUploader;
import com.testkart.exam.edu.helper.MknUtils;
import com.testkart.exam.edu.helper.MyGlideEngine;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.profile.EditProfileDIalog;
import com.testkart.exam.edu.profile.OnProfileEditListener;
import com.testkart.exam.edu.profile.PasswordResetDialog;
import com.testkart.exam.edu.profile.ProfileResponse;
import com.testkart.exam.edu.register.ResendModel;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.helper.DialogHelper;
import com.testkart.exam.testkart.contact_us.ContactUsNativeActivity;
import com.testkart.exam.testkart.policies.PoliciesActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by testkart on 15/4/17.
 */

/*

 1 Native
 2 display user info

 */

public class ProfileActivity extends AppCompatActivity implements OnProfileEditListener, FileUploader.OnImageUplaodListener, View.OnClickListener {

    public static boolean updatePic = false;

    ImagePicker imagePicker;

    private Uri destinationUri;

    private Context context = this;
    private SessionManager session;

    private TextView welcomeStudentName, viewName, viewEmail, viewRollNo, viewPhone, viewGroup, viewLogout, viewPhoneAlt, viewAdmissionDate;
    private CircleImageView viewProfilePic;

    private TextView viewEditProfile;

    private ProgressDialog progressDialog;


    private LinearLayout proDisclaimer, proRefundPolicy, proPrivacyPolicy, proUserPolicy, proRateApp1, proMoreApps;
    private LinearLayout proTermOfUse, proAboutUs, proLiveChat, proContactUs, proClearData;
    private LinearLayout proFeedback, proRateApp;


    private ImageView backButton;

    private TextView rateApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //initialization
        initialization();
    }

    private void initialization() {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/edu";
        File createDir = new File(root+"profile"+ File.separator);
        if(!createDir.exists()) {
            createDir.mkdir();
        }

        destinationUri.parse(createDir.getAbsolutePath());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        session = new SessionManager(context);

        imagePicker = new ImagePicker();
        imagePicker.setTitle("Camera");
        imagePicker.setCropImage(false);

        viewProfilePic = (CircleImageView) findViewById(R.id.viewProfilePic);
        viewName = (TextView) findViewById(R.id.viewName);
        viewEmail= (TextView) findViewById(R.id.viewEmail);
        viewRollNo = (TextView) findViewById(R.id.viewRollNo);
        viewPhone = (TextView) findViewById(R.id.viewPhone);
        viewGroup = (TextView) findViewById(R.id.viewGroup);
        viewLogout = (TextView) findViewById(R.id.viewLogout);
        welcomeStudentName = (TextView)findViewById(R.id.welcomeStudentName);

        rateApp = (TextView) findViewById(R.id.rateApp);

        rateAppListener();

        viewPhoneAlt  = (TextView) findViewById(R.id.viewPhoneAlt);
        viewAdmissionDate  = (TextView) findViewById(R.id.viewAdmissionDate);

        viewEditProfile = (TextView) findViewById(R.id.viewEditProfile);
        viewEditProfileListener();

        //viewProfilePicListener();

        //enable once login process start working
        updateProfileInformation();

        viewLogoutLisitener();



        proDisclaimer = (LinearLayout)findViewById(R.id.proDisclaimer);
        proRefundPolicy = (LinearLayout)findViewById(R.id.proRefundPolicy);
        proPrivacyPolicy = (LinearLayout)findViewById(R.id.proPrivacyPolicy);
        proUserPolicy = (LinearLayout)findViewById(R.id.proUserPolicy);
        proRateApp1 = (LinearLayout)findViewById(R.id.proRateApp1);
        proMoreApps = (LinearLayout)findViewById(R.id.proMoreApps);

        proMoreAppsListener();
        proRateApp1.setOnClickListener(this);
        proDisclaimer.setOnClickListener(this);
        proRefundPolicy.setOnClickListener(this);
        proPrivacyPolicy.setOnClickListener(this);
        proUserPolicy.setOnClickListener(this);

        proTermOfUse = (LinearLayout)findViewById(R.id.proTermOfUse);
        proAboutUs = (LinearLayout)findViewById(R.id.proAboutUs);
        proLiveChat = (LinearLayout)findViewById(R.id.proLiveChat);
        proContactUs = (LinearLayout)findViewById(R.id.proContactUs);
        proClearData = (LinearLayout)findViewById(R.id.proClearData);

        proClearData.setOnClickListener(this);
        proTermOfUse.setOnClickListener(this);
        proAboutUs.setOnClickListener(this);
        proLiveChat.setOnClickListener(this);
        proContactUs.setOnClickListener(this);


        proFeedback = (LinearLayout)findViewById(R.id.proFeedback);
        proRateApp = (LinearLayout)findViewById(R.id.proRateApp);
        proFeedback.setOnClickListener(this);
        proRateApp.setOnClickListener(this);


        backButton = (ImageView)findViewById(R.id.backButton);
        backButtonListener();

    }

    private void proMoreAppsListener() {

        proMoreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "Testkart";//getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id="+appPackageName)));
                }
            }
        });
    }

    private void rateAppListener() {

        rateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogHelper.openPlayStoreToRate(context);

            }
        });
    }

    private void backButtonListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }

    private void viewEditProfileListener() {

        viewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickProfileEditAction();

            }
        });
    }



    private int REQUEST_CODE_CHOOSE = 999;
    private void viewProfilePicListener() {


        viewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickExamAction();

            }
        });


    }



    private void pickExamAction() {

        final CharSequence colors[] = new CharSequence[] {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Pick a color");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]

                if(which == 0){

                    //camera

                    isCamera = true;

                    imagePicker.startCamera(ProfileActivity.this, new ImagePicker.Callback() {

                        @Override
                        public void onPickImage(Uri imageUri) {

                            Log.v("Camera image uri: ", imageUri.getPath());

                           // Log.v("Matisse", "mSelected: " + mSelected);

                            CropImage.activity(imageUri)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .start(ProfileActivity.this);

                        }


                        /*@Override public void onCropImage(Uri imageUri) {

                            ProfileActivity.this.resultUri = resultUri;

                            //make api request

                            final ArrayList<String> imageData = new ArrayList<>();
                            imageData.add(resultUri.getPath());

                            if (ApiClient.isNetworkAvailable(ProfileActivity.this)) {

                                progressDialog.show();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        uploadFile(imageData);

                                    }
                                }).start();
                            }else{

                                Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                            }


                        }*/

                    });

                }else{

                    //gallery
                    Matisse.from(ProfileActivity.this)
                            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                            .countable(true)
                            .maxSelectable(1)
                            //.gridExpectedSize(getResources().getDimensionPixelSize(16))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new MyGlideEngine())
                            .forResult(REQUEST_CODE_CHOOSE);

                }
            }
        });
        builder.show();
    }




    List<Uri> mSelected;

    private Uri resultUri;

    private boolean isCamera = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(isCamera){

            isCamera = false;

            imagePicker.onActivityResult(this, requestCode, resultCode, data);
        }else{


            if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

                mSelected = Matisse.obtainResult(data);

                Log.v("Matisse", "mSelected: " + mSelected);

                CropImage.activity(mSelected.get(0))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

            }


            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    Uri resultUri = result.getUri();

                    this.resultUri = resultUri;

                    //make api request

                    final ArrayList<String> imageData = new ArrayList<>();
                    imageData.add(resultUri.getPath());

                    if (ApiClient.isNetworkAvailable(this)) {

                        progressDialog.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                uploadFile(imageData);

                            }
                        }).start();
                    }else{

                        Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }





                    //uploadImage(resultUri);


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                    Exception error = result.getError();
                    error.printStackTrace();

                }
            }

        }


    }



    //--------------------

    public void uploadFile(ArrayList<String> imgPaths) {

        String charset = "UTF-8";
        //File uploadFile1 = new File("e:/Test/PIC1.JPG");
        //File uploadFile2 = new File("e:/Test/PIC2.JPG");

        File sourceFile[] = new File[imgPaths.size()];
        for (int i=0;i<imgPaths.size();i++){
            sourceFile[i] = new File(imgPaths.get(i));
            // Toast.makeText(getApplicationContext(),imgPaths.get(i),Toast.LENGTH_SHORT).show();
        }

        String requestURL = "http://www.testkart.com/rest/Profiles/changePhoto.json";

        try {

            FileUploader multipart = new FileUploader(context, requestURL, charset);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

            multipart.addFormField("public_key", session.getUserDetails().get(SessionManager.KEY_PUBLIC));
            multipart.addFormField("private_key", session.getUserDetails().get(SessionManager.KEY_PRIVATE));

            for (int i=0;i<imgPaths.size();i++){
                multipart.addFilePart("data[photo]", sourceFile[i]);
            }

            /*multipart.addFilePart("fileUpload", uploadFile1);
            multipart.addFilePart("fileUpload", uploadFile2);*/

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }




    //----------------------------


    private void uploadImage(final Uri resultUri) {

        //refresh profile view
        if (ApiClient.isNetworkAvailable(this)) {

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            //show progress dialog
            progressDialog.show();

            File file = new File(resultUri.getPath());

            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData("data[photo]", file.getName(), requestFile);
           // httpPost.setHeader("Content-type", "multipart/form-data; boundary="+boundary);

            RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            RequestBody publicKey = RequestBody.create(MediaType.parse("text/plain"), session.getUserDetails().get(SessionManager.KEY_PUBLIC));
            RequestBody privateKey = RequestBody.create(MediaType.parse("text/plain"), session.getUserDetails().get(SessionManager.KEY_PRIVATE));

            Call<ResendModel> call = apiService.uploadImage(body, publicKey, privateKey);

            call.enqueue(new Callback<ResendModel>() {
                @Override
                public void onResponse(Call<ResendModel> call, Response<ResendModel> response) {

                    progressDialog.dismiss();

                    int code = response.code();

                    if(code == 200){

                        if(response.body() != null){

                            if(response.body().getStatus()){

                                Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                viewProfilePic.setImageURI(resultUri);

                                //update profile
                                makeAPiCall(true);

                            }else{

                                Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }else{

                            Toast.makeText(ProfileActivity.this, "POResponse body null", Toast.LENGTH_SHORT).show();
                        }

                    }else{


                    }

                }

                @Override
                public void onFailure(Call<ResendModel> call, Throwable t) {

                    progressDialog.dismiss();
                    t.printStackTrace();
                }
            });


        } else {

            Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }


    private void updateProfileInformation() {

        HashMap<String, String> user = session.getUserDetails();

        if(user.get(SessionManager.KEY_PROFILE_IMAGE) != null && !user.get(SessionManager.KEY_PROFILE_IMAGE).isEmpty()){

            //load profile image
            Glide.with(context)
                    .load(user.get(SessionManager.KEY_PROFILE_IMAGE))
                   // .placeholder(R.raw.default_profile)
                    .into(viewProfilePic);
        }



        welcomeStudentName.setText("Hi, "+user.get(SessionManager.KEY_NAME));
        viewName.setText(user.get(SessionManager.KEY_NAME));
        viewEmail.setText(user.get(SessionManager.KEY_EMAIL));
        viewRollNo.setText(user.get(SessionManager.KEY_STUDENT_ENROLL));
        viewPhone.setText(user.get(SessionManager.KEY_PHONE_NUMBER));
        viewPhoneAlt.setText(user.get(SessionManager.KEY_ALTERNATE_NO));

        String admissionDate = user.get(SessionManager.KEY_ADMISSION_DATE).split(" ")[0];
        if(admissionDate != null){

            String formattedDate = MknUtils.getFormatDate(admissionDate, "yyyy-MM-dd", "dd-MM-yyyy");
            viewAdmissionDate.setText((formattedDate != null)? formattedDate:"");

        }

       // viewAdmissionDate.setText(user.get(SessionManager.KEY_ADMISSION_DATE));
        viewGroup.setText(user.get(SessionManager.KEY_TAGS));

    }

    private void viewLogoutLisitener() {

        viewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Testkart");
                builder.setMessage("Are you sure you want to logout?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // logout session and clear session
                        session.logoutUser(false);
                        finish();
                        MainActivity.mainActivity.finish();

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
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    private void pickProfileEditAction() {

        final CharSequence colors[] = new CharSequence[] {"Change Photo", "Change Mobile Number", "Change Password"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Pick a color");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]

                if(which == 0){

                    pickExamAction();

                } else if(which == 1){

                    // edit profile
                    EditProfileDIalog epd = new EditProfileDIalog();

                    epd.show(getSupportFragmentManager(), "EPD");


                }else if(which == 2){

                    //change profile
                    PasswordResetDialog prd = new PasswordResetDialog();
                    prd.show(getSupportFragmentManager(), "PRD");

                }
            }
        });
        builder.show();
    }

    @Override
    public void onProfileEdit() {

        Toast.makeText(context, "Profile Edit successfully", Toast.LENGTH_SHORT).show();

        //refresh profile view
        if (ApiClient.isNetworkAvailable(this)) {

            //show progress dialog
            progressDialog.show();

            makeAPiCall(false);

        } else {

            Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }


    }

    private void makeAPiCall(final boolean fromImage) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<ProfileResponse> call = apiService.getUserProfile(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));


        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {


                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body().getStatus()){

                        Log.v("Student:", response.body().getResponse().getStudent().toString());

                        Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        String studenName = session.getUserDetails().get(SessionManager.KEY_NAME);
                        String studentId = response.body().getResponse().getStudent().getId();
                        String email = session.getUserDetails().get(SessionManager.KEY_EMAIL);
                        String publicKey = session.getUserDetails().get(SessionManager.KEY_PUBLIC);
                        String privateKey = session.getUserDetails().get(SessionManager.KEY_PRIVATE);
                        String currencyCode = session.getUserDetails().get(SessionManager.KEY_CURRENCY);
                        String paidExam = session.getUserDetails().get(SessionManager.KEY_PAID_EXAM);
                        String certificate = session.getUserDetails().get(SessionManager.KEY_CERTIFICATE);
                        String admissionDate = session.getUserDetails().get(SessionManager.KEY_ADMISSION_DATE);

                        String tags = session.getUserDetails().get(SessionManager.KEY_TAGS);


                        HashMap<String, String> userInfo = new HashMap<String, String>();

                        userInfo.put(SessionManager.KEY_ALTERNATE_NO, response.body().getResponse().getStudent().getGuardianPhone());
                        userInfo.put(SessionManager.KEY_STUDENT_ENROLL, response.body().getResponse().getStudent().getEnroll());
                        userInfo.put(SessionManager.KEY_PHONE_NUMBER, response.body().getResponse().getStudent().getPhone());
                        userInfo.put(SessionManager.KEY_PROFILE_IMAGE, response.body().getStudentImage());
                        userInfo.put(SessionManager.KEY_ADDRESS, response.body().getResponse().getStudent().getAddress());

                        Log.v("profile image", response.body().getStudentImage());

                        userInfo.put(SessionManager.KEY_STUDENT_ID, studentId);
                        userInfo.put(SessionManager.KEY_NAME, studenName);
                        userInfo.put(SessionManager.KEY_EMAIL, email);
                        //userInfo.put(SessionManager.KEY_ACCOUNT_BLANCE, response.body().getUser().getStudent().ba);
                        userInfo.put(SessionManager.KEY_PRIVATE, privateKey);
                        userInfo.put(SessionManager.KEY_PUBLIC, publicKey);
                        userInfo.put(SessionManager.KEY_CURRENCY, currencyCode);
                        userInfo.put(SessionManager.KEY_PAID_EXAM, paidExam);
                        userInfo.put(SessionManager.KEY_CERTIFICATE, certificate);
                        userInfo.put(SessionManager.KEY_ADMISSION_DATE, admissionDate);
                        userInfo.put(SessionManager.KEY_TAGS, tags);

                        session.createLoginSession(userInfo);

                        updateProfileInformation();

                        if(fromImage){

                            //update drawer image
                            updatePic = true;
                        }

                    }else{

                        Toast.makeText(ProfileActivity.this, "Update details failed. Please re login", Toast.LENGTH_SHORT).show();
                    }

                }

            }


            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {

                progressDialog.dismiss();

                t.printStackTrace();

            }
        });

    }

    /*private void updateView(ProfileResponse body) {


    }*/

    @Override
    public void onPasswordReset() {

        Toast.makeText(context, "Password reset successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onImageUpload(String response) {

        progressDialog.dismiss();

        try{

            JSONObject jsonObject = new JSONObject(response);

            boolean status = jsonObject.getBoolean("status");
            final String message = jsonObject.getString("message");


            if(status){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        viewProfilePic.setImageURI(resultUri);
                    }
                });


                //update profile
                makeAPiCall(true);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });


            }else{

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }catch (Exception e){

            e.printStackTrace();
        }



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            //user policies
            case R.id.proDisclaimer:

                callPolicies("Disclaimer", "http://www.testkart.com/Contents/pages/DisclaimerM");

                break;

            case R.id.proRefundPolicy:

                callPolicies("Refund Policy", "http://www.testkart.com/Contents/pages/RefundPolicyM");

                break;


            case R.id.proPrivacyPolicy:

                callPolicies("Privacy Policy", "http://www.testkart.com/Contents/pages/PrivacyPolicyM");

                break;


            case R.id.proUserPolicy:

                callPolicies("User Policy", "http://www.testkart.com/Contents/pages/UserPolicyM");

                break;


            //support
            case R.id.proTermOfUse:

                callPolicies("Terms of Use", "http://www.testkart.com/Contents/pages/TermsOfUseM");

                break;

            case R.id.proAboutUs:

                callPolicies("About Us", "http://www.testkart.com/Contents/pages/aboutusm");

                break;


            case R.id.proClearData:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Testkart");
                builder.setMessage("Are you sure you want to clear your app's data and cache?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // logout session and clear session
                        session.logoutUser(true);
                        finish();
                        MainActivity.mainActivity.finish();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //do nothing

                    }
                });

                builder.create().show();


                break;


            case R.id.proContactUs:

                Intent contactUsIntent = new Intent(this, ContactUsNativeActivity.class);
                startActivity(contactUsIntent);

                break;


            case R.id.proFeedback:

                sendEmail();

                /*Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"admin@testkart.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Testkart Android app Feedback");
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

*/
                break;



            case R.id.proRateApp1:

                //DialogHelper.openPlayStoreToRate(context);

                break;

        }

    }



    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"admin@testkart.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Testkart Android app Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            //Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ProfileActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    private void callPolicies(String policyName, String policyUrl){

        Intent policyIntent = new Intent(context, PoliciesActivity.class);
        policyIntent.putExtra(PoliciesActivity.KEY_POLICY_NAME, policyName);
        policyIntent.putExtra(PoliciesActivity.KEY_POLICY_URL, policyUrl);
        startActivity(policyIntent);

        //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }
}