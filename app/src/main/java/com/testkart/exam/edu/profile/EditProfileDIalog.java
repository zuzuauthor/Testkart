package com.testkart.exam.edu.profile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.register.ResendModel;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by testkart on 22/5/17.
 */

public class EditProfileDIalog extends DialogFragment implements Validator.ValidationListener{

    @NotEmpty
    private EditText inputEnroll, inputPhone, inputGuardianPhone;

    private ProgressDialog progressDialog;

    private Validator validator;

    private SessionManager session;

    private OnProfileEditListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (OnProfileEditListener)context;

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_profile, null);

        initViews(rootView);

        builder.setView(rootView);
        builder.setTitle("Edit Profile");

        builder.setPositiveButton("Update", null/*new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }*/);


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //hide keyboard
                InputMethodManager im = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(inputPhone.getWindowToken(), 0);

                //dismiss
                dismiss();

            }
        });


        final AlertDialog alert = builder.create();
        alert.setCancelable(false);



        alert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        //check validation
                        validator.validate();
                    }
                });
            }
        });


        return alert;
    }


    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<ResendModel> call = apiService.editProfile(
                inputEnroll.getText().toString().toString(),
                inputPhone.getText().toString().trim(),
                inputGuardianPhone.getText().toString().trim(),
                session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));


        call.enqueue(new Callback<ResendModel>() {
            @Override
            public void onResponse(Call<ResendModel> call, Response<ResendModel> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body().getStatus()){


                        //dismiss dialog
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        if(mListener != null){

                            mListener.onProfileEdit();

                        }

                        dismiss();

                    }else{

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(getContext(), "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResendModel> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });

    }

    private void initViews(View rootView) {

        session = new SessionManager(getContext());

        validator = new Validator(this);
        validator.setValidationListener(this);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Edit Profile");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        inputEnroll = (EditText)rootView.findViewById(R.id.inputEnroll);
        inputPhone = (EditText)rootView.findViewById(R.id.inputPhone);
        inputGuardianPhone = (EditText)rootView.findViewById(R.id.inputGuardianPhone);

        inputEnroll.setText((session.getUserDetails().get(SessionManager.KEY_STUDENT_ENROLL) != null) ? session.getUserDetails().get(SessionManager.KEY_STUDENT_ENROLL) : "");
        inputPhone.setText((session.getUserDetails().get(SessionManager.KEY_PHONE_NUMBER) != null) ? session.getUserDetails().get(SessionManager.KEY_PHONE_NUMBER) : "");
        inputGuardianPhone.setText((session.getUserDetails().get(SessionManager.KEY_ALTERNATE_NO) != null) ? session.getUserDetails().get(SessionManager.KEY_ALTERNATE_NO) : "");

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
