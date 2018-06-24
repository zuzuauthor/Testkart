package com.testkart.exam.edu.exam;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.testkart.exam.edu.exam.fragments.FillblankQuestion;
import com.testkart.exam.edu.exam.fragments.MultipleChoiceQuestion;
import com.testkart.exam.edu.exam.fragments.SingleChoiceQuestion;
import com.testkart.exam.edu.exam.fragments.SubjectiveQuestion;
import com.testkart.exam.edu.exam.fragments.TrueFalseQuestion;
import com.testkart.exam.edu.exam.ibps.datamodel.DataPagerQuestion;
import com.testkart.exam.edu.helper.DBHelper;

import java.util.ArrayList;

/**
 * Created by testkart on 4/5/17.
 */

public class QuestionPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<DataPagerQuestion> questionList;
    private DBHelper dbHelper;
    private Context context;
    private String newExam;

    public QuestionPagerAdapter(FragmentManager fm, ArrayList<DataPagerQuestion> questionList, DBHelper dbHelper, Context context, String newExam) {
        super(fm);
        this.questionList = questionList;
        this.dbHelper = dbHelper;
        this.context = context;
        this.newExam = newExam;
    }
    @Override
    public Fragment getItem(int position) {

        Log.v("FID",position+"");

        DataPagerQuestion question  = questionList.get(position);

        switch (question.getQuestionStat().getQuestionType()){

            case "M":

                    //multiple choice
                    MultipleChoiceQuestion mcf = new MultipleChoiceQuestion();
                    mcf.setQuestion(question, position+1, dbHelper, context, newExam);


                    return mcf;


            case "W":

                //single choice
                SingleChoiceQuestion scf = new SingleChoiceQuestion();
                scf.setQuestion(question, position+1, dbHelper, context, newExam);


                return scf;

            case "T":

                TrueFalseQuestion tff = new TrueFalseQuestion();
                tff.setQuestion(question, position+1, dbHelper, context, newExam);

                return tff;

            case "F":

                FillblankQuestion fbf = new FillblankQuestion();
                fbf.setQuestion(question, position+1, dbHelper, context, newExam);

                return fbf;

            case "S":

                SubjectiveQuestion sbf = new SubjectiveQuestion();
                sbf.setQuestion(question, position+1, dbHelper, context, newExam);

                return sbf;

        }

        return null;

    }

    /*private int id = 0;
    @Override
    public long getItemId(int position) {

        id = (int)position;

        return position;
    }*/


    @Override
    public int getItemPosition(Object object) {

        if (object instanceof MultipleChoiceQuestion) {

            if(fun == 1){

                ((MultipleChoiceQuestion)object).updateView();

            }else if(fun == 2){

                ((MultipleChoiceQuestion)object).updateQuestionLanguage();
            }

        }else if(object instanceof SingleChoiceQuestion){

            if(fun == 1){

                ((SingleChoiceQuestion)object).updateView();
            }else if(fun == 2){

                ((SingleChoiceQuestion)object).updateQuestionLanguage();
            }



        }else if(object instanceof TrueFalseQuestion){

            if(fun == 1){

                ((TrueFalseQuestion)object).updateView();
            }else if(fun == 2){

                ((TrueFalseQuestion)object).updateQuestionLanguage();
            }

        }else if(object instanceof FillblankQuestion){

            if(fun == 1){

                ((FillblankQuestion)object).updateView();
            }else if(fun == 2){

                ((FillblankQuestion)object).updateQuestionLanguage();
            }

        }else if(object instanceof SubjectiveQuestion){

            if(fun == 1){

                ((SubjectiveQuestion)object).updateView();
            }else if(fun == 2){

                ((SubjectiveQuestion)object).updateQuestionLanguage();
            }
        }
        return super.getItemPosition(object);
    }


        //return POSITION_NONE;
   // }


    int fun;
    public void updateData(int fun){
        this.fun = fun;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.questionList.size();
    }
}
