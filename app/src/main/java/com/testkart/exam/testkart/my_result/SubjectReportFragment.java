package com.testkart.exam.testkart.my_result;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.testkart.my_result.subject_report.GrandTotal;
import com.testkart.exam.testkart.my_result.subject_report.SubjectReportAdapter;
import com.testkart.exam.testkart.my_result.subject_report.SubjectReportResponse;
import com.testkart.exam.testkart.my_result.subject_report.SubjectStat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elfemo on 17/8/17.
 */

public class SubjectReportFragment extends Fragment {

    private SessionManager sessionManager;
    private ProgressDialog progressDialog;

    private ListView subjectReportList;
    private TextView header;


    private String resultId;

    public void updateResultId(String resultId){

        this.resultId = resultId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_my_result_subject_report, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");

        header = (TextView)view.findViewById(R.id.header);
        subjectReportList = (ListView)view.findViewById(R.id.subjectReportList);

        getSubjectStatus();

    }

    private void getSubjectStatus() {

        if (ApiClient.isNetworkAvailable(getActivity())) {

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

        //show progress dialog
        progressDialog.show();

        Call<SubjectReportResponse> call = apiService.getSubjectResponse(resultId, sessionManager.getUserDetails().get(SessionManager.KEY_STUDENT_ID));

        call.enqueue(new Callback<SubjectReportResponse>() {
            @Override
            public void onResponse(Call<SubjectReportResponse> call, Response<SubjectReportResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if (code == 200) {

                    if(response.body() != null){


                        if(response.body().getStatus().equals("true")){


                            if(!response.body().getSubjectReport().getSubjectStats().isEmpty()){

                                List<SubjectStat> ssList = response.body().getSubjectReport().getSubjectStats();


                                GrandTotal gt = response.body().getSubjectReport().getGrandTotal();

                                header.setText("Subject Report for "+response.body().getSubjectReport().getGrandTotal().getExamName());

                                Log.v("Grand Total", gt.toString());

                                SubjectStat ss = new SubjectStat();
                                ss.setSubjectname("Grand Total");
                                ss.setTotalQuestions(gt.getTotalQuestions());
                                ss.setCorrectIncorrectCount(gt.getCorrectIncorrectCount());
                                ss.setPositiveNegativeMarks(gt.getPositiveNegativeMarks());
                                ss.setUnattemptQuestionCountMarks(gt.getUnattemptQuestionCountMarks());

                                ssList.add(ss);

                               // Log.v("Subject List", "Size: "+dataSet.size()+"\n"+dataSet.toString());

                                SubjectReportAdapter adapter = new SubjectReportAdapter(getContext(), ssList);

                                subjectReportList.setAdapter(adapter);

                            }else{

                                Toast.makeText(getActivity(), "Subject report is empty!", Toast.LENGTH_SHORT).show();
                            }


                        }else{

                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }else{

                        Toast.makeText(getActivity(), "POResponse body is null: " + code, Toast.LENGTH_SHORT).show();

                    }


                }else {

                    Toast.makeText(getActivity(), "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubjectReportResponse> call, Throwable t) {

                //hide progress dialog
                progressDialog.dismiss();

                Toast.makeText(getContext(), Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });


    }

}
