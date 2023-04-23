package com.ftn.slagalica.util;

import android.content.Context;
import android.util.AttributeSet;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

// Possible workaround for when viewPager throws NullExc, due to fragment being
// inflated before getView returns (so viewPager isn't attached to parent yet)
// This class is lazily setting the previously stored adapter only once it's been attached to Window
// (in src code, use storeAdapter instead of setAdapter & TabsViewPager instead of ViewPager)
public class TabsViewPager extends ViewPager {
    PagerAdapter mPagerAdapter;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPagerAdapter != null) {
            super.setAdapter(mPagerAdapter);
//            mPageIndicator.setViewPager(this);
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
    }

    public void storeAdapter(PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
    }

    public TabsViewPager(Context context) {
        super(context);
    }

    public TabsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}