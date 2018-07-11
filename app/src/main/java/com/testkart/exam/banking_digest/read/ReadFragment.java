package com.testkart.exam.banking_digest.read;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.clans.fab.FloatingActionButton;
import com.testkart.exam.R;
import com.testkart.exam.banking_digest.read.control.ObservableWebView;
import com.testkart.exam.banking_digest.read.model.Response;
import com.testkart.exam.edu.helper.MknUtils;

/**
 * Created by elfemo on 27/2/18.
 */

public class ReadFragment extends Fragment implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, ObservableWebView.OnScrollChangedCallback, View.OnClickListener {

    private ObservableWebView viewContent;
    FloatingActionButton fab_up;

    private Response magPost;

    private GestureDetectorCompat mDetector;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {

            mListener = (ReadBookListener) context;

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_read, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(getContext(), this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);

        fab_up = view.findViewById(R.id.fab_up);

        viewContent = view.findViewById(R.id.viewContent);
        viewContent.setLongClickable(false);
        viewContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        viewContent.setOnScrollChangedCallback(this);

        fab_up.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            viewContent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            viewContent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        ObjectAnimator anim = ObjectAnimator.ofInt(viewContent, "scrollY", viewContent.getScrollY(), 0);
        anim.setDuration(100).start();

        viewContent.setOnTouchListener(touchListener);

        WebSettings webSettings = viewContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        //webSettings.setTextZoom(webSettings.getTextZoom() + 5);
        webSettings.setDefaultTextEncodingName("utf-8");
        String option = MknUtils.changedHeaderHtml3((magPost != null && magPost.getContent() != null) ? magPost.getContent() : "<h3>Post is empty<h3>");

        //viewContent.loadData(option, "text/html", "UTF-8");

        viewContent.scrollTo(0, 0);

        viewContent.loadDataWithBaseURL(null, option,
                "text/html", "UTF-8", null);

    }

    private int getScale() {
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width) / new Double(30);
        val = val * 100d;
        return val.intValue();
    }

    public void updatePage(Response magPost) {

        this.magPost = magPost;
    }


    // This touch listener passes everything on to the gesture detector.
    // That saves us the trouble of interpreting the raw touch events
    // ourselves.
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // pass the events to the gesture detector
            // a return value of true means the detector is handling it
            // a return value of false means the detector didn't
            // recognize the event
            return mDetector.onTouchEvent(event);

        }
    };


    private ReadBookListener mListener;
    ObjectAnimator anim;

    public void scrollToTop(int speedMode) {

        if(animTop != null && animTop.isRunning()){

            animTop.cancel();
            //  anim.end();
            animTop = null;

        }

        long duration = 5000;

        if (speedMode == 1) {
            //cheetha
            duration = 15000;

        } else if (speedMode == 2) {
            //rabbit
          /*  if (viewContent.getContentHeight() >= 2000 && viewContent.getContentHeight() < 4000 )
                duration = viewContent.getContentHeight() - 1200;
            else if(viewContent.getContentHeight() >= 4000 && viewContent.getContentHeight() <= 8000)
                duration = viewContent.getContentHeight() - 4200;
            else if (viewContent.getContentHeight() > 8000)
                duration = viewContent.getContentHeight() - 7500;
            else
                duration = viewContent.getContentHeight();*/

            duration = viewContent.getContentHeight() + 20000;
        } else {
            //turtle
            duration = viewContent.getContentHeight() + 88000;
        }

        int h = viewContent.getContentHeight();

        anim = ObjectAnimator.ofInt(viewContent, "scrollY", viewContent.getScrollY(), viewContent.getContentHeight());
        anim.setDuration(duration);

        if (viewContent.getScrollY() + (viewContent.getBottom() - viewContent.getScrollY()) < viewContent.getContentHeight() - 10) {
            anim.start();
        }

        // viewContent.scrollTo(0,0);
    }

    public void onScrollTopPage() {
        viewContent.scrollTo(0, 0);
        if(anim != null){
            anim.cancel();
            anim.end();
            anim = null;
        }
    }


    @Override
    public void onScroll(int l, int t, int oldl, int oldt) {
        if (t > oldt && t > 30) {
            //Do stuff
            System.out.println("Swipe UP");
            fab_up.setVisibility(View.VISIBLE);
            //Do stuff
        } else if (t < 40) {
            System.out.println("Swipe Down");
            fab_up.setVisibility(View.GONE);
        }


        //detecting webview scroll end
        int height = (int) Math.floor(viewContent.getContentHeight() * viewContent.getScale());
        int webViewHeight = viewContent.getMeasuredHeight();
        if(viewContent.getScrollY() + webViewHeight >= height){
            if(anim != null) {
                anim.cancel();
                anim.end();
            }
            anim = null;
        }

        Log.d("adfa", "We Scrolled etc...");
    }

    ObjectAnimator animTop;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab_up) {

            if(anim != null && anim.isRunning()){

                anim.cancel();
                //  anim.end();
                anim = null;

            }

            animTop = ObjectAnimator.ofInt(viewContent, "scrollY",
                    viewContent.getScrollY(), 0);
            animTop.setDuration(3000);

            if(viewContent.getScrollY() > 0){

                animTop.start();
            }
            //viewContent.scrollTo(0, 0);

        }
    }

    public interface ReadBookListener {

        void onSingleTap();
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {

        if(anim != null && anim.isRunning()){


            anim.cancel();
          //  anim.end();
            anim = null;

        }else{

            if (mListener != null) {

                mListener.onSingleTap();

            }
        }

        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {

        /*if(mListener != null){

            mListener.onSingleTap();
        }*/

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
