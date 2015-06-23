package com.translationheader.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.translationheader.TranslationHeaderParentFragment;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

/**
 * Created by Ray on 23/6/15.
 */
public class MainFragment extends TranslationHeaderParentFragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private View rootView;
    private View imageView;
    private View header;
    private RecyclerViewFragmentAdapter adapter;
    private ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        pager = (ViewPager) rootView.findViewById(R.id.pager);
        header = rootView.findViewById(R.id.header);
        imageView = rootView.findViewById(R.id.imageView);
        adapter = new RecyclerViewFragmentAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.slidingTabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setOnPageChangeListener(new OnParentPageChangeListener());
        pager.setOffscreenPageLimit(6);
        return rootView;
    }

    @Override
    public boolean isSetToolbarBackgroundAlpha() {
        return ((AppCompatActivity)getActivity()).getSupportActionBar() != null && true;
    }

    @Override
    @NonNull
    public FragmentPagerAdapter getAdapter() {
        return adapter;
    }

    @Override
    @NonNull
    public View getHeader() {
        return header;
    }

    @Override
    @NonNull
    public ViewPager getViewPager() {
        return pager;
    }

    @Override
    public int getMaxTranslationY() {
        int maxTranslationY = 0;
        if (imageView != null) {
            maxTranslationY = imageView.getHeight();
            if (isSetToolbarBackgroundAlpha()) {
                maxTranslationY -= ((AppCompatActivity)getActivity()).getSupportActionBar().getHeight();
            }
        }
        Log.d(TAG, "getMaxTranslationY maxTranslationY=" + maxTranslationY);
        return maxTranslationY;
    }
}
