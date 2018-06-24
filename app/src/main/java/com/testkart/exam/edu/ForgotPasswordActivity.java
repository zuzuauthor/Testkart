package com.testkart.exam.edu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.login.PasswordResetActivity;
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
 * Created by testkart on 10/4/17.
 */

/*

 1 Check network, validation, view initialization, wrap in try catch block
 2 make api call for forgot password, parse and check status
 3 if response data is ok then check status for true / false
 4 true, then show success message : A Verification code is send to your email id.
 5 false, message : this email id is not registred with us


 */

public class ForgotPasswordActivity extends AppCompatActivity implements Validator.ValidationListener{

    private Button viewButtonSubmit;

    @NotEmpty
    private EditText viewInputEmail;

    private TextView viewBack;

    private Validator validator;
    private Context context = this;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //initialization
        initialization();
    }

    private void initialization() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Forgot Password");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        validator = new Validator(this);
        validator.setValidationListener(this);

        viewBack = (TextView)findViewById(R.id.viewBack);
        viewInputEmail = (EditText)findViewById(R.id.viewInputEmail);
        viewButtonSubmit = (Button)findViewById(R.id.viewButtonSubmit);
        viewButtonSubmitListener();
        viewBackListener();

        backButton = (ImageView)findViewById(R.id.backButton);
        backButtonListener();

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



    private void viewBackListener() {

        viewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            //enable when api integrated
            passwordRetrieveProcess();

           // Toast.makeText(context, "Reset link has been send to your registred email id. ", Toast.LENGTH_SHORT).show();

           // finish();

        }else{

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void passwordRetrieveProcess() {

        //make api request
        //show success or fail messsage. If fail? then stay
        //else go to login screen
        //finish current activity

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<ResendModel> call = apiService.passwordResend(viewInputEmail.getText().toString().trim());
        call.enqueue(new Callback<ResendModel>() {
            @Override
            public void onResponse(Call<ResendModel> call, Response<ResendModel> response) {

                //hide progress dialog
                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null && response.body().getStatus()){

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();

                        //go to reset password
                        Intent resetPassword = new Intent(ForgotPasswordActivity.this, PasswordResetActivity.class);
                        resetPassword.putExtra(SessionManager.KEY_EMAIL, viewInputEmail.getText().toString().trim());
                        startActivity(resetPassword);

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
}
