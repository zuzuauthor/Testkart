package com.testkart.exam.edu.exam;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.testkart.exam.R;
import com.testkart.exam.edu.exam.examlist.Exam;

/**
 * Created by testkart on 28/4/17.
 */

public class StartExamConfirmationDialog extends DialogFragment {

    public static final String KEY_EXAM_DETAILS = "exam_details";
    public static final String KEY_EXAM_EXPIRE = "exam_expire";

    private String examId;

    //private ImageView viewNoQuestions, viewExamDuration, viewTotalMarks, viewCorrectMarks, viewPeneltyMarks;

    private TextView examName, examExpiry, examType, startDate, endDate, paidExam, amount, attemptCont, expiry, attempt, attemptOrder;

    private OnexamSelectListener mListener;
    public interface OnexamSelectListener{

        void onExamSelect(String examId);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (OnexamSelectListener)context;

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_start_confermation_dialog, null);

        //initialize
        initialization(rootView);

        builder.setView(rootView);

        builder.setPositiveButton("Attempt Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //show feedback activity

                if(mListener != null){

                    mListener.onExamSelect(examId);

                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dismiss();
            }
        });


        AlertDialog d = builder.create();

        return d;
    }

    private void initialization(View rv) {

        Exam exam = (Exam) getArguments().getSerializable(KEY_EXAM_DETAILS);
        String ee = getArguments().getString(KEY_EXAM_EXPIRE);

        examName = (TextView)rv.findViewById(R.id.examName);
        examExpiry = (TextView)rv.findViewById(R.id.examExpiry);
        examType = (TextView)rv.findViewById(R.id.examType);
        startDate = (TextView)rv.findViewById(R.id.startDate);
        endDate = (TextView)rv.findViewById(R.id.endDate);
        paidExam = (TextView)rv.findViewById(R.id.paidExam);
        amount = (TextView)rv.findViewById(R.id.amount);
        attemptCont = (TextView)rv.findViewById(R.id.attemptCont);
        expiry = (TextView)rv.findViewById(R.id.expiry);
        attempt = (TextView)rv.findViewById(R.id.attempt);
        attemptOrder = (TextView)rv.findViewById(R.id.attemptOrder);

        if(exam != null){

            examId = exam.getId();

            examName.setText(exam.getName());
            examType.setText(exam.getType());
            startDate.setText(exam.getStartDate());
            endDate.setText(exam.getEndDate());
            paidExam.setText(exam.getPaidExam());
            amount.setText((String)exam.getAmount());
            attemptCont.setText(exam.getAttemptCount());
            expiry.setText((String)exam.getExpiry());
            attempt.setText(exam.getAttempt());
            attemptOrder.setText(exam.getAttemptOrder());

        }

        if(ee != null){

            examExpiry.setText(ee);
        }

    }

}