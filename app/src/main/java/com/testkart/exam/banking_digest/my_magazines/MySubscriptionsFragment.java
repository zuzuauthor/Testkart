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

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.testkart.exam.R;
import com.testkart.exam.banking_digest.buy.model.MagazinesResponse;
import com.testkart.exam.banking_digest.my_magazines.model.MyMagazineResponse;
import com.testkart.exam.banking_digest.my_magazines.model.my_subscription.MySubscriptionResponse;
import com.testkart.exam.banking_digest.read.ReadMagazineActivity;
import com.testkart.exam.banking_digest.read.ReadPdfActivity;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.packages.PackageDetailActivity;
import com.testkart.exam.packages.model.DataOrderSummary;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elfemo on 12/2/18.
 */

public class MySubscriptionsFragment extends Fragment implements SubscriptionListAdapter.SubscriptionActionListener{

    public static final String KEY_INTENT_FILE_PATH = "intent_file_path";
    public static final String KEY_INTENT_FILE_NAME = "intent_file_name";

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

               com.testkart.exam.banking_digest.my_magazines.model.Response response = (com.testkart.exam.banking_digest.my_magazines.model.Response) viewSubscriptions.getItemAtPosition(position);

               Intent readActivity = new Intent(getContext(), ReadMagazineActivity.class );

               readActivity.putExtra(ReadMagazineActivity.KEY_INTENT_MAGID, response.getMagazine().getId());
               readActivity.putExtra(ReadMagazineActivity.KEY_INTENT_MAGNAME, response.getMagazine().getTitle());
               readActivity.putExtra(ReadMagazineActivity.KEY_INTENT_COVER_PAGE, response.getMagazine().getPhoto());

               startActivity(readActivity);
            }
        });
    }

    private void downloadMagazine(String s, String fn) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");

        //directory path
        String path = MyMagazinesActivity.FILE_PATH;

        //file name
        final String fileName = fn;

        PRDownloader.download(s, path, fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                        progressDialog.show();
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                        double total = 100.0 * (progress.currentBytes / progress.totalBytes);

                        progressDialog.setMessage("Initializing ");

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        progressDialog.cancel();

                        // bundle pdf with path and go to read activity
                        Intent readActivity = new Intent(getContext(), ReadPdfActivity.class);
                        readActivity.putExtra(KEY_INTENT_FILE_PATH, MyMagazinesActivity.FILE_PATH);
                        readActivity.putExtra(KEY_INTENT_FILE_NAME, fileName);
                        startActivity(readActivity);

                    }

                    @Override
                    public void onError(Error error) {

                        //retry dialog;
                        progressDialog.cancel();
                    }
                });
    }


    private void getAllMyMagazines() {

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
        progressDialog.show();

        Call<MyMagazineResponse> call = apiService.getAllMyMagazines(sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC), sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE));

        call.enqueue(new Callback<MyMagazineResponse>() {
            @Override
            public void onResponse(Call<MyMagazineResponse> call, Response<MyMagazineResponse> response) {

                progressDialog.dismiss();

                int code = response.code();

                if(code == 200){

                    if(response.body() != null){

                        if(response.body().getStatus()){


                            //check if packages are empty are empty
                            if(response.body().getResponse() !=  null && !response.body().getResponse().isEmpty()){

                                    SubscriptionListAdapter1 adapter = new SubscriptionListAdapter1(getActivity(), response.body().getResponse(), 0, getLayoutInflater());

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
            public void onFailure(Call<MyMagazineResponse> call, Throwable t) {

                progressDialog.dismiss();
                t.printStackTrace();

            }
        });

    }



    /*private void makeAPiCall() {

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


                            //check if packages are empty are empty
                            if(!response.body().getData().isEmpty()){


                                SubscriptionListAdapter adapter = new SubscriptionListAdapter(getContext(), response.body().getData(), 0);

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

    }*/

    @Override
    public void readMagazine(String magazineId) {

        //goto read activity
        Toast.makeText(getContext(), "Read Magazine", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void viewAllMagazine() {

        //show all magazine dialog
        Toast.makeText(getContext(), "View All Magazine", Toast.LENGTH_SHORT).show();

    }
}
