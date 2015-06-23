package com.translationheader.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by raychum on 9/6/15.
 */
public class RecyclerViewFragmentAdapter extends FragmentPagerAdapter {

    private static final String TAG = RecyclerViewFragmentAdapter.class.getSimpleName();
    private final ArrayList<Fragment> fragments = new ArrayList<>();

    public RecyclerViewFragmentAdapter(FragmentManager fm) {
        super(fm);
        for (int i = 0; i < 6; i++) {
            final ChildFragment childFragment = new ChildFragment();
            if (i % 2 == 0) {
                childFragment.isShowSubTabBar(true);
            }
            final Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            childFragment.setArguments(bundle);
            fragments.add(childFragment);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
