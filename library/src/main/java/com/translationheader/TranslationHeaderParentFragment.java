package com.translationheader;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by raychum on 9/6/15.
 */
public abstract class TranslationHeaderParentFragment extends Fragment {

    private static final String TAG = TranslationHeaderParentFragment.class.getSimpleName();
    private final TranslationHeaderChildFragment.OnScrollListener onScrollListener = new TranslationHeaderChildFragment.OnScrollListener() {
        @Override
        public void onScrolled(TranslationHeaderChildFragment translationHeaderChildFragment, int x, int y) {
            int currentPage = getViewPager().getCurrentItem();
            final TranslationHeaderChildFragment currentPageFragment = ((TranslationHeaderChildFragment) getAdapter().getItem(currentPage));
            Log.d(TAG, "currentPage= " + currentPage + ", onScrolled=" + y);
            if (translationHeaderChildFragment.equals(currentPageFragment)) {
                translateHeader(y, 0);
            }
        }
    };

    public abstract boolean isSetToolbarBackgroundAlpha();

    public TranslationHeaderChildFragment.OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    @NonNull
    public abstract FragmentPagerAdapter getAdapter();

    @NonNull
    public abstract View getHeader();

    @NonNull
    public abstract ViewPager getViewPager();

    private void setToolbarBackgroundAlpha(float y) {
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (isSetToolbarBackgroundAlpha() && actionBar != null) {
            float alpha = Math.min(1, y / getMaxTranslationY());
            Log.d(TAG, "setToolbarBackgroundAlpha=" + alpha);
            actionBar.setBackgroundDrawable(new ColorDrawable(getColorWithAlpha(alpha, getResources().getColor(R.color.primary))));
        }
    }

    public void translateHeader(int y, long duration) {
        final int translationY;
        translationY = Math.min(y, getMaxTranslationY());
        getHeader().animate().cancel();
        getHeader().animate().setDuration(duration).translationY(-translationY);
        Log.d(TAG, "translateHeader headerTranslationY=" + -translationY);
        setToolbarBackgroundAlpha(y);
    }

    public abstract int getMaxTranslationY();

    private int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    private enum Direction {
        left, right
    }

    public class OnParentPageChangeListener implements ViewPager.OnPageChangeListener {

        private boolean isDragging;
        private int currentPage = 0;
        private float positionOffset = -1;
        private Integer headerTranslationY = null;
        private Integer currentFragmentScrollY = null;
        private Integer targetFragmentScrollY = null;
        private Direction direction = null;

        @Override
        public void onPageScrolled(int i, float v, int i2) {
            Log.d(TAG, "onPageScrolled=" + i);
            Log.d(TAG, "onPageScrolled positionOffset=" + v);
            if (isDragging) {
                if (positionOffset < 0) {
                    positionOffset = v;
                } else {
                    if (v != positionOffset && v > 0) {
                        final TranslationHeaderChildFragment currentFragment;
                        final TranslationHeaderChildFragment targetFragment;
                        // scrolling to right
                        if (v > positionOffset) {
                            currentFragment = ((TranslationHeaderChildFragment) getAdapter().getItem(i));
                            targetFragment = ((TranslationHeaderChildFragment) getAdapter().getItem(i + 1));
                            if (!Direction.right.equals(direction)) {
                                direction = Direction.right;
                                initValues(currentFragment, targetFragment);
                            }
                            // scrolling to left
                        } else {
                            currentFragment = ((TranslationHeaderChildFragment) getAdapter().getItem(i + 1));
                            targetFragment = ((TranslationHeaderChildFragment) getAdapter().getItem(i));
                            if (!Direction.left.equals(direction)) {
                                direction = Direction.left;
                                initValues(currentFragment, targetFragment);
                            }
                        }
                        int headerTranslationDY = getTranslationDY(direction, v);
                        Log.d(TAG, "onPageScrolled scrolling to " + (direction == Direction.right ? "right" : "left"));
                        Log.d(TAG, "onPageScrolled ###### currentFragmentScrollY=" + currentFragmentScrollY);
                        Log.d(TAG, "onPageScrolled ###### targetFragmentScrollY=" + targetFragmentScrollY);
                        Log.d(TAG, "onPageScrolled ###### headerTranslationY=" + headerTranslationY);
                        Log.d(TAG, "onPageScrolled ###### headerTranslationDY=" + headerTranslationDY);
                        targetFragment.getRecyclerHeader().translateHeader(headerTranslationDY, 0);
                        currentFragment.getRecyclerHeader().translateHeader(headerTranslationDY, 0);
                        translateHeader(headerTranslationDY, 0);
                        positionOffset = v;
                    }
                }
            } else {
                translateHeader(getMaxTranslationY(), 0);
                final TranslationHeaderChildFragment fragment = ((TranslationHeaderChildFragment) getAdapter().getItem(currentPage));
                fragment.setScrollY(getMaxTranslationY(), getMaxTranslationY(), 0);
            }
        }

        private int getTranslationDY(Direction direction, float offset) {
            int translationDY;
            if (currentFragmentScrollY > targetFragmentScrollY) {
                offset = direction == Direction.right ? (1 - offset) : offset;
                translationDY = Math.round(headerTranslationY * offset + Math.abs(targetFragmentScrollY));
            } else {
                offset = direction == Direction.right ? offset : (1 - offset);
                translationDY = Math.round(headerTranslationY * offset + Math.abs(currentFragmentScrollY));
                if (translationDY == headerTranslationY - 1) {
                    translationDY = headerTranslationY;
                }
            }
            translationDY = Math.min(translationDY, getMaxTranslationY());
            return translationDY;
        }

        private void initValues(TranslationHeaderChildFragment currentFragment, TranslationHeaderChildFragment targetFragment) {
            currentFragmentScrollY = Math.round(currentFragment.getScrollY());
            targetFragmentScrollY = Math.round(targetFragment.getScrollY());
            headerTranslationY = Math.abs(currentFragmentScrollY - targetFragmentScrollY);
            headerTranslationY = Math.min(headerTranslationY, getMaxTranslationY());
        }

        private void resetValues() {
            headerTranslationY = null;
            currentFragmentScrollY = null;
            targetFragmentScrollY = null;
            positionOffset = -1;
            isDragging = false;
            direction = null;
        }

        @Override
        public void onPageSelected(int i) {
            Log.d(TAG, "onPageSelected prevPage=" + currentPage);
            Log.d(TAG, "onPageSelected=" + i);
            currentPage = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {
            Log.d(TAG, "onPageScrollStateChanged=" + i);
            if (i != ViewPager.SCROLL_STATE_IDLE) {
                if (!isDragging) {
                    isDragging = i == ViewPager.SCROLL_STATE_DRAGGING;
                }
            } else {
                resetValues();
            }
            Log.d(TAG, "onPageScrollStateChanged isDragging=" + isDragging);
        }
    }
}