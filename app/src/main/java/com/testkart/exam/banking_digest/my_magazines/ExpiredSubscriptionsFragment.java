package com.testkart.exam.banking_digest.my_magazines;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.testkart.exam.R;
import com.testkart.exam.banking_digest.my_magazines.model.MyMagazineResponse;
import com.testkart.exam.banking_digest.my_magazines.model.my_subscription.MySubscriptionResponse;
import com.testkart.exam.banking_digest.read.ReadPdfActivity;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.helper.DialogHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elfemo on 12/2/18.
 */

public class ExpiredSubscriptionsFragment extends Fragment {

    private GridView viewSubscriptions;
    private LinearLayout viewEmpty;

    private ProgressDialog progressDialog;

    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_subsriptions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");

        viewEmpty = (LinearLayout) view.findViewById(R.id.viewEmpty);
        viewEmpty.setVisibility(View.GONE);
        viewSubscriptions = (GridView) view.findViewById(R.id.viewSubscriptions);
        viewSubscriptions.setVisibility(View.GONE);

        actGridPackagesListener();

        //get my magazines
        getAllMyMagazines();
    }


    private void actGridPackagesListener() {

        viewSubscriptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DialogHelper.showInfoDialog(getContext(), "Testkart", "Your subscription to this magazine is over.");
                /*Intent packageDetailActivity = new Intent(getContext(), ReadPdfActivity.class);
                startActivity(packageDetailActivity);*/

            }
        });
    }


    private void getAllMyMagazines() {

        if (ApiClient.isNetworkAvailable(getContext())) {

            //show progress dialog
           // progressDialog.show();

            //makeAPiCall();

        } else {

            Toast.makeText(getContext(), Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }
    }

    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        progressDialog.show();

        Call<MySubscriptionResponse> call = apiService.getAllSubscriptions("https://zuzusoft.co.in/products/mkn/my_magazines.php");

        call.enqueue(new Callback<MySubscriptionResponse>() {
            @Override
            public void onResponse(Call<MySubscriptionResponse> call, Response<MySubscriptionResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){


                            //check if magazines are empty
                            if(!response.body().getData().isEmpty()){


                                SubscriptionListAdapter adapter = new SubscriptionListAdapter(getContext(), response.body().getData(), 1);

                                viewSubscriptions.setAdapter(adapter);

                                viewEmpty.setVisibility(View.GONE);
                                viewSubscriptions.setVisibility(View.VISIBLE);

                            }else {

                                viewEmpty.setVisibility(View.VISIBLE);
                                viewSubscriptions.setVisibility(View.GONE);

                            }


                        }else{

                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{

                        Toast.makeText(getContext(), "POResponse body is null", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(getContext(), "ERROR RESPONSE CODE: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MySubscriptionResponse> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });

    }
}
