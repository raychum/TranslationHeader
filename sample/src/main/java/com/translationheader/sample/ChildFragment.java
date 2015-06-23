package com.translationheader.sample;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.translationheader.RecyclerViewHeader;
import com.translationheader.TranslationHeaderChildFragment;
import com.translationheader.TranslationHeaderParentFragment;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ray on 23/6/15.
 */
public class ChildFragment extends TranslationHeaderChildFragment {
    // Testing images
    public static final ArrayList<Integer> imageList = new ArrayList<>();
    private static final String TAG = ChildFragment.class.getSimpleName();

    static {
        imageList.add(R.drawable.image0);
        imageList.add(R.drawable.image1);
        imageList.add(R.drawable.image2);
        imageList.add(R.drawable.image3);
        imageList.add(R.drawable.image4);
        imageList.add(R.drawable.image5);
        imageList.add(R.drawable.image6);
        imageList.add(R.drawable.image7);
        imageList.add(R.drawable.image8);
        imageList.add(R.drawable.image9);
        imageList.add(R.drawable.image10);
        imageList.add(R.drawable.image11);
    }

    private boolean isShowSubTabBar;
    private RecyclerViewHeader recyclerHeader;
    private RecyclerView recyclerView;

    public void isShowSubTabBar(boolean isShowSubTabBar) {
        this.isShowSubTabBar = isShowSubTabBar;
        if (recyclerHeader != null) {
            recyclerHeader.findViewById(R.id.sub_tab).setVisibility(isShowSubTabBar ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public RecyclerViewHeader getRecyclerHeader() {
        return recyclerHeader;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        index = getArguments().getInt("index", index);
        Log.d(TAG + index, "onCreateView");
        parentFragment = (TranslationHeaderParentFragment) getParentFragment();
        rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        int random = new Random().nextInt(4);
        final ArrayList<String> titleList = new ArrayList<>();
        for (int i = 0; i < random; i++) {
            titleList.add(String.valueOf((i + 1)));
        }
        final ImageRecyclerAdapter adapter = new ImageRecyclerAdapter(getActivity(), titleList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnChildScrollListener());
        recyclerHeader = RecyclerViewHeader.fromXml(getActivity(), R.layout.view_header);
        recyclerHeader.findViewById(R.id.sub_tab).setVisibility(isShowSubTabBar ? View.VISIBLE : View.GONE);
        recyclerHeader.attachTo(recyclerView);
        recyclerHeader.setTag(index);
        setScrollY(scrollY, scrollY, 0);
        return rootView;
    }
}
