package com.testkart.exam.edu;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linchaolong.android.imagepicker.ImagePicker;
import com.testkart.exam.R;
import com.testkart.exam.edu.helper.FileUploader;
import com.testkart.exam.edu.helper.MknUtils;
import com.testkart.exam.edu.helper.MyGlideEngine;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.register.GroupModel;
import com.testkart.exam.edu.register.RegisterPostModel;
import com.testkart.exam.edu.register.RegisterResponse;
import com.testkart.exam.edu.register.Responses;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.thefinestartist.utils.content.Ctx;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.gujun.android.taggroup.TagGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by testkart on 10/4/17.
 */


/*

 1 view initialization, network check, validation
 2 make signup api call. this api call also allow to end OTP to registred email id, while sending check if user already registred or not
   if user new then insert new record, else if user not new but not active then update its new info other then email id
 3 check response true / false
 4 true, check user_status : 0/1/2, 1 for active and 0 for inactive and 2 a fresh registration
 5 if 1 then show error message: this email already registred with us.
 6 if 0 bundle email and password then go to otp screen
 7 if 2 bundle email and password then go to otp screen

*/

public class SignupActivity extends AppCompatActivity implements Validator.ValidationListener, FileUploader.OnImageUplaodListener {

    private Context context = this;
    private ProgressDialog progressDialog;

    private Validator validator;

    @NotEmpty
    private EditText viewInputName;

    @NotEmpty
    @Email
    private EditText viewInputEmail;

    @Password(min = 1) //, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS
    private ShowHidePasswordEditText viewInputPassword;

    @ConfirmPassword
    private ShowHidePasswordEditText viewConfirmPassword;

    @NotEmpty
    @Pattern(regex = "[789][0-9]{9}", message = "Please provide a valid 10 digit mobile number.")
    private EditText viewInputPhone;

    @NotEmpty
    private EditText viewInputAddress;

    private TagGroup mTagGroup;

    private TextView viewSelectGroup;

    private Button viewButtonSignup;

    private ScrollView  scrollContainer;

    private CircleImageView viewProfilePic;
    private ImagePicker imagePicker;
    List<Uri> mSelected = new ArrayList<>();

    private Uri resultUri;

    private boolean isCamera = false;
    private int REQUEST_CODE_CHOOSE = 999;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup1);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //initialization
        initialization();

    }

    private void initialization() {

        scrollContainer = (ScrollView)findViewById(R.id.scrollContainer);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating your Account");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        validator = new Validator(this);
        validator.setValidationListener(this);

        String primaryEmail = getPrimaryEmailAddress();

        viewInputName = (EditText)findViewById(R.id.viewInputName);
        viewInputEmail = (EditText)findViewById(R.id.viewInputEmail);
        viewInputPassword = (ShowHidePasswordEditText)findViewById(R.id.viewInputPassword);
        viewConfirmPassword = (ShowHidePasswordEditText)findViewById(R.id.viewConfirmPassword);
        viewInputAddress = (EditText)findViewById(R.id.viewInputAddress);
        viewInputPhone = (EditText) findViewById(R.id.viewInputPhone);

        viewProfilePic = (CircleImageView)findViewById(R.id.viewProfilePic);
        viewProfilePicListener();

        if(primaryEmail != null){

            viewInputEmail.setText(primaryEmail);

        }

        viewButtonSignup = (Button)findViewById(R.id.viewButtonSignup);
        viewButtonSignupListener();

        viewSelectGroup = (TextView) findViewById(R.id.viewSelectGroup);
        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        viewSelectGroupListener();


        backButton = (ImageView)findViewById(R.id.backButton);
        backButtonListener();


        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                scrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
            }
        };
        scrollContainer.post(runnable);


        imagePicker = new ImagePicker();
        imagePicker.setTitle("Camera");
        imagePicker.setCropImage(false);

    }



    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private ImageView backButton;
    private void backButtonListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeKeyboard();

                finish();

            }
        });
    }




    HashMap<String, String> groupList = new HashMap<>();

    private void getGroups() {


        //show progress dialog
        progressDialog.setTitle("Fetching Groups");
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);


                    String body = "plain text request body";
                    Call<String> call = apiService.getGroupScalar();

                    Response<String> response = call.execute();

                    final int code = response.code();

                    if(code == 200){

                        String value = response.body();

                        Log.v("Scaler Groups", value);

                        if(value != null){

                            JSONObject jObj = new JSONObject(value);

                            boolean status = jObj.getBoolean("status");
                            String message = jObj.getString("message");

                            if(status){

                                JSONObject groups = jObj.getJSONObject("group");

                                final String[] dl = new String[groups.length()];
                                final boolean[] ckp = new boolean[groups.length()];

                                Iterator keys = groups.keys();

                                for (int i = 0; keys.hasNext(); i++) {

                                    GroupModel gpm = new GroupModel();

                                    String currentDynamicKey = (String)keys.next();
                                    String keyValue = groups.getString(currentDynamicKey);

                                    gpm.setGroupId(currentDynamicKey);
                                    gpm.setGroupName(keyValue);

                                    dl[i] = keyValue;
                                    ckp[i] = false;

                                    groupList.put(keyValue, currentDynamicKey);

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        //dismiss progress dialog
                                        progressDialog.dismiss();

                                        groupSlectDialog(dl, ckp);

                                    }
                                });

                            }else{

                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }

                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    progressDialog.dismiss();

                                    Toast.makeText(context, "No response", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }else{

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //dismiss progress dialog
                                progressDialog.dismiss();

                                Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                }catch (Exception e){

                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void viewSelectGroupListener() {

        viewSelectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if courseTag is empty
                // yes then make api call and update course tags array and show multiple selection dialog
                // else show multiple selection dialog
                // update tags on taping dialog ok button

                if(ApiClient.isNetworkAvailable(context)){

                    //manual login
                    getGroups();

                }else{

                    Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void viewButtonSignupListener() {

        viewButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check input fields validation
                validator.validate();
            }
        });
    }


    @Override
    public void onValidationSucceeded() {

        if(mTagGroup.getTags().length > 0){

            //check network connection
            if(ApiClient.isNetworkAvailable(context)){

                if(viewInputPhone.getText().toString().trim().length() == 10){

                    //enable when api integrated
                    signupProcess();
                }else{

                   // Toast.makeText(context, "Please provide valid 10 digits mobile no.", Toast.LENGTH_SHORT).show();
                }

                //Ctx.startActivity( new Intent(context, OTPVerificationActivity.class));

            }else{

                Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

            }

        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //builder.setTitle("Testkart");
            builder.setMessage("Please provide at least one group.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //finishAffinity();
                }
            });


            builder.create().show();

            //Toast.makeText(context, "Please provide at least one group", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

    }


    private void signupProcess() {

        //if user already registered and not active - goto OTP screen
        //if user already active and registered - Error message - user already existed with us
        //if user new - normal process.

        //make api request
        //if(ok)
        //bundle email address
        //go to validation screen
        //else, stay ith error message

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.setTitle("Creating your Account");
        progressDialog.show();

        /*Call<ResponseStatus> call = apiService.createUser(viewInputEmail.getText().toString().trim(),
                                                          viewInputName.getText().toString().trim(),
                                                          viewInputPassword.getText().toString().trim(),
                                                          viewInputAddress.getText().toString().trim(),
                                                          mTagGroup.getTags(),
                                                          viewInputPhone.getText().toString().trim());*/

        String phoneNo1=  viewInputPhone.getText().toString();
        //Log.v("Mobile", phoneNo1);

        Responses responses = new Responses();
        responses.setName(viewInputName.getText().toString());
        responses.setAddress(viewInputAddress.getText().toString());
        responses.setEmail(viewInputEmail.getText().toString().trim());
        responses.setEnroll("");

        String gids = "";

        for (String groupName:
             mTagGroup.getTags()) {

            gids += groupList.get(groupName)+",";

        }

        gids = gids.replaceAll(",$", "");

        responses.setGroupName(gids);
        responses.setPassword(viewInputPassword.getText().toString());
        responses.setGuardianPhone("");
        responses.setPhone(viewInputPhone.getText().toString());

        //get device info and add device info

        HashMap<String, String> deviceInfo = MknUtils.getDeviceInfo(context);

        responses.setDevice("Android");
        responses.setManufacturer(deviceInfo.get(MknUtils.KEY_PHONE_MANUFACTURER));
        responses.setModel(deviceInfo.get(MknUtils.KEY_PHONE_MODEL));
        responses.setImei_no(deviceInfo.get(MknUtils.KEY_PHONE_IMEI));

        RegisterPostModel r = new RegisterPostModel();
        r.setResponses(responses);

        Call<RegisterResponse> call = apiService.registerUser(r);


        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null && response.body().getStatus()){

                        studentId = response.body().getStudent_id();
                        Log.v("Student Id", "ID "+studentId);
                        // now upload image if any
                        if(resultUri != null){

                            ArrayList<String> imgPaths = new ArrayList<String>();
                            imgPaths.add(resultUri.getPath());

                            startUploadingImage(imgPaths, response.body().getMessage());

                        }else{

                            signupAndNext(response.body().getMessage());

                        }

                    }else{

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        //builder.setTitle("Testkart");
                        builder.setMessage(response.body().getMessage());
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });


                        builder.create().show();

                        //Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else{


                    Toast.makeText(SignupActivity.this, "Error POResponse Code: "+ code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {


                //hide progress dialog
                progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });



        /*call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, POResponse<ResponseStatus> response) {

                //hide progress dialog
                progressDialog.hide();

                if(response.body() != null && response.body().getStatus().equalsIgnoreCase(Consts.STATUS_SUCESS)){

                    //if user already registred and not active - goto OTP screen
                    //if user already active and registred - Error message - user already existed with us
                    //if user new - normal process.

                    if(response.body().getData().getUserStatus().equals("0") || response.body().getData().getUserStatus().equals("1")){

                        Bundle args = new Bundle();
                        args.putString(SessionManager.KEY_EMAIL, viewInputEmail.getText().toString().trim());
                        args.putString(SessionManager.KEY_PASSWORD, viewInputPassword.getText().toString().trim());

                        //go to home / dashboard
                        Ctx.startActivity( new Intent(context, OTPVerificationActivity.class));

                    }else if(response.body().getData().getUserStatus().equals("2")){

                        Toast.makeText(context, viewInputEmail.getText().toString().trim()+" already registred with us.", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(context, "Registration failed. POResponse is null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {

                //hide progress dialog
                progressDialog.hide();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });*/

    }

    private void signupAndNext(String message) {

        //Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();

        Bundle args = new Bundle();
        args.putString(SessionManager.KEY_EMAIL, viewInputEmail.getText().toString().trim());
        args.putString(SessionManager.KEY_PASSWORD, viewInputPassword.getText().toString().trim());

        Intent otpScreen = new Intent(SignupActivity.this, OTPVerificationActivity.class);
        otpScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        otpScreen.putExtra(OTPVerificationActivity.KEY_BUNDLE_OTP, args);

        //go to OTP verification screen
        Ctx.startActivity( otpScreen, args);

        //go to home / dashboard
        // Ctx.startActivity( new Intent(context, OTPVerificationActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), args);

        finish();

    }




    /*String[] colors = new String[]{

            "Red",
            "Green",
            "Blue",
            "Purple",
            "Olive",
            "Aoura",
            "Barn Red",
            "Amber",
            "Bazaar",
            "Live"
    };

    // Boolean array for initial selected items
    final boolean[] checkedColors = new boolean[]{

            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false

    };*/

    private void groupSlectDialog(final String[] ld, final boolean[] ckp){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final ArrayList<String> selectedTags = new ArrayList<>();

        builder.setMultiChoiceItems(ld, ckp, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if(which == 0){
                    //selected all
                    // mark all the items checked else nothing
                    if(isChecked){

                        // mark all checks
                        for (int i = 0; i < ckp.length; i++) {

                            ckp[i] = true;
                            ((AlertDialog) dialog).getListView().setItemChecked(i, true);
                        }


                    }else{

                        //remove all check
                        for (int i = 0; i < ckp.length; i++) {

                            ckp[i] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                        }
                    }


                }else{

                    // Update the current focused item's checked status
                    ckp[which] = isChecked;

                    ckp[0] = false;
                    ((AlertDialog) dialog).getListView().setItemChecked(0, false);

                    for (int i = 1; i < ckp.length; i++) {

                        if(i == 0){
                            //do nothing

                        }else{

                            if(!ckp[0]){

                                if(ckp[i]){

                                    if(i == ckp.length-1){

                                        ckp[0] = true;
                                        ((AlertDialog) dialog).getListView().setItemChecked(0, true);
                                    }

                                }else{

                                    break;
                                }

                            }else{

                                break;
                            }
                        }


                    }

                }

                // Update the current focused item's checked status
                //ckp[which] = isChecked;

            }
        });


        // Specify the dialog is not cancelable
        builder.setCancelable(false);

        // Set a title for alert dialog
        builder.setTitle("Your preferred Groups?");

        // Set the positive/yes button click listener
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //update group select tags
                for (int i = 1; i<ckp.length; i++){
                    boolean checked = ckp[i];
                    if (checked) {

                        String c = ld[i];
                        selectedTags.add(c);
                    }
                }

                viewSelectGroup.setText(selectedTags.size()+" Groups selected");
                mTagGroup.setTags(selectedTags);
            }
        });


        // Set the neutral/cancel button click listener
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click the neutral button
            }
        });

        AlertDialog dialog = builder.create();

        // Display the alert dialog on interface
        dialog.show();


    }


    private String getPrimaryEmailAddress(){

        //showing dialog to select image
        String possibleEmail=null;

        java.util.regex.Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
                Log.e("keshav","possibleEmail"+possibleEmail);
            }
        }

        Log.e("keshav","possibleEmail gjhh->"+possibleEmail);
        Log.e("permission", "granted Marshmallow O/S");

        return  possibleEmail;
    }


    ///update profile image

    private void viewProfilePicListener() {

        viewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pickAction();
            }
        });
    }


    private void pickAction() {

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

                    imagePicker.startCamera(SignupActivity.this, new ImagePicker.Callback() {

                        @Override
                        public void onPickImage(Uri imageUri) {

                            Log.v("Camera image uri: ", imageUri.getPath());

                            // Log.v("Matisse", "mSelected: " + mSelected);

                            CropImage.activity(imageUri)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .start(SignupActivity.this);

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
                    Matisse.from(SignupActivity.this)
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




    String studentId;

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

                    //update image
                    viewProfilePic.setImageURI(resultUri);

                    //uploadImage(resultUri);


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                    Exception error = result.getError();
                    error.printStackTrace();

                }
            }

        }


    }


    String msgSignup = "";
    private void startUploadingImage(final ArrayList<String> imgPaths, final String message){

        msgSignup = message;

        if (ApiClient.isNetworkAvailable(this)) {

            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    uploadFile(imgPaths);

                }
            }).start();
        }else{

            Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }


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

           // multipart.addFormField("public_key", session.getUserDetails().get(SessionManager.KEY_PUBLIC));
           // multipart.addFormField("private_key", session.getUserDetails().get(SessionManager.KEY_PRIVATE));
            multipart.addFormField("student_id", studentId);

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

                        signupAndNext(msgSignup);

                    }
                });

            }else{

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }catch (Exception e){

            e.printStackTrace();
        }


    }
}
