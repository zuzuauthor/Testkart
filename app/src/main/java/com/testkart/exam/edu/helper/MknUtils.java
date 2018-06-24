package com.testkart.exam.edu.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by testkart on 5/5/17.
 */

public class MknUtils {


    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }


    public static float getScreenWidth(Activity context){


        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        float width = (float) displayMetrics.widthPixels;

        return width;

    }


    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }


    public static String changedHeaderHtml3(String content) {

        if(!content.contains("http://www.testkart.com/magazine/wp-content/")){

            content = content.replace("/magazine/wp-content/", "http://www.testkart.com/magazine/wp-content/");

        }

        //"<meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" />\n" +

        String viewContent = "<html>\n" +
                "            \n" +
                "                <head>\n" +
                "                \n" +

                "                \n" +
                "                </head>\n" +
                "                \n" +
                "                <body>\n" +
                "                \n" +
                "                    \n" +
                "                \n" +
                                 content+
                "                </body>\n" +
                "            \n" +
                "            </html>";

        return viewContent;

    }

    public static String changedHeaderHtml(String htmlText) {

        String head = "<head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";

        String closedTag = "</body></html>";
        String changeFontHtml = head + htmlText + closedTag;
        return changeFontHtml;
    }


    public static String changeHeaderHtml2(String htmlText){

        //<meta name="viewport" content="user-scalable=no"/>

        String head = "<head><meta name=\"viewport\" content=\"user-scalable=no\"/></head>";
        String closedTag = "</body></html>";
        String changeFontHtml = head + htmlText + closedTag;
        return changeFontHtml;
    }

    public static int getScale(Context context, double PIC_WIDTH){
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(PIC_WIDTH);
        val = val * 100d;
        return val.intValue();
    }


    public static String getDateTime(long timestamp){

        String S = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);

        return S;
    }


    public static String getFormatDate(String date, String inputFormat, String outputFormat){

        try{


            Date d = new SimpleDateFormat(inputFormat, Locale.ENGLISH).parse(date);

            SimpleDateFormat dayFormat = new SimpleDateFormat(outputFormat);

            String ffd = dayFormat.format(d);

            return ffd;

        }catch (Exception e){

            e.printStackTrace();
        }

        return null;
    }


    //----------------------------------
    private  String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;


    public void  uplaodImage(String requestURL, String charset)
            throws IOException {
        this.charset = charset;

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
        httpConn.setRequestProperty("Test", "Bonjour");
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    /**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile, String publicKey, String privateKey)
            throws IOException {

        String fileName = uploadFile.getName();

        writer.append("--" + boundary).append(LINE_FEED);

        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     * @param name - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public List<String> finish() throws IOException {
        List<String> response = new ArrayList<String>();

        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }

        return response;
    }



    static HashMap<String, String> currencyCode = new HashMap<>();

    public static final String KEY_CURRENCY_AUD = "AUD";
    public static final String KEY_CURRENCY_BND = "BND";
    public static final String KEY_CURRENCY_KHR = "KHR";
    public static final String KEY_CURRENCY_CNY = "CNY";
    public static final String KEY_CURRENCY_HKD = "HKD";
    public static final String KEY_CURRENCY_INR = "INR";
    public static final String KEY_CURRENCY_IDR = "IDR";
    public static final String KEY_CURRENCY_JPY = "JPY";
    public static final String KEY_CURRENCY_KPW = "KPW";
    public static final String KEY_CURRENCY_KRW = "KRW";
    public static final String KEY_CURRENCY_LAK = "LAK";
    public static final String KEY_CURRENCY_MYR = "MYR";
    public static final String KEY_CURRENCY_NGN = "NGN";
    public static final String KEY_CURRENCY_PKR = "PKR";
    public static final String KEY_CURRENCY_PHP = "SGD";
    public static final String KEY_CURRENCY_LKR = "LKR";
    public static final String KEY_CURRENCY_TWD = "TWD";
    public static final String KEY_CURRENCY_THB = "THB";
    public static final String KEY_CURRENCY_GBP = "GBP";
    public static final String KEY_CURRENCY_USD = "USD";
    public static final String KEY_CURRENCY_VND = "VND";



    public static String getCurrencyCode(String ccd){

        currencyCode.put(KEY_CURRENCY_AUD, "\u0024");
        currencyCode.put(KEY_CURRENCY_BND, "\u0024");
        currencyCode.put(KEY_CURRENCY_KHR, "\u0024");
        currencyCode.put(KEY_CURRENCY_INR, "\u20B9");
        currencyCode.put(KEY_CURRENCY_HKD, "\u0024");
        currencyCode.put(KEY_CURRENCY_IDR, "\u0024");
        currencyCode.put(KEY_CURRENCY_JPY, "\u0024");
        currencyCode.put(KEY_CURRENCY_KPW, "\u0024");
        currencyCode.put(KEY_CURRENCY_KRW, "\u0024");
        currencyCode.put(KEY_CURRENCY_LAK, "\u0024");
        currencyCode.put(KEY_CURRENCY_MYR, "\u0024");
        currencyCode.put(KEY_CURRENCY_NGN, "\u0024");
        currencyCode.put(KEY_CURRENCY_PKR, "\u0024");
        currencyCode.put(KEY_CURRENCY_PHP, "\u0024");
        currencyCode.put(KEY_CURRENCY_LKR, "\u0024");
        currencyCode.put(KEY_CURRENCY_TWD, "\u0024");
        currencyCode.put(KEY_CURRENCY_THB, "\u0024");
        currencyCode.put(KEY_CURRENCY_GBP, "\u00A3");
        currencyCode.put(KEY_CURRENCY_USD, "\u0024");
        currencyCode.put(KEY_CURRENCY_VND, "\u0024");

        return currencyCode.get(ccd);

    }


    public interface Visitor{
        int doJob(Bundle args);
    }

    public void invalidTokenLogoutUser(Context context, final SessionManager session, final Visitor callback){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Logout");
        builder.setMessage("Token is expired! please logout and login again");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(session != null){

                    session.logoutUser(false);
                    callback.doJob(null);

                }

            }
        });


        builder.create().show();
    }



    public static final String KEY_PHONE_MODEL = "phone_model";
    public static final String KEY_PHONE_MANUFACTURER = "phone_manufacturer";
    public static final String KEY_PHONE_IMEI = "phone_imei";

    public static HashMap<String, String> getDeviceInfo(Context context){

        HashMap<String, String> deviceInfo = new HashMap<>();

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        String imei = (telephonyManager.getDeviceId() != null)? telephonyManager.getDeviceId() : "Not Available";
        String manufacturer = (Build.MANUFACTURER != null)? Build.MANUFACTURER : "Not Available";
        String model = (Build.MODEL != null)? Build.MODEL : "Not Available";

        //add manufacturer
        deviceInfo.put(KEY_PHONE_MANUFACTURER, manufacturer);

        //add model
        deviceInfo.put(KEY_PHONE_MODEL, model);

        //add IMEI no
        deviceInfo.put(KEY_PHONE_IMEI, imei);

        Log.d("Device Details", deviceInfo.toString());

        return deviceInfo;
    }

}
