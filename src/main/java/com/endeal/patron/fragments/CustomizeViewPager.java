package com.endeal.patron.fragments;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.MeasureSpec;
import android.util.AttributeSet;

public class CustomizeViewPager extends ViewPager
{
    PagerAdapter mPagerAdapter;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        /*
        if (mPagerAdapter != null) {
            super.setAdapter(mPagerAdapter);
            //mPageIndicator.setViewPager(this);
        }
        */
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        //mPagerAdapter = adapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        /*
        int height = 0;
        for(int i = 0; i < getChildCount(); i++)
        {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        */
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public CustomizeViewPager(Context context) {
        super(context);
    }

    public CustomizeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
