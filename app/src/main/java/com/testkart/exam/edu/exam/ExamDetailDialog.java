package com.testkart.exam.edu.exam;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.testkart.exam.R;
import com.testkart.exam.edu.exam.examlist.examdetails.Exam;
import com.testkart.exam.edu.exam.examlist.examdetails.Subject;
import com.testkart.exam.edu.helper.MknUtils;
import com.testkart.exam.edu.helper.SessionManager;

import java.util.ArrayList;

/**
 * Created by testkart on 4/5/17.
 */

public class ExamDetailDialog extends DialogFragment {

    public static final String KEY_EXAM_DETAILS = "exam_details";
    public static final String KEY_SUBJECT_DETAILS = "subject_details";
    public static final String KEY_ONLY_VIEW = "only_view";

    private SessionManager session;

    private TextView examName,viewAmount, viewPP, viewExamDuration, viewNegativeMarking, viewExamType, viewExamStart, viewExamEnd, viewTotalMarks;
    private TableLayout table;
    private LinearLayout containerAmount;


    private OnExamStartListener mListener;
    public interface OnExamStartListener{

        void onShowInstruction(Exam exam);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (OnExamStartListener)context;

        }catch (Exception e){

            e.printStackTrace();
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //, R.style.AppTheme_Fullscreen
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_exam_details, null);

        Bundle args = getArguments();

        initViews(rootView, args);

        builder.setView(rootView);

        if(args.getBoolean(KEY_ONLY_VIEW)){
            //start

            builder.setPositiveButton("Take Exam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(mListener != null){

                        mListener.onShowInstruction((Exam) getArguments().getSerializable(KEY_EXAM_DETAILS));

                    }

                }
            });

        }else{

            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dismiss();

                }
            });

        }


        return builder.create();
    }

    private void initViews(View rootView, Bundle args) {

        session = new SessionManager(getContext());

        examName = (TextView)rootView.findViewById(R.id.viewExamName);
        viewPP  = (TextView)rootView.findViewById(R.id.viewPP);
        viewExamDuration  = (TextView)rootView.findViewById(R.id.viewExamDuration);
        viewNegativeMarking = (TextView)rootView.findViewById(R.id.viewNegativeMarking);

        viewExamType = (TextView)rootView.findViewById(R.id.viewExamType);
        viewExamStart = (TextView)rootView.findViewById(R.id.viewExamStart);
        viewExamEnd = (TextView)rootView.findViewById(R.id.viewExamEnd);
        viewTotalMarks = (TextView)rootView.findViewById(R.id.viewTotalMarks);
        viewAmount = (TextView)rootView.findViewById(R.id.viewAmount);
        containerAmount = (LinearLayout)rootView.findViewById(R.id.containerAmount);
        containerAmount.setVisibility(View.GONE);
        table = (TableLayout)rootView.findViewById(R.id.table);

        if(args != null){

            Exam exam = (Exam) args.getSerializable(KEY_EXAM_DETAILS);
            ArrayList<Subject> subjectList = (ArrayList<Subject>) args.getSerializable(KEY_SUBJECT_DETAILS);

            examName.setText(exam.getName());
            viewPP.setText(exam.getPassingPercent()+"%");
            viewExamDuration.setText(exam.getDuration());
            viewNegativeMarking.setText(exam.getNegativeMarking());
            viewExamType.setText(exam.getType());

            String dateF = exam.getStartDate().split(" ")[0];
            String dateE = exam.getEndDate().split(" ")[0];

            if(dateF != null && dateE != null){

                String startDate = MknUtils.getFormatDate(dateF, "yyyy-MM-dd", "dd-MM-yyyy");
                String endDate = MknUtils.getFormatDate(dateE, "yyyy-MM-dd", "dd-MM-yyyy");

                viewExamStart.setText((startDate != null)? startDate:"");
                viewExamEnd.setText((endDate != null)? endDate:"");

            }

            //viewExamEnd.setText((exam.getEndDate() != null)? exam.getEndDate().split(" ")[0] : "");
            viewTotalMarks.setText(exam.getTotalMarks());

            if(exam.getAmount() != null){

                viewAmount.setText(MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+exam.getAmount());
                containerAmount.setVisibility(View.VISIBLE);
            }


            //populate table


            int count = 1;
            int tqc = 0;
            for (Subject s:
                 subjectList) {

                TableRow row= new TableRow(getContext());
                row.setId(100+count);
                row.setOrientation(LinearLayout.HORIZONTAL);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(1,1,1,1);
                row.setLayoutParams(lp);

                //add subject
                TextView tvSubject = new TextView(getContext());
                TextView qCount = new TextView(getContext());

                tvSubject.setId(200+count);
                qCount.setId(300+count);

                tvSubject.setText(s.getSubject());
                qCount.setText(s.getTotalQuestion()+"");

                tqc += s.getTotalQuestion();

                TableRow.LayoutParams lpData = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                lpData.weight = 1;
                lpData.leftMargin = 25;

                tvSubject.setPadding(8,8,8,8);
                qCount.setPadding(8,8,8,8);

                tvSubject.setLayoutParams(lpData);
                qCount.setLayoutParams(lpData);


                row.addView(tvSubject, 0);
                row.addView(qCount, 1);

                table.addView(row);

                count++;

            }


            //add total question count

            TableRow lastRow= new TableRow(getContext());
            lastRow.setId(900+count);
            lastRow.setOrientation(LinearLayout.HORIZONTAL);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(1,1,1,1);
            lastRow.setLayoutParams(lp);

            TableRow.LayoutParams lpData = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpData.weight = 1;
            lpData.leftMargin = 25;

            TextView totalQuestionText = new TextView(getContext());
            totalQuestionText.setId(550+count);
            totalQuestionText.setText("Total");
            totalQuestionText.setTextColor(Color.parseColor("#191919"));
            totalQuestionText.setPadding(8,8,8,8);
            totalQuestionText.setTypeface(Typeface.DEFAULT_BOLD);


            TextView totalQuestionCount = new TextView(getContext());
            totalQuestionCount.setId(650+count);
            totalQuestionCount.setText(tqc+"");
            totalQuestionCount.setTextColor(Color.parseColor("#191919"));
            totalQuestionCount.setPadding(8,8,8,8);
            totalQuestionCount.setTypeface(Typeface.DEFAULT_BOLD);

            totalQuestionText.setLayoutParams(lpData);
            totalQuestionCount.setLayoutParams(lpData);

            lastRow.addView(totalQuestionText, 0);
            lastRow.addView(totalQuestionCount, 1);

            table.addView(lastRow);

        }

    }
}
