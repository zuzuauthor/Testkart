package com.testkart.exam.testkart.study_material;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.Download;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.myresult.ProgressUpdateDialog;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.helper.DialogHelper;
import com.testkart.exam.packages.EmptyCartActivity;
import com.testkart.exam.testkart.notifications.NotificationActivity;
import com.thefinestartist.utils.content.Ctx;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elfemo on 8/8/17.
 */

public class StudyMaterialActivity extends AppCompatActivity {
    private static final String TAG = "Study Material";

    /*

    //check network
    //retry
    //show study material
    //download file
    //view file

     */

    private SessionManager sessionManager;
    private ListView viewStudyMaterialList;
    private ProgressDialog progressDialog;
    private Context context = this;


    private HashMap<String, String> fileTypes = new HashMap<>();

    private String KEY_TYPE_PDF = "pdf";
    private String KEY_TYPE_ZIP = "zip";
    private String KEY_TYPE_RAR = "rar";
    private String KEY_TYPE_DOC = "doc";
    private String KEY_TYPE_DOCX = "docx";
    private String KEY_TYPE_XLS = "xls";
    private String KEY_TYPE_XLSX = "xlsx";
    private String KEY_TYPE_PPT = "ppt";
    private String KEY_TYPE_PPTX = "pptx";
    private String KEY_TYPE_TXT = "txt";
    private String KEY_TYPE_RTF = "rtf";
    private String KEY_TYPE_JPG = "jpg";
    private String KEY_TYPE_PNG = "png";
    private String KEY_TYPE_BMP = "bmp";


    private void buildFileTypes() {

        fileTypes.put(KEY_TYPE_PDF, KEY_TYPE_PDF);
        fileTypes.put(KEY_TYPE_ZIP, KEY_TYPE_ZIP);
        fileTypes.put(KEY_TYPE_RAR, KEY_TYPE_RAR);
        fileTypes.put(KEY_TYPE_DOC, KEY_TYPE_DOC);
        fileTypes.put(KEY_TYPE_DOCX, KEY_TYPE_DOCX);
        fileTypes.put(KEY_TYPE_XLS, KEY_TYPE_XLS);
        fileTypes.put(KEY_TYPE_XLSX, KEY_TYPE_XLSX);
        fileTypes.put(KEY_TYPE_PPT, KEY_TYPE_PPT);
        fileTypes.put(KEY_TYPE_PPTX, KEY_TYPE_PPTX);
        fileTypes.put(KEY_TYPE_TXT, KEY_TYPE_TXT);
        fileTypes.put(KEY_TYPE_RTF, KEY_TYPE_RTF);
        fileTypes.put(KEY_TYPE_JPG, KEY_TYPE_JPG);
        fileTypes.put(KEY_TYPE_PNG, KEY_TYPE_PNG);
        fileTypes.put(KEY_TYPE_BMP, KEY_TYPE_BMP);
    }

    private String getFileType(String type){

        String i = fileTypes.get(type);

        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_study_material);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Study Material");
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
        //progressDialog.setTitle("Study Material");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        viewStudyMaterialList = (ListView) findViewById(R.id.viewStudyMaterialList);

        viewStudyMaterialListListener();

        getStudyMaterialData();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rep_menu, menu);

        return true;
    }



    String fileType = "pdf";
    String fileName = "file";
    private void viewStudyMaterialListListener() {

        viewStudyMaterialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                StudyMaterial sm = (StudyMaterial) viewStudyMaterialList.getItemAtPosition(i);

                fileName = sm.getFileName();

                fileType= sm.getFileType();
                new ImageDownloadWithProgressDialog().execute(sm.getFileUrl());


               // new DownloadFileFromURL().execute(sm.getFileUrl());

               // dwnFile(sm.getFileUrl());

            }
        });

    }

    private void getStudyMaterialData() {

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

        Call<StudyMaterialResponse> call = apiService.getStudyMaterial();

        call.enqueue(new Callback<StudyMaterialResponse>() {
            @Override
            public void onResponse(Call<StudyMaterialResponse> call, retrofit2.Response<StudyMaterialResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if (response.body().getStatus()) {

                        //build adapter

                        if(!response.body().getStudyMaterial().isEmpty()){

                            StudyMaterialAdapter adapter = new StudyMaterialAdapter(context, response.body().getStudyMaterial());
                            viewStudyMaterialList.setAdapter(adapter);

                        }else{


                            //show dialog ... leader board is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Study Material");
                            builder.setMessage("There is nothing to download.");
                            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    getStudyMaterialData();
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
            public void onFailure(Call<StudyMaterialResponse> call, Throwable t) {

                //hide progress dialog
                progressDialog.dismiss();

                Toast.makeText(context, Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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



    ProgressDialog progressdialog;
    public static final int Progress_Dialog_Progress = 0;
    URL url;
    URLConnection urlconnection ;
    int FileSize;
    InputStream inputstream;
    OutputStream outputstream;
    byte dataArray[] = new byte[1024];
    long totalSize = 0;



    public class ImageDownloadWithProgressDialog extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            showDialog(Progress_Dialog_Progress);
        }

        @Override
        protected String doInBackground(String... aurl) {

            int count;

            try {

                url = new URL(aurl[0]);
                urlconnection = url.openConnection();
                urlconnection.connect();

                FileSize = urlconnection.getContentLength();

                inputstream = new BufferedInputStream(url.openStream());

                //getFileType(studyMaterial.getFileType())

                outputstream = new FileOutputStream(Environment.getExternalStorageDirectory().toString() +"/"+fileName+"."+fileType);


                while ((count = inputstream.read(dataArray)) != -1) {

                    totalSize += count;

                    publishProgress(""+(int)((totalSize*100)/FileSize));

                    outputstream.write(dataArray, 0, count);
                }

                outputstream.flush();
                outputstream.close();
                inputstream.close();

            } catch (Exception e) {}
            return null;

        }
        protected void onProgressUpdate(String... progress) {

            progressdialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {

            dismissDialog(Progress_Dialog_Progress);

            File myFile = new File(Environment.getExternalStorageDirectory().toString() +"/"+fileName+"."+fileType);

            try {

                DialogHelper.openFile(context, myFile, fileType);

            } catch (IOException e) {

                e.printStackTrace();
            }

           // imageview.setImageDrawable(Drawable.createFromPath(GetPath));

            Toast.makeText(context, "File Downloaded", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Progress_Dialog_Progress:

                progressdialog = new ProgressDialog(context);
                progressdialog.setMessage("Please Wait...");
                progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressdialog.setCancelable(false);
                progressdialog.show();
                return progressdialog;

            default:

                return null;
        }
    }






















    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdu = new ProgressUpdateDialog();
            pdu.show(getSupportFragmentManager(), "PUD");
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();
                totalFileSize = lenghtOfFile;

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream("/sdcard/file.pdf");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pdu.updateProgress(Integer.parseInt(progress[0]), "File Size: " +totalFileSize+ " K" , "Study Material" );
            //pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            pdu.updateProgress(100, totalFileSize+" K", "Study Material" );
           // dismissDialog(progress_bar_type);

            pdu.dismiss();

            File myFile = new File(Environment.getExternalStorageDirectory().toString() + "/file.pdf");

            try {

                DialogHelper.openFile(context, myFile, fileType);

            } catch (IOException e) {

                e.printStackTrace();
            }

        }

    }








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

                           // pdu.updateProgress(0, "0 K", "Study Material");
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

                            File myFile = new File("/storage/emulated/0/Download/" + "file.pdf");

                            try {

                                DialogHelper.openFile(context, myFile, fileType);

                            } catch (IOException e) {

                                e.printStackTrace();
                            }

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



    ProgressUpdateDialog pdu;
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

                        pdu.updateProgress(download.getProgress(), "File Size: " +totalFileSize+ " K" , "Study Material" );
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

                pdu.updateProgress(100, s, "Study Material" );
            }
        });


    }

}
