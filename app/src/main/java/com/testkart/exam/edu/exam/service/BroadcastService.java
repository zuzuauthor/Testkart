package com.testkart.exam.edu.exam.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.testkart.exam.edu.exam.ibps.datamodel.DataExam;
import com.testkart.exam.edu.helper.DBHelper;

import java.util.concurrent.TimeUnit;

/**
 * Created by testkart on 4/5/17.
 */

public class BroadcastService extends Service{

        private final static String TAG = "BroadcastService";

    public static boolean finalize = false;

        public static final String COUNTDOWN_BR = "your_package_name.countdown_br";
        Intent bi = new Intent(COUNTDOWN_BR);

        CountDownTimer cdt = null;

        DBHelper dbHelper;
        int examDuration;

        @Override
        public void onCreate() {
            super.onCreate();

            Log.i(TAG, "Starting timer...");

            /*startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                    notification);*/

            dbHelper = new DBHelper(this);

            examDuration = dbHelper.getExamDuration();

            if(examDuration > 0){

                long totalDurationMills = TimeUnit.MINUTES.toMillis(examDuration);

                cdt = new CountDownTimer(totalDurationMills, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    /*Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);*/
                        bi.putExtra("countdown", millisUntilFinished);
                        bi.putExtra("finalize", false);
                        sendBroadcast(bi);
                    }

                    @Override
                    public void onFinish() {
                        Log.i(TAG, "Timer finished");

                        //update exam finilize flag
                        //send responses to server

                        dbHelper.updateExamEndTime(dbHelper.getExam().getExamId(), System.currentTimeMillis());
                        finalize = true;
                        dbHelper.updateExamFinalize(dbHelper.getExam().getExamId(), "true");
                        bi.putExtra("finalize", true);

                        //post result
                        sendBroadcast(bi);


                        //stopForeground(true);
                        stopSelf();

                    }
                };

                cdt.start();
            }else{

                //unlimited timer

            }

        }

        @Override
        public void onDestroy() {

            cdt.cancel();
            Log.i(TAG, "Timer cancelled");


            if(!finalize){

                long currentMills = System.currentTimeMillis();

                long stopTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(currentMills);
                long stopTimeMinutes = TimeUnit.MILLISECONDS.toMinutes(currentMills);
                Log.v("stopTimeSeconds", stopTimeSeconds+"");
                Log.v("stopTimeMinutes", stopTimeMinutes+"");

                dbHelper.updateExamEndTime(dbHelper.getExam().getExamId(), System.currentTimeMillis());
                dbHelper.updateExamFinalize(dbHelper.getExam().getExamId(), "false");

                DataExam em = dbHelper.getExam();

                long startTimeMinutes = TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(em.getExamStartTime()));

                int updatedExamTime = examDuration - ((int)stopTimeMinutes - (int)startTimeMinutes);
                Log.v("updatedExamTime", updatedExamTime+"");

                dbHelper.updateExamTime(dbHelper.getExam().getExamId(), (updatedExamTime>0)? updatedExamTime:1);

            }else{

                dbHelper.updateExamEndTime(dbHelper.getExam().getExamId(), System.currentTimeMillis());
                dbHelper.updateExamFinalize(dbHelper.getExam().getExamId(), "true");
            }

            super.onDestroy();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }


}

