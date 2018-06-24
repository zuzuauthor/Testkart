package com.testkart.exam.edu.exam.examlist;

/**
 * Created by testkart on 19/5/17.
 */


public class ExamListActivity {
    private static final String TAG = "ExamListActivity";
    public static final String KEY_STUDENT = "student";
    public static final String KEY_STUDENT_IMAGE = "student_image";
    public static final String KEY_NEW_EXAM = "new_exam";

    public static final String KEY_IS_PREVIOUS_EXAM = "is_previous";
    public static final String KEY_PREVIOUS_EXAM_ID = "previous_exam_id";
    public static final String KEY_PREVIOUS_EXAM_NAME = "exam_name";
    public static final String KEY_PREVIOUS_EXAM_MESSAGE = "previous_exam_message";

}/*extends AppCompatActivity implements ExamSelectListener,
        StartExamConfirmationDialog.OnexamSelectListener,
        FinilizeExamDialog.OnExamFinishListener,
        ExamDetailDialog.OnExamStartListener,
        com.mkn.testkart.com.testkart.exam.edu.exam.examlist.ExamInstructionDialog.OnExamFinallyStartListener {

    private static final String TAG = "ExamListActivity";
    public static final String KEY_STUDENT = "student";
    public static final String KEY_STUDENT_IMAGE = "student_image";
    public static final String KEY_NEW_EXAM = "new_exam";

    private SessionManager session;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Context context = this;

    private ProgressDialog progressDialog;

    private DBHelper dbHelper;

    private ExamModel examDetails;

    private IbpsExamResponse eR;
    private String instructionText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_examlist);

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
        toolbar.setTitle("My Exam");
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
            builder.setMessage(getIntent().getStringExtra(KEY_PREVIOUS_EXAM_MESSAGE));

            builder.setPositiveButton("Attempt Now", new DialogInterface.OnClickListener() {
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
            public void onResponse(Call<CheckExamResponse> call, final POResponse<CheckExamResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){

                            //show dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(response.body().getRemExamName());
                            builder.setCancelable(false);
                            builder.setMessage(response.body().getMessage());

                            builder.setPositiveButton("Attempt Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //get exam details
                                    //if exam is not expired false then go to exam screen
                                    // if true then make api call to finalize

                                    if (ApiClient.isNetworkAvailable(context)) {

                                        isPreviousExam = true;

                                        loadPreviousExam1(response.body().getTestId());

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


                            // show nothing
                            // Toast.makeText(context, "No pending exam", Toast.LENGTH_SHORT).show();
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

                adapter.addFragment(new TodaysExamFragment(), "Today's Exam");
                //adapter.addFragment(new PurchaseExamFragment(), "Purchase Exam");
                adapter.addFragment(new UpcomingExamFragment(), "Upcoming Exam");
                adapter.addFragment(new ExpiredExamFragment(), "Expired Exam");

            }else{

                adapter.addFragment(new TodaysExamFragment(), "Today's Exam");
                //adapter.addFragment(new PurchaseExamFragment(), "Purchase Exam");
                adapter.addFragment(new UpcomingExamFragment(), "Upcoming Exam");
                adapter.addFragment(new ExpiredExamFragment(), "Expired Exam");

            }
        }

        viewPager.setAdapter(adapter);
    }


    private void pickExamAction(final Exam exam) {

        final CharSequence colors[] = new CharSequence[] {"View Details", "Attempt Now"};

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
                            progressDialog.show();

                            getExamDetails(exam.getId());

                        } else {

                            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

                        }

                    }else{

                        Toast.makeText(context, "You can only view this exam details.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        builder.show();
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

    public static ArrayList<com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Subject> printMap(Map mp) {

        ArrayList<com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Subject> dataSet = new ArrayList<>();

        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();

            com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Subject subject = new com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Subject();
            subject.setTotalQuestion(((com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Subject)pair.getValue()).getTotalQuestion());
            subject.setSubject((String) pair.getKey());
            subject.setTotalAttemptQuestion(((com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Subject)pair.getValue()).getTotalAttemptQuestion());

            System.out.println(pair.getKey() + " = " + ((com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Subject)pair.getValue()).getTotalQuestion());
            it.remove(); // avoids a ConcurrentModificationException

            dataSet.add(subject);

        }

        return dataSet;
    }

    private void makeExamInfoCall(Exam exam) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<ExamDetailsResponse> call = apiService.examDetails(exam.getId(), session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<ExamDetailsResponse>() {
            @Override
            public void onResponse(Call<ExamDetailsResponse> call, POResponse<ExamDetailsResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){

                            // build subject arrayList

                            Map<String, com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Subject> subjectDetail = response.body().getSubjectDetail();

                            ArrayList<com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Subject> subjectList = printMap(subjectDetail);

                            com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Exam e = response.body().getResponse().getExam();

                            *//*Log.v("Total Marks", response.body().getTotalMarks());
                            Log.v("Total Exam Count", response.body().getExamCount()+"");*//*

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
                session.getUserDetails().get(SessionManager.KEY_PRIVATE), examId);

        call.enqueue(new Callback<IbpsExamResponse>() {
            @Override
            public void onResponse(Call<IbpsExamResponse> call, POResponse<IbpsExamResponse> response) {

                try{

                    progressDialog.dismiss();

                    int code = response.code();

                    if(code == 200){


                       *//* if(response.body().isExamExpire()){

                            progressDialog.dismiss();

                            //show finalize dialog
                            //Toast.makeText(context, "Exam has expired", Toast.LENGTH_SHORT).show();
                            showFinalizeDialog();


                        }else{*//*

                            //check attempt count
                           *//* if(response.body().getPost().getExam().getAttemptCount()){


                            }else{*//*


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

                                *//*eR = response.body();
                                instructionText = response.body().getPost().getExam().getInstruction();


                                String checkFinalize = dbHelper.getExamFinilize();
                                if(checkFinalize.equals("false")){

                                    //show dialog to finalize exam
                                    loadPreviousExam();

                                }else{

                                    //initialize model classes and data sets
                                    initDataSt(response.body());
                                }*//*
                           // }

                       // }

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

        final ExamModel exam = dbHelper.getExam();

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
                args.putString(ExamInstructionDialog.KEY_INSTRUCTIONS, instructionText);
                args.putString(KEY_NEW_EXAM, "false");

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
    private void initDataSt(final ExamResponse body) {

        try{

            //all mapping part done here: exam, subjects, questions
            if (body != null) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //update exam data set
                        updateExam(body.getPost().getExam());

                        //update subject
                        updateSubject(body);

                        //update questions
                        updateQuestion(body.getUserExamQuestion());

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
                                args.putString(ExamInstructionDialog.KEY_INSTRUCTIONS, instructionText);
                                args.putString(KEY_NEW_EXAM, "true");

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

    private void updateSubject(ExamResponse exam) {

        Set<String> entry = exam.getUserSectionQuestion().keySet();
        ArrayList<SubjectModel> subjectList = new ArrayList<>();

        int subjectId = 0;

        for (String subjectName:
                entry) {

            String qIds = "";
            int questionCount = 0;
            SubjectModel subject = new SubjectModel();
            subject.setSubjectName(subjectName);
            subject.setSubjectId(subjectId+"");

            for (int i = 0; i < exam.getUserExamQuestion().size(); i++) {

                //check given subject equal
                if(subjectName.equalsIgnoreCase(exam.getUserExamQuestion().get(i).getSubject().getSubjectName())){

                    qIds += exam.getUserExamQuestion().get(i).getQuestion().getId()+",";

                    questionCount++;

                }
            }

            subject.setQuestionsCount(questionCount+"");
            subject.setqIds(qIds.replaceAll(",$", ""));

            subjectList.add(subject);

            subjectId++;

        }

        dbHelper.addSubjects(subjectList);

    }

    private void updateQuestion(List<UserExamQuestion> userExamQuestion) {

        if(!userExamQuestion.isEmpty()){

            ArrayList<QuestionModel> dataSet = new ArrayList<>();

            HashMap<String, String> sIds = dbHelper.getAllSubjectsIDs();

            int qCount = 1;

            for (UserExamQuestion key:
                    userExamQuestion) {

                HTMLTagValidator htmlValidate = new HTMLTagValidator();
                QuestionModel question = new QuestionModel();
                ArrayList<String> options = new ArrayList();


                Qtype qType = key.getQtype();
                com.mkn.testkart.com.testkart.exam.edu.exam.model.Exam exam = key.getExam();
                ExamStat eStat = key.getExamStat();
                Question q = key.getQuestion();
                Subject subject = key.getSubject();

                options.add(q.getOption1());
                options.add(q.getOption2());
                options.add(q.getOption3());
                options.add(q.getOption4());
                options.add(q.getOption5());
                options.add(q.getOption6());

                //init
                question.setQuestionId(q.getId());
                question.setLocalQuestionId(qCount+"");
                question.setSubject(subject.getSubjectName());
                question.setSubjectId(sIds.get(subject.getSubjectName()));
                question.setQuestion(q.getQuestion());
                question.setQuestionHtml(true);  //check if question contain html
                question.setOptions(options);
                question.setOptionsHtml(htmlValidate.validate(options.get(0)));  //check if options contain html
                question.setHint(q.getHint());
                question.setMarks(eStat.getMarks());
                question.setIsNegativeMarks(exam.getNegativeMarking());
                question.setQuestionType(qType.getType());
                question.setMultipleType((q.getAnswer().length()>1)? true+"":false+"");   //multiple / single choice
                question.setTrueFalseResponse("");    //update later
                question.setFillBlankResponse("");    // update later
                question.setSubjectiveResponse("");   //update later
                question.setMultipleChoiceResponse(""); //update later
                question.setNegativeMarks(q.getNegativeMarks());
                question.setOpened("0");   //set default  0 1
                question.setAnswered("0");  //set default 0 1
                question.setReview("0");   // set default 0 1
                question.setTimeSpendOnQuestion(0);  //update runtime
                question.setAttemptTime("0");
                question.setJumbledOptions((eStat.getOptions() != null)? eStat.getOptions():"");

                // question.toString(question);

                dataSet.add(question);

                qCount++;

            }

            dbHelper.addQuestions(dataSet);

        }else{

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(context, "Fallback, question data set is null", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }


    String examId = "";
    private void updateExam(Exam_ exam) {

        if (exam != null) {

            examId = exam.getId();

            ExamModel em = new ExamModel();

            em.setExamId(exam.getId());
            em.setExamName(exam.getName());
            em.setExamDetails(exam.getInstruction());
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


    *//*private boolean isValid(String htmlData){

        Tidy tidy = new Tidy();
        InputStream stream = new ByteArrayInputStream(htmlData.getBytes());
        tidy.parse(stream, System.out);
        return (tidy.getParseErrors() == 0);
    }
*//*



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }




    //----------------------- SUBMIT RESPONSE ---------------------------------//

    private void whatNext(RegisterResponse response){

        *//*

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

        *//*

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
            String url = "http://elfemo.com/demo/server3/api/rest/Results/view?&id="+response.getExamResultId()
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
            public void onResponse(Call<RegisterResponse> call, POResponse<RegisterResponse> response) {

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

        ArrayList<QuestionModel> questionList = dbHelper.getQuestionList();

        ExamModel exam = dbHelper.getExam();

        List<com.mkn.testkart.com.testkart.exam.edu.exam.model.POResponse> resList = new ArrayList<>();

        //bundle responses
        ExamSubmitModel esm = new ExamSubmitModel();
        esm.setEndTime(exam.getExamEndTime());
        esm.setExamId(Integer.parseInt(exam.getExamId()));
        esm.setPrivateKey(session.getUserDetails().get(SessionManager.KEY_PRIVATE));
        esm.setPublicKey(session.getUserDetails().get(SessionManager.KEY_PUBLIC));
        esm.setStartTime(exam.getExamStartTime());
        esm.setStudentId(Integer.parseInt("1"));


        for (QuestionModel q:
                questionList) {

            com.mkn.testkart.com.testkart.exam.edu.exam.model.POResponse res = new com.mkn.testkart.com.testkart.exam.edu.exam.model.POResponse();

            res.setQuestionId(Integer.parseInt(q.getQuestionId()));
            res.setQuestionNo(Integer.parseInt(q.getLocalQuestionId()));
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
    }


    @Override
    public void onExamFinish() {

        if(ApiClient.isNetworkAvailable(context)){

            //enable when api integrated
            submitResponses();

        }else{

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }
    }

   *//* @Override
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

    }*//*


   boolean isAttempt;
    @Override
    public void onExamSelect(Exam exam, boolean isAttempt) {

        this.isAttempt = isAttempt;

        if(!exam.isCanAttempt()){

            //show dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(exam.getName()+"");
            builder.setMessage("You have maximum attempt for exam "+exam.getName());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.create().show();


        }else{

            if (ApiClient.isNetworkAvailable(context)) {

                pickExamAction(exam);

            } else {

                Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

            }

        }

    }

    @Override
    public void onShowInstruction(com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Exam exam) {

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
            public void onResponse(Call<ExamDetailsResponse> call, POResponse<ExamDetailsResponse> response) {

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
                            com.mkn.testkart.com.testkart.exam.edu.exam.examlist.ExamInstructionDialog e = new com.mkn.testkart.com.testkart.exam.edu.exam.examlist.ExamInstructionDialog();

                            Bundle args = new Bundle();
                            args.putSerializable(com.mkn.testkart.com.testkart.exam.edu.exam.examlist.ExamInstructionDialog.KEY_INSTRUCTIONS, response.body().getResponse().getExam());
                            args.putBoolean(com.mkn.testkart.com.testkart.exam.edu.exam.examlist.ExamInstructionDialog.KEY_IS_PAID, true);
                            args.putStringArrayList(com.mkn.testkart.com.testkart.exam.edu.exam.examlist.ExamInstructionDialog.KEY_LANGUAGE, langArray);
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

    @Override
    public void onExamFinallyStart(com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Exam exam) {

        if (ApiClient.isNetworkAvailable(context)) {

            progressDialog.setTitle("Starting Exam");
            progressDialog.show();
            getExamDetails(exam.getId());

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

        Call<ExamResponse> call = apiService.getExam(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE), examId);


        call.enqueue(new Callback<ExamResponse>() {
            @Override
            public void onResponse(Call<ExamResponse> call, POResponse<ExamResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().isExamExpire()){

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

                            eR = response.body();
                            instructionText = response.body().getPost().getExam().getInstruction();

                            if(response.body().getStatus()){

                                if(dbHelper.checkExam()){

                                    //true load exam from local database
                                    //update start time
                                    dbHelper.updateExamStartTime(examId, System.currentTimeMillis());

                                    Bundle args = new Bundle();
                                    args.putSerializable(KEY_STUDENT, eR.getStudentDetail().getStudent());
                                    args.putString(KEY_STUDENT_IMAGE, (String)eR.getStudentPhoto());
                                    args.putString(ExamInstructionDialog.KEY_INSTRUCTIONS, instructionText);
                                    args.putString(KEY_NEW_EXAM, "false");

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
            public void onFailure(Call<ExamResponse> call, Throwable t) {

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
                public void onResponse(Call<RegisterResponse> call, POResponse<RegisterResponse> response) {

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

}
*/