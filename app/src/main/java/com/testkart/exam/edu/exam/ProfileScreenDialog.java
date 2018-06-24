package com.testkart.exam.edu.exam;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.testkart.exam.edu.MyExamActivity1;
import com.testkart.exam.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by testkart on 28/4/17.
 */

public class ProfileScreenDialog extends DialogFragment{

    private TextView viewStudentName, viewEnroll, viewInputAddress, viewContact, viewEmail, viewActive;
    private CircleImageView viewProfilePic;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_profile_screen, null);

        initViews(rootView);

        builder.setView(rootView);

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //show feedback activity


            }
        });



        AlertDialog d = builder.create();

        return d;

    }

    private void initViews(View rootView) {

        Bundle args = getArguments();

        com.testkart.exam.edu.exam.ibps.model.Student student = (com.testkart.exam.edu.exam.ibps.model.Student) args.getSerializable(MyExamActivity1.KEY_STUDENT);
        String image = args.getString(MyExamActivity1.KEY_STUDENT_IMAGE);

        viewStudentName = rootView.findViewById(R.id.viewStudentName);
        viewEnroll = rootView.findViewById(R.id.viewEnroll);
        viewInputAddress = rootView.findViewById(R.id.viewInputAddress);
        viewContact = rootView.findViewById(R.id.viewContact);
        viewEmail = rootView.findViewById(R.id.viewEmail);
        viewActive = rootView.findViewById(R.id.viewActive);

        viewStudentName.setText(student.getName());
        viewEnroll.setText(student.getPhone());
        viewInputAddress.setText(student.getAddress());
        viewContact.setText(student.getPhone());
        viewEmail.setText(student.getEmail());
        viewActive.setText(student.getStatus());

        viewProfilePic = rootView.findViewById(R.id.viewProfilePic);

        if(image != null){

            Glide
                    .with(getActivity())
                    .load(image)
                    //.placeholder(R.drawable.loading_spinner)
                    .into(viewProfilePic);

        }

    }
}
