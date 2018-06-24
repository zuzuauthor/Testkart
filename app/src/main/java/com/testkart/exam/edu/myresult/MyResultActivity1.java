package com.testkart.exam.edu.myresult;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.MyResultActivity;
import com.testkart.exam.R;
import com.testkart.exam.edu.helper.Download;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.testkart.my_result.ViewResultActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by testkart on 18/5/17.
 */

public class MyResultActivity1 extends AppCompatActivity implements MyResultAdapter.OnMyresultListener {

    private ListView viewMyResult;

    private SessionManager sessionManager;
    private ProgressDialog progressDialog;
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myresult);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Result");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        //initialization
        initialization();

    }

    private void initialization() {

        sessionManager = new SessionManager(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("My Result");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        viewMyResult = (ListView)findViewById(R.id.viewMyResult);

        getMyResult();
    }

    private void getMyResult() {

        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            progressDialog.show();

            makeAPiCall();

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<MyResultResponse> call = apiService.getMyResult(sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC),
                sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<MyResultResponse>() {
            @Override
            public void onResponse(Call<MyResultResponse> call, Response<MyResultResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if (response.body().getStatus()) {

                        //build adapter

                        if(!response.body().getResponse().isEmpty()){

                            ArrayList<ResultModel> dataSet = new ArrayList<>();

                            for (com.testkart.exam.edu.myresult.Response r :
                                    response.body().getResponse()) {

                                ResultModel rm = new ResultModel();

                                rm.setDate(r.getResult().getStartTime());
                                rm.setExamName(r.getExam().getName());
                                rm.setExamResult((r.getResult().getResult() != null)? r.getResult().getResult() : "Fail");
                                rm.setPercentage(r.getResult().getPercent());
                                rm.setScore(r.getResult().getObtainedMarks()+"/"+r.getResult().getTotalMarks());
                                rm.setResultDeclear(r.getExam().getDeclareResult());
                                rm.setCertificate((sessionManager.getUserDetails().get(SessionManager.KEY_CERTIFICATE) != null)?Boolean.parseBoolean(sessionManager.getUserDetails().get(SessionManager.KEY_CERTIFICATE)): false);
                                rm.setResultId(r.getResult().getId());
                                rm.setExamId(r.getExam().getId());

                                dataSet.add(rm);
                            }


                            MyResultAdapter adapter = new MyResultAdapter(context, dataSet);

                            viewMyResult.setAdapter(adapter);

                        }else{


                            //show dialog ... leader board is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("My Result");
                            builder.setMessage("My result is empty");
                            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    getMyResult();
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


                    } else {

                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MyResultResponse> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });

    }



    public static  final String KEY_RESULT_URL = "res_url";
    public static  final String KEY_RESULT_ID = "res_id";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }




    private void downloadCertificate(String url) {

        if (ApiClient.isNetworkAvailable(context)) {

            //show progress dialog
            progressDialog.show();

            dwnFile(url);

        } else {

            Toast.makeText(context, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }
    }



    ProgressUpdateDialog pdu;
    String TAG = "PDF Download";
    private void dwnFile(String url) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.downloadFileWithDynamicUrlSync(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");

                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();

                            pdu = new ProgressUpdateDialog();

                            pdu.show(getSupportFragmentManager(), "PUD");

                           // pdu.updateProgress(0, "0 K", "Download Certificate" );
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            //boolean writtenToDisk = writeResponseBodyToDisk( response.body());

                            try {

                                downloadFile(response.body());

                            } catch (IOException e) {

                                e.printStackTrace();
                            }

                            // Log.d(TAG, "file download was a success? " + writtenToDisk);
                            return null;
                        }


                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            pdu.dismiss();

                            viewPDF();

                        }
                    }.execute();
                }
                else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");

                progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }


    private int totalFileSize;
    String fileSIze = "";
    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file.pdf");

        Log.v("PDF FILE PATH", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());



        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            final Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        fileSIze = "File Size: " +download.getCurrentFileSize()+ " K";

                        pdu.updateProgress(download.getProgress(), "File Size: " +totalFileSize+ " K" , "Download Certificate");
                    }
                });

                timeCount++;
            }

            output.write(data, 0, count);
        }
        onDownloadComplete(fileSIze);
        output.flush();
        output.close();
        bis.close();

    }

    private void onDownloadComplete(final String s){

        Download download = new Download();
        download.setProgress(100);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                pdu.updateProgress(100, s, "Download Certificate" );
            }
        });


    }

    public void viewPDF()
    {
        File pdfFile = new File("/storage/emulated/0/Download/" + "file.pdf");  // -> filename = maven.pdf
       // Uri path = Uri.fromFile(pdfFile);

        Uri path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", pdfFile);

        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){

            e.printStackTrace();

            Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "Future Studio Icon.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void onViewResult(ResultModel result) {

       /*String URL = "http://testkart.com/crm/Results/view?&id="*//*+result.getResultId()+"&public_key="+sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC)
                +"&private_key="+sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE)*//*;
*/

      //  String URL = "http://www.testkart.com/crm/Results/viewm/"+result.getResultId()+"/"+sessionManager.getUserDetails().get(SessionManager.KEY_STUDENT_ID);

        //Toast.makeText(context, "Under maintenance for 1 hr", Toast.LENGTH_SHORT).show();


        Intent resultIntent = new Intent(this, ViewResultActivity.class);
        resultIntent.putExtra(KEY_RESULT_ID, result.getResultId());
        //resultIntent.putExtra(KEY_RESULT_PRINT, false);
        startActivity(resultIntent);

    }

    @Override
    public void onPrintCertificate(ResultModel result) {

        String URL = "http://testkart.com/envato/demos/edu-com.testkart.exam/crm/Results/certificate/1.pdf?id="+result.getResultId()+"&public_key="+sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC)
                +"&private_key=5494"+sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE);



        Log.v("Certificate Url ", URL);

        //download certificate
        downloadCertificate(URL);

        /*Intent resultIntent = new Intent(this, MyResultActivity.class);
        resultIntent.putExtra(KEY_RESULT_URL, URL);

        startActivity(resultIntent);*/
    }



    public static final String KEY_RESULT_PRINT = "result_print";
    @Override
    public void onPrintResult(ResultModel result) {

        /*String URL = "http://testkart.com/crm/Results/printresult?id=5494&public_key="*//*+result.getResultId()+"&public_key="+sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC)
                +"&private_key="+sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE)*//*;*/

        String URL = "http://www.testkart.com/crm/Results/viewm/"+result.getResultId()+"/"+sessionManager.getUserDetails().get(SessionManager.KEY_STUDENT_ID);



        Intent resultIntent = new Intent(this, MyResultActivity.class);
        resultIntent.putExtra(KEY_RESULT_URL, URL);
        resultIntent.putExtra(KEY_RESULT_PRINT, true);

        startActivity(resultIntent);
    }
}
