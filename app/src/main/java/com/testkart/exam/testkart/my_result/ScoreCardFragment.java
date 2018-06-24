package com.testkart.exam.testkart.my_result;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.testkart.my_result.score_card_model.ScoreCardResponse;
import com.testkart.exam.testkart.my_result.score_card_model.Scorecard;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elfemo on 17/8/17.
 */

public class ScoreCardFragment extends Fragment {

    private SessionManager session;
    private ProgressDialog progressDialog;

    private TextView scTotalStudent, scMyMarks, scCorrectQuestion, scIncorrectQuestion, scTotalMarksTest,
            scMyPercentile, scRightMarks, scNegativeMarks, scTotalQuestion,
            scTotalAnsweredQuestion, scLeftQuestion, scLeftQuestionMarks, scTotalTime,
            scMyTime, scMyRank, scResult;

    private String resultId;
    private TextView scoreCardHeading;

    public void updateResultId(String resultId){

        this.resultId = resultId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_my_result_score_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        session = new SessionManager(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");

        scTotalStudent = (TextView)view.findViewById(R.id.scTotalStudent);
        scMyMarks = (TextView)view.findViewById(R.id.scMyMarks);
        scCorrectQuestion = (TextView)view.findViewById(R.id.scCorrectQuestion);
        scIncorrectQuestion = (TextView)view.findViewById(R.id.scIncorrectQuestion);
        scTotalMarksTest = (TextView)view.findViewById(R.id.scTotalMarksTest);
        scMyPercentile = (TextView)view.findViewById(R.id.scMyPercentile);
        scRightMarks = (TextView)view.findViewById(R.id.scRightMarks);
        scNegativeMarks = (TextView)view.findViewById(R.id.scNegativeMarks);
        scTotalQuestion = (TextView)view.findViewById(R.id.scTotalQuestion);
        scTotalAnsweredQuestion = (TextView)view.findViewById(R.id.scTotalAnsweredQuestion);
        scLeftQuestion = (TextView)view.findViewById(R.id.scLeftQuestion);
        scLeftQuestionMarks = (TextView)view.findViewById(R.id.scLeftQuestionMarks);
        scTotalTime = (TextView)view.findViewById(R.id.scTotalTime);
        scMyTime = (TextView)view.findViewById(R.id.scMyTime);
        scMyRank = (TextView)view.findViewById(R.id.scMyRank);
        scResult = (TextView)view.findViewById(R.id.scResult);

        scoreCardHeading = (TextView)view.findViewById(R.id.scoreCardHeading);


        getScoreCardData();

    }


    private void getScoreCardData() {

        if (ApiClient.isNetworkAvailable(getContext())) {

            //show progress dialog
            progressDialog.show();

            makeAPiCall();

        } else {

            Toast.makeText(getContext(), Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ScoreCardResponse> call = apiService.getScoreCard(resultId, session.getUserDetails().get(SessionManager.KEY_STUDENT_ID));

        Log.v("Result : ", resultId+", "+session.getUserDetails().get(SessionManager.KEY_STUDENT_ID));

        progressDialog.show();

        call.enqueue(new Callback<ScoreCardResponse>() {
            @Override
            public void onResponse(Call<ScoreCardResponse> call, Response<ScoreCardResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if(response.body() != null){

                        if(response.body().getStatus().equals("true")){

                            updateViews(response.body().getScorecard());

                        }else{

                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }else{

                        Toast.makeText(getActivity(), "POResponse body is null: " + code, Toast.LENGTH_SHORT).show();

                    }


                }else {

                    Toast.makeText(getActivity(), "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ScoreCardResponse> call, Throwable t) {


                //hide progress dialog
                progressDialog.dismiss();

                Toast.makeText(getContext(), Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });

    }

    private void updateViews(Scorecard scorecard) {

        if(scorecard != null){

            Log.v("Scorecard", scorecard.toString());

            scoreCardHeading.setText("Score card for "+scorecard.getExamName());
            scTotalStudent.setText(scorecard.getStudentCount()+"");
            scMyMarks.setText(scorecard.getMyMarks()+"");
            scCorrectQuestion.setText(scorecard.getCorrectQuestions()+"");// = (TextView)view.findViewById(R.id.scCorrectQuestion);
            scIncorrectQuestion.setText(scorecard.getIncorrectQuestions()+"");// = (TextView)view.findViewById(R.id.scIncorrectQuestion);
            scTotalMarksTest.setText(scorecard.getTestMarks()+"");// = (TextView)view.findViewById(R.id.scTotalMarksTest);
            scMyPercentile.setText(scorecard.getMyPercentile()+"");// = (TextView)view.findViewById(R.id.scMyPercentile);
            scRightMarks.setText(scorecard.getRightMarks()+"");// = (TextView)view.findViewById(R.id.scRightMarks);
            scNegativeMarks.setText(scorecard.getNegativeMarks()+"");// = (TextView)view.findViewById(R.id.scNegativeMarks);
            scTotalQuestion.setText(scorecard.getTotalQuestions()+"");// = (TextView)view.findViewById(R.id.scTotalQuestion);
            scTotalAnsweredQuestion.setText(scorecard.getTotalQuestionAttempts()+"");// = (TextView)view.findViewById(R.id.scTotalAnsweredQuestion);
            scLeftQuestion.setText(scorecard.getUnattemptQuestions()+"");// = (TextView)view.findViewById(R.id.scLeftQuestion);
            scLeftQuestionMarks.setText(scorecard.getIncorrectQuestionsMarks()+"");// = (TextView)view.findViewById(R.id.scLeftQuestionMarks);
            scTotalTime.setText(scorecard.getTestTime()+"");// = (TextView)view.findViewById(R.id.scTotalTime);
            scMyTime.setText(scorecard.getTimeTaken()+"");// = (TextView)view.findViewById(R.id.scMyTime);
            scMyRank.setText(Html.fromHtml(scorecard.getMyRank()+""));// = (TextView)view.findViewById(R.id.scMyRank);
            scResult.setText(scorecard.getMyResult()+"");// = (TextView)view.findViewById(R.id.scResult);

        }else{

            Toast.makeText(getContext(), "Scorecard is null", Toast.LENGTH_SHORT).show();
        }
    }

}
