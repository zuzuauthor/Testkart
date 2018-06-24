package com.testkart.exam.edu.myresult;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.testkart.exam.R;

/**
 * Created by testkart on 25/5/17.
 */

public class ProgressUpdateDialog extends DialogFragment {

    private TextView progress, size, title;
    private ProgressBar progressBarView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View rootVIew = LayoutInflater.from(getContext()).inflate(R.layout.dialog_progress_update, null);

        initialization(rootVIew);

        builder.setView(rootVIew);

        AlertDialog d = builder.create();
        d.setCancelable(false);


        return d;
    }

    private void initialization(View rootVIew) {

       // title = (TextView) rootVIew.findViewById(R.id.title);

        progress = (TextView)rootVIew.findViewById(R.id.progress);
        size = (TextView)rootVIew.findViewById(R.id.size1);
        progressBarView = (ProgressBar)rootVIew.findViewById(R.id.progressBarView);
    }


    public void updateProgress(int updateProgress, String sizeK, String titleStr){

       // title.setText(titleStr);

        progress.setText("Progress: "+updateProgress+"%");
        size.setText(sizeK);
        progressBarView.setProgress(updateProgress);
    }
}
