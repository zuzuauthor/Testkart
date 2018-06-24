package com.testkart.exam.helper;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by elfemo on 29/7/17.
 */

public class DialogHelper {

    public static void showInfoDialog(Context context, String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //do nothing
            }
        });

        builder.create().show();

    }

    public static int getFileTypeImage(String fileType){

        return 0;
    }



    public static void openFile(Context context, File url, String fileType) throws IOException {
        // Create URI
        File file=url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if(url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/zip");

        } else if(url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }


        try{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

                viewPDF(context, uri, fileType, url);

            }else{

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        }catch (Exception e){

           // Toast.makeText(context, "No application found to open this "+fileType+" file. Please download compatible application and then retry.", Toast.LENGTH_SHORT).show();

            String msg = "No application found to open this "+fileType+" file. Please download compatible application and then retry.";

            DialogHelper.showInfoDialog(context, "Application Not Found", msg );

            e.printStackTrace();

        }

    }


    public static void viewPDF(Context context, Uri path1, String fileType, File url)
    {
       // File pdfFile = new File(Environment.getExternalStorageDirectory().toString() + "/file."+fileType);  // -> filename = maven.pdf
         //Uri path = Uri.fromFile(pdfFile);

       /* File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File pdfFile = new File(downloads, "file."+fileType);*/

       Log.v("Before Path m", url.getAbsolutePath());

        //Uri path = FileProvider.getUriForFile(context, "com.mkn.testkart.com.testkart.exam.provider", url);

        Uri photoURI = FileProvider.getUriForFile(context,
                "com.testkart.exam.provider",
                url);

        Uri path = Uri.parse("content://com.testkart.exam.provider/"+"file."+fileType);

        Log.v("File Path m", path.getPath());

        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(photoURI, "application/"+fileType);
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try{
            context.startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){

            e.printStackTrace();

            Toast.makeText(context, "No Application available to view this file", Toast.LENGTH_SHORT).show();
        }
    }


    public static void openPlayStoreToRate(Context context){

        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

}
