package com.testkart.exam.banking_digest.read;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.testkart.exam.banking_digest.my_magazines.model.Magazine;
import com.testkart.exam.banking_digest.read.model.MagazinesPost;
import com.testkart.exam.edu.exam.fragments.FillblankQuestion;
import com.testkart.exam.edu.exam.fragments.MultipleChoiceQuestion;
import com.testkart.exam.edu.exam.fragments.SingleChoiceQuestion;
import com.testkart.exam.edu.exam.fragments.SubjectiveQuestion;
import com.testkart.exam.edu.exam.fragments.TrueFalseQuestion;
import com.testkart.exam.edu.exam.ibps.datamodel.DataPagerQuestion;
import com.testkart.exam.edu.helper.DBHelper;

import com.testkart.exam.banking_digest.read.model.Response;

import java.util.ArrayList;

/**
 * Created by elfemo on 27/2/18.
 */

public class MagazineContentPager extends FragmentStatePagerAdapter {

    private ArrayList<Response> questionList;
    private DBHelper dbHelper;
    private Context context;
    private String newExam;
    private String coverImage;

    public MagazineContentPager(FragmentManager fm, ArrayList<Response> questionList, Context context, String coverImage) {
        super(fm);


        questionList.add(0, getCoverImageObj(coverImage));


        this.questionList = questionList;
        this.dbHelper = dbHelper;
        this.context = context;
        this.coverImage = coverImage;

    }

    private Response getCoverImageObj(String coverImage) {

        com.testkart.exam.banking_digest.read.model.Response res = new Response();

        res.setId("0");
        res.setTitle("Cover Page");
        res.setContent("<img src=http://www.testkart.com/img/magazines/"+coverImage+" width=\"100%\" />");

        //res.setContent("<style>img{display: inline;height: auto;max-width: 100%;}</style>"+"<img src=http://www.testkart.com/img/magazines/"+coverImage);

        return res;
    }

    @Override
    public Fragment getItem(int position) {

        Response magPost =  questionList.get(position);
        ReadFragment readFragment = new ReadFragment();
        readFragment.updatePage(magPost);

        return readFragment;

    }

    /*private int id = 0;
    @Override
    public long getItemId(int position) {

        id = (int)position;

        return position;
    }*/


    @Override
    public int getItemPosition(Object object) {

        if (object instanceof ReadFragment) {

            //((ReadFragment)object).scrollToTop(speedMode);
            ((ReadFragment)object).scrollToTop(speedMode);
        }

        return super.getItemPosition(object);

    }


    //return POSITION_NONE;
    // }


    int speedMode = 1;
    public void updateData(int sm){

        speedMode = sm;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.questionList.size();
    }
}
