package com.testkart.exam.testkart.contact_us;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mobsandgeeks.saripaar.Validator;

/**
 * Created by elfemo on 8/8/17.
 */

public class ContactUsNativeActivity extends AppCompatActivity implements Validator.ValidationListener {

    private final int MY_PERMISSIONS_REQUEST_MAKE_CALL = 9999;
    private int REQUEST_CODE_CHOOSE = 111;
    private Context context  = this;
    private SessionManager session;
    private ProgressDialog progressDialog;

    private Spinner contactQuery;

    @NotEmpty
    private EditText input_name;

    @NotEmpty
    @Email
    private EditText input_email;

    @NotEmpty
    @Pattern(regex = "[789][0-9]{9}", message = "Please provide a valid 10 digit mobile number.")
    private EditText input_mobile;

    @NotEmpty
    private EditText messageBody;

    private TextView attachment;

    private ImageView send, call;


    private ScrollView scrollContainer;

    // Initializing a String Array
    String[] queries = new String[]{
            "Select Your Query...",
            "Registration Issue",
            "Login Issue",
            "Account Activation",
            "Payment Issue",
            "Exam Activation",
            "Magazine Activation",
            "Result Issue",
            "Multiple login issue",
            "Others"
    };


    private Validator validator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_contact_us);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact us");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        //initialization
        initViews();

    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private void initViews() {

        scrollContainer = (ScrollView)findViewById(R.id.scrollContainer);

        session = new SessionManager(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");

        input_name = (EditText)findViewById(R.id.input_name);
        input_email = (EditText)findViewById(R.id.input_email);
        input_mobile = (EditText)findViewById(R.id.input_mobile);
        messageBody = (EditText)findViewById(R.id.messageBody);

        attachment = (TextView)findViewById(R.id.attachment);
        attachment.setTag("0");
        attachmentListener();

        contactQuery = (Spinner)findViewById(R.id.contactQuery);
        initSpinnerAdapter();
        contactQueryListener();

        send = (ImageView)findViewById(R.id.send);
        call = (ImageView)findViewById(R.id.call);
        sendListener();
        callListener();

        resetViews();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                scrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
            }
        };
        scrollContainer.post(runnable);

    }

    private void attachmentListener() {

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attachment.setTag("0");
                attachment.setText("Add Attachment");

                //gallery
                Matisse.from(ContactUsNativeActivity.this)
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                        .countable(true)
                        .maxSelectable(1)
                        //.gridExpectedSize(getResources().getDimensionPixelSize(16))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });
    }


    List<Uri> mSelected = new ArrayList<>();
    String mediaPath1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            mSelected = Matisse.obtainResult(data);

            Log.v("Matisse", "mSelected: " + mSelected);

            File file = new File(mSelected.get(0).getPath());

            attachment.setTag("1");
            attachment.setText(file.getName());



            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(mSelected.get(0), filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPath1 = cursor.getString(columnIndex);
            attachment.setText(mediaPath1);
            // Set the Image in ImageView for Previewing the Media
           // imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
            cursor.close();

           //Uri uri = mSelected.get(0);

            //File attachFile = new File(new URI(uri.getPath()));

        }
    }


    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String filePath) throws URISyntaxException {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(filePath);//FileUtils.getFile(this, fileUri);

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        // MultipartBody.Part is used to send also the actual file name
        return fileToUpload;

    }


    private void sendListener() {

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validator.validate();
            }
        });

    }

    private void callListener() {

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermissionAndMakeCall();

            }
        });

    }

    private void checkPermissionAndMakeCall() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            //check permission for 6.0
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE);

            if(permissionCheck == PackageManager.PERMISSION_GRANTED){

                makeCall("tel:08451004448");

            }else{

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_MAKE_CALL);
            }

        }else{

            makeCall("tel:08451004448"); //
        }

    }

    private void makeCall(String phoneNo) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(phoneNo));

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);

    }

    private void contactQueryListener() {

        contactQuery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }


    @Override
    public void onValidationSucceeded() {

        //check network connection
        if(ApiClient.isNetworkAvailable(context)){

            //check if query subject selected
            if(contactQuery.getSelectedItemPosition() > 0){

                if(input_mobile.getText().toString().trim().length() == 10){

                    try {
                        sendQuery();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                }else{

                    Toast.makeText(context, "Please enter 10 digits valid mobile no.", Toast.LENGTH_SHORT).show();
                }



            }else{

                Toast.makeText(context, "Please Select Your Query.", Toast.LENGTH_SHORT).show();
            }


        }else{

            Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

    }

    private void sendQuery() throws URISyntaxException {

        // create upload service client
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        progressDialog.show();

        // create a map of data to pass along
        RequestBody name = createPartFromString(input_name.getText().toString());
        RequestBody email = createPartFromString(input_email.getText().toString().trim());
        RequestBody mobile = createPartFromString(input_mobile.getText().toString());
        RequestBody subject = createPartFromString((String) contactQuery.getSelectedItem());
        RequestBody bodyContent = createPartFromString(messageBody.getText().toString());

       // RequestBody filename = createPartFromString("mkn");

        HashMap<String, RequestBody> map = new HashMap<>();
       // map.put("file", filename);
        map.put("name", name);
        map.put("email", email);
        map.put("mobile", mobile);
        map.put("subject", subject);
        map.put("body", bodyContent);


        // finally, execute the request
        Call<String> call;
        if(mSelected.size() > 0){

            // Map is used to multipart the file using okhttp3.RequestBody
            File file = new File(mediaPath1
            );

            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

            call = apiService.uploadDataWithAttachmentStr(map, fileToUpload);

        }else{

            call = apiService.uploadDataWithOutAttachmentStr(map);
        }


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                progressDialog.dismiss();

                Toast.makeText(context, "Your query has been submitted successfully.", Toast.LENGTH_SHORT).show();

                //reset all vals to default
                resetViews();

                Log.v("Response_Upload", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                progressDialog.dismiss();

                t.printStackTrace();
            }
        });


        /*call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, POResponse<ServerResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){


                        if(response.body().getSuccess()){

                            Log.v("Contact us", "data send succesfully");
                            Toast.makeText(ContactUsNativeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }else{

                            Toast.makeText(ContactUsNativeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{

                        Toast.makeText(ContactUsNativeActivity.this, "POResponse Body null", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(ContactUsNativeActivity.this, "RESPONSE ERROR CODE: "+code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progressDialog.dismiss();

                Log.v("Contact us", "data send failed");

                t.printStackTrace();

            }
        });*/

    }

    private void resetViews() {

        if(session.isLoggedIn()){

            input_name.setText(session.getUserDetails().get(SessionManager.KEY_NAME));
            input_email.setText(session.getUserDetails().get(SessionManager.KEY_EMAIL));
            input_mobile.setText(session.getUserDetails().get(SessionManager.KEY_PHONE_NUMBER));

        }else{

            input_name.setText("");
            input_email.setText("");
            input_mobile.setText("");

        }

        messageBody.setText("");

        initSpinnerAdapter();

        attachment.setTag("0");
        attachment.setText("Add Attachment");

        mSelected.clear();
        mediaPath1 = "";

    }

    private RequestBody createPartFromString(String s) {
        return RequestBody.create(MediaType.parse("text/plain"), s);
    }

    /*@Override
    public void onValidationFailed(List<ValidationError> errors) {




    }*/


    private void initSpinnerAdapter() {

        final List<String> queryList = new ArrayList<>(Arrays.asList(queries));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,queryList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactQuery.setAdapter(spinnerArrayAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            closeKeyboard();

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_MAKE_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "Now we are ready to make calls.", Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
