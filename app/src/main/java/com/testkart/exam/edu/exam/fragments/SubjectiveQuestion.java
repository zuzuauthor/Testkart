package com.testkart.exam.edu.exam.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.testkart.exam.R;
import com.testkart.exam.edu.exam.ExamActivity;
import com.testkart.exam.edu.exam.ibps.datamodel.DataPagerQuestion;
import com.testkart.exam.edu.exam.ibps.datamodel.DataQuestion;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.MknUtils;
import com.tooltip.Tooltip;

/**
 * Created by testkart on 4/5/17.
 */

public class SubjectiveQuestion extends Fragment implements FragmentLifecycle, ExamActivity.HostActionListener{


    private EditText viewInputAnswer;
    private ToggleButton viewAnswerButton;

    private WebView viewQuestion;
    private DataPagerQuestion question;
    private static SubjectiveQuestion fInstance;

    private TextView viewQno;
    private int qNo = 0;
    private DBHelper dbHelper;
    private long questionTimeSpend;
    private Context context;
    private String newExam;

    private TextView marksView;
    private ImageButton btnMarkReview;

    private FragmentActionListener mListener;

    public static SubjectiveQuestion getInstance(){

        if(fInstance == null){

            fInstance = new SubjectiveQuestion();

        }

        return fInstance;
    }

    public void setQuestion(DataPagerQuestion question, int qNo, DBHelper dbHelper, Context context, String newExam){

        this.newExam = newExam;
        this.question = question;
        this.qNo = qNo;
        this.dbHelper = dbHelper;

        this.context = context;

        this.mListener = (FragmentActionListener)context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_question_subjective, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewQuestion = view.findViewById(R.id.viewQuestion);

        viewQno = view.findViewById(R.id.viewQno);
        viewQno.setText("Question "+qNo);

        viewInputAnswer = view.findViewById(R.id.viewInputAnswer);

        viewAnswerButton = view.findViewById(R.id.viewAnswerButton);
        viewAnswerButtonListener();

        String previousResponse ;
        if(newExam.equals("false")){

            previousResponse = dbHelper.getQuestionResponse(question.getQuestionStat().getQuestionId(), DBHelper.KEY_YOUR_ANSWER);

            if(!previousResponse.isEmpty()){

                viewInputAnswer.setText(previousResponse);
                viewAnswerButton.setChecked(true);

            }

        }


        btnMarkReview = view.findViewById(R.id.btnMarkReview);
        marksView = view.findViewById(R.id.marksView);

        if(questionReviewStat.equals("3") || questionReviewStat.equals("4")){

            btnMarkReview.setImageResource(R.drawable.ic_bookmark_selected_24dp);

        }else{

            btnMarkReview.setImageResource(R.drawable.ic_bookmark_normal_black_24dp);

        }

        marksViewListener();
        btnMarkReviewListener();

    }




    private void btnMarkReviewListener() {

        btnMarkReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check review tag

                if(questionReviewStat.equals("3") || questionReviewStat.equals("4")){

                    btnMarkReview.setImageResource(R.drawable.ic_bookmark_normal_black_24dp);

                    //remove book mark
                    //update question mark review  1 for review   0 for not review
                    dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "0", DBHelper.KEY_QUESTION_REVIEW);

                    questionReviewStat = "0";

                }else{

                    btnMarkReview.setImageResource(R.drawable.ic_bookmark_selected_24dp);

                    //update question mark review  1 for review   0 for not review
                    dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "1", DBHelper.KEY_QUESTION_REVIEW);

                    questionReviewStat = "3";

                    Toast.makeText(context, "Mark as review", Toast.LENGTH_SHORT).show();

                }

                updateQuestionStatus();

            }

        });
    }

    private void marksViewListener() {

        marksView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tooltip tooltip = new Tooltip.Builder(marksView)
                        .setText("  Correct : "+question.getQuestionStat().getMarks()+"  "+"\n"+"  Negative : "+question.getQuestionStat().getNegativeMarks()+"  ")
                        .setCancelable(true)
                        .setTextColor(Color.parseColor("#fefefe"))
                        .setBackgroundColor(Color.parseColor("#2691a1"))
                        .setCornerRadius(5f)
                        .show();

            }
        });
    }


    private void viewAnswerButtonListener() {


        viewAnswerButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    // if toggle button is enabled/on


                    if(viewInputAnswer.getText().toString().isEmpty()){

                        dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "0", DBHelper.KEY_QUESTION_ANSWERED);

                    }else{

                        dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "1", DBHelper.KEY_QUESTION_ANSWERED);
                    }

                    dbHelper.updateQuestionResponse(question.getQuestionStat().getQuestionId(), viewInputAnswer.getText().toString(), DBHelper.KEY_YOUR_ANSWER);


                    viewInputAnswer.setEnabled(false);

                    updateQuestionStatus();

                }
                else{
                    // If toggle button is disabled/off

                    viewInputAnswer.setEnabled(true);
                }

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DataQuestion mlq = question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG);

        if(mlq != null){

            loadQuestion();
            // loadOptions();

        }else{

            Toast.makeText(context, ExamActivity.KEY_CURRENT_LANG+" version not available.", Toast.LENGTH_SHORT).show();
        }

        //loadQuestion
        //loadQuestion();
    }

    private void loadQuestion() {

       /* WebSettings webSettings = viewQuestion.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setTextZoom(webSettings.getTextZoom() + 10);

        //Log.v("Text Zoom :", webSettings.getTextZoom()+"mkn");

        viewQuestion.setWebChromeClient(new WebChromeClient());

        viewQuestion.loadData(question.getQuestion(), "text/html", "UTF-8");*/


        WebSettings webSettings = viewQuestion.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
       // webSettings.setTextZoom(webSettings.getTextZoom() + 10);

        viewQuestion.setWebChromeClient(new WebChromeClient());

        String q = MknUtils.changedHeaderHtml(question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG).getQuestionText());

        viewQuestion.loadUrl("");
        viewQuestion.loadData(q, "text/html", "UTF-8");

    }

    @Override
    public void onPauseFragment() {

        Log.v("Fragment ", "onPauseFragment");

        Log.v("Mills recorded QL ", System.currentTimeMillis()+" - "+tempPreviousTimeRecorded);

        questionTimeSpend = (System.currentTimeMillis() - tempPreviousTimeRecorded)/1000;
        tempPreviousTimeRecorded = tempNewTimeRecorded;
        tempNewTimeRecorded = 0;

        int fSec = Math.round(questionTimeSpend);
        Log.v("Total Time Sec", fSec+"");

        try{

            //record time stamp and update individual question spend time
         //   dbHelper.updateQuestionTimeSpend(question.getQuestionId(), fSec+"");

        }catch (Exception e){

            e.printStackTrace();
        }

    }



    private boolean justOnce = false;
    @Override
    public void onResumeFragment() {

        Log.v("Fragment ", "onResumeFragment");

        //record time stamp
        questionTimeSpend = 0;
        questionTimeSpend = System.currentTimeMillis();

    }


    @Override
    public void onClickMarkReview() {

        //update question mark review  1 for review   0 for not review
      //  dbHelper.updateQuestionStatus(question.getQuestionId(), "1", DBHelper.KEY_QUESTION_REVIEW);

        //update drawer
        //updateQuestionStatus();

    }


    static long tempPreviousTimeRecorded;
    static long tempNewTimeRecorded;
    private String questionReviewStat = "";
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            //record time stamp
            questionTimeSpend = 0;
            questionTimeSpend = System.currentTimeMillis();

            if(tempPreviousTimeRecorded == 0){

                tempPreviousTimeRecorded = questionTimeSpend;

            }else{

                tempNewTimeRecorded = questionTimeSpend;
            }

            Log.v("Mills recorded 1st time", questionTimeSpend+"");

            //make question visited
            if(!justOnce){

                justOnce = true;


                String at = MknUtils.getDateTime(System.currentTimeMillis());

                Log.v("Attempt Time: ", at);

                dbHelper.updateAttemptTime(question.getQuestionStat().getQuestionId(), at);

                dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "1", DBHelper.KEY_QUESTION_OPENED);

                updateQuestionStatus();
            }


            //get question stat
            questionReviewStat = dbHelper.getQuestionStatus(question.getQuestionStat().getQuestionId());
        }
    }


    private void updateQuestionStatus(){

        if(mListener != null){

          mListener.updateQuestionStatus1(question.getQuestionStat().getQuestionId(), question.getQuestionStat().getSubjectId());

        }
    }


    public void updateView(){

        //clear response
        //update db: 1 empty your answer, update column answer to 0
        //update response
        dbHelper.updateQuestionResponse(question.getQuestionStat().getQuestionId(), "", DBHelper.KEY_YOUR_ANSWER);

        dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "0", DBHelper.KEY_QUESTION_ANSWERED);


        //clear response
        viewInputAnswer.setText("");
        viewAnswerButton.setChecked(false);

        updateQuestionStatus();

    }

    public void updateQuestionLanguage() {

        DataQuestion mlq = question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG);

        if(mlq != null){

            loadQuestion();

        }else{

            Toast.makeText(context, ExamActivity.KEY_CURRENT_LANG+" version not available.", Toast.LENGTH_SHORT).show();
        }

    }

}
