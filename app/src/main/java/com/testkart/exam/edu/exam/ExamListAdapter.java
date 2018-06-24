package com.testkart.exam.edu.exam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.testkart.exam.R;
import com.testkart.exam.edu.exam.examlist.Exam;
import com.testkart.exam.edu.helper.MknUtils;
import com.testkart.exam.edu.helper.SessionManager;

import java.util.List;

/**
 * Created by testkart on 28/4/17.
 */

public class ExamListAdapter extends BaseAdapter {

    private Context context;
    private List<Exam> dataSet;
    private LayoutInflater inflater;
    private int examFilter = 0; // 1 - today's exam, 2 - purchased exam, 3 - Upcoming exam, 4 - Expired exam
    private SessionManager session;

    private boolean canTakeExam = false;

    private ExamListActionListener  mListener;

    public interface ExamListActionListener{

        void onTakeExam(Exam exam, boolean canTakeExam);
        void onViewDetails(Exam exam, boolean canTakeExam);
    }

    public ExamListAdapter(Context context, List<Exam> dataSet, int examFilter, boolean canTakeExam){

        this.canTakeExam = canTakeExam;

        this.mListener = (ExamListActionListener) context;

        this.context = context;
        this.dataSet = dataSet;
        this.inflater = LayoutInflater.from(context);
        this.examFilter = examFilter;
        this.session = new SessionManager(context);
    }


    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataSet.indexOf(dataSet.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.row_item_exam, parent, false);
            mHolder = new ViewHolder();

            mHolder.viewAmount = (TextView)convertView.findViewById(R.id.viewAmount);

            mHolder.viewExam = (TextView)convertView.findViewById(R.id.viewExam);
            mHolder.viewAttempted = (TextView)convertView.findViewById(R.id.viewAttempted);
            mHolder.viewDate = (TextView)convertView.findViewById(R.id.viewDate);
            mHolder.viewExpiry = (TextView)convertView.findViewById(R.id.viewExpiry);
            mHolder.endDate = (TextView)convertView.findViewById(R.id.endDate);


            mHolder.viewExamBtn = (TextView)convertView.findViewById(R.id.viewExamBtn);
            mHolder.takeExamBtn = (TextView)convertView.findViewById(R.id.takeExamBtn);

            mHolder.containerEndDate = (LinearLayout)convertView.findViewById(R.id.containerEndDate);
            mHolder.containerExpiry  = (LinearLayout)convertView.findViewById(R.id.containerExpiry);
            mHolder.containerAttempted  = (LinearLayout)convertView.findViewById(R.id.containerAttempted);
            mHolder.containerAmount  = (LinearLayout)convertView.findViewById(R.id.containerAmount);
            mHolder.containerAmount.setVisibility(View.GONE);

            convertView.setTag(mHolder);

        }else{


            mHolder = (ViewHolder) convertView.getTag();

        }


        final Exam exam = (Exam) getItem(position);


        if(exam != null){

            //hide and show relevent fields
            switch (examFilter){

                case 1:
                    //today's exam - show all fields

                    mHolder.endDate.setText("End Date");
                    mHolder.containerAttempted.setVisibility(View.VISIBLE);
                    mHolder.containerAmount.setVisibility(View.GONE);
                    mHolder.containerEndDate.setVisibility(View.VISIBLE);
                    mHolder.containerExpiry.setVisibility(View.VISIBLE);

                    String dateE = exam.getEndDate().split(" ")[0];
                    if(dateE != null){

                        String endDate = MknUtils.getFormatDate(dateE, "yyyy-MM-dd", "dd-MM-yyyy");
                        mHolder.viewDate.setText((endDate != null)? endDate:"");

                    }


                    //expiry
                    mHolder.containerExpiry.setVisibility(View.VISIBLE);

                    if(session.getUserDetails().get(SessionManager.KEY_EXAM_EXPIRY) != null
                            && session.getUserDetails().get(SessionManager.KEY_EXAM_EXPIRY).equals("1")){

                        mHolder.containerExpiry.setVisibility(View.VISIBLE);

                        mHolder.viewExpiry.setText((exam.getExpiry() != null)? (String)exam.getExpiry()+" days" : "Unlimited");

                    }else{

                        mHolder.containerExpiry.setVisibility(View.GONE);
                    }


                    break;

                case 2:

                    //purchased exam - show all fields
                    mHolder.endDate.setText("End Date");
                    mHolder.containerAttempted.setVisibility(View.VISIBLE);
                    mHolder.containerAmount.setVisibility(View.GONE);
                    mHolder.containerEndDate.setVisibility(View.VISIBLE);
                    mHolder.containerExpiry.setVisibility(View.VISIBLE);

                    String dateE1 = exam.getEndDate().split(" ")[0];
                    if(dateE1 != null){

                        String endDate = MknUtils.getFormatDate(dateE1, "yyyy-MM-dd", "dd-MM-yyyy");
                        mHolder.viewDate.setText((endDate != null)? endDate:"");

                    }


                    //exam expiry
                    mHolder.containerExpiry.setVisibility(View.VISIBLE);

                    if(session.getUserDetails().get(SessionManager.KEY_EXAM_EXPIRY) != null
                            && session.getUserDetails().get(SessionManager.KEY_EXAM_EXPIRY).equals("1")){

                        mHolder.containerExpiry.setVisibility(View.VISIBLE);

                        mHolder.viewExpiry.setText((exam.getExpiry() != null)? (String)exam.getExpiry()+" days" : "Unlimited");

                    }else{

                        mHolder.containerExpiry.setVisibility(View.GONE);
                    }


                    //attempt count
                    if(exam.getAttemptCount().equals("0")){

                        mHolder.viewAttempted.setText("Unlimited");

                        exam.setCanAttempt(true);

                    }else{

                        int attemptRemaning = (Integer.parseInt(exam.getAttemptOrder()) * Integer.parseInt(exam.getAttemptCount())) - Integer.parseInt(exam.getAttempt());

                        if(attemptRemaning > 0){

                            exam.setCanAttempt(true);
                        }else{

                            exam.setCanAttempt(false);
                        }

                        mHolder.viewAttempted.setText((exam.getAttempt() != null)? attemptRemaning+"" : "");
                    }



                    break;

                case 3:

                    //Upcoming exam - hide attempt remaining and change end date text to start date
                    mHolder.containerAttempted.setVisibility(View.GONE);
                    mHolder.containerAmount.setVisibility(View.GONE);
                    mHolder.containerEndDate.setVisibility(View.VISIBLE);
                    mHolder.containerExpiry.setVisibility(View.VISIBLE);
                    mHolder.endDate.setText("Start Date");
                    mHolder.takeExamBtn.setVisibility(View.GONE);

                    exam.setCanAttempt(true);

                    String dateS = exam.getStartDate().split(" ")[0];
                    if(dateS != null){

                        String startDate = MknUtils.getFormatDate(dateS, "yyyy-MM-dd", "dd-MM-yyyy");
                        mHolder.viewDate.setText((startDate != null)? startDate:"");

                    }



                    //exam expiry
                    mHolder.containerExpiry.setVisibility(View.VISIBLE);

                    if(session.getUserDetails().get(SessionManager.KEY_EXAM_EXPIRY) != null
                            && session.getUserDetails().get(SessionManager.KEY_EXAM_EXPIRY).equals("1")){

                        mHolder.containerExpiry.setVisibility(View.VISIBLE);

                        mHolder.viewExpiry.setText((exam.getExpiry() != null)? (String)exam.getExpiry()+" days" : "Unlimited");

                    }else{

                        mHolder.containerExpiry.setVisibility(View.GONE);
                    }


                    break;

                case 4:

                    //expired exam - end date and attempt remaining hide
                    mHolder.containerAttempted.setVisibility(View.GONE);
                    mHolder.containerAmount.setVisibility(View.GONE);
                    mHolder.containerEndDate.setVisibility(View.GONE);
                    mHolder.containerExpiry.setVisibility(View.VISIBLE);
                    mHolder.endDate.setText("End Date");
                    mHolder.takeExamBtn.setVisibility(View.GONE);

                    exam.setCanAttempt(true);

                    String dateE2 = exam.getEndDate().split(" ")[0];
                    if(dateE2 != null){

                        String endDate = MknUtils.getFormatDate(dateE2, "yyyy-MM-dd", "dd-MM-yyyy");
                        mHolder.viewDate.setText((endDate != null)? endDate:"");

                    }



                    if(exam.getFexpiry_date() == null){

                        mHolder.containerExpiry.setVisibility(View.GONE);

                    }else{

                        mHolder.containerExpiry.setVisibility(View.VISIBLE);

                        if(session.getUserDetails().get(SessionManager.KEY_EXAM_EXPIRY) != null
                                && session.getUserDetails().get(SessionManager.KEY_EXAM_EXPIRY).equals("1")){

                            mHolder.containerExpiry.setVisibility(View.VISIBLE);

                            mHolder.viewExpiry.setText((exam.getExpiry() != null)? (String)exam.getExpiry()/*+" days"*/ : "Unlimited");

                        }else{

                            mHolder.containerExpiry.setVisibility(View.GONE);
                        }
                    }


                    break;
            }



            mHolder.viewExam.setText((exam.getName() != null)? exam.getName() : "");


            //amount
            if(exam.getPaidExam().equals("1")){

                mHolder.viewAmount.setText((exam.getAmount() != null)? MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+(String)exam.getAmount() : MknUtils.getCurrencyCode(session.getUserDetails().get(SessionManager.KEY_CURRENCY))+"0");

            }else{

                mHolder.viewAmount.setText("Free");

            }


            if(examFilter == 1){

                //attempt count
                if(exam.getAttemptCount().equals("0")){

                    mHolder.viewAttempted.setText("Unlimited");
                    exam.setCanAttempt(true);

                }else{

                    int totalAttemp = Integer.parseInt(exam.getAttemptCount())*Integer.parseInt(exam.getAttemptOrder());

                    int attemptRemaning = totalAttemp - Integer.parseInt(exam.getAttempt());

                    if(attemptRemaning > 0){

                        exam.setCanAttempt(true);

                    }else{

                        exam.setCanAttempt(false);
                    }

                    mHolder.viewAttempted.setText((attemptRemaning >= 0)? attemptRemaning+"" : "0");
                }

            }



            mHolder.viewExamBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mListener != null){

                        mListener.onViewDetails(exam, canTakeExam);

                    }
                }
            });


            mHolder.takeExamBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mListener != null){

                        mListener.onTakeExam(exam, canTakeExam);

                    }

                }
            });

        }

        return convertView;
    }


    class ViewHolder {

        TextView viewExam, viewDate, viewExpiry, viewAttempted, viewAmount, endDate;
        LinearLayout containerEndDate, containerExpiry, containerAttempted, containerAmount;

        TextView viewExamBtn, takeExamBtn;
    }

}
