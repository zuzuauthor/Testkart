package com.testkart.exam.edu.login;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.testkart.exam.R;
import com.testkart.exam.edu.helper.MknUtils;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.register.ResendModel;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elfemo on 7/3/18.
 */

public class RequestNewLoginDeviceDialog extends DialogFragment implements Validator.ValidationListener{

    public static final String KEY_DEVICE_INFO = "device_info";
    private String studentId = "0";

    @NotEmpty
    private EditText inputProblem;
    private TextView viewTitleMessage;

    private ProgressDialog progressDialog;
    private Validator validator;

    private OnMultipleLoginDetectedListener mListener;

    public interface OnMultipleLoginDetectedListener{

        void onReportSuccess();
        void onReportFail();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (OnMultipleLoginDetectedListener) context;

        }catch(Exception e){

            e.printStackTrace();

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_request_new_login, null);
        initialization(rootView);

        builder.setView(rootView);

        builder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //make api call

                    //check validation
                    validator.validate();

            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        return builder.create();
    }

    private void initialization(View rootView) {

        Bundle args = getArguments();

        String manufacture = args.getString(MknUtils.KEY_PHONE_MANUFACTURER);
        String model = args.getString(MknUtils.KEY_PHONE_MODEL);
        String imei = args.getString(MknUtils.KEY_PHONE_IMEI);
        studentId = args.getString("STUDENT_ID");

        String titleMesage = "You cannot login into multiple devices. Your previous device is "+manufacture+" "+model+".";

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");

        inputProblem = (EditText) rootView.findViewById(R.id.inputProblem);
        viewTitleMessage = (TextView) rootView.findViewById(R.id.viewTitleMessage);
        viewTitleMessage.setText(titleMesage);

        validator = new Validator(this);
        validator.setValidationListener(this);


    }


    @Override
    public void onValidationSucceeded() {

        if (ApiClient.isNetworkAvailable(getContext())) {

            //show progress dialog
            progressDialog.show();

            makeAPiCall();

        } else {

            Toast.makeText(getContext(), Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }

    }


    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        //get system info
        HashMap<String, String> deviceInfo = MknUtils.getDeviceInfo(getContext());


        //show progress dialog
        progressDialog.show();

        Call<ResendModel> call = apiService.sendNewDeviceLoginRequest(
                studentId,
                inputProblem.getText().toString(),
                "Android",
                deviceInfo.get(MknUtils.KEY_PHONE_MANUFACTURER),
                deviceInfo.get(MknUtils.KEY_PHONE_MODEL),
                deviceInfo.get(MknUtils.KEY_PHONE_IMEI));


        call.enqueue(new Callback<ResendModel>() {
            @Override
            public void onResponse(Call<ResendModel> call, Response<ResendModel> response) {

                progressDialog.dismiss();

                final int code = response.code();

                if(code == 200){

                    if(response.body().getStatus()){


                        if(mListener != null){

                            mListener.onReportSuccess();
                        }



                       /* getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                //dismiss dialog
                                Toast.makeText(getContext(), "Your query submitted successfully ", Toast.LENGTH_SHORT).show();

                                dismiss();
                            }
                        });*/


                    }else{

                     //

                        if(mListener != null){

                            mListener.onReportFail();
                        }



                        /*getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                //dismiss dialog
                                Toast.makeText(getContext(), "Error while submitting query", Toast.LENGTH_SHORT).show();
                            }
                        });*/

                    }

                }else{

                    /*getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            //dismiss dialog
                            Toast.makeText(getContext(), "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                        }
                    });*/

                 //   Toast.makeText(getContext(), "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResendModel> call, Throwable t) {

                progressDialog.dismiss();

                t.printStackTrace();

            }
        });

    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }

    }

}
