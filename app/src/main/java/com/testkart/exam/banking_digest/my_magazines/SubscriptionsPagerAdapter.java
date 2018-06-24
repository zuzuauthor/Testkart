package com.testkart.exam.banking_digest.my_magazines;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by elfemo on 12/2/18.
 */

public class SubscriptionsPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;
    String resultId;

    public SubscriptionsPagerAdapter(FragmentManager fm, int NumOfTabs, String resultId) {
        super(fm);

        this.mNumOfTabs = NumOfTabs;
        this.resultId = resultId;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MySubscriptionsFragment tab1 = new MySubscriptionsFragment();

                return tab1;
            case 1:
                ExpiredSubscriptionsFragment tab2 = new ExpiredSubscriptionsFragment();

                return tab2;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}