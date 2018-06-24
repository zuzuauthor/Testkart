package com.testkart.exam.edu.exam;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.testkart.exam.R;

import java.util.ArrayList;

/**
 * Created by testkart on 27/4/17.
 */

public class QuestionGridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<QuestionGridModel> dataSet;

    public QuestionGridAdapter(Context context, ArrayList<QuestionGridModel> dataSet){

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;

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

        ViewHolder viewHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.question = (ImageView)convertView.findViewById(R.id.circularTextView);
            viewHolder.optBaseBg = (FrameLayout)convertView.findViewById(R.id.optBaseBg);

            convertView.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }


        //check question status
        QuestionGridModel q = (QuestionGridModel) getItem(position);

        Log.v("Question Status", q.getQuestionStatus());

        switch (q.getQuestionStatus()){

            //not visit
            case "0":

                viewHolder.optBaseBg.setBackgroundResource(R.drawable.not_visited);

                TextDrawable drawable0 = TextDrawable.builder()
                        .beginConfig()
                        .textColor(Color.parseColor("#191919"))
                        .useFont(Typeface.DEFAULT)
                        .endConfig()
                        .buildRound(q.getQuestionNo(), Color.TRANSPARENT);

                viewHolder.question.setImageDrawable(drawable0);

               /* TextDrawable drawable0 = TextDrawable.builder()
                        .textColor(Color.parseColor("#008000"))
                        .buildRound(q.getQuestionNo(), Color.TRANSPARENT);
                viewHolder.question.setImageDrawable(drawable0);*/

                break;

            //not answered
            case "1":

                viewHolder.optBaseBg.setBackgroundResource(R.drawable.not_answered);

                TextDrawable drawable1 = TextDrawable.builder()
                        .buildRound(q.getQuestionNo(), Color.TRANSPARENT);
                viewHolder.question.setImageDrawable(drawable1);

                break;

            //answered
            case "2":

                viewHolder.optBaseBg.setBackgroundResource(R.drawable.answered);

                TextDrawable drawable2 = TextDrawable.builder()
                        .buildRound(q.getQuestionNo(), Color.TRANSPARENT);
                viewHolder.question.setImageDrawable(drawable2);

                break;

            //marked for review
            case "3":

                viewHolder.optBaseBg.setBackgroundResource(R.drawable.review);

                TextDrawable drawable3 = TextDrawable.builder()
                        .buildRound(q.getQuestionNo(), Color.TRANSPARENT);
                viewHolder.question.setImageDrawable(drawable3);

                break;

            //marked for review and answered
            case "4":

                viewHolder.optBaseBg.setBackgroundResource(R.drawable.review_answer);

                TextDrawable drawable4 = TextDrawable.builder()
                .beginConfig()
                   // .textColor(Color.parseColor("#008000"))
                    .useFont(Typeface.DEFAULT)
                    .endConfig()
                    .buildRound(q.getQuestionNo(), Color.TRANSPARENT);

                viewHolder.question.setImageDrawable(drawable4);

                break;

            //not answered
            default:

                viewHolder.optBaseBg.setBackgroundResource(R.drawable.not_visited);

                TextDrawable drawable5 = TextDrawable.builder()
                        .buildRound(q.getQuestionNo(), Color.TRANSPARENT);
                viewHolder.question.setImageDrawable(drawable5);

                break;
        }


        return convertView;
    }

    class ViewHolder{

        private ImageView question;
        private FrameLayout optBaseBg;
    }


    public void setData() {

        notifyDataSetChanged();

    }

}
