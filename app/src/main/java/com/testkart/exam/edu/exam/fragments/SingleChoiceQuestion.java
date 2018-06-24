package com.testkart.exam.edu.exam.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.exam.ExamActivity;
import com.testkart.exam.edu.exam.ibps.datamodel.DataPagerQuestion;
import com.testkart.exam.edu.exam.ibps.datamodel.DataQuestion;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.MknUtils;
import com.tooltip.Tooltip;

import worker8.com.github.radiogroupplus.RadioGroupPlus;


/**
 * Created by testkart on 4/5/17.
 */

public class SingleChoiceQuestion extends Fragment implements FragmentLifecycle, ExamActivity.HostActionListener {

    private RadioGroupPlus viewOptionSingleChoice;
    private WebView viewQuestion;
    private DataPagerQuestion question;
    private static SingleChoiceQuestion fInstance;

    private TextView viewQno;
    private int qNo = 0;
    long questionTimeSpend;
    private DBHelper dbHelper;
    private Context context;
    private FragmentActionListener mListener;
    private String newExam;

    //option views
    private LinearLayout optContainer1, optContainer2, optContainer3, optContainer4, optContainer5, optContainer6;
    private RadioButton radio1, radio2, radio3, radio4, radio5, radio6;
    private TextView opt1, opt2, opt3, opt4, opt5, opt6;

    private TextView marksView;
    private ImageButton btnMarkReview;

  //  private android.support.v4.widget.NestedScrollView mkn;

    public static SingleChoiceQuestion getInstance(){

        if(fInstance == null){

            fInstance = new SingleChoiceQuestion();

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

        try{

            if(question.getQuestionStat().getPassageInclude().equalsIgnoreCase("Yes")){

                return inflater.inflate(R.layout.layout_passage_single_choice, container, false);

            }else{

                return inflater.inflate(R.layout.layout_question_single_choice, container, false);
            }

        }catch (Exception e){

            return inflater.inflate(R.layout.layout_question_single_choice, container, false);
        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // mkn = (android.support.v4.widget.NestedScrollView) view.findViewById(R.id.mkn);

        viewQuestion = (WebView)view.findViewById(R.id.viewQuestion);
        viewOptionSingleChoice = (RadioGroupPlus)view.findViewById(R.id.viewOptionSingleChoice);

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


        radio1 = (RadioButton) view.findViewById(R.id.radio1);
        radio2 = (RadioButton) view.findViewById(R.id.radio2);
        radio3 = (RadioButton) view.findViewById(R.id.radio3);
        radio4 = (RadioButton) view.findViewById(R.id.radio4);
        radio5 = (RadioButton) view.findViewById(R.id.radio5);
        radio6 = (RadioButton) view.findViewById(R.id.radio6);

        opt1 = (TextView) view.findViewById(R.id.opt1);
        opt2 = (TextView) view.findViewById(R.id.opt2);
        opt3 = (TextView) view.findViewById(R.id.opt3);
        opt4 = (TextView) view.findViewById(R.id.opt4);
        opt5 = (TextView) view.findViewById(R.id.opt5);
        opt6 = (TextView) view.findViewById(R.id.opt6);


        btnMarkReview = (ImageButton) view.findViewById(R.id.btnMarkReview);
        marksView = (TextView) view.findViewById(R.id.marksView);

        marksView.setText("Max Marks: "+question.getQuestionStat().getMarks());


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
                        Toast.makeText(context, "Marked for Review", Toast.LENGTH_SHORT).show();

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

        /*//loadQuestion
        loadQuestion();

        //load options
        loadOptions();*/
    }


    private void loadOptions() {

        String previousResponse = "-1";
        if(newExam.equals("false")){

            previousResponse = dbHelper.getQuestionResponse(question.getQuestionStat().getQuestionId(), DBHelper.KEY_YOUR_ANSWER);
        }


        String[] jumbledId = question.getQuestionStat().getJumbleOptions().split(",");
        for (int i = 0; i < 6; i++) {

            String id = jumbledId[i];

            fun1(i, id, previousResponse);

        }

        optContainer1Listener();
        optContainer2Listener();
        optContainer3Listener();
        optContainer4Listener();
        optContainer5Listener();
        optContainer6Listener();


        viewOptionSingleChoice.setOnCheckedChangeListener(new  RadioGroupPlus.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroupPlus group, int checkedId) {

                // RadioButton rb = (RadioButton) group.findViewById(checkedId);


                int id = viewOptionSingleChoice.getCheckedRadioButtonId();
                RadioButton rd = (RadioButton) viewOptionSingleChoice.findViewById(id);

                if(rd != null){

                    int index = Integer.parseInt((String)rd.getTag());

                    Log.v("OnChecked", "Did you Call me : "+index);

                    //update response
                    dbHelper.updateQuestionResponse(question.getQuestionStat().getQuestionId(), index+"", DBHelper.KEY_YOUR_ANSWER);

                    dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "1", DBHelper.KEY_QUESTION_ANSWERED);

                    updateQuestionStatus();

                }else{

                    dbHelper.updateQuestionResponse(question.getQuestionStat().getQuestionId(), "", DBHelper.KEY_YOUR_ANSWER);

                    dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "0", DBHelper.KEY_QUESTION_ANSWERED);

                    updateQuestionStatus();

                }

            }

        });


    }

    private void optContainer6Listener() {

        optContainer6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "6", Toast.LENGTH_SHORT).show();

                if(radio6.isChecked()){

                    viewOptionSingleChoice.clearCheck();

                }else{

                    radio6.setChecked(true);
                }

            }
        });

    }

    private void optContainer5Listener() {

        optContainer5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "5", Toast.LENGTH_SHORT).show();

                if(radio5.isChecked()){

                    viewOptionSingleChoice.clearCheck();

                }else{

                    radio5.setChecked(true);
                }

            }
        });
    }

    private void optContainer4Listener() {

        optContainer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();

                if(radio4.isChecked()){

                    viewOptionSingleChoice.clearCheck();

                }else{

                    radio4.setChecked(true);
                }

            }
        });
    }

    private void optContainer3Listener() {

        optContainer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();

                if(radio3.isChecked()){

                    viewOptionSingleChoice.clearCheck();

                }else{

                    radio3.setChecked(true);
                }

            }
        });
    }

    private void optContainer2Listener() {

        optContainer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();

                if(radio2.isChecked()){

                    viewOptionSingleChoice.clearCheck();

                }else{

                    radio2.setChecked(true);
                }


            }
        });
    }

    private void optContainer1Listener() {

        optContainer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();

                if(radio1.isChecked()){

                    viewOptionSingleChoice.clearCheck();

                }else{

                    radio1.setChecked(true);
                }

            }
        });

    }


    private void fun1(int position, String optionNo, String previousResponse) {

        switch (position){

            case 0 :

                String o1 = getOptionFromNo(optionNo);

                if(o1 != null && !o1.isEmpty()){

                    optContainer1.setVisibility(View.VISIBLE);
                    radio1.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        radio1.setChecked(true);

                    }

                    //load option in webview
                    loadOptionText(opt1, o1);

                }


                break;

            case 1 :

                String o2 = getOptionFromNo(optionNo);

                if(o2 != null && !o2.isEmpty()){

                    optContainer2.setVisibility(View.VISIBLE);
                    radio2.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        radio2.setChecked(true);

                    }

                    //load option in webview
                    loadOptionText(opt2, o2);

                }

                break;

            case 2 :

                String o3 = getOptionFromNo(optionNo);

                if(o3 != null && !o3.isEmpty()){

                    optContainer3.setVisibility(View.VISIBLE);
                    radio3.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        radio3.setChecked(true);

                    }

                    //load option in webview
                    loadOptionText(opt3, o3);

                }

                break;

            case 3 :

                String o4 = getOptionFromNo(optionNo);

                if(o4 != null && !o4.isEmpty()){

                    optContainer4.setVisibility(View.VISIBLE);
                    radio4.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        radio4.setChecked(true);

                    }

                    //load option in webview
                    loadOptionText(opt4, o4);

                }

                break;

            case 4 :

                String o5 = getOptionFromNo(optionNo);

                if(o5 != null && !o5.isEmpty()){

                    optContainer5.setVisibility(View.VISIBLE);
                    radio5.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        radio5.setChecked(true);

                    }

                    //load option in webview
                    loadOptionText(opt5, o5);

                }

                break;

            case 5 :

                String o6 = getOptionFromNo(optionNo);

                if(o6 != null && !o6.isEmpty()){

                    optContainer6.setVisibility(View.VISIBLE);
                    radio6.setTag(optionNo);

                    if(previousResponse.equals(position+"")){

                        radio6.setChecked(true);

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

    private void loadOptionText(TextView opt, String option1) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

            opt.setText(Html.fromHtml(option1, Html.FROM_HTML_MODE_COMPACT));

        }else{

            opt.setText(Html.fromHtml(option1));
        }



        /*WebSettings webSettings = opt.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //webSettings.setTextZoom(webSettings.getTextZoom() + 5);

        String option = MknUtils.changedHeaderHtml(option1);

        opt.loadData(option, "text/html", "UTF-8");*/

    }

    private void loadQuestion() {

        /*if(question.getQuestionStat().getPassageInclude().equalsIgnoreCase("Yes")){

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = 250;

            mkn.setLayoutParams(params);
            //mkn.(R.style.scrollbar_shape_style);

        }else{

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
           // params.height = 250;

            mkn.setLayoutParams(params);

        }*/


        WebSettings webSettings = viewQuestion.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webSettings.setTextZoom(webSettings.getTextZoom() + 10);

        viewQuestion.setWebChromeClient(new WebChromeClient());

        //check if this language question exist

        String mlq = question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG).getQuestionText();

        if(mlq != null){

            String q = MknUtils.changedHeaderHtml(mlq);

            viewQuestion.loadUrl("");
            viewQuestion.loadData(q, "text/html", "UTF-8");

        }else{

            Toast.makeText(context, ExamActivity.KEY_CURRENT_LANG+ " version question not available", Toast.LENGTH_SHORT).show();
        }


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

        //update drawer
       // updateQuestionStatus();

    }

    public void updateView(){

        //clear response
        //update db: 1 empty your answer, update column answer to 0
        //update response
        dbHelper.updateQuestionResponse(question.getQuestionStat().getQuestionId(), "", DBHelper.KEY_YOUR_ANSWER);

        dbHelper.updateQuestionStatus(question.getQuestionStat().getQuestionId(), "0", DBHelper.KEY_QUESTION_ANSWERED);


        //clear response
        viewOptionSingleChoice.clearCheck();
       // radio1.setChecked(false);
       // radio2.setChecked(false);
        //radio3.setChecked(false);
        //radio4.setChecked(false);
       // radio5.setChecked(false);
       // radio6.setChecked(false);

        updateQuestionStatus();

    }


    public void updateQuestionLanguage() {

        DataQuestion mlq = question.getQuestionMap().get(ExamActivity.KEY_CURRENT_LANG);

        if(mlq != null){

            loadQuestion();
            loadOptions();

        }else{

            Toast.makeText(context, ExamActivity.KEY_CURRENT_LANG+" version not available.", Toast.LENGTH_SHORT).show();
        }

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

}
