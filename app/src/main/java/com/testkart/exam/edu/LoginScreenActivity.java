package com.testkart.exam.edu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.ProgressDialogHelper;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.models.ResponseStatus;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
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

 1 Views Initialize, Check validation, add listeners
 2 Check network
 3 Check if session is not empty and user already login
 4 yes, make login process and go to home screen
 5 no, DO nothing , let user manually login by using his/her email id and password
 6 start login process, check if user exist, exist but not active , exist and active
 7 if user is exist and active then go to home screen
 8 if user is exist and not active then go to OTP verification screen
 9 if user not exist then show error message : please mention your correct email id and password
 10 status code true / false, User Status 1 / 0 ; 1 for active and 0 for inactive


 */

public class LoginScreenActivity extends AppCompatActivity implements Validator.ValidationListener {

    private SessionManager session;
    private ProgressDialogHelper progressDialog;

    private TextView viewForgotPassword, viewSignup;

    @NotEmpty
    @Email
    private EditText viewInputEmail;

    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText viewInputPassword;

    private Button viewButtonLogin;
    private Context context = this;

    private Validator validator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //initialization
        initialization();

        // check if prefrence null
        //if yes , do nothing
        //else , get values from prefrence and filled the email and password and auto login
    }

    private void initialization() {

        session = new SessionManager(context);
        progressDialog = new ProgressDialogHelper();

        viewForgotPassword = (TextView)findViewById(R.id.viewForgotPassword);
        viewSignup = (TextView)findViewById(R.id.viewSignup);
        viewButtonLogin = (Button) findViewById(R.id.viewButtonLogin);
        viewInputEmail = (EditText) findViewById(R.id.viewInputEmail);
        viewInputPassword = (EditText) findViewById(R.id.viewInputPassword);

        viewButtonLoginListener();
        viewForgotPasswordListener();
        viewSignupListener();

        validator = new Validator(this);
        validator.setValidationListener(this);


        //check login status
        loginStatus();

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

             //   validator.validate();

                Intent forgotIntent = new Intent(LoginScreenActivity.this, MainActivity.class);
                startActivity(forgotIntent);
            }
        });

    }

    private void viewSignupListener() {

        viewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signupIntent = new Intent(LoginScreenActivity.this, SignupActivity.class);
                startActivity(signupIntent);
            }
        });

    }

    private void viewForgotPasswordListener() {

        viewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent forgotIntent = new Intent(LoginScreenActivity.this, ForgotPasswordActivity.class);
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

        Call<ResponseStatus> call = null;

        if(newLogin){

            //new login, get values from input fields
           // call = apiService.getUserInfo(viewInputEmail.getText().toString().trim(), viewInputPassword.getText().toString().trim());

        }else{

            //get values from session
            HashMap<String, String> oldUser = session.getUserDetails();

           // call = apiService.getUserInfo(oldUser.get(SessionManager.KEY_EMAIL), oldUser.get(SessionManager.KEY_PASSWORD));
        }


        call.enqueue(new Callback<ResponseStatus>() {

            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {

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

                Toast.makeText(LoginScreenActivity.this, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }

        });

    }

}
