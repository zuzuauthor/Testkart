package com.testkart.exam.edu.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.register.ResendModel;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by testkart on 17/5/17.
 */

public class PasswordResetActivity extends AppCompatActivity implements Validator.ValidationListener, SmsReceiver.SMSListener{

    @NotEmpty
    private EditText viewInputCode, viewInputPassword, viewInputConfirmPassword;

    private Button viewButtonSubmit;
    private Validator validator;
    private Context context = this;
    private ProgressDialog progressDialog;

    private SmsReceiver smsReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_password_reset);
        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //initialization
        initialization();

    }


    private void initialization() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Password Reset");
        progressDialog.setMessage("Please Wait...");

        validator = new Validator(this);
        validator.setValidationListener(this);


        viewInputCode = (EditText)findViewById(R.id.viewInputCode);
        viewInputPassword = (EditText)findViewById(R.id.viewInputPassword);
        viewInputConfirmPassword = (EditText)findViewById(R.id.viewInputConfirmPassword);

        viewButtonSubmit = (Button)findViewById(R.id.viewButtonSubmit);
        viewButtonSubmitListener();

        backButton = (ImageView)findViewById(R.id.backButton);
        backButtonListener();

       // automaticOTPDetect();

        smsReceiver = new SmsReceiver(this);

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



                String regEmail = getIntent().getStringExtra(SessionManager.KEY_EMAIL);

                otpDetect("2", regEmail);

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
                        viewInputCode.setText(response.body().getVerificationCode());

                        //call automatic summit function
                        //varificationProcess();


                    }else{

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
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



    private ImageView backButton;
    private void backButtonListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
    }


    private void viewButtonSubmitListener() {

        viewButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validate fields
                validator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {

        //check network connection
        if(ApiClient.isNetworkAvailable(context)){

            if(viewInputPassword.getText().toString().equals(viewInputConfirmPassword.getText().toString())){

                //enable when api integrated
                passwordResetProcess();

            }else{

                Toast.makeText(context, "Password do not match", Toast.LENGTH_SHORT).show();
            }


        }else{

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void passwordResetProcess() {

        //make api request
        //show success or fail messsage. If fail? then stay
        //else go to login screen
        //finish current activity

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<ResendModel> call = apiService.resetPassword(viewInputCode.getText().toString().trim(),
                viewInputPassword.getText().toString().trim(),
                viewInputConfirmPassword.getText().toString().trim());

        call.enqueue(new Callback<ResendModel>() {
            @Override
            public void onResponse(Call<ResendModel> call, Response<ResendModel> response) {

                //hide progress dialog
                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null && response.body().getStatus()){

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();

                        //finish current activity
                        finish();

                    }else{

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }else{

                    Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_LONG).show();
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

        viewInputCode.setText(code);

        //Toast.makeText(context, "Verification Code: "+code, Toast.LENGTH_SHORT).show();
    }
}

