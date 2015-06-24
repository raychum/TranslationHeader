package com.translationheader;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by raychum on 9/6/15.
 */
public abstract class TranslationHeaderChildFragment extends Fragment {
    private static final String TAG = TranslationHeaderChildFragment.class.getSimpleName();
    protected TranslationHeaderParentFragment parentFragment;
    protected View rootView;
    protected int scrollY = 0;
    protected int scrollX = 0;
    protected int index = -1;

    public abstract RecyclerViewHeader getRecyclerHeader();

    public abstract RecyclerView getRecyclerView();

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int desireScrollY, int desireTranslateHeaderY, int translateDuration) {
        Log.d(TAG + index, "setScrollY desireScrollY=" + desireScrollY);
        if (getRecyclerView() != null && getRecyclerHeader() != null && getRecyclerView().getLayoutManager() != null) {
            Log.d(TAG + index, "scrollToPosition=" + scrollY);
            scrollY = desireScrollY - scrollY;
            getRecyclerView().scrollBy(scrollX, scrollY);
            scrollY = getRecyclerView().computeVerticalScrollOffset();
            Log.d(TAG + index, "setScrollY scrollY=" + scrollY);
            desireTranslateHeaderY = Math.min(desireTranslateHeaderY, scrollY);
            getRecyclerHeader().translateHeader(desireTranslateHeaderY, translateDuration);
            parentFragment.getOnScrollListener().onScrolled(this, scrollX, desireTranslateHeaderY);
        }
    }

    public interface OnScrollListener {
        void onScrolled(TranslationHeaderChildFragment translationHeaderChildFragment, int x, int y);
    }

    public class OnChildScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            parentFragment.getOnScrollListener().onScrolled(TranslationHeaderChildFragment.this, scrollX += dx, scrollY += dy);
            int maxTranslationY = parentFragment.getMaxTranslationY();
            getRecyclerHeader().translateHeader(Math.min(scrollY, maxTranslationY), 0);
            Log.d(TAG + index, "onScrolled scrollY=" + Math.min(scrollY, maxTranslationY));
        }
    }
}
