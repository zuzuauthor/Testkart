package com.testkart.exam.testkart.my_result;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.SessionManager;

/**
 * Created by elfemo on 17/8/17.
 */

public class CompareReportFragment extends Fragment{


    private SessionManager session;
    SwipeRefreshLayout swipeView;
    WebView myWebView;
    ProgressDialog progressBar;
    String myUrl;

    String resultId;

    public void updateResultId(String resultId){

        this.resultId = resultId;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_my_result_solution, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        http://www.testkart.com/crm/Results/viewcompare/5592/978
         */

        session = new SessionManager(getContext());
        final String mySolution = "http://www.testkart.com/crm/Results/viewcompare/"+resultId+"/"+session.getUserDetails().get(SessionManager.KEY_STUDENT_ID);

        Log.v("My Solution", mySolution);

        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        myWebView = (WebView) view.findViewById(R.id.webview);
        progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Please Wait...");
        myWebView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.setHorizontalScrollBarEnabled(false);

        progressBar.show();
        myWebView.loadUrl(mySolution);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                myWebView.loadUrl(mySolution);

            }});

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {

            swipeView.setRefreshing(false);
            progressBar.dismiss();

        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            myUrl = url;
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onReceivedError(WebView view, int errorCod,String description, String failingUrl) {
            //myWebView.loadUrl("file:///android_asset/error_page.html");

        }
    }
}
