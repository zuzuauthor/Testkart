package com.testkart.exam.edu.exam;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.MyExamActivity1;
import com.testkart.exam.edu.exam.examlist.ResultViewActivity;
import com.testkart.exam.edu.exam.fragments.FragmentActionListener;
import com.testkart.exam.edu.exam.fragments.FragmentLifecycle;
import com.testkart.exam.edu.exam.ibps.adapter.LanguageAdapter;
import com.testkart.exam.edu.exam.ibps.datamodel.DataExam;
import com.testkart.exam.edu.exam.ibps.datamodel.DataLanguage;
import com.testkart.exam.edu.exam.ibps.datamodel.DataPagerQuestion;
import com.testkart.exam.edu.exam.ibps.datamodel.DataResponse;
import com.testkart.exam.edu.exam.ibps.datamodel.DataSubject;
import com.testkart.exam.edu.exam.model.ExamSubmitModel;
import com.testkart.exam.edu.exam.service.BroadcastService;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.register.RegisterResponse;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.thefinestartist.utils.content.Ctx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by testkart on 26/4/17.
 */

public class ExamActivity extends AppCompatActivity implements FinilizeExamDialog.OnExamFinishListener,
        ExpandableListAdapter.OnQuestionPickListener,
        FragmentActionListener {

    private static final String TAG = "ExamActivity";

    public static String KEY_CURRENT_LANG = "";

    private SessionManager session;
    private Context context = this;

    private ImageButton viewInstructionButton, viewDrawerButton;

    private int lastExpandedPosition = -1;

    private GridView gridview;
    private ArrayList<DataResponse> qList;
    private ArrayList<QuestionGridModel> ql = new ArrayList<>();
    private QuestionGridAdapter qGridAdapter;

    private CircleImageView viewProfilePic;
    private Button viewFinishButton;
    private TextView viewMarkReview, viewSaveNext, viewSubjectName;
    private DrawerLayout drawerLayout;
    private AppCompatSpinner langSpinner;

    private TextView viewTimer;
    private ViewPager viewpager;
    private QuestionPagerAdapter pagerAdapter;
    private DBHelper dbHelper;

    private ArrayList<ExpandableHeader> listDataHeader;
    private HashMap<ExpandableHeader, ArrayList<QuestionGridModel>> listDataChild;

    private boolean automatic = false;

    private ProgressDialog progressDialog;

    private HostActionListener mListener;

    private TextView txtNotVisit, txtAnswered, txtNotAnswered, txtReview, txtReviewAnswer;

    private Button btnInstruction, btnQuestionPaper;

    public interface HostActionListener {

        void onClickMarkReview();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //initialization();
        initialization();

    }


    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(this, BroadcastService.class));
        Log.i(TAG, "Started service");
    }


    ArrayList<DataPagerQuestion> questionList;
    com.testkart.exam.edu.exam.ibps.model.Student student;
    String studentImageUrl;
    String instructionText;
    String newExam;

    ArrayList<String>  spinnerLangList = new ArrayList<>();

    private void initialization() {

        session = new SessionManager(this);

        Bundle args = getIntent().getBundleExtra("B");
        studentImageUrl = args.getString(MyExamActivity1.KEY_STUDENT_IMAGE);
        student = (com.testkart.exam.edu.exam.ibps.model.Student) args.getSerializable(MyExamActivity1.KEY_STUDENT);
        instructionText = args.getString(ExamInstructionDialog.KEY_INSTRUCTIONS);
        newExam = args.getString(MyExamActivity1.KEY_NEW_EXAM);

        KEY_CURRENT_LANG = "English";/*args.getString(ExamListActivity1.KEY_LANGUAGE);*/

        dbHelper = new DBHelper(this);

        questionList = new ArrayList<>();
        questionList = dbHelper.getPagerQuestion();

        txtNotVisit = (TextView)findViewById(R.id.txtNotVisit);
        txtAnswered = (TextView)findViewById(R.id.txtAnswered);
        txtNotAnswered = (TextView)findViewById(R.id.txtNotAnswered);
        txtReview = (TextView)findViewById(R.id.txtReview);
        txtReviewAnswer = (TextView)findViewById(R.id.txtReviewAnswer);

        viewTimer = (TextView) findViewById(R.id.viewTimer);

        langSpinner = (AppCompatSpinner) findViewById(R.id.langSpinner);
        ArrayList<DataLanguage> langList = dbHelper.getLanguages();


        for (DataLanguage lang:
             langList) {

            spinnerLangList.add(lang.getLangName());

        }

        langSpinner.setAdapter(new LanguageAdapter(context, spinnerLangList));

        //set default language
        langSpinner.setSelection(spinnerLangList.indexOf(KEY_CURRENT_LANG));

        langSpinnerListener();

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), questionList, dbHelper, this, newExam);
        viewpagerListener();

        viewpager.setAdapter(pagerAdapter);

        gridview = (GridView) findViewById(R.id.gridview);

        // preparing list data
        perepareDataSet();
        qGridAdapter = new QuestionGridAdapter(context, ql);
        gridview.setAdapter(qGridAdapter);

        gridviewListener();


        txtNotVisit.setText((ql.size()-1)+"");
        txtAnswered.setText("0");
        txtNotAnswered.setText("1");
        txtReview.setText("0");
        txtReviewAnswer.setText("0");


        //init views
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        viewInstructionButton = (ImageButton) findViewById(R.id.viewInstructionButton);
        viewDrawerButton = (ImageButton) findViewById(R.id.viewDrawerButton);

        viewProfilePic = (CircleImageView) findViewById(R.id.viewProfilePic);

        if (studentImageUrl != null) {

            Glide
                    .with(this)
                    .load(studentImageUrl)
                    .apply(new RequestOptions().centerCrop())
                    //.placeholder(R.drawable.loading_spinner)
                    //.crossFade()
                    .into(viewProfilePic);
        }


        viewFinishButton = findViewById(R.id.viewFinishButton);
        viewMarkReview = findViewById(R.id.viewMarkReview);
        viewSaveNext = findViewById(R.id.viewSaveNext);
        viewSubjectName = findViewById(R.id.viewSubjectName);
        viewSubjectName.setText(questionList.get(0).getQuestionStat().getSubjectName());

        viewSubjectNameListener();

        btnInstruction = findViewById(R.id.btnInstruction);
        btnQuestionPaper = findViewById(R.id.btnQuestionPaper);

        viewInstructionButtonListener();
        viewDrawerButtonListener();
        viewProfilePicListener();
        viewFinishButtonListerner();
        viewMarkReviewListener();
        viewSaveNextListener();


        //interface
        mListener = (HostActionListener) pagerAdapter.getItem(currentPosition);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        /*if(newExam.equals("false")){

            new AsyncTask<String, String, String>(){

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    progressDialog.setTitle("Starting Exam");
                    progressDialog.show();

                }

                @Override
                protected String doInBackground(String... params) {

                    updateResponses();

                    return null;
                }


                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    progressDialog.dismiss();
                }
            }.execute(null, null, null);



        }*/

    }

    private void viewSubjectNameListener() {

        viewSubjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ArrayList<DataSubject> subjectList = dbHelper.getAllSubjects();

                SubjectAdaapter adaapter = new SubjectAdaapter(context, subjectList);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Subjects")
                        .setAdapter(adaapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //load 1st question of the subject
                                viewpager.setCurrentItem(Integer.parseInt(subjectList.get(which).getOrdering()), true);

                            }
                        });

                builder.create().show();

            }
        });
    }

    private void gridviewListener() {

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                QuestionGridModel qm = (QuestionGridModel) gridview.getItemAtPosition(position);

                int qid = Integer.parseInt(qm.getQuestionId());

                viewpager.setCurrentItem(position, true);

                drawerLayout.closeDrawer(GravityCompat.END);

            }
        });
    }


    //ArrayList<DataSubject> subList = new ArrayList<>();
    private void perepareDataSet() {

        qList = dbHelper.getQuestionList();
       // subList = dbHelper.getAllSubjects();

        for (DataResponse dResponse:
                qList) {

            ql.add(new QuestionGridModel(dResponse.getQuestionId(), dResponse.getQuestionLocalId(), dbHelper.getQuestionStatus(dResponse.getQuestionId())));


        }
    }

    boolean secondTime = false;

    private void langSpinnerListener() {

        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String dl = (String) langSpinner.getItemAtPosition(position);

                KEY_CURRENT_LANG = (String) langSpinner.getSelectedItem();



                if(secondTime){

                  //  Toast.makeText(context, "Hello from spinner listener 2nd time", Toast.LENGTH_SHORT).show();

                    /*//update fragments
                    Fragment f = pagerAdapter.getItem(currentPosition);

                    ((SingleChoiceQuestion)f).selectDiffLngQuestion(KEY_CURRENT_LANG);

                    if(f instanceof SingleChoiceQuestion){



                    }else if(f instanceof MultipleChoiceQuestion){


                    }*/

                    pagerAdapter.updateData(2);
                   // pagerAdapter.notifyDataSetChanged();

                }else{

                    secondTime = true;

                   // Toast.makeText(context, "Hello from spinner listener 1st time", Toast.LENGTH_SHORT).show();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //if exam resume
    private void updateResponses() {

        for (DataPagerQuestion question:
             questionList) {

            updateQuestionStatus1(question.getQuestionStat().getQuestionId(), question.getQuestionStat().getSubjectId());

        }
    }



    int currentPosition;

    private void viewpagerListener() {

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int newPosition) {

                FragmentLifecycle fragmentToShow = (FragmentLifecycle) pagerAdapter.getItem(newPosition);
                fragmentToShow.onResumeFragment();

                FragmentLifecycle fragmentToHide = (FragmentLifecycle) pagerAdapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();

                currentPosition = newPosition;

                DataPagerQuestion q = questionList.get(currentPosition);
                viewSubjectName.setText(q.getQuestionStat().getSubjectName());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void viewInstructionButtonListener() {

        btnInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataExam examDetails = dbHelper.getExam();

                //show instruction dialog
               /* ExamDetailDialog ed = new ExamDetailDialog();

                Bundle args = new Bundle();
                args.putSerializable("ED", examDetails);
                args.putBoolean("EXAM", false);
                ed.setArguments(args);
                ed.show(getSupportFragmentManager(), "ed");*/


                ExamInstructionDialog eid = new ExamInstructionDialog();
                Bundle args = new Bundle();
                args.putString(ExamInstructionDialog.KEY_INSTRUCTIONS, instructionText);
                eid.setArguments(args);

                eid.show(getSupportFragmentManager(), "EID");

            }
        });


    }

    private void viewDrawerButtonListener() {

        viewDrawerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //open and close drawer
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {

                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {

                    drawerLayout.openDrawer(GravityCompat.END);
                }

            }
        });

    }

    private void viewProfilePicListener() {

        viewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //show student / user profile dialog
                ProfileScreenDialog pd = new ProfileScreenDialog();

                Bundle args = new Bundle();
                args.putSerializable(MyExamActivity1.KEY_STUDENT, student);
                args.putString(MyExamActivity1.KEY_STUDENT_IMAGE, studentImageUrl);

                //Log.v("Student Url", studentImageUrl);

                pd.setArguments(args);

                pd.show(getSupportFragmentManager(), "pd");

            }
        });

    }

    private void viewFinishButtonListerner() {


        viewFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> examSummary = dbHelper.getQuestionStatSummary();

                //show confermiation dialog
                FinilizeExamDialog fd = new FinilizeExamDialog();

                Bundle args = new Bundle();

                args.putSerializable("EXAM_SUMMARY", examSummary);

                fd.setArguments(args);

                fd.show(getSupportFragmentManager(), "FD");

            }
        });

    }


    private void viewMarkReviewListener() {

        viewMarkReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pagerAdapter.updateData(1);


                //mark review and next question
                /*if (viewpager.getCurrentItem() < pagerAdapter.getCount()) {

                    //update db and net
                    if(mListener != null){

                        mListener.onClickMarkReview();
                    }


                    DataPagerQuestion q = questionList.get(viewpager.getCurrentItem());

                   // dbHelper.updateQuestionStatus(q.getQuestionId(), "1", DBHelper.KEY_QUESTION_REVIEW);

                   // updateQuestionStatus1(q.getQuestionId(), q.getSubjectId());

                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1, true);

                    Toast.makeText(ExamActivity.this, "Mark as review", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(ExamActivity.this, "No more questions", Toast.LENGTH_SHORT).show();
                }
*/
            }
        });

    }

    private void viewSaveNextListener() {

        viewSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //load next question
                if (viewpager.getCurrentItem()+1 < pagerAdapter.getCount()) {

                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1, true);

                }else{

                Log.v("Current item: ", viewpager.getCurrentItem()+"");
                Log.v("Total Questions", pagerAdapter.getCount()+"");



                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Are you sure?");
                    builder.setMessage("You have reached the last question of test, do you want to go to the first question again?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            viewpager.setCurrentItem(0, true);

                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //do nothing

                        }
                    });


                    builder.create().show();

                    //Toast.makeText(ExamActivity.this, "No more questions", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    //submit exam manually
    @Override
    public void onExamFinish() {

        if(!automatic){

            BroadcastService.finalize = true;

            if(ApiClient.isNetworkAvailable(this)){

                //enable when api integrated
                submitResponses();

            }else{

                Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

            }

        }else{

            //automatic
            whatNext(examSubmitResponse);

        }

    }


    private BroadcastReceiver br = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getExtras() != null) {

                if(intent.getBooleanExtra("finalize", false)){

                    // Time exhaust send responses
                    sendExamResponse();

                }else{

                    // continue with timer
                    updateGUI(intent); // or whatever method used to update your GUI fields
                }

            }

        }
    };

    private void sendExamResponse() {

        //send exam response retrofit once finish show finalize dialog
        viewTimer.setText("00:00:00");
        automatic = true;
        submitResponses();

       // showFinalizeDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        Log.i(TAG, "Registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }

        //refresh dashboard
        Consts.KEY_REFRESH_DASHBOARD = true;

        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, BroadcastService.class));
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            Log.i(TAG, "Countdown seconds remaining: " +  millisUntilFinished / 1000);

            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

            viewTimer.setText(hms);
        }
    }


    @Override
    public void onQuestionLoad(int qNo) {

        drawerLayout.closeDrawer(GravityCompat.END);
        viewpager.setCurrentItem(qNo - 1);
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {

            drawerLayout.closeDrawer(GravityCompat.END);
        }

        showFinalizeDialog();
    }

    private void showFinalizeDialog() {

        HashMap<String, String> examSummary = dbHelper.getQuestionStatSummary();

        //show confermiation dialog
        FinilizeExamDialog fd = new FinilizeExamDialog();

        Bundle args = new Bundle();

        args.putSerializable("EXAM_SUMMARY", examSummary);

        fd.setArguments(args);

        if (fd != null && !fd.isVisible()) {

            fd.show(getSupportFragmentManager(), "fd");

        } else {

            //do nothing

        }
    }


    @Override
    public void updateQuestionStatus1(String questionId, String subjectId) {

        Log.v("Q_STATUS ", "questionId: " + questionId + ", " + subjectId);

        String questionStatus = dbHelper.getQuestionStatus(questionId);

        Log.v("Q_STATUS ", "status: " + questionStatus);

        //subject id need

        for (QuestionGridModel q :
                ql) {

            if (q.getQuestionId().equals(questionId)) {

                q.setQuestionStatus(questionStatus);

                qGridAdapter.setData();

            } else {


            }

        }

        HashMap<String, String> qs = dbHelper.getQuestionStatSummary();
        String notVisit = qs.get(Consts.KEY_NOT_VISIT);
        String notAns = qs.get(Consts.KEY_NOTANSWERED);
        String ans = qs.get(Consts.KEY_ANSWERED);
        String review = qs.get(Consts.KEY_REVIEW);
        String reviewAns = qs.get(Consts.KEY_REVIEW_ANSWERED);

        txtAnswered.setText(ans);
        txtNotAnswered.setText(notAns);
        txtNotVisit.setText(notVisit);
        txtReview.setText(review);
        txtReviewAnswer.setText(reviewAns);

    }


    private RegisterResponse examSubmitResponse;
    private void submitResponses() {

        progressDialog.setTitle("Submitting Exam");
        progressDialog.show();

        final ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RegisterResponse> call = apiService.sendExamResponse(getExamResponse());

        call.enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                Log.v("POResponse", response.body().toString());

                progressDialog.cancel();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){

                            examSubmitResponse = response.body();

                            // empty tables
                            dbHelper.deleteRecords();
                            whatNext(response.body());

                        }else{

                            if(response.body().getMessage().equalsIgnoreCase("No exam opened")){

                                //clear db
                                dbHelper.deleteRecords();
                                //close exam screen
                                finish();

                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }else{

                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                    }else{

                        Toast.makeText(context, "Exam submit response body null", Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

                progressDialog.cancel();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }

        });

    }



    /*private ExamSubmitModel getExamResponse() {

        ArrayList<DataResponse> questionList = dbHelper.getQuestionList();

        DataExam exam = dbHelper.getExam();

        List<com.mkn.testkart.com.testkart.exam.edu.exam.model.POResponse> resList = new ArrayList<>();

        //bundle responses
        ExamSubmitModel esm = new ExamSubmitModel();
        esm.setEndTime(exam.getExamEndTime());
        esm.setExamId(Integer.parseInt(exam.getExamId()));
        esm.setPrivateKey(session.getUserDetails().get(SessionManager.KEY_PRIVATE));
        esm.setPublicKey(session.getUserDetails().get(SessionManager.KEY_PUBLIC));
        esm.setStartTime(exam.getExamStartTime());
        esm.setStudentId(Integer.parseInt("1"));


        for (DataResponse q:
                questionList) {

            com.mkn.testkart.com.testkart.exam.edu.exam.model.POResponse res = new com.mkn.testkart.com.testkart.exam.edu.exam.model.POResponse();

            res.setQuestionId(Integer.parseInt(q.getQuestionId()));
            res.setQuestionNo(Integer.parseInt(q.getQuestionLocalId()));
            res.setQuestionType(q.getQuestionType());

            switch (q.getQuestionType()){

                case "M":

                    res.setResponse(q.getMultipleChoiceResponse());

                    break;

                case "F":

                    res.setResponse(q.getFillBlankResponse());

                    break;

                case "S":

                    res.setResponse(q.getSubjectiveResponse());

                    break;

                case "T":

                    res.setResponse(q.getTrueFalseResponse());

                    break;

            }


            res.setAttemptTime(q.getAttemptTime());// add in db
            res.setOpened(Integer.parseInt(q.getOpened()));
            res.setAnswered(Integer.parseInt(q.getAnswered()));
            res.setReview(Integer.parseInt(q.getReview()));
            res.setTimeTaken(q.getTimeSpendOnQuestion());

            resList.add(res);

        }

        esm.setResponse(resList);

        Gson gson = new Gson();
        String j = gson.toJson(esm);

        Log.v("My_Json", j);

        return esm;
    }*/


    private ExamSubmitModel getExamResponse() {

        ArrayList<DataResponse> questionList = dbHelper.getQuestionList();

        DataExam exam = dbHelper.getExam();

        List<com.testkart.exam.edu.exam.model.Response> resList = new ArrayList<>();

        //bundle responses
        ExamSubmitModel esm = new ExamSubmitModel();
        esm.setEndTime(exam.getExamEndTime());
        esm.setExamId(Integer.parseInt(exam.getExamId()));
        esm.setPrivateKey(session.getUserDetails().get(SessionManager.KEY_PRIVATE));
        esm.setPublicKey(session.getUserDetails().get(SessionManager.KEY_PUBLIC));
        esm.setStartTime(exam.getExamStartTime());
        esm.setStudentId(Integer.parseInt("1"));


        for (DataResponse q:
                questionList) {

            com.testkart.exam.edu.exam.model.Response res = new com.testkart.exam.edu.exam.model.Response();

            res.setQuestionId(Integer.parseInt(q.getQuestionId()));
            res.setQuestionNo(Integer.parseInt(q.getQuestionLocalId()));
            res.setQuestionType((q.getQuestionType().equals("W"))? "M" : q.getQuestionType());

            res.setResponse(q.getYourAnswer());

            res.setAttemptTime(q.getQuestionViewTime());// add in db
            res.setOpened(Integer.parseInt(q.getOpened()));
            res.setAnswered(Integer.parseInt(q.getAnswer()));
            res.setReview(Integer.parseInt(q.getReview()));
            res.setTimeTaken(Integer.parseInt(q.getQuestionSpendTime()));

            resList.add(res);

        }

        esm.setResponse(resList);

        Gson gson = new Gson();
        String j = gson.toJson(esm);

        Log.v("My_Json", j);

        return esm;
    }


    //----------------------------------



    public static final String KEY_DISPLAY_RESULT = "result";
    public static final String KEY_RESULT_ID = "result_id";
    private void whatNext(RegisterResponse response){



       /* "status": true,
        "message": "Please complete feedback.",
        "feedback": true,
        "result": true

        if status=true && feedback=true
        then goto feedback form

        if status=true && feedback=false && result=true
        then goto results view
        testkart.com/demo/server3/api/crm/Results/view/81
        81= examResultId

        if status=true && feedback=false && result=false
        then goto Exam screen*/



        if(response.getStatus() && Boolean.parseBoolean(response.getFeedback())){

            //then goto feedback form
            Intent feedActivity = new Intent(context, FeedbackActivity.class);

            feedActivity.putExtra(KEY_RESULT_ID, response.getExamResultId());

            Ctx.startActivity(feedActivity);

            finish();



        }else if(response.getStatus()
                && !Boolean.parseBoolean(response.getFeedback())
                && Boolean.parseBoolean(response.getFeedback())) {

            //then goto results webview
            Intent feedActivity = new Intent(context, ResultViewActivity.class);
            String url = "http://testkart.com/demo/server3/api/rest/Results/view?&id="+response.getExamResultId()
                    +"&public_key="+session.getUserDetails().get(SessionManager.KEY_PUBLIC)
                    +"&private_key="+session.getUserDetails().get(SessionManager.KEY_PRIVATE);

            feedActivity.putExtra(KEY_DISPLAY_RESULT, url );
            Ctx.startActivity(feedActivity);

            finish();

        }else if(response.getStatus()
                && !Boolean.parseBoolean(response.getFeedback())
                && !Boolean.parseBoolean(response.getFeedback())){

            //then goto Exam screen
            finish();
        }else{

            finish();
        }

    }

}



