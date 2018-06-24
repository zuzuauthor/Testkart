package com.testkart.exam.edu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.login.SmsReceiver;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.register.OTPModel;
import com.testkart.exam.edu.register.ResendModel;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.thefinestartist.utils.content.Ctx;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by testkart on 10/4/17.
 */


/*

 1 check connectivity, validation, view initialization
 2 make otp verification api call
 3 if true, create login sesion and goto home screen
 4 else, show message: invalid OTP, Please enter correct one.
 5 resend otp api call
 6 if true, show message: OTP has been send tyo your registred email id
 7 else, message : unable to send otp on your registred email id

http://elfemo.com/demo/server3/api/rest/Emailverifacations/emailVerify.json


fail:

{
  "status": false,
  "message": "Invalid Verification Code",
  "accountStatus": "Pending",
  "user": {},
  "public_key": null,
  "private_key": null,
  "studentPhoto": null
}

success:

{
  "status": true,
  "message": "Email verified successfully. Please wait for admin approval to activate account.",
  "accountStatus": "Pending"
}


*/

public class OTPVerificationActivity extends AppCompatActivity implements Validator.ValidationListener, SmsReceiver.SMSListener {

    public static final String KEY_BUNDLE_OTP = "otp_bundle";

    private Context context = this;
    private SessionManager session;
    private ProgressDialog progressDialog;

    @NotEmpty
    private EditText viewInputOTP;
    private TextView viewResendOTP;
    private Validator validator;

    private Button viewButtonVerify;

    private SmsReceiver smsReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otp_verify);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //initialization
        initialization();

    }

    private void initialization() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Verifying");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        session = new SessionManager(context);

        validator = new Validator(this);
        validator.setValidationListener(this);

        viewInputOTP = (EditText) findViewById(R.id.viewInputOTP);
        viewResendOTP = (TextView) findViewById(R.id.viewResendOTP);
        viewButtonVerify = (Button) findViewById(R.id.viewButtonVerify);
        viewButtonVerifyListener();
        viewResendOTPListener();


        backButton = (ImageView)findViewById(R.id.backButton);
        backButtonListener();

        //start automatic detect otp
       // automaticOTPDetect();

        smsReceiver = new SmsReceiver(this);
        stratCountDown();

    }


    @Override
    protected void onResume() {
        super.onResume();

        this.registerReceiver(smsReceiver, new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED"));
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.unregisterReceiver(smsReceiver);
    }

    private void automaticOTPDetect() {

        if(ApiClient.isNetworkAvailable(context)){

            try{

                Bundle extras = getIntent().getBundleExtra(KEY_BUNDLE_OTP);

                String regEmail = extras.getString(SessionManager.KEY_EMAIL);

                otpDetect("1", regEmail);

            }catch (Exception e){

                e.printStackTrace();

            }


        }else{

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void otpDetect(String s, String regEmail) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.setTitle("");
        progressDialog.show();

        Call<ResendModel> call = apiService.getOTP(regEmail, s);
        call.enqueue(new Callback<ResendModel>() {
            @Override
            public void onResponse(Call<ResendModel> call, Response<ResendModel> response) {

                //hide progress dialog
                progressDialog.dismiss();


                int code = response.code();

                if(code == 200){

                    if(response.body() != null && response.body().getStatus()){

                        //OTP Fetched successfully
                        viewInputOTP.setText(response.body().getVerificationCode());

                        //call automatic summit function
                        varificationProcess();


                    }else{

                        Toast.makeText(OTPVerificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(OTPVerificationActivity.this, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResendModel> call, Throwable t) {

                //hide progress dialog
                progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });


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


    private void viewResendOTPListener() {

        viewResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check network connection
                if(ApiClient.isNetworkAvailable(context)){


                    try{

                        Bundle extras = getIntent().getBundleExtra(KEY_BUNDLE_OTP);

                        String regEmail = extras.getString(SessionManager.KEY_EMAIL).trim();

                        if(!regEmail.isEmpty()){

                            //OTP resend process  enable when api integrated
                            otpResendProcess(regEmail);

                        }else{

                            Toast.makeText(context, "Email is empty", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){

                        e.printStackTrace();
                    }


                    //Toast.makeText(context, "OTP send Sucessfully on your registred email.", Toast.LENGTH_LONG).show();

                }else{

                    Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void otpResendProcess(String email) {

        //make api call
        //if(ok) show sucess message
        //else show fail message

        //check network connection
        if(ApiClient.isNetworkAvailable(context)){

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            //show progress dialog
            progressDialog.setTitle("Resend OTP");
            progressDialog.show();

            Call<ResendModel> call = apiService.reSendVerificationCode(email);
            call.enqueue(new Callback<ResendModel>() {
                @Override
                public void onResponse(Call<ResendModel> call, Response<ResendModel> response) {

                    //hide progress dialog
                    progressDialog.dismiss();


                    int code = response.code();

                    if(code == 200){

                        if(response.body() != null && response.body().getStatus()){

                            //email send successfully
                            Toast.makeText(OTPVerificationActivity.this, "A verification code is sent to your registered email address and registered mobile number"/*response.body().getMessage()*/, Toast.LENGTH_SHORT).show();

                        }else{

                            Toast.makeText(OTPVerificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }else{

                        Toast.makeText(OTPVerificationActivity.this, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<ResendModel> call, Throwable t) {

                    //hide progress dialog
                    progressDialog.dismiss();

                    Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                    t.printStackTrace();

                }
            });


        }else{

            Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }


    private void viewButtonVerifyListener() {

        viewButtonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validator.validate();
            }
        });
    }


    @Override
    public void onValidationSucceeded() {

        //enable when api integrated
        varificationProcess();

        //Ctx.startActivity( new Intent(context, LoginActivity1.class));
    }

    private void varificationProcess() {

        //check network connection
        if(ApiClient.isNetworkAvailable(context)){

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            //show progress dialog
            progressDialog.setTitle("Verifying");
            progressDialog.show();

            Call<OTPModel> call = apiService.verifyOTP(viewInputOTP.getText().toString().trim());
            call.enqueue(new Callback<OTPModel>() {
                @Override
                public void onResponse(Call<OTPModel> call, Response<OTPModel> response) {

                    //hide progress dialog
                    progressDialog.dismiss();

                    int code = response.code();

                    if(code == 200){

                        if(response.body() != null && response.body().getStatus()){

                            if(response.body().getAccountStatus().equalsIgnoreCase("Active")){


                                // user active
                                //update session manager
                                HashMap<String, String> userInfo = new HashMap<String, String>();

                                userInfo.put(SessionManager.KEY_STUDENT_ID, response.body().getUser().getStudent().getId());
                                userInfo.put(SessionManager.KEY_NAME, response.body().getUser().getStudent().getName());
                                userInfo.put(SessionManager.KEY_EMAIL, response.body().getUser().getStudent().getEmail());
                                //userInfo.put(SessionManager.KEY_ACCOUNT_BLANCE, response.body().getUser().getStudent().ba);
                                userInfo.put(SessionManager.KEY_PROFILE_IMAGE, (response.body().getStudentPhoto() != null) ? (String)response.body().getStudentPhoto() : "");
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

                                userInfo.put(SessionManager.KEY_TAGS, (String)response.body().getUser().getStudent().getStudentGroup());

                                userInfo.put(SessionManager.KEY_ADDRESS, response.body().getUser().getStudent().getAddress());

                                // Log.v("Profile Image", response.body().getStudentPhoto().toString());

                                session.createLoginSession(userInfo);

                                //go to home / dashboard
                                Ctx.startActivity( new Intent(context, MainActivity.class));

                                //finish current activity
                                finishAffinity();


                            }else if(response.body().getAccountStatus().equalsIgnoreCase("Pending")){

                                //admin approval needed

                                Toast.makeText(OTPVerificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                finish();

                            }else{

                                //stay on otp screen and retry
                                Toast.makeText(OTPVerificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }else{

                            //stay on otp screen and retry
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }else{

                        Toast.makeText(OTPVerificationActivity.this, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<OTPModel> call, Throwable t) {

                    //hide progress dialog
                    progressDialog.dismiss();

                    Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                    t.printStackTrace();
                }
            });

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

    @Override
    public void onCodeReceive(String code) {

        viewInputOTP.setText("");

        viewInputOTP.setText(code);

        varificationProcess();

        //Toast.makeText(context, "Verification Code: "+code, Toast.LENGTH_SHORT).show();
    }



    private void stratCountDown(){

        new CountDownTimer(45000, 1000) {

            public void onTick(long millisUntilFinished) {

                /*String timeremaning = String.format("%02d min, %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                );
*/
                int seconds = (int) (millisUntilFinished / 1000) % 60 ;

                viewButtonVerify.setEnabled(false);
                viewButtonVerify.setText("Wating for OTP "+seconds);

               // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                viewButtonVerify.setEnabled(true);
                viewButtonVerify.setText("Verify");

            }
        }.start();
    }

}

