package com.testkart.exam.edu.exam.examlist;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.exam.examlist.examdetails.ExamDetailsResponse;
import com.testkart.exam.edu.helper.MknUtils;

import java.util.ArrayList;

/**
 * Created by testkart on 30/5/17.
 */

public class ExamInstructionDialog extends DialogFragment {

    public static final String KEY_INSTRUCTIONS = "instruction";
    public static final String KEY_IS_PAID = "is_paid";
    public static final String KEY_LANGUAGE = "language";

    com.testkart.exam.edu.exam.examlist.examdetails.Exam instructionText;
    boolean isPaid;
    private WebView instructionView;
    private LinearLayout linearLayout;
    private CheckBox checkBox;
    private Button startExam;
    private OnExamFinallyStartListener mListener;
    private Spinner languageSpinner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {

            mListener = (OnExamFinallyStartListener) context;

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Fullscreen);

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_exam_instruction, null);

        initViews(rootView);

        builder.setView(rootView);
        builder.setCancelable(false);

        /*builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dismiss();

            }
        });*/

        return builder.create();
    }


    private ArrayList langArray;
    private ExamDetailsResponse examDetails;
    private void initViews(View rootView) {

        instructionText = (com.testkart.exam.edu.exam.examlist.examdetails.Exam) getArguments().getSerializable(KEY_INSTRUCTIONS);
        isPaid = getArguments().getBoolean(KEY_IS_PAID);
        langArray = getArguments().getStringArrayList(KEY_LANGUAGE);

        if(langArray.size()==0){

            langArray.add("English");

        }


        languageSpinner = (Spinner)rootView.findViewById(R.id.languageSpinner);
        languageSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, langArray));


        linearLayout = (LinearLayout) rootView.findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.VISIBLE);

        startExam = (Button) rootView.findViewById(R.id.startExam);
        //startExam.setEnabled(false);
        checkBox = (CheckBox) rootView.findViewById(R.id.checkBox);
        checkBoxListener();

        if (isPaid) {

            startExam.setBackgroundColor(Color.parseColor("#4dac4c"));

        } else {

            startExam.setBackgroundColor(Color.parseColor("#fc6c4d"));
        }

        startExamListener();

        instructionView = (WebView) rootView.findViewById(R.id.instructionView);

        WebSettings webSettings = instructionView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);

        instructionView.setWebChromeClient(new WebChromeClient());

        String q = MknUtils.changedHeaderHtml(instructionText.getInstruction());

        instructionView.loadData(q, "text/html", "UTF-8");
    }

    private void checkBoxListener() {

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    //darker version

                    if (isPaid) {

                        startExam.setBackgroundColor(Color.parseColor("#028900"));

                    } else {

                        startExam.setBackgroundColor(Color.parseColor("#fb2e01"));
                    }

                }else{

                    //lighter version

                    if (isPaid) {

                        startExam.setBackgroundColor(Color.parseColor("#4dac4c"));

                    } else {

                        startExam.setBackgroundColor(Color.parseColor("#fc6c4d"));
                    }
                }

            }
        });
    }

    private void startExamListener() {

        startExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  if(isPaid){

                if (checkBox.isChecked()) {

                    if (mListener != null) {

                        //send back exam info

                        System.out.println("Select Language: "+(String)languageSpinner.getSelectedItem());

                        mListener.onExamFinallyStart(instructionText, (String)languageSpinner.getSelectedItem());

                        dismiss();
                    }

                } else {

                    Toast.makeText(getContext(), "Please check I am ready to begin", Toast.LENGTH_SHORT).show();
                }

                /*}else{

                    if(checkBox.isChecked()){

                        dismiss();

                    }else{

                        Toast.makeText(getContext(), "Please check I am ready to begin", Toast.LENGTH_SHORT).show();
                    }*/

                //}

            }
        });

    }

    public interface OnExamFinallyStartListener {

        void onExamFinallyStart(com.testkart.exam.edu.exam.examlist.examdetails.Exam exam, String languageText);
    }
}
