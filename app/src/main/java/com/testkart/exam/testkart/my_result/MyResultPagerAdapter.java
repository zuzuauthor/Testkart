package com.testkart.exam.testkart.my_result;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by elfemo on 18/8/17.
 */

public class MyResultPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;
    String resultId;

    public MyResultPagerAdapter(FragmentManager fm, int NumOfTabs, String resultId) {
        super(fm);

        this.mNumOfTabs = NumOfTabs;
        this.resultId = resultId;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ScoreCardFragment tab1 = new ScoreCardFragment();
                tab1.updateResultId(resultId);
                return tab1;
            case 1:
                SubjectReportFragment tab2 = new SubjectReportFragment();

                tab2.updateResultId(resultId);

                return tab2;

            case 2:
                SolutionFragment tab3 = new SolutionFragment();
                tab3.updateResultId(resultId);

                return tab3;


            case 3:
                CompareReportFragment tab4 = new CompareReportFragment();
                tab4.updateResultId(resultId);

                return tab4;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
