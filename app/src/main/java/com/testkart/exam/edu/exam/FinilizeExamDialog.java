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
import com.testkart.exam.edu.models.Consts;

import java.util.HashMap;

/**
 * Created by testkart on 28/4/17.
 */

public class FinilizeExamDialog extends DialogFragment {

    private TextView viewNotVisit, viewNotAnswered,viewAnswered, viewMarkReview, viewMarkReviewAnswered;


    private OnExamFinishListener mListener;
    public interface OnExamFinishListener{

        void onExamFinish();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (OnExamFinishListener)context;

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_finilize_exam, null);

        //initialize
        initialization(rootView);

        builder.setView(rootView);
        //builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //show feedback activity

                if(mListener != null){

                    mListener.onExamFinish();
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
        d.setCancelable(false);
        return d;
    }

    private void initialization(View rv) {

        HashMap<String, String> examSummary = (HashMap<String, String>) getArguments().getSerializable("EXAM_SUMMARY");

        viewNotVisit = (TextView)rv.findViewById(R.id.viewNotVisit);
        viewNotAnswered = (TextView)rv.findViewById(R.id.viewNotAnswered);
        viewAnswered = (TextView)rv.findViewById(R.id.viewAnswered);
        viewMarkReview = (TextView)rv.findViewById(R.id.viewMarkReview);
        viewMarkReviewAnswered = (TextView)rv.findViewById(R.id.viewMarkReviewAnswered);

        //not visited
        /*TextDrawable drawable1 = TextDrawable.builder()
                .buildRound(examSummary.get(Consts.KEY_NOT_VISIT), Color.parseColor("#999999"));
        viewNotVisit.setImageDrawable(drawable1);*/

        viewNotVisit.setText(examSummary.get(Consts.KEY_NOT_VISIT));

        //not Answered
       /* TextDrawable drawable2 = TextDrawable.builder()
                .buildRound(examSummary.get(Consts.KEY_NOTANSWERED), Color.parseColor("#ff0000"));
        viewNotAnswered.setImageDrawable(drawable2);*/

        viewNotAnswered.setText(examSummary.get(Consts.KEY_NOTANSWERED));


        //Answered
        /*TextDrawable drawable3 = TextDrawable.builder()
                .buildRound(examSummary.get(Consts.KEY_ANSWERED), Color.parseColor("#008000"));
        viewAnswered.setImageDrawable(drawable3);*/

        viewAnswered.setText(examSummary.get(Consts.KEY_ANSWERED));


        //Mark review
        /*TextDrawable drawable4 = TextDrawable.builder()
                .buildRound(examSummary.get(Consts.KEY_REVIEW), Color.parseColor("#800080"));
        viewMarkReview.setImageDrawable(drawable4);*/

        viewMarkReview.setText(examSummary.get(Consts.KEY_REVIEW));


        //mark review and answered
        /*TextDrawable drawable5 = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.parseColor("#008000"))
                .useFont(Typeface.DEFAULT)
                .endConfig()
                .buildRound(examSummary.get(Consts.KEY_REVIEW_ANSWERED), Color.parseColor("#800080"));
        viewMarkReviewAnswered.setImageDrawable(drawable5);*/

        viewMarkReviewAnswered.setText(examSummary.get(Consts.KEY_REVIEW_ANSWERED));

    }
}
