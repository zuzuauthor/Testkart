package com.testkart.exam.edu.exam.examlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.edu.exam.ExamListAdapter;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by testkart on 19/5/17.
 */

public class PurchaseExamFragment extends Fragment implements ExamListAdapter.ExamListActionListener{

    private LinearLayout viewEmpty;
    private ListView viewExamList;
    private ProgressDialog progressDialog;
    private SessionManager session;

    private ExamSelectListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (ExamSelectListener)context;
        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_examlist, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
       // progressDialog.setTitle("Purchase Exam");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        session = new SessionManager(getContext());

        viewEmpty = (LinearLayout)view.findViewById(R.id.viewEmpty);
        viewExamList = (ListView)view.findViewById(R.id.viewExamList);
       // viewExamListListener();
    }


    /*private void viewExamListListener() {

        viewExamList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(mListener != null){

                    Exam exam = (Exam) viewExamList.getItemAtPosition(position);

                    mListener.onExamSelect(exam, true);
                }
            }
        });
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get today's exam
        todayExam();

    }

    private void todayExam() {

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

        //show progress dialog
        if(progressDialog != null){

            //hide progress dialog
            progressDialog.show();
        }

        Call<ExamListResponse> call = apiService.getPurchaseExam(session.getUserDetails().get(SessionManager.KEY_PUBLIC),
                session.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<ExamListResponse>() {
            @Override
            public void onResponse(Call<ExamListResponse> call, retrofit2.Response<ExamListResponse> response) {

                if(progressDialog != null){

                    //hide progress dialog
                    progressDialog.dismiss();
                }

                int code = response.code();

                if(code == 200){

                    if(response.body().getStatus()){

                        if(response.body().getResponse().isEmpty()){

                            viewEmpty.setVisibility(View.VISIBLE);
                            viewExamList.setVisibility(View.GONE);

                        }else{

                            //bundle data and set adapter
                            viewEmpty.setVisibility(View.GONE);
                            viewExamList.setVisibility(View.VISIBLE);

                            ArrayList<Exam> dataSet = new ArrayList<Exam>();

                            for (Response r:
                                    response.body().getResponse()) {

                                Exam  e = r.getExam();
                                e.setExpiry((String)r.getExamOrder().getExpiryDate());
                               // e.setExamExpiry((String)r.getExamOrder().getExpiryDate());

                                dataSet.add(e);
                            }

                            ExamListAdapter adapter = new ExamListAdapter(getContext(), dataSet,2, false);

                            viewExamList.setAdapter(adapter);
                        }


                    }else{

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(getContext(), "Error POResponse Code: "+code, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ExamListResponse> call, Throwable t) {

                if(progressDialog != null){

                    //hide progress dialog
                    progressDialog.dismiss();
                }

                Toast.makeText(getContext(), Consts.SERVER_ERROR+t.getMessage(), Toast.LENGTH_SHORT).show();

                t.printStackTrace();

            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(progressDialog != null){

            //hide progress dialog
            progressDialog.dismiss();
        }
    }

    @Override
    public void onTakeExam(Exam exam, boolean canTakeExam) {



    }

    @Override
    public void onViewDetails(Exam exam, boolean canTakeExam) {


        if(mListener != null){

            mListener.onExamSelect(exam, true, 2);
        }

    }
}
