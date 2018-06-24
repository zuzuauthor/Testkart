package com.testkart.exam.edu.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.testkart.exam.edu.exam.QuestionModel;
import com.testkart.exam.edu.exam.ibps.datamodel.DataExam;
import com.testkart.exam.edu.exam.ibps.datamodel.DataLanguage;
import com.testkart.exam.edu.exam.ibps.datamodel.DataPagerQuestion;
import com.testkart.exam.edu.exam.ibps.datamodel.DataQuestion;
import com.testkart.exam.edu.exam.ibps.datamodel.DataResponse;
import com.testkart.exam.edu.exam.ibps.datamodel.DataSubject;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.packages.model.DataOrderSummary;
import com.testkart.exam.testkart.notifications.DataNotification;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by testkart on 1/5/17.
 * close cursor once extraction done
 */

public class DBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 5;
    // Database Name
    private static final String DATABASE_NAME = "edu_expression";

   // private DBHelper mDbHelper = new DBHelper(getContext());


    private static final String KEY_ID = "id";

    // exam table;
    private static final String TABLE_EXAM = "exam";
    public static final String KEY_EXAM_ID = "exam_id";
    public static final String KEY_EXAM_NAME = "exam_name";
    public static final String KEY_EXAM_DETAILS = "exam_details";
    public static final String KEY_EXAM_DURATION = "exam_duration";
    public static final String KEY_EXAM_START_DATE = "exam_start_date";
    public static final String KEY_EXAM_END_DATE = "exam_end_date";
    public static final String KEY_EXAM_PASSING_PERCENTAGE = "exam_passing_percentage";
    public static final String KEY_EXAM_NEGATIVE_MARKING = "exam_negative_marking";
    public static final String KEY_EXAM_PAID = "exam_paid";
    public static final String KEY_EXAM_AMOUNT = "exam_amount";
    public static final String KEY_EXAM_START_TIME = "exam_start";
    public static final String KEY_EXAM_END_TIME = "exam_end";
    public static final String KEY_EXAM_FINILIZE = "exam_finilize";
    public static final String KEY_EXAM_DURATION_CONTINUE = "duration_continue";
    public static final String KEY_EXAM_MULTI_LANGUAGE = "multi_language";


    // subject table
    private static final String TABLE_SUBJECT = "subject";
    public static final String KEY_SUBJECT_ID = "subject_id";
    public static final String KEY_SUBJECT_NAME = "subject_name";
    public static final String KEY_QUESTION_IDS = "question_id"; //comma sepreted
    private static final String KEY_SUBJECT_QUESTION_COUNT = "question_count";



    // package table
    private static final String TABLE_PACKAGE = "package";
    public static final String KEY_PACKAGE_ID = "package_id";
    public static final String KEY_PACKAGE_NAME = "package_name";
    public static final String KEY_PACKAGE_DESCRIPTION = "package_description";
    public static final String KEY_PACKAGE_AMOUNT = "package_amount";
    public static final String KEY_PACKAGE_SHOW_AMOUNT = "package_show_mount";
    public static final String KEY_PACKAGE_PHOTO = "package_photo";
    public static final String KEY_PACKAGE_EXPIRY_DAYS = "package_expiry_days";
    public static final String KEY_PACKAGE_STATUS = "package_status";
    public static final String KEY_PACKAGE_CREATED = "package_created";
    public static final String KEY_PACKAGE_MODIFIED = "package_modify";
    public static final String KEY_PACKAGE_EXAM_IDS = "package_exam_ids";
    public static final String KEY_PACKAGE_EXAM_NAME = "package_exam_name";
    public static final String KEY_PACKAGE_CURRANCY_CODE = "package_currancy_code";



    private static final String TABLE_NOTIFICATION = "notification";
    public static final String KEY_NOTIFY_USER_ID = "user_id";
    public static final String KEY_NOTIFY_ID = "notification_id";
    public static final String KEY_NOTIFY_TITLE = "title";
    public static final String KEY_NOTIFY_MESSAGE = "message";
    public static final String KEY_NOTIFY_IMAGE_URL = "image_url";
    public static final String KEY_NOTIFY_TIME = "notify_time";
    public static final String KEY_NOTIFY_IS_IMAGE_INCLUDE = "is_image_include";
    public static final String KEY_NOTIFY_VIEWED = "notification_viewed";

    //create notification table
    private String CREATE_NOTIFICATION_TABLE =

            "CREATE TABLE " + TABLE_NOTIFICATION + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    KEY_NOTIFY_USER_ID + " TEXT," +
                    KEY_NOTIFY_ID + " TEXT," +
                    KEY_NOTIFY_TITLE + " TEXT," +
                    KEY_NOTIFY_MESSAGE + " TEXT," +
                    KEY_NOTIFY_IMAGE_URL + " TEXT," +
                    KEY_NOTIFY_TIME + " TEXT," +
                    KEY_NOTIFY_VIEWED + " TEXT," +
                    KEY_NOTIFY_IS_IMAGE_INCLUDE + " TEXT)";


    //table creation   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
    private String CREATE_PACKAGE_TABLE =

            "CREATE TABLE " + TABLE_PACKAGE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    KEY_PACKAGE_ID + " TEXT," +
                    KEY_PACKAGE_NAME + " TEXT," +
                    KEY_PACKAGE_DESCRIPTION + " TEXT," +
                    KEY_PACKAGE_AMOUNT + " TEXT," +
                    KEY_PACKAGE_SHOW_AMOUNT + " TEXT," +
                    KEY_PACKAGE_PHOTO + " TEXT," +
                    KEY_PACKAGE_EXPIRY_DAYS + " TEXT," +
                    KEY_PACKAGE_STATUS + " TEXT," +
                    KEY_PACKAGE_CREATED + " TEXT," +
                    KEY_PACKAGE_MODIFIED + " TEXT," +
                    KEY_PACKAGE_EXAM_IDS + " TEXT," +
                    KEY_PACKAGE_EXAM_NAME + " TEXT," +
                    KEY_PACKAGE_CURRANCY_CODE + " TEXT)";





    //table creation   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
    private String CREATE_EXAM_TABLE =

            "CREATE TABLE " + TABLE_EXAM + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    KEY_EXAM_ID + " TEXT," +
                    KEY_EXAM_NAME + " TEXT," +
                    KEY_EXAM_DETAILS + " TEXT," +
                    KEY_EXAM_DURATION + " TEXT," +
                    KEY_EXAM_START_DATE + " TEXT," +
                    KEY_EXAM_END_DATE + " TEXT," +
                    KEY_EXAM_PASSING_PERCENTAGE + " TEXT," +
                    KEY_EXAM_NEGATIVE_MARKING + " TEXT," +
                    KEY_EXAM_START_TIME + " TEXT," +
                    KEY_EXAM_END_TIME + " TEXT," +
                    KEY_EXAM_FINILIZE + " TEXT," +
                    KEY_EXAM_PAID + " TEXT," +
                    KEY_EXAM_MULTI_LANGUAGE + " TEXT," +
                    KEY_EXAM_DURATION_CONTINUE + " TEXT," +
                    KEY_EXAM_AMOUNT + " TEXT)";
    ;


    private String CREATE_SUBJECT_TABLE =

            "CREATE TABLE " + TABLE_SUBJECT + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    KEY_SUBJECT_ID + " TEXT," +
                    KEY_SUBJECT_NAME + " TEXT," +
                    KEY_QUESTION_IDS + " TEXT," +
                    KEY_SUBJECT_QUESTION_COUNT + " TEXT)";



    private String CREATE_QUESTION1_TABLE =

            "CREATE TABLE " + TABLE_QUESTION + " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    KEY_QUESTION_ID + " TEXT," +
                    KEY_QUESTION_LOCAL_ID + " TEXT," +
                    KEY_QUESTION_SUBJECT + " TEXT," +
                    KEY_QUESTION_SUBJECT_ID + " TEXT," +
                    KEY_QUESTION_TEXT + " TEXT," +
                    KEY_QUESTION_HTML + " TEXT," +
                    KEY_QUESTION_OPTION1 + " TEXT," +
                    KEY_QUESTION_OPTION2 + " TEXT," +
                    KEY_QUESTION_OPTION3 + " TEXT," +
                    KEY_QUESTION_OPTION4 + " TEXT," +
                    KEY_QUESTION_OPTION5 + " TEXT," +
                    KEY_QUESTION_OPTION6 + " TEXT," +
                    KEY_QUESTION_OPTION_HTML + " TEXT," +
                    KEY_QUESTION_HINT + " TEXT," +
                    KEY_QUESTION_MARKS + " TEXT," +
                    KEY_QUESTION_NEGATIVE_MARKS + " TEXT," +
                    KEY_QUESTION_TYPE + " TEXT," +
                    KEY_QUESTION_MULTIPLE_TYPE + " TEXT," +
                    KEY_QUESTION_RESPONSE_TRUE_FALSE + " TEXT," +
                    KEY_QUESTION_RESPONSEFILL_BLANKS + " TEXT," +
                    KEY_QUESTION_RESPONSE_SUBJECTIVE + " TEXT," +
                    KEY_QUESTION_RESPONSE_MULTIPLE_CHOICE + " TEXT," +
                    KEY_QUESTION_IS_NEGATIVE_MARKS + " TEXT," +
                    KEY_QUESTION_OPENED + " TEXT," +
                    KEY_QUESTION_ANSWERED + " TEXT," +
                    KEY_QUESTION_REVIEW + " TEXT," +
                    KEY_QUESTION_ATTEMPT_TIME + " TEXT," +
                    KEY_JUMBLED_OPTIONS + " TEXT," +
                    KEY_QUESTION_TIME_SPEND + " TEXT)";


    //drop exam, subject, subjects
    private static final String SQL_DELETE_EXAM =
            "DROP TABLE IF EXISTS " + TABLE_EXAM;

    private static final String SQL_DELETE_SUBJECT =
            "DROP TABLE IF EXISTS " + TABLE_SUBJECT;

    private static final String SQL_DELETE_PACKAGE =
            "DROP TABLE IF EXISTS " + TABLE_PACKAGE;

    private static final String SQL_DELETE_NOTIFICATION_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NOTIFICATION;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_EXAM_TABLE);
        db.execSQL(CREATE_SUBJECT_TABLE);
        db.execSQL(CREATE_LANGUAGE_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);
        db.execSQL(CREATE_RESPONSE_TABLE);
        db.execSQL(CREATE_PACKAGE_TABLE);
        db.execSQL(CREATE_NOTIFICATION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_EXAM);
        db.execSQL(SQL_DELETE_SUBJECT);
        db.execSQL(SQL_DELETE_QUESTION);
        db.execSQL(SQL_DELETE_PACKAGE);
        db.execSQL(SQL_DELETE_LANGUAGE);
        db.execSQL(SQL_DELETE_RESPONSE);
        db.execSQL(SQL_DELETE_NOTIFICATION_TABLE);
        onCreate(db);

    }



    //------------------------Notification START---------------------------

    //insert notification
    public boolean addNotification(String userId, DataNotification notify){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTIFY_USER_ID, userId);
        values.put(KEY_NOTIFY_ID, notify.getNotificationId());
        values.put(KEY_NOTIFY_TITLE, notify.getTitle());
        values.put(KEY_NOTIFY_MESSAGE, notify.getMessage());
        values.put(KEY_NOTIFY_IMAGE_URL, notify.getImageUrl());
        values.put(KEY_NOTIFY_TIME, notify.getDateTime());
        values.put(KEY_NOTIFY_IS_IMAGE_INCLUDE, notify.isIncludeImage()+"");
        values.put(KEY_NOTIFY_VIEWED, "0"); //0 not read, 1 read

        long newRowId = db.insert(TABLE_NOTIFICATION, null, values);

        db.close();

        if(newRowId != -1){

            return true;

        }else{

            return false;
        }

    }


    //get latest 5 notifications respect to user

    //select * from (select * from tblmessage order by sortfield ASC limit 10) order by sortfield DESC;
    public ArrayList<DataNotification> getNotifications(String userId, int limit){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<DataNotification> dataSet = new ArrayList<>();

        String query;

        if(limit == -1){

             query = "select * from (select * from "+TABLE_NOTIFICATION+" where user_id="+userId+") order by id DESC;";

        }else{

             query = "select * from (select * from "+TABLE_NOTIFICATION+" where user_id="+userId+" limit "+limit+") order by id DESC;";
        }



        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {

                    DataNotification notify = new DataNotification();

                    notify.setNotificationId(cursor.getString(cursor.getColumnIndex(KEY_NOTIFY_ID)));
                    notify.setTitle(cursor.getString(cursor.getColumnIndex(KEY_NOTIFY_TITLE)));
                    notify.setMessage(cursor.getString(cursor.getColumnIndex(KEY_NOTIFY_MESSAGE)));
                    notify.setImageUrl(cursor.getString(cursor.getColumnIndex(KEY_NOTIFY_IMAGE_URL)));
                    notify.setIncludeImage(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_NOTIFY_IS_IMAGE_INCLUDE))));
                    notify.setDateTime(cursor.getString(cursor.getColumnIndex(KEY_NOTIFY_TIME)));

                    dataSet.add(notify);

                } while (cursor.moveToNext());

            }

        }

        return dataSet;

    }



    //get total notification count respect to user
    public int getNotificationCount(String userId){

        String countQuery = "SELECT  * FROM " + TABLE_NOTIFICATION+" WHERE "+KEY_NOTIFY_USER_ID+"="+userId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;

    }

    //delete notification respect to notification id

    public void deleteOldNotifications(ArrayList<DataNotification> notificationIds){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        for (DataNotification noti:
             notificationIds) {

            int count = db.delete(TABLE_NOTIFICATION, KEY_NOTIFY_ID+"="+noti.getNotificationId(), null);
            Log.v("Notification Delete", count+"");
        }
    }


    //mark notification all viewed respect to user
    public void markNotificationView(String userId){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTIFY_VIEWED, "1");

        db.update(TABLE_NOTIFICATION, values, KEY_NOTIFY_USER_ID+"="+userId, null);

    }


    //get un read notifications respect to user

    public int getUnreadMessageCount(String userId){

        String countQuery = "SELECT  * FROM " + TABLE_NOTIFICATION+" WHERE "+KEY_NOTIFY_USER_ID+"="+userId+" AND "+KEY_NOTIFY_VIEWED+"="+"0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    //------------------------Notification END ----------------------------


    //----------------------Package / Shopping ------------------

    public boolean addCartItem(DataOrderSummary cartItem){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PACKAGE_ID, cartItem.getPackageId());
        values.put(KEY_PACKAGE_NAME, cartItem.getPackageName());
        values.put(KEY_PACKAGE_DESCRIPTION, cartItem.getPackageDescription());
        values.put(KEY_PACKAGE_AMOUNT, cartItem.getPackageAmount());
        values.put(KEY_PACKAGE_SHOW_AMOUNT, cartItem.getPackageSHowAmount());
        values.put(KEY_PACKAGE_PHOTO, cartItem.getPackageImage());
        values.put(KEY_PACKAGE_EXPIRY_DAYS, cartItem.getPackageExpiryDays());
        values.put(KEY_PACKAGE_STATUS, cartItem.getPackageStatus());
        values.put(KEY_PACKAGE_CREATED, cartItem.getPackageCreated());
        values.put(KEY_PACKAGE_MODIFIED, cartItem.getPackageModify());
        values.put(KEY_PACKAGE_EXAM_IDS, cartItem.getPackageExamIds());
        values.put(KEY_PACKAGE_EXAM_NAME, cartItem.getPackageExamName());
        values.put(KEY_PACKAGE_CURRANCY_CODE, cartItem.getPackageCurrencyCode());

        long newRowId = db.insert(TABLE_PACKAGE, null, values);

        if(newRowId != -1){

            return true;

        }else{

            return false;
        }

    }


    public ArrayList<DataOrderSummary> getTotalCartItems(){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<DataOrderSummary> dataSet = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PACKAGE, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {

                    DataOrderSummary cartItem = new DataOrderSummary();

                    cartItem.setPackageId(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_ID)));
                    cartItem.setPackageName(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_NAME)));
                    cartItem.setPackageDescription(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_DESCRIPTION)));
                    cartItem.setPackageAmount(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_AMOUNT)));
                    cartItem.setPackageSHowAmount(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_SHOW_AMOUNT)));
                    cartItem.setPackageExpiryDays(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_EXPIRY_DAYS)));
                    cartItem.setPackageStatus(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_STATUS)));
                    cartItem.setPackageCreated(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CREATED)));
                    cartItem.setPackageModify(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_MODIFIED)));
                    cartItem.setPackageImage(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_PHOTO)));
                    cartItem.setPackageExamIds(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_EXAM_IDS)));
                    cartItem.setPackageExamName(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_EXAM_NAME)));
                    cartItem.setPackageCurrencyCode(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_CURRANCY_CODE)));

                    dataSet.add(cartItem);

                } while (cursor.moveToNext());

            }

        }

        return dataSet;

    }


    public boolean removeCartItem(String packageId){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        int count = db.delete(TABLE_PACKAGE, KEY_PACKAGE_ID+"="+packageId, null);

        if(count > 0){

            return true;
        }else{

            return false;
        }

    }


    public void clearCart() {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        //exam
        db.delete(TABLE_PACKAGE, null, null);

    }


    //----------------------------Finish Package / Shopping ----------------------------------




    //---------------------------IBPS Exam --------------------------

    //access method
    public boolean addExam(DataExam exam){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EXAM_ID, exam.getExamId());
        values.put(KEY_EXAM_NAME, exam.getExamName());
        values.put(KEY_EXAM_DETAILS, exam.getExamInstruction());
        values.put(KEY_EXAM_DURATION, exam.getDuration());
        values.put(KEY_EXAM_START_DATE, exam.getStartDate());
        values.put(KEY_EXAM_END_DATE, exam.getEndDate());
        values.put(KEY_EXAM_PASSING_PERCENTAGE, exam.getPassingPercent());
        values.put(KEY_EXAM_NEGATIVE_MARKING, exam.getNegativeMarking());
        values.put(KEY_EXAM_START_TIME, "0"/*exam.getExamStartTime()*/);
        values.put(KEY_EXAM_END_TIME, "0"/*exam.getExamEndTime()*/);
        values.put(KEY_EXAM_PAID, exam.getPaidExam());
        values.put(KEY_EXAM_AMOUNT, exam.getAmount());
        values.put(KEY_EXAM_FINILIZE, "false"/*exam.getExamFinilize()*/);
        values.put(KEY_EXAM_DURATION_CONTINUE, exam.getExamDurationContinue());
        values.put(KEY_EXAM_MULTI_LANGUAGE, exam.getMultiLanguage());

        long newRowId = db.insert(TABLE_EXAM, null, values);

        if(newRowId != -1){

            return true;

        }else{

            return false;
        }

    }


    //method2
    public int getExamDuration(){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        int examDuration = 0;

        String query = "SELECT * FROM "+TABLE_EXAM;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null){

            if(cursor.moveToFirst()){

                do{

                    examDuration = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_EXAM_DURATION)));

                }while (cursor.moveToNext());

            }

        }

        return examDuration;
    }



    public DataExam getExam(){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        DataExam exam = new DataExam();

        String query = "SELECT * FROM "+TABLE_EXAM;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null){

            if(cursor.moveToFirst()){

                do{

                    exam.setExamId(cursor.getString(cursor.getColumnIndex(KEY_EXAM_ID)));
                    exam.setExamName(cursor.getString(cursor.getColumnIndex(KEY_EXAM_NAME)));
                    exam.setExamInstruction(cursor.getString(cursor.getColumnIndex(KEY_EXAM_DETAILS)));
                    exam.setDuration(cursor.getString(cursor.getColumnIndex(KEY_EXAM_DURATION)));
                    exam.setStartDate(cursor.getString(cursor.getColumnIndex(KEY_EXAM_START_DATE)));
                    exam.setEndDate(cursor.getString(cursor.getColumnIndex(KEY_EXAM_END_DATE)));
                    exam.setPassingPercent(cursor.getString(cursor.getColumnIndex(KEY_EXAM_PASSING_PERCENTAGE)));
                    exam.setNegativeMarking(cursor.getString(cursor.getColumnIndex(KEY_EXAM_NEGATIVE_MARKING)));
                    exam.setPaidExam(cursor.getString(cursor.getColumnIndex(KEY_EXAM_PAID)));
                    exam.setAmount(cursor.getString(cursor.getColumnIndex(KEY_EXAM_AMOUNT)));
                    exam.setExamStartTime(cursor.getString(cursor.getColumnIndex(KEY_EXAM_START_TIME)));
                    exam.setExamEndTime(cursor.getString(cursor.getColumnIndex(KEY_EXAM_END_TIME)));
                    exam.setExamDurationContinue(cursor.getString(cursor.getColumnIndex(KEY_EXAM_DURATION_CONTINUE)));
                    exam.setMultiLanguage(cursor.getString(cursor.getColumnIndex(KEY_EXAM_DURATION_CONTINUE)));

                }while (cursor.moveToNext());
            }

        }

        return exam;
    }


    //method13
    public void updateExamStartTime(String examId, long l) {

        Log.v("Exam_time", examId+", "+l);

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues c = new ContentValues();
        c.put(KEY_EXAM_START_TIME, l+"");

        db.update(TABLE_EXAM, c, KEY_EXAM_ID+"="+examId, null);
    }


    //method14
    public void updateExamEndTime(String examId, long l) {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues c = new ContentValues();
        c.put(KEY_EXAM_END_TIME, l+"");

        db.update(TABLE_EXAM, c, KEY_EXAM_ID+"="+examId, null);
    }


    //method15
    public void updateExamFinalize(String examId, String aTrue) {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues c = new ContentValues();
        c.put(KEY_EXAM_FINILIZE, aTrue);

        db.update(TABLE_EXAM, c, KEY_EXAM_ID+"="+examId, null);
    }


    //method16
    public String getExamFinilize() {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        String finalize = "";
        String query = "SELECT * FROM "+TABLE_EXAM;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null){

            if(cursor.moveToFirst()){

                do {

                    finalize = cursor.getString(cursor.getColumnIndex(KEY_EXAM_FINILIZE));

                }while (cursor.moveToNext());

            }

        }

        return finalize;
    }


    //method17
    public void updateExamTime(String examId, long updateExamMinuits) {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues c = new ContentValues();
        c.put(KEY_EXAM_DURATION, updateExamMinuits+"");

        db.update(TABLE_EXAM, c, KEY_EXAM_ID+"="+examId, null);

    }


    //method8
    public boolean checkExam() {

        SQLiteDatabase db = getWritableDatabase();

        String count = "SELECT count(*) FROM "+TABLE_EXAM;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if(icount>0){

            return true;

        }else {

            return false;
        }

    }


    //---------------------------------Finish Exam ---------------------------



    //----------------------Subject ----------------------------

    public boolean addSubjects(ArrayList<DataSubject> dataSet){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        long newRowId = 0;

        if(!dataSet.isEmpty()){

            for (DataSubject subject:
                 dataSet) {

                if(newRowId != -1){

                    ContentValues values = new ContentValues();
                    values.put(KEY_SUBJECT_ID, subject.getSubjectId());
                    values.put(KEY_SUBJECT_NAME, subject.getSubjectName());

                    newRowId = db.insert(TABLE_SUBJECT, null, values);

                }else{

                    break;
                }

            }

        }

        if(newRowId != -1){

            return true;

        }else{

            return false;
        }

    }


    public void updateSubjectWithQNo(String subjectId, String questionNo){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_QUESTION_IDS, questionNo);

        db.update(TABLE_SUBJECT, cv, KEY_SUBJECT_ID+"="+subjectId, null);

    }


    public HashMap<String, String> getAllSubjectsIDs(){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        HashMap<String, String> dataSet = new HashMap<>();

        String query = "SELECT * FROM "+TABLE_SUBJECT;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null){

            if(cursor.moveToFirst()){

                do {

                    String sName = cursor.getString(cursor.getColumnIndex(KEY_SUBJECT_NAME));
                    String sId = cursor.getString(cursor.getColumnIndex(KEY_SUBJECT_ID));

                    dataSet.put(sName, sId);

                }while (cursor.moveToNext());
            }

        }

        return dataSet;

    }


    //--------------------------Finish Subject----------------------


    public void deleteRecords(){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        //exam
        db.delete(TABLE_EXAM, null, null);

        //subject
        db.delete(TABLE_SUBJECT, null, null);

        //Question
        db.delete(TABLE_QUESTION, null, null);

        db.delete(TABLE_RESPONSE, null, null);

        db.delete(TABLE_LANGUAGE, null, null);
    }



    //method3
    public ArrayList<DataResponse> getQuestionList(){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<DataResponse> dataSet = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESPONSE, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {

                    ArrayList<String> options = new ArrayList<>();
                    DataResponse question = new DataResponse();

                    question.setQuestionId(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ID)));
                    question.setQuestionLocalId(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_LOCAL_ID)));
                    question.setSubjectName(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_SUBJECT)));
                    question.setSubjectId(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_SUBJECT_ID)));
                   // question.setQuestionType(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_TEXT)));

                    /*if(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION1)).length() > 0){
                        options.add(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION1)));
                    }

                    if(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION2)).length() > 0){
                        options.add(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION2)));
                    }

                    if(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION3)).length() > 0){
                        options.add(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION3)));
                    }

                    if(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION4)).length() > 0){
                        options.add(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION4)));
                    }

                    if(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION5)).length() > 0){
                        options.add(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION5)));
                    }

                    if(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION6)).length() > 0){
                        options.add(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION6)));
                    }*/

                   // question.setOptions(options);  //arrayList

                  //  question.setHint(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_HINT)));
                    question.setMarks(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_MARKS)));
                    question.setNegativeMarks(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_NEGATIVE_MARKS)));
                    question.setQuestionType(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_TYPE)));
                    //question.setQuestionType(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_MULTIPLE_TYPE)));   //multiple / single choice
                   // question.setTrueFalseResponse(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_RESPONSE_TRUE_FALSE)));    //update later
                   // question.setFillBlankResponse(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_RESPONSEFILL_BLANKS)));    // update later
                   // question.setSubjectiveResponse(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_RESPONSE_SUBJECTIVE)));   //update later
                    question.setYourAnswer(cursor.getString(cursor.getColumnIndex(KEY_YOUR_ANSWER))); //update later
                    question.setNegativeMarks(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_NEGATIVE_MARKS)));
                    question.setOpened(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPENED)));   //set default  0 1
                    question.setAnswer(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ANSWERED)));  //set default 0 1
                    question.setReview(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_REVIEW)));   // set default 0 1
                    question.setQuestionSpendTime(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_TIME_SPEND)));  //update runtime
                    question.setQuestionViewTime(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ATTEMPT_TIME)));
                    question.setJumbleOptions(cursor.getString(cursor.getColumnIndex(KEY_JUMBLED_OPTIONS)));
                    dataSet.add(question);

                } while (cursor.moveToNext());

            }

        }

        return dataSet;
    }


    //method4
    public void updateQuestionTimeSpend(String qustionId, String totalTime){

        Log.v("DB helper", "qid: "+qustionId +"/n TIme: "+totalTime);

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        String previousSec = null;

        //get previous seconds
        String query = "SELECT * FROM "+TABLE_RESPONSE+" WHERE "+KEY_QUESTION_ID+"="+qustionId;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null){

            if(cursor.moveToFirst()){

                do {

                    previousSec = cursor.getString(cursor.getColumnIndex(KEY_QUESTION_TIME_SPEND));

                }while (cursor.moveToNext());

            }

        }

        int ts = Integer.parseInt(totalTime) + Integer.parseInt(previousSec);

        ContentValues cv = new ContentValues();
        cv.put(KEY_QUESTION_TIME_SPEND, ts+"");

        db.update(TABLE_RESPONSE, cv, KEY_QUESTION_ID+"="+qustionId, null);

    }


    //method5
    public String getQuestionResponse(String qustionId, String type) {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        String response = "";

        String query = "SELECT * FROM " + TABLE_RESPONSE + " WHERE " + KEY_QUESTION_ID + "=" + qustionId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {

                    response = cursor.getString(cursor.getColumnIndex(type));

                } while (cursor.moveToNext());

            }
        }

        return response;
    }



    //method6
    public void updateQuestionResponse(String qustionId, String newResponse, String type){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(type, newResponse);

        db.update(TABLE_RESPONSE, cv, KEY_QUESTION_ID+"="+qustionId, null);

    }


    //method7
    public void updateQuestionStatus(String questionId, String s, String keyQuestionOpened) {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(keyQuestionOpened, s);

        db.update(TABLE_RESPONSE, cv, KEY_QUESTION_ID+"="+questionId, null);

    }


    //method8
    public void updateQuestionIsAnswered(String questionId, String keyQuestionResponseMultipleChoice) {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        String response = getQuestionResponse(questionId, keyQuestionResponseMultipleChoice);

        ContentValues cv = new ContentValues();
        if(response.isEmpty()){
            cv.put(KEY_QUESTION_ANSWERED, "0");

            db.update(TABLE_QUESTION, cv, KEY_QUESTION_ID+"="+questionId, null);

        }else{

            cv.put(KEY_QUESTION_ANSWERED, "1");

            db.update(TABLE_QUESTION, cv, KEY_QUESTION_ID+"="+questionId, null);

        }
    }


    //method9
    public String getQuestionStatus(String questionId) {

         /*

        opened 0 not visited
        opened 1 visted
        answered 0 : not naswered
        answered 1 answered
        reveiw 0 no review
        review 1 : revie
        review 1 ans answered 1 : review and answered

     */


        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        String response = "";

        String query = "SELECT * FROM " + TABLE_RESPONSE + " WHERE " + KEY_QUESTION_ID + "=" + questionId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {


                    String visited = cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPENED));
                    String answered = cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ANSWERED));
                    String review = cursor.getString(cursor.getColumnIndex(KEY_QUESTION_REVIEW));

                    if(visited.equals("0")
                            && answered.equals("0")
                            && review.equals("0")){

                        //not visited GREY


                        return "0";

                    }else if(visited.equals("1")
                            && answered.equals("0")
                            && review.equals("0")){

                        //not answered RED

                        return "1";

                    }else if(visited.equals("1")
                            && answered.equals("1")
                            && review.equals("0")){
                        //answered GREEN

                        return "2";

                    }else if(visited.equals("1")
                            && answered.equals("1")
                            && review.equals("1")){
                        //answered and mark review PURPLE with GREEN Tick

                        return "4";


                    }else if(visited.equals("1")
                            && answered.equals("0")
                            && review.equals("1")){

                        //mark review Purple
                        return "3";
                    }


                } while (cursor.moveToNext());

            }
        }

        return "0";
    }



    //method10
    public ArrayList<DataSubject> getAllSubjects() {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ArrayList<DataSubject> subjectList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_SUBJECT;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {

                    DataSubject subject = new DataSubject();
                    subject.setSubjectId(cursor.getString(cursor.getColumnIndex(KEY_SUBJECT_ID)));
                    subject.setSubjectName(cursor.getString(cursor.getColumnIndex(KEY_SUBJECT_NAME)));
                   // subject.setQuestionsCount(cursor.getString(cursor.getColumnIndex(KEY_SUBJECT_QUESTION_COUNT)));
                    subject.setOrdering(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_IDS)));

                    subjectList.add(subject);

                } while (cursor.moveToNext());
            }

        }

        return subjectList;
    }


    //method11
    public ArrayList<QuestionModel> getQuestionsGroupBySubjects(String subjectName){


        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ArrayList<QuestionModel> questionList = new ArrayList<>();

        String query = "SELECT * FROM "+TABLE_QUESTION+" WHERE "+KEY_QUESTION_SUBJECT+"="+"\""+subjectName+"\";";

        Cursor cursor = db.rawQuery(query, null);


        if(cursor != null){

            if (cursor.moveToFirst()){

                do {

                    QuestionModel question = new QuestionModel();

                    question.setQuestionId(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ID)));
                    question.setSubject(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_SUBJECT)));
                    question.setQuestion(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_TEXT)));
                    question.setHint(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_HINT)));
                    question.setMarks(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_MARKS)));
                    question.setIsNegativeMarks(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_NEGATIVE_MARKS)));
                    question.setQuestionType(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_TYPE)));
                    question.setMultipleType(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_MULTIPLE_TYPE)));   //multiple / single choice
                    question.setTrueFalseResponse(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_RESPONSE_TRUE_FALSE)));    //update later
                    question.setFillBlankResponse(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_RESPONSEFILL_BLANKS)));    // update later
                    question.setSubjectiveResponse(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_RESPONSE_SUBJECTIVE)));   //update later
                    question.setMultipleChoiceResponse(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_RESPONSE_MULTIPLE_CHOICE))); //update later
                    question.setNegativeMarks(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_NEGATIVE_MARKS)));
                    question.setOpened(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPENED)));   //set default  0 1
                    question.setAnswered(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ANSWERED)));  //set default 0 1
                    question.setReview(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_REVIEW)));   // set default 0 1
                    question.setTimeSpendOnQuestion(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_TIME_SPEND))));  //update runtime


                    questionList.add(question);

                }while (cursor.moveToNext());
            }

        }

        return questionList;

    }


    //method12
    public HashMap<String, String> getQuestionStatSummary(){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        HashMap<String, String> dataSet = new HashMap<>();


        String queryNotVisit = "SELECT * FROM "+TABLE_RESPONSE+" WHERE "+KEY_QUESTION_OPENED +"="+"\"0\" AND "
                +KEY_QUESTION_ANSWERED +"="+"\"0\" AND "
                +KEY_QUESTION_REVIEW +"="+"\"0\"";

        String queryVisitedNotAnswered = "SELECT * FROM "+TABLE_RESPONSE+" WHERE "+KEY_QUESTION_OPENED +"="+"\"1\" AND "
                +KEY_QUESTION_ANSWERED +"="+"\"0\" AND "
                +KEY_QUESTION_REVIEW +"="+"\"0\"";


        String queryAnswered = "SELECT * FROM "+TABLE_RESPONSE+" WHERE "+KEY_QUESTION_OPENED +"="+"\"1\" AND "
                +KEY_QUESTION_ANSWERED +"="+"\"1\" AND "
                +KEY_QUESTION_REVIEW +"="+"\"0\"";

        String queryReview = "SELECT * FROM "+TABLE_RESPONSE+" WHERE "+KEY_QUESTION_OPENED +"="+"\"1\" AND "
                +KEY_QUESTION_ANSWERED +"="+"\"0\" AND "
                +KEY_QUESTION_REVIEW +"="+"\"1\"";

        String queryReviewAnswered = "SELECT * FROM "+TABLE_RESPONSE+" WHERE "+KEY_QUESTION_OPENED +"="+"\"1\" AND "
                +KEY_QUESTION_ANSWERED +"="+"\"1\" AND "
                +KEY_QUESTION_REVIEW +"="+"\"1\"";


        Cursor cursor1 = db.rawQuery(queryNotVisit, null);
        dataSet.put(Consts.KEY_NOT_VISIT, cursor1.getCount()+"");

        Cursor cursor2 = db.rawQuery(queryVisitedNotAnswered, null);
        dataSet.put(Consts.KEY_NOTANSWERED, cursor2.getCount()+"");

        Cursor cursor3 = db.rawQuery(queryAnswered, null);
        dataSet.put(Consts.KEY_ANSWERED, cursor3.getCount()+"");

        Cursor cursor4 = db.rawQuery(queryReview, null);
        dataSet.put(Consts.KEY_REVIEW, cursor4.getCount()+"");

        Cursor cursor5 = db.rawQuery(queryReviewAnswered, null);
        dataSet.put(Consts.KEY_REVIEW_ANSWERED, cursor5.getCount()+"");

        return dataSet;
    }


    //method19
    public void updateAttemptTime(String questionId, String at) {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_QUESTION_ATTEMPT_TIME, at);

        db.update(TABLE_RESPONSE, cv, KEY_QUESTION_ID+"="+questionId, null);
    }




    // IBPS QUESTIONS, RESPONSE, and LANGUAGE TABLE

    // question table
    private static final String TABLE_RESPONSE = "response";
    private static final String TABLE_QUESTION = "question";
    public static final String KEY_QUESTION_ID = "question_id";
    public static final String KEY_QUESTION_LOCAL_ID = "question_local_id";
    public static final String KEY_QUESTION_SUBJECT = "question_subject";
    public static final String KEY_QUESTION_SUBJECT_ID = "question_subject_id";
    public static final String KEY_QUESTION_TEXT = "question_text";
    public static final String KEY_QUESTION_HTML = "question_html";
    public static final String KEY_QUESTION_OPTION1 = "question_option1";
    public static final String KEY_QUESTION_OPTION2 = "question_option2";
    public static final String KEY_QUESTION_OPTION3 = "question_option3";
    public static final String KEY_QUESTION_OPTION4 = "question_option4";
    public static final String KEY_QUESTION_OPTION5 = "question_option5";
    public static final String KEY_QUESTION_OPTION6 = "question_option6";
    public static final String KEY_LANGUAGE_ARRAY = "language_array";
    public static final String KEY_QUESTION_OPTION_HTML = "question_option_html";
    public static final String KEY_QUESTION_HINT = "question_hint";
    public static final String KEY_QUESTION_MARKS = "question_marks";
    public static final String KEY_QUESTION_NEGATIVE_MARKS = "question_negative_marks";
    public static final String KEY_QUESTION_TYPE = "question_type";
    public static final String KEY_QUESTION_MULTIPLE_TYPE = "question_multiple_type";
    public static final String KEY_QUESTION_RESPONSE_TRUE_FALSE = "question_response_true_false";
    public static final String KEY_QUESTION_RESPONSEFILL_BLANKS = "question_response_fillblanks";
    public static final String KEY_QUESTION_RESPONSE_SUBJECTIVE = "question_response_subjective";
    public static final String KEY_QUESTION_RESPONSE_MULTIPLE_CHOICE = "question_response_multiple_choice";
    public static final String KEY_QUESTION_IS_NEGATIVE_MARKS = "question_is_negative_marks";
    public static final String KEY_QUESTION_OPENED = "question_opened";
    public static final String KEY_QUESTION_ANSWERED = "question_answered";
    public static final String KEY_QUESTION_REVIEW = "question_review";
    public static final String KEY_QUESTION_TIME_SPEND = "question_time_spend";
    public static final String KEY_QUESTION_ATTEMPT_TIME = "question_attempt_time";
    public static final String KEY_JUMBLED_OPTIONS = "jumbled_options";
    public static final String KEY_CORRECT_ANSWER = "correct_answer";
    public static final String KEY_YOUR_ANSWER = "your_answer";
    public static final String KEY_QUESTION_WITH_PASSAGE = "with_passage";


    //Table Language
    public static final String TABLE_LANGUAGE = "language";
    public static final String KEY_LANGUAGE_ID = "language_id";
    public static final String KEY_LANGUAGE_NAME = "language_name";

    private String CREATE_LANGUAGE_TABLE =

            "CREATE TABLE " + TABLE_LANGUAGE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    KEY_LANGUAGE_ID + " TEXT," +
                    KEY_LANGUAGE_NAME + " TEXT)";

    private static final String SQL_DELETE_LANGUAGE =
            "DROP TABLE IF EXISTS " + TABLE_LANGUAGE;



    private static final String CREATE_QUESTION_TABLE =

            "CREATE TABLE " + TABLE_QUESTION + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    KEY_QUESTION_ID + " TEXT," +
                    KEY_QUESTION_SUBJECT + " TEXT," +
                    KEY_QUESTION_SUBJECT_ID + " TEXT," +
                    KEY_LANGUAGE_ID + " TEXT," +
                    KEY_LANGUAGE_NAME + " TEXT," +
                    KEY_QUESTION_TEXT + " TEXT," +
                    KEY_QUESTION_HINT + " TEXT," +
                    KEY_QUESTION_OPTION1 + " TEXT," +
                    KEY_QUESTION_OPTION2 + " TEXT," +
                    KEY_QUESTION_OPTION3 + " TEXT," +
                    KEY_QUESTION_OPTION4 + " TEXT," +
                    KEY_QUESTION_OPTION5 + " TEXT," +
                    KEY_QUESTION_OPTION6 + " TEXT)";

    private static final String SQL_DELETE_QUESTION =
            "DROP TABLE IF EXISTS " + TABLE_QUESTION;



    private static final String CREATE_RESPONSE_TABLE =

            "CREATE TABLE " + TABLE_RESPONSE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    KEY_QUESTION_ID + " TEXT," +
                    KEY_QUESTION_LOCAL_ID + " TEXT," +
                    KEY_QUESTION_SUBJECT + " TEXT," +
                    KEY_QUESTION_SUBJECT_ID + " TEXT," +
                    KEY_LANGUAGE_ARRAY + " TEXT," +
                    KEY_QUESTION_MARKS + " TEXT," +
                    KEY_QUESTION_NEGATIVE_MARKS + " TEXT," +
                    KEY_QUESTION_IS_NEGATIVE_MARKS + " TEXT," +
                    KEY_JUMBLED_OPTIONS + " TEXT," +
                    KEY_QUESTION_WITH_PASSAGE + " TEXT," +
                    KEY_QUESTION_TYPE + " TEXT," +
                    KEY_CORRECT_ANSWER + " TEXT," +
                    KEY_YOUR_ANSWER + " TEXT," +
                    KEY_QUESTION_OPENED + " TEXT," +
                    KEY_QUESTION_ANSWERED + " TEXT," +
                    KEY_QUESTION_REVIEW + " TEXT," +
                    KEY_QUESTION_ATTEMPT_TIME + " TEXT," +
                    KEY_QUESTION_TIME_SPEND + " TEXT)";


    private static final String SQL_DELETE_RESPONSE =
            "DROP TABLE IF EXISTS " + TABLE_RESPONSE;





    //-----------------------Language ---------------------

    public boolean addLanguage(ArrayList<DataLanguage> dataSet){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        long newRowId = 0;

        if(!dataSet.isEmpty()){

            for (DataLanguage lang:
                    dataSet) {

                if(newRowId != -1){

                    ContentValues values = new ContentValues();
                    values.put(KEY_LANGUAGE_ID, lang.getLangId());
                    values.put(KEY_LANGUAGE_NAME, lang.getLangName());

                    newRowId = db.insert(TABLE_LANGUAGE, null, values);

                }else{

                    break;
                }

            }

        }

        if(newRowId != -1){

            return true;

        }else{

            return false;
        }

    }



    public ArrayList<DataLanguage> getLanguages() {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ArrayList<DataLanguage> dataSet = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LANGUAGE, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {

                    DataLanguage dpq = new DataLanguage();

                    dpq.setLangId(cursor.getString(cursor.getColumnIndex(KEY_LANGUAGE_ID)));
                    dpq.setLangName(cursor.getString(cursor.getColumnIndex(KEY_LANGUAGE_NAME)));

                    //add language stat
                    dataSet.add(dpq);

                } while (cursor.moveToNext());

            }

        }

        return dataSet;
    }



    //---------------------------Finish Language ---------------------------

    //------------------- POResponse / Question stats

    //method1
    public boolean addQuestionsStats(ArrayList<DataResponse> dataSet){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        long newRowId = 0;

        if(!dataSet.isEmpty()){

            for (DataResponse question:
                    dataSet) {

                if(newRowId != -1){

                    ContentValues values = new ContentValues();
                    values.put(KEY_QUESTION_ID, question.getQuestionId());
                    values.put(KEY_QUESTION_LOCAL_ID, question.getQuestionLocalId());
                    values.put(KEY_QUESTION_SUBJECT, question.getSubjectName());
                    values.put(KEY_QUESTION_SUBJECT_ID, question.getSubjectId());
                    values.put(KEY_LANGUAGE_ARRAY, question.getLanguageArray());
                    values.put(KEY_QUESTION_MARKS, question.getMarks());
                    values.put(KEY_QUESTION_NEGATIVE_MARKS, question.getNegativeMarks());
                    values.put(KEY_QUESTION_TYPE, question.getQuestionType());
                    values.put(KEY_JUMBLED_OPTIONS, question.getJumbleOptions());
                    values.put(KEY_QUESTION_WITH_PASSAGE, question.getPassageInclude());
                    values.put(KEY_CORRECT_ANSWER, question.getCorrectAnswer());
                    values.put(KEY_YOUR_ANSWER, question.getYourAnswer());
                    values.put(KEY_QUESTION_OPENED, question.getOpened());
                    values.put(KEY_QUESTION_ANSWERED, question.getAnswer());
                    values.put(KEY_QUESTION_REVIEW, question.getReview());
                    values.put(KEY_QUESTION_ATTEMPT_TIME, question.getQuestionViewTime());
                    values.put(KEY_QUESTION_TIME_SPEND, question.getQuestionSpendTime());

                    newRowId = db.insert(TABLE_RESPONSE, null, values);

                }else{

                    break;
                }

            }

        }

        if(newRowId != -1){

            return true;

        }else{

            return false;
        }

    }


    //add multi language question
    public boolean addQuestions(ArrayList<DataQuestion> dataSet){

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        long newRowId = 0;

        if(!dataSet.isEmpty()){

            for (DataQuestion question:
                    dataSet) {

                if(newRowId != -1){

                    ContentValues values = new ContentValues();
                    values.put(KEY_QUESTION_ID, question.getQuestionId());
                    values.put(KEY_QUESTION_SUBJECT, question.getSubjectName());
                    values.put(KEY_QUESTION_SUBJECT_ID, question.getSubjectId());
                    values.put(KEY_QUESTION_TEXT, question.getQuestionText());
                    values.put(KEY_QUESTION_OPTION1, question.getOption1());
                    values.put(KEY_QUESTION_OPTION2, question.getOption2());
                    values.put(KEY_QUESTION_OPTION3, question.getOption3());
                    values.put(KEY_QUESTION_OPTION4, question.getOption4());
                    values.put(KEY_QUESTION_OPTION5, question.getOption5());
                    values.put(KEY_QUESTION_OPTION6, question.getOption6());
                    values.put(KEY_QUESTION_HINT, question.getHint());
                    values.put(KEY_LANGUAGE_ID, question.getLangId());
                    values.put(KEY_LANGUAGE_NAME, question.getLangName());

                    newRowId = db.insert(TABLE_QUESTION, null, values);

                }else{

                    break;
                }

            }

        }

        if(newRowId != -1){

            return true;

        }else{

            return false;
        }

    }

    public ArrayList<DataPagerQuestion> getPagerQuestion() {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        ArrayList<DataPagerQuestion> dataSet = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESPONSE, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {

                    DataPagerQuestion dpq = new DataPagerQuestion();

                    DataResponse question = new DataResponse();

                    question.setQuestionId(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ID)));
                    question.setQuestionLocalId(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_LOCAL_ID)));
                    question.setSubjectName(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_SUBJECT)));
                    question.setSubjectId(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_SUBJECT_ID)));
                    question.setMarks(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_MARKS)));
                    question.setNegativeMarks(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_NEGATIVE_MARKS)));
                    question.setQuestionType(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_TYPE)));
                    question.setYourAnswer(cursor.getString(cursor.getColumnIndex(KEY_YOUR_ANSWER))); //update later
                    question.setNegativeMarks(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_NEGATIVE_MARKS)));
                    question.setOpened(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPENED)));   //set default  0 1
                    question.setAnswer(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ANSWERED)));  //set default 0 1
                    question.setReview(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_REVIEW)));   // set default 0 1
                    question.setQuestionSpendTime(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_TIME_SPEND)));  //update runtime
                    question.setQuestionViewTime(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ATTEMPT_TIME)));
                    question.setJumbleOptions(cursor.getString(cursor.getColumnIndex(KEY_JUMBLED_OPTIONS)));
                    question.setPassageInclude(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_WITH_PASSAGE)));//KEY_QUESTION_WITH_PASSAGE
                    //add question stat
                    dpq.setQuestionStat(question);


                    //add question map
                    HashMap<String, DataQuestion> qMap = getMultiLangQuestionMap(question.getQuestionId());

                    dpq.setQuestionMap(qMap);

                    dataSet.add(dpq);

                } while (cursor.moveToNext());

            }

        }

        return dataSet;
    }

    private HashMap<String, DataQuestion> getMultiLangQuestionMap(String questionId) {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        HashMap<String, DataQuestion> questionMap = new HashMap<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_QUESTION + " WHERE " + KEY_QUESTION_ID + "='" + questionId+"\'", null);


        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {

                    DataQuestion question = new DataQuestion();

                    question.setQuestionId(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ID)));
                    question.setSubjectId(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_SUBJECT_ID)));
                    question.setSubjectName(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_SUBJECT)));
                    question.setLangId(cursor.getString(cursor.getColumnIndex(KEY_LANGUAGE_ID)));
                    question.setLangName(cursor.getString(cursor.getColumnIndex(KEY_LANGUAGE_NAME)));
                    question.setQuestionText(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_TEXT)));
                    question.setOption1(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION1)));
                    question.setOption2(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION2)));
                    question.setOption3(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION3)));
                    question.setOption4(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION4)));
                    question.setOption5(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION5)));
                    question.setOption6(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_OPTION6))); //update later
                    question.setHint(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_HINT)));

                    //add question to map
                    questionMap.put(question.getLangName(), question);

                } while (cursor.moveToNext());

            }

        }

        return questionMap;
    }

    //----------------------------

}
