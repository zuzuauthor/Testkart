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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.exam.ExamActivity;
import com.testkart.exam.edu.exam.ibps.datamodel.DataPagerQuestion;
import com.testkart.exam.edu.exam.ibps.datamodel.DataQuestion;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.MknUtils;
import com.tooltip.Tooltip;

import java.util.ArrayList;

/**
 * Created by testkart on 4/5/17.
 */

public class MultipleChoiceQuestion extends Fragment implements FragmentLifecycle, ExamActivity.HostActionListener {



    private WebView viewQuestion;
    private LinearLayout viewOptionGroup;

    private static MultipleChoiceQuestion fInstance;
    private DataPagerQuestion question;

    private TextView viewQno;
    private int qNo = 0;


    //option views
    private LinearLayout optContainer1, optContainer2, optContainer3, optContainer4, optContainer5, optContainer6;
    private CheckBox check1, check2, check3, check4, check5, check6;
    private WebView opt1, opt2, opt3, opt4, opt5, opt6;

    private TextView marksView;
    private ImageButton btnMarkReview;


    private long questionTimeSpend = 0;
    private DBHelper dbHelper;
    private Context context;

    private String newExam;

    private FragmentActionListener mListener;

    public static MultipleChoiceQuestion getInstance(){

        if(fInstance == null){

            fInstance = new MultipleChoiceQuestion();

        }

        return fInstance;
    }

    public void setQuestion(DataPagerQuestion question, int qNo, DBHelper dbHelper, Context context, String newExam){

        this.newExam = newExam;

        this.question = question;
        this.qNo = qNo;

        this.dbHelper = dbHelper;

        this.context = context;
        mListener = (FragmentActionListener)context;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_question_multiple_choice, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewQuestion = (WebView)view.findViewById(R.id.viewQuestion);
        viewOptionGroup = (LinearLayout)view.findViewById(R.id.viewOptionGroup);

        viewQno = (TextView)view.findViewById(R.id.viewQno);
        viewQno.setText("Question "+qNo);


        //init opt views;
        optContainer1 = (LinearLayout) view.findViewById(R.id.optContainer1);
        optContainer2 = (LinearLayout) view.findViewById(R.id.optContainer2);
        optContainer3 = (LinearLayout) view.findViewById(R.id.optContainer3);
        optContainer4 = (LinearLayout) view.findViewById(R.id.optContainer4);
        optContainer5 = (LinearLayout) view.findViewById(R.id.optContainer5);
        optContainer6 = (LinearLayout) view.findViewById(R.id.optContainer6);

        optContainer1.setVisibility(View.GONE);
        optContainer2.setVisibility(View.GONE);
        optContainer3.setVisibility(View.GONE);
        optContainer4.setVisibility(View.GONE);
        optContainer5.setVisibility(View.GONE);
        optContainer6.setVisibility(View.GONE);


        check1 = (CheckBox) view.findViewById(R.id.check1);
        check2 = (CheckBox) view.findViewById(R.id.check2);
        check3 = (CheckBox) view.findViewById(R.id.check3);
        check4 = (CheckBox) view.findViewById(R.id.check4);
        check5 = (CheckBox) view.findViewById(R.id.check5);
        check6 = (CheckBox) view.findViewById(R.id.check6);

        checkListener(check1);
        checkListener(check2);
        checkListener(check3);
        checkListener(check4);
        checkListener(check5);
        checkListener(check6);


        opt1 = (WebView) view.findViewById(R.id.opt1);
        opt2 = (WebView) view.findViewById(R.id.opt2);
        opt3 = (WebView) view.findViewById(R.id.opt3);
        opt4 = (WebView) view.findViewById(R.id.opt4);
        opt5 = (WebView) view.findViewById(R.id.opt5);
        opt6 = (WebView) view.findViewById(R.id.opt6);


        btnMarkReview = (ImageButton) view.findViewById(R.id.btnMarkReview);
        marksView = (TextView) view.findViewById(R.id.marksView);

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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DataQuestion mlq = question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG);

        if(mlq != null){

            loadQuestion();
            loadOptions();

        }else{

            Toast.makeText(context, ExamActivity.KEY_CURRENT_LANG+" version not available.", Toast.LENGTH_SHORT).show();
        }

        //load options
        //loadOptions();
    }



    public static boolean contains(String s, char value){
        return s != null && s.indexOf(value) > -1;
    }


    private void loadOptions() {

        ArrayList<LinearLayout> opl = new ArrayList<>();

        String previousResponse = "-1";
        if(newExam.equals("false")){

            previousResponse = dbHelper.getQuestionResponse(question.getQuestionStat().getQuestionId(), DBHelper.KEY_YOUR_ANSWER);
        }


        String[] jumbledId = question.getQuestionStat().getJumbleOptions().split(",");
        for (int i = 0; i < 6; i++) {

            String id = jumbledId[i];

            fun1(i, id, previousResponse);

        }

    }


    private void checkListener(final CheckBox checkBox){

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String preResponse = dbHelper.getQuestionResponse(question.getQuestionStat().getQuestionId(), DBHelper.KEY_YOUR_ANSWER);

                String index = (String)checkBox.getTag();

                if(isChecked){
                    //update response, update question status
                    //get response from db

                    if(preResponse.length()>0){

                        preResponse = preResponse+","+index;

                    }else{

                        //update new answer
                        preResponse = index;
                    }

                    preResponse = preResponse.replaceAll(",$", "");

                    dbHelper.updateQuestionResponse(question.getQuestionStat().getQuestionId(), preResponse, DBHelper.KEY_YOUR_ANSWER);


                }else{

                    //remove response from db

                    if(preResponse.length()>0){

                        String [] responses = preResponse.split(",");

                        String newResponse = "";

                        for (int k = 0; k<responses.length; k++) {

                            if(!responses[k].equals(index)){

                                if(k<responses.length){

                                    newResponse += responses[k]+",";
                                }else{

                                    newResponse += responses[k];
                                }

                            }
                        }

                        newResponse = newResponse.replaceAll(",$", "");

                        dbHelper.updateQuestionResponse(question.getQuestionStat().getQuestionId(), newResponse, DBHelper.KEY_YOUR_ANSWER);

                    }else{

                        //do nothing
                    }

                }



                String check = dbHelper.getQuestionResponse(question.getQuestionStat().getQuestionId(), DBHelper.KEY_YOUR_ANSWER);

                if(check.isEmpty()){
                    //make q answer status  0

                    dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "0", DBHelper.KEY_QUESTION_ANSWERED);

                }else{

                    dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "1", DBHelper.KEY_QUESTION_ANSWERED);
                }


                updateQuestionStatus();

            }
        });
    }



    private void fun1(int position, String optionNo, String previousResponse) {

        switch (position){

            case 0 :

                String o1 = getOptionFromNo(optionNo);

                if(o1 != null && !o1.isEmpty()){

                    optContainer1.setVisibility(View.VISIBLE);
                    check1.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        check1.setChecked(true);

                    }

                    //load option in webview
                    loadOptionText(opt1, o1);

                }


                break;

            case 1 :

                String o2 = getOptionFromNo(optionNo);

                if(o2 != null && !o2.isEmpty()){

                    optContainer2.setVisibility(View.VISIBLE);
                    check2.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        check2.setChecked(true);

                    }

                    //load option in webview
                    loadOptionText(opt2, o2);

                }

                break;

            case 2 :

                String o3 = getOptionFromNo(optionNo);

                if(o3 != null && !o3.isEmpty()){

                    optContainer3.setVisibility(View.VISIBLE);
                    check3.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        check3.setChecked(true);

                    }

                    //load option in webview
                    loadOptionText(opt3, o3);

                }

                break;

            case 3 :

                String o4 = getOptionFromNo(optionNo);

                if(o4 != null && !o4.isEmpty()){

                    optContainer4.setVisibility(View.VISIBLE);
                    check4.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        check4.setChecked(true);

                    }

                    //load option in webview
                    loadOptionText(opt4, o4);

                }

                break;

            case 4 :

                String o5 = getOptionFromNo(optionNo);

                if(o5 != null && !o5.isEmpty()){

                    optContainer5.setVisibility(View.VISIBLE);
                    check5.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        check5.setChecked(true);

                    }

                    //load option in webview
                    loadOptionText(opt5, o5);

                }

                break;

            case 5 :

                String o6 = getOptionFromNo(optionNo);

                if(o6 != null && !o6.isEmpty()){

                    optContainer6.setVisibility(View.VISIBLE);
                    check6.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        check6.setChecked(true);

                    }

                    //load option in webview
                    loadOptionText(opt6, o6);
                }

                break;
        }

    }

    private String getOptionFromNo(String optionNo) {

        String option = "";

        switch (optionNo){

            case "1":

                option =  question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG).getOption1();

                break;

            case "2":

                option = question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG).getOption2();

                break;

            case "3":

                option = question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG).getOption3();

                break;

            case "4":

                option = question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG).getOption4();

                break;

            case "5":

                option = question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG).getOption5();

                break;

            case "6":

                option = question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG).getOption6();

                break;

        }

        return option;
    }

    private void loadOptionText(WebView opt, String option1) {


        WebSettings webSettings = opt.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //webSettings.setTextZoom(webSettings.getTextZoom() + 5);

        String option = MknUtils.changedHeaderHtml(option1);

        opt.loadData(option, "text/html", "UTF-8");

    }




    private void loadQuestion() {


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

        Log.v("Mills recorded QL ", System.currentTimeMillis()+" - "+tempPreviousTimeRecorded);

        questionTimeSpend = (System.currentTimeMillis() - tempPreviousTimeRecorded)/1000;
        tempPreviousTimeRecorded = tempNewTimeRecorded;
        tempNewTimeRecorded = 0;

        int fSec = Math.round(questionTimeSpend);
        Log.v("Total Time Sec", fSec+"");

        try{

            //record time stamp and update individual question spend time
            dbHelper.updateQuestionTimeSpend(question.getQuestionStat().getQuestionId(), fSec+"");

            //dbHelper.updateQuestionIsAnswered(question.getQuestionId(), DBHelper.KEY_QUESTION_RESPONSE_MULTIPLE_CHOICE);


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

        //update question visit  1 for visited  0 for not visited
        dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "1", DBHelper.KEY_QUESTION_OPENED);

    }

    @Override
    public void onClickMarkReview() {

        //update question mark review  1 for review   0 for not review
       // dbHelper.updateQuestionStatus(question.getQuestionId(), "1", DBHelper.KEY_QUESTION_REVIEW);

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
        check1.setChecked(false);
        check2.setChecked(false);
        check3.setChecked(false);
        check4.setChecked(false);
        check5.setChecked(false);
        check6.setChecked(false);


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
