package com.testkart.exam.edu.exam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.exam.feedback.Responses;
import com.testkart.exam.edu.exam.feedback.SubmitFeedbackResponse;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.myresult.MyResultActivity1;
import com.testkart.exam.edu.register.RegisterResponse;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by testkart on 28/4/17.
 */

public class FeedbackActivity extends AppCompatActivity {

    private SessionManager session;
    private ProgressDialog progressDialog;

    private Spinner viewSpinnerInstructions;
    private Spinner viewSpinnerLangauge;
    private Spinner viewSpinnerExperience;
    private EditText inputComment;
    private Button viewFeedbackSubmit;

    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Exam Feedback");
        setSupportActionBar(toolbar);

        /*if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }*/


        //initialization
        initialization();

    }

    private void initialization() {

        session = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Submitting Feedback");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        viewSpinnerInstructions = (Spinner)findViewById(R.id.viewSpinnerInstructions);
        viewSpinnerLangauge = (Spinner)findViewById(R.id.viewSpinnerLangauge);
        viewSpinnerExperience = (Spinner)findViewById(R.id.viewSpinnerExperience);
        inputComment = (EditText)findViewById(R.id.inputComment);

        viewFeedbackSubmit = (Button)findViewById(R.id.viewFeedbackSubmit);
        viewFeedbackSubmitListener();

    }

    private void viewFeedbackSubmitListener() {

        viewFeedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!inputComment.getText().toString().isEmpty()){

                    //make api call
                    if (ApiClient.isNetworkAvailable(context)) {

                        //show progress dialog
                        progressDialog.show();

                        makeAPiCall();

                    } else {

                        Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

                    }


                }else{

                    Toast.makeText(FeedbackActivity.this, "We would loved to hear you. Please provide some comments", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        // bundle feedback
        SubmitFeedbackResponse sfr = new SubmitFeedbackResponse();

        sfr.setExamResultId(Integer.parseInt(getIntent().getStringExtra(ExamActivity.KEY_RESULT_ID).trim()));
        sfr.setPrivateKey(session.getUserDetails().get(SessionManager.KEY_PRIVATE));
        sfr.setPublicKey(session.getUserDetails().get(SessionManager.KEY_PUBLIC));

        Responses r = new Responses();
        r.setComment1((String)viewSpinnerInstructions.getSelectedItem());
        r.setComment2((String)viewSpinnerLangauge.getSelectedItem());
        r.setComment3((String)viewSpinnerExperience.getSelectedItem());
        r.setComments(inputComment.getText().toString());

        sfr.setResponses(r);

        Call<RegisterResponse> call = apiService.submitFeedback(sfr);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {


                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body().getStatus()){

                        Toast.makeText(FeedbackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        //go to somewhere
                        Intent myResultActivity = new Intent(context, MyResultActivity1.class);
                        startActivity(myResultActivity);

                        finish();


                    }else{

                        Toast.makeText(FeedbackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(FeedbackActivity.this, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
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

    }


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }*/


    @Override
    protected void onStop() {

        //refresh dashboard
        Consts.KEY_REFRESH_DASHBOARD = true;

        super.onStop();
    }
}
