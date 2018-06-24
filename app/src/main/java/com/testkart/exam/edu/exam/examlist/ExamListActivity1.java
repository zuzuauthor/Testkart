package com.testkart.exam.edu.exam.examlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.MainActivity;
import com.testkart.exam.edu.exam.ExamActivity;
import com.testkart.exam.edu.exam.ExamDetailDialog;
import com.testkart.exam.edu.exam.ExamListAdapter;
import com.testkart.exam.edu.exam.FeedbackActivity;
import com.testkart.exam.edu.exam.FinilizeExamDialog;
import com.testkart.exam.edu.exam.StartExamConfirmationDialog;
import com.testkart.exam.edu.exam.examlist.examdetails.ExamDetailsResponse;
import com.testkart.exam.edu.exam.examlist.examdetails.Subject;
import com.testkart.exam.edu.exam.ibps.datamodel.DataExam;
import com.testkart.exam.edu.exam.ibps.datamodel.DataLanguage;
import com.testkart.exam.edu.exam.ibps.datamodel.DataQuestion;
import com.testkart.exam.edu.exam.ibps.datamodel.DataResponse;
import com.testkart.exam.edu.exam.ibps.datamodel.DataSubject;
import com.testkart.exam.edu.exam.ibps.model.IbpsExamResponse;
import com.testkart.exam.edu.exam.ibps.model.OtherLangQuestion;
import com.testkart.exam.edu.exam.model.CheckExamResponse;
import com.testkart.exam.edu.exam.model.ExamSubmitModel;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.register.RegisterResponse;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.packages.EmptyCartActivity;
import com.testkart.exam.testkart.notifications.NotificationActivity;
import com.thefinestartist.utils.content.Ctx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import retrofit2.*;

/**
 * Created by testkart on 29/6/17.
 */

public class ExamListActivity1 extends AppCompatActivity implements ExamSelectListener,
        StartExamConfirmationDialog.OnexamSelectListener,
        FinilizeExamDialog.OnExamFinishListener,
        ExamDetailDialog.OnExamStartListener,
        com.testkart.exam.edu.exam.examlist.ExamInstructionDialog.OnExamFinallyStartListener,
        ExamListAdapter.ExamListActionListener{

    private static final String TAG = "ExamListActivity";
    public static final String KEY_STUDENT = "student";
    public static final String KEY_STUDENT_IMAGE = "student_image";
    public static final String KEY_NEW_EXAM = "new_exam";
    public static final String KEY_LANGUAGE = "language";

    private SessionManager session;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Context context = this;

    private ProgressDialog progressDialog;

    private DBHelper dbHelper;

    private DataExam examDetails;

    private IbpsExamResponse eR;
    private String instructionText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_examlist);
        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        checkInternet();

        //initialization();
    }




    public void checkInternet(){

        if (ApiClient.isNetworkAvailable(context)) {

            //initialization
            initialization();

        } else {

            //show dialog ... leader board is empty
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Network");
            builder.setMessage("Please check your internet connection and try again");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    checkInternet();
                }
            });

            builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }
            });

            AlertDialog d = builder.create();
            d.setCancelable(false);
            d.show();

        }

    }



    private boolean isPreviousExam = false;
    public static final String KEY_IS_PREVIOUS_EXAM = "is_previous";
    public static final String KEY_PREVIOUS_EXAM_ID = "previous_exam_id";
    public static final String KEY_PREVIOUS_EXAM_NAME = "exam_name";
    public static final String KEY_PREVIOUS_EXAM_MESSAGE = "previous_exam_message";
    private void initialization() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Exams");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Initializing Your Exam");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        session = new SessionManager(this);

        dbHelper = new DBHelper(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);


        //check if exam is pending coming from dashboard
        if(getIntent().getBooleanExtra(KEY_IS_PREVIOUS_EXAM, false)){

            isPreviousExam = true;

            //show dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getIntent().getStringExtra(KEY_PREVIOUS_EXAM_NAME));
            builder.setCancelable(false);
            //builder.setMessage(getIntent().getStringExtra(KEY_PREVIOUS_EXAM_MESSAGE));
            builder.setMessage("Do you want to resume?");

            builder.setPositiveButton("Take Exam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //get exam details
                    //if exam is not expired false then go to exam screen
                    // if true then make api call to finalize

                    if (ApiClient.isNetworkAvailable(context)) {

                        isPreviousExam = true;

                        // get exam details for exam id
                        String examId = getIntent().getStringExtra(KEY_PREVIOUS_EXAM_ID);
                        loadPreviousExam1(examId);

                       // DialogHelper.showInfoDialog(context, "In Progress", "The Exam module is under construction.");

                    }

                }
            });


            builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });

            builder.create().show();


        }else{

            //not coming from dashboard, so checking previous exam
            checkExamFinalize();

        }

    }


    private void checkRemaningExam(){

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<CheckExamResponse> call = apiService.checkExam(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<CheckExamResponse>() {
            @Override
            public void onResponse(Call<CheckExamResponse> call, final retrofit2.Response<CheckExamResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){

                            //show dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(response.body().getRemExamName());
                            builder.setCancelable(false);
                           // builder.setMessage(response.body().getMessage());
                            builder.setMessage("Do you want to resume?");

                            builder.setPositiveButton("Take Exam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //get exam details
                                    //if exam is not expired false then go to exam screen
                                    // if true then make api call to finalize

                                    if (ApiClient.isNetworkAvailable(context)) {

                                        isPreviousExam = true;

                                        loadPreviousExam1(response.body().getTestId());

                                       // DialogHelper.showInfoDialog(context, "In Progress", "The Exam module is under construction.");

                                    }

                                }
                            });

                            builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    finish();
                                }
                            });

                            builder.create().show();

                        }else{


                            //here check if exam start from dashboard
                             if(getIntent().getBooleanExtra(MainActivity.KEY_EXAM_FROM_DASHBOARD, false)){

                                String dashExamId = getIntent().getStringExtra(MainActivity.KEY_EXAM_ID_FROM_DASH);
                                boolean takeExam = getIntent().getBooleanExtra(MainActivity.KEY_TAKE_EXAM, false);

                                if(takeExam){

                                    getExamInstructions(dashExamId);

                                }else{

                                    makeExamInfoCall1(dashExamId);
                                }

                            }else{

                                 //Do nothing
                             }


                        }


                    }else{

                        Toast.makeText(context, "POResponse body null", Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CheckExamResponse> call, Throwable t) {

                progressDialog.dismiss();

                t.printStackTrace();

            }
        });


    }



    private void checkExamFinalize() {

        if (ApiClient.isNetworkAvailable(context)) {

            checkRemaningExam();

        } else {

            //show dialog ... leader board is empty
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("What?");
            builder.setMessage("Check your internet connection.");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    checkRemaningExam();
                }
            });

            builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }
            });

            AlertDialog d = builder.create();
            d.setCancelable(false);
            d.show();

        }

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(session.getUserDetails().get(SessionManager.KEY_PAID_EXAM) != null){

            if(!session.getUserDetails().get(SessionManager.KEY_PAID_EXAM).equalsIgnoreCase("1")){

                adapter.addFragment(new TodaysExamFragment(), "Today's Exams");
                //adapter.addFragment(new PurchaseExamFragment(), "Purchase Exams");
                adapter.addFragment(new UpcomingExamFragment(), "Upcoming Exams");
                adapter.addFragment(new ExpiredExamFragment(), "Expired Exams");

            }else{

                adapter.addFragment(new TodaysExamFragment(), "Today's Exams");
                //adapter.addFragment(new PurchaseExamFragment(), "Purchase Exams");
                adapter.addFragment(new UpcomingExamFragment(), "Upcoming Exams");
                adapter.addFragment(new ExpiredExamFragment(), "Expired Exams");

            }
        }

        viewPager.setAdapter(adapter);
    }


    private void pickExamAction(final Exam exam, int what) {


        if(what == 2){

            // show detail dialog
            //make api call and get exam details
            examInfo(exam);

        }else{

            if(isAttempt){

                //check network connection
                if (ApiClient.isNetworkAvailable(context)) {

                    //show progress dialog
                    //progressDialog.show();

                    //getExamDetails(exam.getId());

                    getExamInstructions(exam.getId());

                    //DialogHelper.showInfoDialog(context, "In Progress", "The Exam module is under construction.");

                } else {

                    Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

                }

            }else{

                Toast.makeText(context, "You can only view this exam details.", Toast.LENGTH_SHORT).show();
            }

        }



        /*final CharSequence colors[] = new CharSequence[] {"View Details", "Take Exam"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Pick a color");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]

                if(which == 0){

                    // show detail dialog
                    //make api call and get exam details
                    examInfo(exam);

                }else{

                    if(isAttempt){

                        //check network connection
                        if (ApiClient.isNetworkAvailable(context)) {

                            //show progress dialog
                            //progressDialog.show();

                            //getExamDetails(exam.getId());

                            getExamInstructions(exam.getId());

                            //DialogHelper.showInfoDialog(context, "In Progress", "The Exam module is under construction.");

                        } else {

                            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

                        }

                    }else{

                        Toast.makeText(context, "You can only view this exam details.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        builder.show();*/
    }


    //for exam detail dialog
    private void examInfo(Exam exam) {

        if (ApiClient.isNetworkAvailable(context)) {

            makeExamInfoCall(exam);

        }else{

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    //getting exam info for dialog

    public static ArrayList<Subject> printMap(Map mp) {

        ArrayList<com.testkart.exam.edu.exam.examlist.examdetails.Subject> dataSet = new ArrayList<>();

        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();

            com.testkart.exam.edu.exam.examlist.examdetails.Subject subject = new com.testkart.exam.edu.exam.examlist.examdetails.Subject();
            subject.setTotalQuestion(((com.testkart.exam.edu.exam.examlist.examdetails.Subject)pair.getValue()).getTotalQuestion());
            subject.setSubject((String) pair.getKey());
            subject.setTotalAttemptQuestion(((com.testkart.exam.edu.exam.examlist.examdetails.Subject)pair.getValue()).getTotalAttemptQuestion());

            System.out.println(pair.getKey() + " = " + ((com.testkart.exam.edu.exam.examlist.examdetails.Subject)pair.getValue()).getTotalQuestion());
            it.remove(); // avoids a ConcurrentModificationException

            dataSet.add(subject);

        }

        return dataSet;
    }

    private void makeExamInfoCall(Exam exam) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.setMessage("Getting Exam Info");
        progressDialog.show();

        Call<ExamDetailsResponse> call = apiService.examDetails(exam.getId(), session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<ExamDetailsResponse>() {
            @Override
            public void onResponse(Call<ExamDetailsResponse> call, retrofit2.Response<ExamDetailsResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){

                            // build subject arrayList

                            Map<String, com.testkart.exam.edu.exam.examlist.examdetails.Subject> subjectDetail = response.body().getSubjectDetail();

                            ArrayList<com.testkart.exam.edu.exam.examlist.examdetails.Subject> subjectList = printMap(subjectDetail);

                            com.testkart.exam.edu.exam.examlist.examdetails.Exam e = response.body().getResponse().getExam();

                            /*Log.v("Total Marks", response.body().getTotalMarks());
                            Log.v("Total Exam Count", response.body().getExamCount()+"");*/

                            e.setTotalExamCount(response.body().getExamCount()+"");
                            e.setTotalMarks(response.body().getTotalMarks()+"");

                            ExamDetailDialog examDetailDialog = new ExamDetailDialog();

                            Bundle args = new Bundle();
                            args.putSerializable(ExamDetailDialog.KEY_EXAM_DETAILS, e);
                            args.putSerializable(ExamDetailDialog.KEY_SUBJECT_DETAILS, subjectList);
                            args.putBoolean(ExamDetailDialog.KEY_ONLY_VIEW, isAttempt);
                            examDetailDialog.setArguments(args);

                            //exception : may produce IllegalStateException
                            examDetailDialog.show(getSupportFragmentManager(), "EDD");

                        }else{

                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{

                        Toast.makeText(context, "Body is null", Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ExamDetailsResponse> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });

    }


    @Override
    public void onExamSelect(String examId) {

        //show progress dialog
        //make api call
        //on success hide progress dialog
        //parse api, initialize model class, data set
        //truncate / delete all previous records.
        //if fail any of the steps ... will show error message
        //once success, show exam detail dialog full screen


        //check network connection
        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            progressDialog.show();

            getExamDetails(examId);

        } else {

            Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }



    public void getExamDetails(String examId) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<IbpsExamResponse> call = apiService.getExam(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE), examId); //change on monday

        call.enqueue(new Callback<IbpsExamResponse>() {
            @Override
            public void onResponse(Call<IbpsExamResponse> call, retrofit2.Response<IbpsExamResponse> response) {

                try{

                    progressDialog.dismiss();

                    int code = response.code();

                    if(code == 200){

                        if(response.body() != null){

                            if(response.body().getStatus()){

                                //load new exam
                                eR = response.body();
                                instructionText = response.body().getPost().getExam().getInstruction();

                                //initialize model classes and data sets
                                initDataSt(response.body());

                            }else{

                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }else{

                            Toast.makeText(context, "Exam data is null", Toast.LENGTH_SHORT).show();

                        }


                    }else{

                        Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                    }


                }catch (Exception e){

                    Toast.makeText(getApplicationContext(), "Sominging went wrong please restart application", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<IbpsExamResponse> call, Throwable t) {


                //hide progress dialog
                progressDialog.hide();

                Toast.makeText(context, Consts.SERVER_ERROR + t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });
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


    private void loadPreviousExam() {

        progressDialog.dismiss();

        final DataExam exam = dbHelper.getExam();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("EduExpression");
        builder.setMessage("Please finalize "+exam.getExamName());
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //update start time
                dbHelper.updateExamStartTime(exam.getExamId(), System.currentTimeMillis());

                Bundle args = new Bundle();
                args.putSerializable(KEY_STUDENT, eR.getStudentDetail().getStudent());
                args.putString(KEY_STUDENT_IMAGE, (String)eR.getStudentPhoto());
                args.putString(com.testkart.exam.edu.exam.ExamInstructionDialog.KEY_INSTRUCTIONS, instructionText);
                args.putString(KEY_NEW_EXAM, "false");
                args.putString(KEY_LANGUAGE, selectedLanguage);

                Intent examActivity = new Intent(context, ExamActivity.class);
                examActivity.putExtra("B", args);

                Ctx.startActivity(examActivity);
                finish();

            }
        });


        AlertDialog d = builder.create();
        d.setCancelable(false);
        d.show();

    }



    //work in background thread
    private void initDataSt(final IbpsExamResponse body) {

        try{

            //all mapping part done here: exam, subjects, questions
            if (body != null) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //update exam data set
                        updateExam(body.getPost().getExam());

                        //update subject
                        ArrayList<DataSubject> ds = updateSubject(body);

                        //update language array
                        ArrayList<DataLanguage> langArray = updateLanguageArray(body);

                        //update questions
                        updateQuestion(body.getUserExamQuestion(), langArray, dbHelper.getAllSubjects());

                        //hide progress dialog
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //hide progress dialog
                                progressDialog.hide();

                                instructionText = body.getPost().getExam().getInstruction();

                                progressDialog.dismiss();

                                //update start time
                                dbHelper.updateExamStartTime(examId, System.currentTimeMillis());

                                Bundle args = new Bundle();
                                args.putSerializable(KEY_STUDENT, eR.getStudentDetail().getStudent());
                                args.putString(KEY_STUDENT_IMAGE, (String)eR.getStudentPhoto());
                                args.putString(com.testkart.exam.edu.exam.ExamInstructionDialog.KEY_INSTRUCTIONS, instructionText);
                                args.putString(KEY_NEW_EXAM, "true");
                                args.putString(KEY_LANGUAGE, selectedLanguage);

                                Intent examActivity = new Intent(context, ExamActivity.class);
                                examActivity.putExtra("B", args);

                                Ctx.startActivity(examActivity);
                                finish();

                            }
                        });
                    }
                }).start();


            } else {

                //hide progress dialog
                progressDialog.hide();

                Toast.makeText(context, "Fallback, data set is null", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){

            //hide progress dialog
            progressDialog.hide();

            Toast.makeText(context, "Fallback, data set is null", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

    }


    private ArrayList<DataLanguage> updateLanguageArray(IbpsExamResponse body) {


        ArrayList<DataLanguage> langList = new ArrayList<>();

        Set<Map.Entry<String,String>> hashSet=body.getLanguageArr().entrySet();
        for(Map.Entry entry:hashSet ) {

            System.out.println("Key="+entry.getKey()+", Value="+entry.getValue());

            DataLanguage langData = new DataLanguage();
            langData.setLangId((String)entry.getKey());
            langData.setLangName((String)entry.getValue());

            langList.add(langData);
        }

        dbHelper.addLanguage(langList);

        return langList;
    }

    private ArrayList<DataSubject> updateSubject(IbpsExamResponse exam) {

        Set<String> entry = exam.getUserSectionQuestion().keySet();
        ArrayList<DataSubject> subjectList = new ArrayList<>();

        int subjectId = 0;

        for (String subjectName:
                entry) {

            DataSubject subject = new DataSubject();
            subject.setSubjectName(subjectName);
            subject.setSubjectId(subjectId+"");

            subjectList.add(subject);

            subjectId++;

        }

        dbHelper.addSubjects(subjectList);

        return subjectList;
    }

    private void updateQuestion(List<com.testkart.exam.edu.exam.ibps.model.UserExamQuestion> userExamQuestion,
                                ArrayList<DataLanguage> langList,
                                ArrayList<DataSubject> ds) {

        if(!userExamQuestion.isEmpty()){

            ArrayList<DataResponse> dataSet = new ArrayList<>();
            ArrayList<DataQuestion> dataQuestionList = new ArrayList<>();

            HashMap<String, String> langMap = new HashMap<>();

            for (DataLanguage dl :
                    langList) {

                langMap.put(dl.getLangId(), dl.getLangName());
            }

            DataLanguage primaryLang = langList.get(0);

            int qCount = 1;
            int preSubjCount = 0;

            for (com.testkart.exam.edu.exam.ibps.model.UserExamQuestion key:
                    userExamQuestion) {

                com.testkart.exam.edu.exam.ibps.model.Question ibpsQuestion = key.getQuestion();
                com.testkart.exam.edu.exam.ibps.model.ExamStat ibpsExamStat = key.getExamStat();


                if(!preSubject.equals(ibpsQuestion.getSubject().getSubjectName())){

                    preSubject = ds.get(preSubjCount).getSubjectName();

                    // update subject table with question no
                    dbHelper.updateSubjectWithQNo(ds.get(preSubjCount).getSubjectId(), qCount+"");

                    preSubjCount++;

                }


                //Question Stats
                DataResponse questionStats = new DataResponse();
                questionStats.setQuestionId(ibpsQuestion.getId());
                questionStats.setQuestionLocalId(qCount+"");
                questionStats.setSubjectName(ibpsQuestion.getSubject().getSubjectName());
                questionStats.setSubjectId(ibpsQuestion.getSubject().getId());

                StringBuilder sb = new StringBuilder();
                sb.append("[");
                sb.append("{\"lang_id\":\""+primaryLang.getLangId()+"\",\"lang_name\":\""+primaryLang.getLangName()+"\"}");
                if(ibpsQuestion.getQuestionsLang() != null && !ibpsQuestion.getQuestionsLang().isEmpty()){

                    for (OtherLangQuestion obj:
                    ibpsQuestion.getQuestionsLang()) {

                        if(ibpsQuestion.getQuestionsLang().indexOf(obj) == (langList.size()-1)){

                            //last element
                            sb.append("{\"lang_id\":\""+obj.getLanguageId()+"\",\"lang_name\":\""+langMap.get(obj.getLanguageId())+"\"}");

                        }else{

                            sb.append(",{\"lang_id\":\""+obj.getLanguageId()+"\",\"lang_name\":\""+langMap.get(obj.getLanguageId())+"\"}");
                        }

                    }

                    sb.append("]");

                }else{

                    sb.append("]");
                }

                Log.v("Other lang", sb.toString());

                questionStats.setLanguageArray(sb.toString());

                questionStats.setMarks(ibpsQuestion.getMarks());
                questionStats.setNegativeMarks(ibpsQuestion.getNegativeMarks());
                questionStats.setQuestionType(getQType(ibpsQuestion.getQtype().getType(), ibpsQuestion.getAnswer()));
                questionStats.setJumbleOptions(ibpsExamStat.getOptions());
                questionStats.setPassageInclude((ibpsQuestion.getPassageId() != null)? "Yes" : "No");
                questionStats.setCorrectAnswer(ibpsQuestion.getAnswer());
                questionStats.setYourAnswer("");
                questionStats.setOpened(ibpsExamStat.getOpened());
                questionStats.setAnswer(ibpsExamStat.getAnswered());
                questionStats.setReview(ibpsExamStat.getReview());
                questionStats.setQuestionViewTime((ibpsExamStat.getAttemptTime() != null)? (String)ibpsExamStat.getAttemptTime() : "0");
                questionStats.setQuestionSpendTime((ibpsExamStat.getTimeTaken() != null)? (String)ibpsExamStat.getTimeTaken() : "0");

                //adding question state to list
                dataSet.add(questionStats);



                //Question With multiple Lang
                //adding primary lang question
                DataQuestion multiLangQuestion1 = new DataQuestion();
                multiLangQuestion1.setQuestionId(ibpsQuestion.getId());
                multiLangQuestion1.setSubjectId(ibpsQuestion.getSubject().getId());
                multiLangQuestion1.setSubjectName(ibpsQuestion.getSubject().getSubjectName());
                multiLangQuestion1.setLangId(primaryLang.getLangId());
                multiLangQuestion1.setLangName(primaryLang.getLangName());

                if(ibpsQuestion.getPassageId() != null){

                    //get passage first
                    String[] passage = ibpsQuestion.getPassage().getPassage().split(Pattern.quote("%^&"));

                    for (String pp:
                         passage) {

                        System.out.println(pp);

                    }

                    String q = ibpsQuestion.getQuestion();

                    Log.i("Passage Primary", passage[0]);

                    String mlq = passage[0] + "<br><br><b>" + q + "<b>";
                    multiLangQuestion1.setQuestionText(mlq);

                }else{

                    multiLangQuestion1.setQuestionText(ibpsQuestion.getQuestion());

                }


                multiLangQuestion1.setOption1((ibpsQuestion.getOption1() != null)? ibpsQuestion.getOption1(): "");
                multiLangQuestion1.setOption2((ibpsQuestion.getOption2() != null)? ibpsQuestion.getOption2(): "");
                multiLangQuestion1.setOption3((ibpsQuestion.getOption3() != null)? ibpsQuestion.getOption3(): "");
                multiLangQuestion1.setOption4((ibpsQuestion.getOption4() != null)? ibpsQuestion.getOption4(): "");
                multiLangQuestion1.setOption5((ibpsQuestion.getOption5() != null)? ibpsQuestion.getOption5(): "");
                multiLangQuestion1.setOption6((ibpsQuestion.getOption6() != null)? ibpsQuestion.getOption6(): "");
                multiLangQuestion1.setHint(ibpsQuestion.getHint());


                //adding primary question to list
                dataQuestionList.add(multiLangQuestion1);


                int langCount = 1;

                //adding multiple lang question
                if(ibpsQuestion.getQuestionsLang() != null && !ibpsQuestion.getQuestionsLang().isEmpty()){

                    for (OtherLangQuestion obj:
                            ibpsQuestion.getQuestionsLang()) {


                        DataQuestion multiLangQuestion2 = new DataQuestion();
                        multiLangQuestion2.setQuestionId(ibpsQuestion.getId());
                        multiLangQuestion2.setSubjectId(ibpsQuestion.getSubject().getId());
                        multiLangQuestion2.setSubjectName(ibpsQuestion.getSubject().getSubjectName());
                        multiLangQuestion2.setLangId(obj.getLanguageId());
                        multiLangQuestion2.setLangName(langMap.get(obj.getLanguageId()));

                        if(ibpsQuestion.getPassageId() != null){

                            //get passage first
                            //String passage = ibpsQuestion.getPassage().getPassage();

                            //get passage first
                            String[] passage = ibpsQuestion.getPassage().getPassage().split(Pattern.quote("%^&"));

                            for (String pp:
                                    passage) {

                                System.out.println("Other Lang "+pp);

                            }

                            String p = "";

                            if(passage.length > langCount){

                                p = passage[langCount];

                            }else{

                                p = "";
                            }

                            Log.i("Passage "+langCount, p);

                            String q = obj.getQuestion();

                            String mlq = p + "<br><br><b>" + q + "<b>";
                            multiLangQuestion2.setQuestionText(mlq);

                            langCount++;

                        }else{

                            multiLangQuestion2.setQuestionText(obj.getQuestion());

                        }


                        multiLangQuestion2.setOption1(obj.getOption1());
                        multiLangQuestion2.setOption2(obj.getOption2());
                        multiLangQuestion2.setOption3(obj.getOption3());
                        multiLangQuestion2.setOption4(obj.getOption4());
                        multiLangQuestion2.setOption5(obj.getOption5());
                        multiLangQuestion2.setOption6(obj.getOption6());
                        multiLangQuestion2.setHint(obj.getHint());

                        //adding multi language question to list
                        dataQuestionList.add(multiLangQuestion2);

                    }

                }else{

                    //no other lang question available
                }

                qCount++;


            }

            dbHelper.addQuestionsStats(dataSet);
            dbHelper.addQuestions(dataQuestionList);

        }else{

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(context, "Fallback, question data set is null", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    //getting question type
    private String getQType(String type, String answer) {

        if(type.equals("M")){

            //check for multiple choice or single choice
            if(answer.length()>1){
                //multiple
                return type;

            }else{

                //single choice
                return "W";

            }

        }else{

            return type;
        }
    }


    String examId = "";
    private void updateExam( com.testkart.exam.edu.exam.ibps.model.Exam_ exam) {

        if (exam != null) {

            examId = exam.getId();

            DataExam em = new DataExam();

            em.setExamId(exam.getId());
            em.setExamName(exam.getName());
            em.setExamInstruction(exam.getInstruction());
            em.setDuration(exam.getDuration());
            em.setStartDate(exam.getStartDate());
            em.setEndDate(exam.getEndDate());
            em.setExamStartTime("0");                            //    will be update in local later using public crud methods
            em.setExamEndTime("0");
            em.setPassingPercent(exam.getPassingPercent());
            em.setNegativeMarking(exam.getNegativeMarking());
            em.setPaidExam(exam.getPaidExam());
            em.setAmount((String) exam.getAmount());
            em.setExamFinilize("false");
            em.setExamDurationContinue("0");
            em.setMultiLanguage(exam.getMultiLanguage());

            //clear / truncate all tables before insertion
            dbHelper.deleteRecords();

            //insert exam
            dbHelper.addExam(em);

            examDetails = em;

        } else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(context, "Fallback, question data set is null", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {

            if (dbHelper != null) {

                dbHelper.close();

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    /*private boolean isValid(String htmlData){

        Tidy tidy = new Tidy();
        InputStream stream = new ByteArrayInputStream(htmlData.getBytes());
        tidy.parse(stream, System.out);
        return (tidy.getParseErrors() == 0);
    }
*/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rep_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        int id = item.getItemId();

        if(id == R.id.action_message){

            Ctx.startActivity( new Intent(context, NotificationActivity.class));
            finish();

        }else if(id == R.id.action_cart){

            Intent emptyCart = new Intent(context, EmptyCartActivity.class);
            startActivity(emptyCart);

            finish();

        }

        return super.onOptionsItemSelected(item);
    }




    //----------------------- SUBMIT RESPONSE ---------------------------------//

    private void whatNext(RegisterResponse response){

        /*

        "status": true,
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
        then goto Exam screen

        */

        if(response.getStatus() && Boolean.parseBoolean(response.getFeedback())){

            //then goto feedback form
            Intent feedActivity = new Intent(context, FeedbackActivity.class);
            feedActivity.putExtra(ExamActivity.KEY_RESULT_ID, response.getExamResultId());
            Ctx.startActivity(feedActivity);



        }else if(response.getStatus()
                && !Boolean.parseBoolean(response.getFeedback())
                && Boolean.parseBoolean(response.getFeedback())) {

            //then goto results webview
            Intent feedActivity = new Intent(context, ResultViewActivity.class);
            String url = "http://testkart.com/demo/server3/api/rest/Results/view?&id="+response.getExamResultId()
                    +"&public_key="+session.getUserDetails().get(SessionManager.KEY_PUBLIC)
                    +"&private_key="+session.getUserDetails().get(SessionManager.KEY_PRIVATE);

            feedActivity.putExtra(ExamActivity.KEY_DISPLAY_RESULT, url );
            Ctx.startActivity(feedActivity);


        }else if(response.getStatus()
                && !Boolean.parseBoolean(response.getFeedback())
                && !Boolean.parseBoolean(response.getFeedback())){

            //stay here
        }else{

            //stay here
        }

    }


    private void submitResponses() {

        progressDialog.setTitle("Submitting Exam");
        progressDialog.show();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RegisterResponse> call = apiService.sendExamResponse(getExamResponse());

        call.enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {

                Log.v("POResponse", response.body().toString());

                progressDialog.cancel();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null && response.body().getStatus()){

                        // empty tables
                        dbHelper.deleteRecords();
                        whatNext(response.body());

                    }else{

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

            //update subject table with question no


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


    private String preSubject = "";

    @Override
    public void onExamFinish() {

        if(ApiClient.isNetworkAvailable(context)){

            //enable when api integrated
            submitResponses();

        }else{

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }
    }

   /* @Override
    public void onExamStart() {

        progressDialog.dismiss();

        //update start time
        dbHelper.updateExamStartTime(examId, System.currentTimeMillis());

        Bundle args = new Bundle();
        args.putSerializable(KEY_STUDENT, eR.getStudentDetail().getStudent());
        args.putString(KEY_STUDENT_IMAGE, (String)eR.getStudentPhoto());
        args.putString(ExamInstructionDialog.KEY_INSTRUCTIONS, instructionText);
        args.putString(KEY_NEW_EXAM, "true");

        Intent examActivity = new Intent(context, ExamActivity.class);
        examActivity.putExtra("B", args);

        Ctx.startActivity(examActivity);
        finish();

    }*/


    boolean isAttempt;
    @Override
    public void onExamSelect(Exam exam, boolean isAttempt, int what) {

        this.isAttempt = isAttempt;

        if(!exam.isCanAttempt()){

            //show dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(exam.getName()+"");
            builder.setMessage("You have exhausted all your attempts for this exam");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.create().show();


        }else{

            if (ApiClient.isNetworkAvailable(context)) {

                pickExamAction(exam, what);

            } else {

                Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

            }

        }

    }

    @Override
    public void onShowInstruction(com.testkart.exam.edu.exam.examlist.examdetails.Exam exam) {

        //make api call for instruction dialog
        if (ApiClient.isNetworkAvailable(context)) {

            getExamInstructions(exam.getId());

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void getExamInstructions(String id) {

        progressDialog.setTitle("Exam Instructions");
        progressDialog.show();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ExamDetailsResponse> call = apiService.getInstructions(id, session.getUserDetails().get(SessionManager.KEY_PUBLIC), session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<ExamDetailsResponse>() {
            @Override
            public void onResponse(Call<ExamDetailsResponse> call, retrofit2.Response<ExamDetailsResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){


                            ArrayList<String> langArray = new ArrayList<String>();

                            Map<String, String> map = response.body().getLanguageArr();
                            for (Map.Entry<String, String> entry : map.entrySet())
                            {
                                langArray.add(entry.getValue());

                                System.out.println(entry.getKey() + "/" + entry.getValue());
                            }


                            //show instruction dialog
                            com.testkart.exam.edu.exam.examlist.ExamInstructionDialog e = new com.testkart.exam.edu.exam.examlist.ExamInstructionDialog();

                            Bundle args = new Bundle();
                            args.putSerializable(com.testkart.exam.edu.exam.examlist.ExamInstructionDialog.KEY_INSTRUCTIONS, response.body().getResponse().getExam());
                            args.putBoolean(com.testkart.exam.edu.exam.examlist.ExamInstructionDialog.KEY_IS_PAID, true);
                            args.putStringArrayList(com.testkart.exam.edu.exam.examlist.ExamInstructionDialog.KEY_LANGUAGE, langArray);
                            e.setArguments(args);

                            e.show(getSupportFragmentManager(), "EID");


                        }else{

                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }else{

                        Toast.makeText(context, "POResponse body is null", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ExamDetailsResponse> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });


    }


    private String selectedLanguage = "";

    @Override
    public void onExamFinallyStart(com.testkart.exam.edu.exam.examlist.examdetails.Exam exam, String languageString) {

        if (ApiClient.isNetworkAvailable(context)) {

            selectedLanguage = languageString;
            System.out.println("Select Language: "+selectedLanguage);

            progressDialog.setTitle("Starting Exam");
            progressDialog.show();
            getExamDetails(exam.getId());

           // DialogHelper.showInfoDialog(context, "In Progress", "The Exam module is under maintenance ");

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }







    //load previous exam
    private void loadPreviousExam1(String examId) {

        if (ApiClient.isNetworkAvailable(context)) {

            getExamData(examId);

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void getExamData(final String examId) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        progressDialog.show();

        Call<IbpsExamResponse> call = apiService.getExam(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE), examId);  // correct on monday


        call.enqueue(new Callback<IbpsExamResponse>() {
            @Override
            public void onResponse(Call<IbpsExamResponse> call, retrofit2.Response<IbpsExamResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getExamExpire()){

                            //submit response
                            //make api call in case there is no data in local db
                            //else get response from local db and submit responses

                            if(dbHelper.checkExam()){

                                //true submit response from local database
                                submitResponses();

                            }else{

                                //submit response from api call
                                submitResponse(examId);
                            }


                        }else{

                            //exam is not expired load previous exam
                            //load exam from api in case of exam start from web
                            //else start exam from local database


                            if(response.body().getStatus()){

                                eR = response.body();
                                instructionText = response.body().getPost().getExam().getInstruction();

                                if(dbHelper.checkExam()){

                                    //true load exam from local database
                                    //update start time
                                    dbHelper.updateExamStartTime(examId, System.currentTimeMillis());

                                    Bundle args = new Bundle();
                                    args.putSerializable(KEY_STUDENT, eR.getStudentDetail().getStudent());
                                    args.putString(KEY_STUDENT_IMAGE, (String)eR.getStudentPhoto());
                                    args.putString(com.testkart.exam.edu.exam.ExamInstructionDialog.KEY_INSTRUCTIONS, instructionText);
                                    args.putString(KEY_NEW_EXAM, "false");
                                    args.putString(KEY_LANGUAGE, selectedLanguage);

                                    Intent examActivity = new Intent(context, ExamActivity.class);
                                    examActivity.putExtra("B", args);

                                    Ctx.startActivity(examActivity);
                                    finish();


                                }else{

                                    //update local db with exam data and start exam screen
                                    initDataSt(response.body());

                                }

                            }else{

                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                    }else{

                        Toast.makeText(context, "Exam data POResponse null", Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<IbpsExamResponse> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });

    }


    //submit response from api call since exam is not started from app
    private void submitResponse(final String examId) {

        if (ApiClient.isNetworkAvailable(context)) {


            progressDialog.setTitle("Submitting Exam");
            progressDialog.show();

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<RegisterResponse> call = apiService.submitExpiredExam(examId, session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                    session.getUserDetails().get(SessionManager.KEY_PRIVATE));

            call.enqueue(new Callback<RegisterResponse>() {

                @Override
                public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {

                    Log.v("POResponse", response.body().toString());

                    progressDialog.cancel();

                    int code = response.code();

                    if(code == 200){

                        if(response.body() != null && response.body().getStatus()){

                            // empty tables
                            dbHelper.deleteRecords();
                            whatNext(response.body());

                        }else{

                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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


        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onTakeExam(Exam exam, boolean canTakeExam) {

        this.isAttempt = canTakeExam;

        if(!exam.isCanAttempt()){

            //show dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(exam.getName()+"");
            builder.setMessage("You have exhausted all your attempts for this exam");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.create().show();


        }else{

            if (ApiClient.isNetworkAvailable(context)) {

                pickExamAction(exam, 1);

            } else {

                Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

            }

        }

    }

    @Override
    public void onViewDetails(Exam exam, boolean canTakeExam) {

        this.isAttempt = canTakeExam;

        if(!exam.isCanAttempt()){

            //show dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(exam.getName()+"");
            builder.setMessage("You have exhausted all your attempts for this exam");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.create().show();


        }else{

            if (ApiClient.isNetworkAvailable(context)) {

                pickExamAction(exam, 2);

            } else {

                Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

            }

        }

    }





    private void makeExamInfoCall1(String examId) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.setMessage("Getting Exam Info");
        progressDialog.show();

        Call<ExamDetailsResponse> call = apiService.examDetails(examId, session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<ExamDetailsResponse>() {
            @Override
            public void onResponse(Call<ExamDetailsResponse> call, retrofit2.Response<ExamDetailsResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){

                            // build subject arrayList

                            Map<String, com.testkart.exam.edu.exam.examlist.examdetails.Subject> subjectDetail = response.body().getSubjectDetail();

                            ArrayList<com.testkart.exam.edu.exam.examlist.examdetails.Subject> subjectList = printMap(subjectDetail);

                            com.testkart.exam.edu.exam.examlist.examdetails.Exam e = response.body().getResponse().getExam();

                            /*Log.v("Total Marks", response.body().getTotalMarks());
                            Log.v("Total Exam Count", response.body().getExamCount()+"");*/

                            e.setTotalExamCount(response.body().getExamCount()+"");
                            e.setTotalMarks(response.body().getTotalMarks()+"");

                            ExamDetailDialog examDetailDialog = new ExamDetailDialog();

                            Bundle args = new Bundle();
                            args.putSerializable(ExamDetailDialog.KEY_EXAM_DETAILS, e);
                            args.putSerializable(ExamDetailDialog.KEY_SUBJECT_DETAILS, subjectList);
                            args.putBoolean(ExamDetailDialog.KEY_ONLY_VIEW, true);
                            examDetailDialog.setArguments(args);

                            //exception : may produce IllegalStateException
                            examDetailDialog.show(getSupportFragmentManager(), "EDD");

                        }else{

                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{

                        Toast.makeText(context, "Body is null", Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(context, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ExamDetailsResponse> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });

    }


}
