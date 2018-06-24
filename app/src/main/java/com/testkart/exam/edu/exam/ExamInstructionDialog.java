package com.testkart.exam.edu.exam;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.testkart.exam.R;
import com.testkart.exam.edu.helper.MknUtils;

/**
 * Created by testkart on 11/5/17.
 */

public class ExamInstructionDialog extends DialogFragment {

    public static final String KEY_INSTRUCTIONS = "instruction";

    private WebView instructionView;

    private LinearLayout linearLayout;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_exam_instruction, null);

        initViews(rootView);

        builder.setView(rootView);
        builder.setCancelable(false);

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dismiss();

            }
        });

        return builder.create();
    }

    private void initViews(View rootView) {

        String instructionText = getArguments().getString(KEY_INSTRUCTIONS);

        linearLayout = (LinearLayout)rootView.findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.GONE);

        instructionView = (WebView)rootView.findViewById(R.id.instructionView);

        WebSettings webSettings = instructionView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
       // / webSettings.setLoadWithOverviewMode(true);

        /*


        <p>Pick the correct android version</p>
 <p><iframe width="560" height="315" src="https://www.youtube.com/embed/7EpOEKQ1IXE" frameborder="0" allowfullscreen></iframe></p>
 <p>&nbsp;</p>

        * */

        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
       // webSettings.setTextZoom(webSettings.getTextZoom() + 30);

        instructionView.setWebChromeClient(new WebChromeClient());

        String q = MknUtils.changedHeaderHtml(instructionText);

        instructionView.loadData(q, "text/html", "UTF-8");
    }
}
