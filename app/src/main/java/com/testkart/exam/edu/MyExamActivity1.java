package com.testkart.exam.edu;

/**
 * Created by testkart on 28/4/17.
 */

public class MyExamActivity1 { private static final String TAG = "MyExamActivity1";
    public static final String KEY_STUDENT = "student";
    public static final String KEY_STUDENT_IMAGE = "student_image";
    public static final String KEY_NEW_EXAM = "new_exam"; }/*extends AppCompatActivity implements StartExamConfirmationDialog.OnexamSelectListener, ExamDetailDialog.OnExamStartListener {

    private static final String TAG = "MyExamActivity1";
    public static final String KEY_STUDENT = "student";
    public static final String KEY_STUDENT_IMAGE = "student_image";
    public static final String KEY_NEW_EXAM = "new_exam";

    private SessionManager session;

    private ListView viewExamList;
    private ExamListAdapter adapter;

    private ProgressDialog progressDialog;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myexam1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Exam");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        initialization();
    }

    private void initialization() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Initializing Your Exam");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        session = new SessionManager(this);

        dbHelper = new DBHelper(this);

        viewExamList = (ListView) findViewById(R.id.viewExamList);

        ArrayList<String> dataSet = new ArrayList<>();
        dataSet.add("Main Exam");
        dataSet.add("Board Exam");
        dataSet.add("Final Exam");
        dataSet.add("IIT Exam");
        dataSet.add("CPMT Exam");
        dataSet.add("BANK Exam");

        //adapter = new ExamListAdapter(this, dataSet);
        viewExamList.setAdapter(adapter);


        viewExamList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                StartExamConfirmationDialog sec = new StartExamConfirmationDialog();
                sec.show(getSupportFragmentManager(), "sec");
            }
        });



        //check if there is any previous exam incomplete
        //if not the make api call with previous exam id

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
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

            //get exam scaler / plane text response
            //getScalerResponse();

            //get exam details
            getExamDetails();


        } else {

            Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }


        *//*Ctx.startActivity( new Intent(MyExamActivity1.this, ExamActivity.class));
        finish();*//*
    }


    public void getExamDetails() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ExamResponse> call = apiService.getExam(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE), "1");

        call.enqueue(new Callback<ExamResponse>() {
            @Override
            public void onResponse(Call<ExamResponse> call, POResponse<ExamResponse> response) {

                try{

                    int code = response.code();

                    if(code == 200){

                        if(response.body().isExamExpire()){

                            progressDialog.dismiss();

                            //show finalize dialog
                            Toast.makeText(MyExamActivity1.this, "Exam has expired", Toast.LENGTH_SHORT).show();


                            if(ApiClient.isNetworkAvailable(context)){

                                //enable when api integrated
                                submitResponses();

                            }else{

                                Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

                            }


                        }else{

                            eR = response.body();
                            instructionText = response.body().getPost().getExam().getInstruction();


                            String checkFinalize = dbHelper.getExamFinilize();
                            if(checkFinalize.equals("false")){

                                //show dialog to finalize exam
                                loadPreviousExam();

                            }else{

                                //initialize model classes and data sets
                                initDataSt(response.body());
                            }

                        }

                    }else{

                        Toast.makeText(MyExamActivity1.this, "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                    }


                }catch (Exception e){

                    Toast.makeText(getApplicationContext(), "Sominging went wrong please restart application", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ExamResponse> call, Throwable t) {


                //hide progress dialog
                progressDialog.hide();

                Toast.makeText(MyExamActivity1.this, Consts.SERVER_ERROR + t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });
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

                Intent examActivity = new Intent(MyExamActivity1.this, ExamActivity.class);
                examActivity.putExtra("B", args);

                Ctx.startActivity(examActivity);
                finish();

            }
        });


        builder.create().show();

    }





    private ExamModel examDetails;

    private ExamResponse eR;
    private String instructionText;

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

                                ExamDetailDialog ed = new ExamDetailDialog();

                                Bundle args = new Bundle();
                                args.putSerializable("ED", examDetails);
                                args.putBoolean("EXAM", true);
                                ed.setArguments(args);
                                ed.show(getSupportFragmentManager(), "ed");

                            }
                        });
                    }
                }).start();


            } else {

                //hide progress dialog
                progressDialog.hide();

                Toast.makeText(MyExamActivity1.this, "Fallback, data set is null", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){

            //hide progress dialog
            progressDialog.hide();

            Toast.makeText(MyExamActivity1.this, "Fallback, data set is null", Toast.LENGTH_SHORT).show();

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
                Exam exam = key.getExam();
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

               // question.toString(question);

                dataSet.add(question);

                qCount++;

            }

            dbHelper.addQuestions(dataSet);

        }else{

            Toast.makeText(MyExamActivity1.this, "Fallback, question data set is null", Toast.LENGTH_SHORT).show();
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

            //clear / truncate all tables before insertion
            dbHelper.deleteRecords();

            //insert exam
            dbHelper.addExam(em);

            examDetails = em;

        } else {

            Toast.makeText(MyExamActivity1.this, "Fallback, exam data set is null", Toast.LENGTH_SHORT).show();
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
    }*//*



  *//*  @Override
    public void onExamStart() {

        progressDialog.dismiss();

        //update start time
        dbHelper.updateExamStartTime(examId, System.currentTimeMillis());

        Bundle args = new Bundle();
        args.putSerializable(KEY_STUDENT, eR.getStudentDetail().getStudent());
        args.putString(KEY_STUDENT_IMAGE, (String)eR.getStudentPhoto());
        args.putString(ExamInstructionDialog.KEY_INSTRUCTIONS, instructionText);
        args.putString(KEY_NEW_EXAM, "true");

        Intent examActivity = new Intent(MyExamActivity1.this, ExamActivity.class);
        examActivity.putExtra("B", args);

        Ctx.startActivity(examActivity);
        finish();

    }*//*


*//*public void getScalerResponse() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);


                    String body = "plain text request body";
                    Call<String> call = apiService.getExamScalar();

                    POResponse<String> response = call.execute();
                    String value = response.body();

                    Log.v(TAG, value);

                }catch (Exception e){

                    e.printStackTrace();
                }

            }
        }).start();


    }*//*




private Context context = this;
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

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        //exam response submitted go to feedback screen
                        Ctx.startActivity(new Intent(context, FeedbackActivity.class));
                        finish();

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


        int count = 1;
        for (QuestionModel q:
                questionList) {

            com.mkn.testkart.com.testkart.exam.edu.exam.model.POResponse res = new com.mkn.testkart.com.testkart.exam.edu.exam.model.POResponse();

            res.setQuestionId(Integer.parseInt(q.getQuestionId()));
            res.setQuestionNo(count);
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


            res.setAttemptTime("2017-05-11 17:47:49");// add in db
            res.setOpened(Integer.parseInt(q.getOpened()));
            res.setAnswered(Integer.parseInt(q.getAnswered()));
            res.setReview(Integer.parseInt(q.getReview()));
            res.setTimeTaken(q.getTimeSpendOnQuestion());

            resList.add(res);

            count++;

        }

        esm.setResponse(resList);

        Gson gson = new Gson();
        String j = gson.toJson(esm);

        Log.v("My_Json", j);

        return esm;
    }


    @Override
    public void onShowInstruction(com.mkn.testkart.com.testkart.exam.edu.exam.examlist.examdetails.Exam exam) {

    }
}
*/