package com.testkart.exam.edu.login;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.ForgotPasswordActivity;
import com.testkart.exam.edu.MainActivity;
import com.testkart.exam.edu.OTPVerificationActivity;
import com.testkart.exam.R;
import com.testkart.exam.edu.SignupActivity;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.MknUtils;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.login.model.LoginResponse;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.thefinestartist.utils.content.Ctx;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.jinesh.easypermissionslib.EasyPermission;

/**
 * Created by testkart on 12/5/17.
 */

public class LoginActivity1 extends AppCompatActivity implements RequestNewLoginDeviceDialog.OnMultipleLoginDetectedListener, Validator.ValidationListener, EasyPermission.OnPermissionResult{

    public static final String KEY_RETAIN_CART = "retain_cart";

    private SessionManager session;
    private ProgressDialog progressDialog;

    private TextView viewForgotPassword, viewSignup;

    private DBHelper dbHelper;

    @NotEmpty
    private EditText viewInputEmail;

    @Password(min = 1)
    private ShowHidePasswordEditText viewInputPassword;

    private Button viewButtonLogin;
    private Context context = this;

    private Validator validator;

    private EasyPermission easyPermission;

    private boolean retainCart;

    private ImageView backButton;

    public static boolean gotoCart = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login1);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //initialization
        initialization();

        // check if prefrence null
        //if yes , do nothing
        //else , get values from prefrence and filled the email and password and auto login
    }

    private void initialization() {

        dbHelper = new DBHelper(this);

        easyPermission = new EasyPermission();

        session = new SessionManager(context);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        String primaryEmail = getPrimaryEmailAddress();

        viewForgotPassword = (TextView)findViewById(R.id.viewForgotPassword);
        viewSignup = (TextView)findViewById(R.id.viewSignup);
        viewButtonLogin = (Button) findViewById(R.id.viewButtonLogin);
        viewInputEmail = (EditText) findViewById(R.id.viewInputEmail);
        viewInputPassword = (ShowHidePasswordEditText) findViewById(R.id.viewInputPassword);


        if(primaryEmail != null){

            viewInputEmail.setText(primaryEmail);

        }

        backButton = (ImageView)findViewById(R.id.backButton);
        backButtonListener();

        viewButtonLoginListener();
        viewForgotPasswordListener();
        viewSignupListener();

        validator = new Validator(this);
        validator.setValidationListener(this);


        //check retain cart
        retainCart = getIntent().getBooleanExtra(KEY_RETAIN_CART, false);

        gotoCart = retainCart;

        if(!retainCart){

            dbHelper.clearCart();

            //gotoCart = false;

        }else{

            Log.v("Cart Retained", "Yes");

            //gotoCart = true;
        }

    }



    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    private void backButtonListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeKeyboard();

                finish();

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        easyPermission.requestPermission(this, Manifest.permission.CAMERA);
        //easyPermission.requestPermission(this, Manifest.permission.GET_ACCOUNTS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try{

            easyPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }catch (Exception e){

            e.printStackTrace();
        }
    }


    boolean allPermissionsOk = false;

    @Override
    public void onPermissionResult(String permission, boolean isGranted) {

        switch (permission) {

            case Manifest.permission.CAMERA:

                if (isGranted) {

                    Log.e("CAMERA", "granted");
                    easyPermission.requestPermission(LoginActivity1.this,Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {

                    Log.e("CAMERA", "denied");

                }

                break;

            case Manifest.permission.READ_EXTERNAL_STORAGE:

                if (isGranted) {

                    Log.e("READ_EXTERNAL_STORAGE", "granted");

                    easyPermission.requestPermission(LoginActivity1.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {

                    Log.e("READ_EXTERNAL_STORAGE", "denied");

                }
                break;

            case Manifest.permission.WRITE_EXTERNAL_STORAGE:

                if (isGranted) {

                    Log.e("READ_EXTERNAL_STORAGE", "granted");

                    allPermissionsOk = true;

                } else {

                    allPermissionsOk = false;

                    Log.e("READ_EXTERNAL_STORAGE", "denied");

                }
                break;


        }
    }


    private void loginStatus() {

        //check session is login
        //yes then login without user user intrection
        //no then go with manuel process
        if(session.isLoggedIn()){

            //automatic login
            loginProcess(false);


        }else{

            //do nothing
            //login user normaly

        }

    }


    private void viewButtonLoginListener() {

        viewButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(allPermissionsOk){

                    validator.validate();

                }else{

                    easyPermission.requestPermission(LoginActivity1.this, Manifest.permission.CAMERA);
                }


                /*Intent forgotIntent = new Intent(LoginActivity1.this, MainActivity.class);
                startActivity(forgotIntent);*/
            }
        });

    }

    private void viewSignupListener() {

        viewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signupIntent = new Intent(LoginActivity1.this, SignupActivity.class);
                startActivity(signupIntent);
            }
        });

    }

    private void viewForgotPasswordListener() {

        viewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent forgotIntent = new Intent(LoginActivity1.this, ForgotPasswordActivity.class);
                startActivity(forgotIntent);
            }
        });

    }



    @Override
    public void onValidationSucceeded() {

        //check network connection
        if(ApiClient.isNetworkAvailable(context)){

            //manual login
            loginProcess(true);


        }else{

            Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

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



    private void loginProcess(boolean newLogin) {

        //make api request
        //update preference / session manager
        //go to home / dashboard
        //finish current activity

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();


        //get device info
        HashMap<String, String> deviceInfo = MknUtils.getDeviceInfo(context);

        Call<LoginResponse> call = null;

        if(newLogin){

            //new login, get values from input fields
            call = apiService.getUserInfo(viewInputEmail.getText().toString().trim(),
                    viewInputPassword.getText().toString().trim(),
                    "Android",
                    deviceInfo.get(MknUtils.KEY_PHONE_MANUFACTURER),
                    deviceInfo.get(MknUtils.KEY_PHONE_MODEL),
                    deviceInfo.get(MknUtils.KEY_PHONE_IMEI));

        }else{

            //get values from session
            HashMap<String, String> oldUser = session.getUserDetails();

            call = apiService.getUserInfo(oldUser.get(SessionManager.KEY_EMAIL),
                    oldUser.get(SessionManager.KEY_PASSWORD),
                    "Android",
                    deviceInfo.get(MknUtils.KEY_PHONE_MANUFACTURER),
                    deviceInfo.get(MknUtils.KEY_PHONE_MODEL),
                    deviceInfo.get(MknUtils.KEY_PHONE_IMEI));
        }


        call.enqueue(new Callback<LoginResponse>() {


            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {


                //hide progress dialog
                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null && response.body().getStatus()){


                        if(response.body().getAccountStatus().equalsIgnoreCase("Active")){

                            Log.v("Student: ", response.body().getUser().getStudent().toString());

                            // user active
                            //update session manager
                            HashMap<String, String> userInfo = new HashMap<String, String>();

                            userInfo.put(SessionManager.KEY_STUDENT_ID, response.body().getUser().getStudent().getId());
                            userInfo.put(SessionManager.KEY_NAME, response.body().getUser().getStudent().getName());
                            userInfo.put(SessionManager.KEY_EMAIL, response.body().getUser().getStudent().getEmail());
                            //userInfo.put(SessionManager.KEY_ACCOUNT_BLANCE, response.body().getUser().getStudent().ba);
                            userInfo.put(SessionManager.KEY_PROFILE_IMAGE, response.body().getStudentPhoto());
                            userInfo.put(SessionManager.KEY_PRIVATE, response.body().getPrivateKey());
                            userInfo.put(SessionManager.KEY_PUBLIC, response.body().getPublicKey());
                            userInfo.put(SessionManager.KEY_PHONE_NUMBER, response.body().getUser().getStudent().getPhone());

                            userInfo.put(SessionManager.KEY_CURRENCY, response.body().getCurrencyCode());
                            userInfo.put(SessionManager.KEY_PAID_EXAM, response.body().getSysSetting().getConfiguration().getPaidExam());
                            userInfo.put(SessionManager.KEY_CERTIFICATE, response.body().getSysSetting().getConfiguration().getCertificate()+"");

                            userInfo.put(SessionManager.KEY_ADMISSION_DATE, response.body().getUser().getStudent().getRenewalDate());
                            userInfo.put(SessionManager.KEY_ALTERNATE_NO, response.body().getUser().getStudent().getGuardianPhone());
                            userInfo.put(SessionManager.KEY_STUDENT_ENROLL, response.body().getUser().getStudent().getEnroll());

                            userInfo.put(SessionManager.KEY_EXAM_EXPIRY, response.body().getSysSetting().getConfiguration().getExamExpiry());

                            userInfo.put(SessionManager.KEY_ADDRESS, response.body().getUser().getStudent().getAddress());


                            //response.body().getSysSetting().getConfiguration().getExamExpiry()

                            userInfo.put(SessionManager.KEY_TAGS, (String)response.body().getUser().getStudent().getStudentGroup());


                            session.createLoginSession(userInfo);

                            //go to home / dashboard
                            Ctx.startActivity( new Intent(context, MainActivity.class));

                            //finish current activity
                            finish();
                            finishAffinity();


                        }else if(response.body().getAccountStatus().equalsIgnoreCase("Pending")){

                            //user inactive, go to OTP verification screen

                            // bundle email , password
                            Bundle args = new Bundle();
                            args.putString(SessionManager.KEY_EMAIL, viewInputEmail.getText().toString().trim());
                            args.putString(SessionManager.KEY_PASSWORD, viewInputPassword.getText().toString().trim());

                            Intent otpScreen = new Intent(LoginActivity1.this, OTPVerificationActivity.class);
                            otpScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            otpScreen.putExtra(OTPVerificationActivity.KEY_BUNDLE_OTP, args);

                            //go to OTP verification screen
                            Ctx.startActivity( otpScreen, args);

                        }else if(response.body().getAccountStatus().equalsIgnoreCase("Abort")){

                            //show multiple device login dialog
                            RequestNewLoginDeviceDialog reqDialog = new RequestNewLoginDeviceDialog();

                            Bundle args = new Bundle();
                            args.putString(MknUtils.KEY_PHONE_IMEI, response.body().getDeviceInfo().getImeiNo());
                            args.putString(MknUtils.KEY_PHONE_MANUFACTURER, response.body().getDeviceInfo().getManufacturer());
                            args.putString(MknUtils.KEY_PHONE_MODEL, response.body().getDeviceInfo().getModel());
                            args.putString("STUDENT_ID", response.body().getUser().getStudent().getId());

                            reqDialog.setArguments(args);

                            reqDialog.show(getSupportFragmentManager(), "REQ_DIALOG");

                        }else{

                            // error in response data
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }else{

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }else{

                    Toast.makeText(LoginActivity1.this, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                //hide progress dialog
                progressDialog.dismiss();

                Toast.makeText(LoginActivity1.this, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }

            /*@Override
            public void onResponse(Call<ResponseStatus> call, POResponse<ResponseStatus> response) {

                //hide progress dialog
                progressDialog.hide();

                if(response.body() != null && response.body().getStatus().equalsIgnoreCase(Consts.STATUS_SUCESS)){


                    if(response.body().getData().getUserStatus().equals("1")){

                        // user active
                        //update session manager
                        HashMap<String, String> userInfo = new HashMap<String, String>();

                        userInfo.put(SessionManager.KEY_STUDENT_ID, response.body().getData().getStudentId());
                        userInfo.put(SessionManager.KEY_NAME, response.body().getData().getStudentName());
                        userInfo.put(SessionManager.KEY_EMAIL, response.body().getData().isStudentEmail());
                        userInfo.put(SessionManager.KEY_ACCOUNT_BLANCE, response.body().getData().getAccountBlance());
                        userInfo.put(SessionManager.KEY_PROFILE_IMAGE, response.body().getData().getProfileImage());
                        userInfo.put(SessionManager.KEY_PRIVATE, response.body().getData().getToken());
                        userInfo.put(SessionManager.KEY_PUBLIC, response.body().getData().getToken());

                        session.createLoginSession(userInfo);

                        //go to home / dashboard
                        Ctx.startActivity( new Intent(context, MainActivity.class));

                        //finish current activity
                        finish();


                    }else if(response.body().getData().getUserStatus().equals("0")){

                        //user inactive, go to OTP verification screen

                        // bundle email , password
                        Bundle args = new Bundle();
                        args.putString(SessionManager.KEY_EMAIL, viewInputEmail.getText().toString().trim());
                        args.putString(SessionManager.KEY_PASSWORD, viewInputPassword.getText().toString().trim());

                        //go to OTP verification screen
                        Ctx.startActivity( new Intent(context, OTPVerificationActivity.class), args);

                    }else{

                        // error in response data
                        Toast.makeText(context, "Error in response data", Toast.LENGTH_LONG).show();

                    }

                }else{

                    Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {

                //hide progress dialog
                progressDialog.hide();

                Toast.makeText(LoginActivity1.this, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }*/

        });

    }


    private String getPrimaryEmailAddress(){

        //showing dialog to select image
        String possibleEmail=null;

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {

            if(!session.isLoggedIn()){

                if(!retainCart){

                    dbHelper.clearCart();

                }

            }

            dbHelper.close();

        }

    }

    @Override
    public void onReportSuccess() {

        Toast.makeText(context, "Your query submitted successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReportFail() {

        Toast.makeText(context, "Error while submitting query", Toast.LENGTH_SHORT).show();

    }
}
