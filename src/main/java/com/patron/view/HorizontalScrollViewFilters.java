package com.patron.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.HorizontalScrollView;
import android.view.View;

public class HorizontalScrollViewFilters extends HorizontalScrollView
{
    private ImageView imageViewHelpCategories;

    public HorizontalScrollViewFilters(Context context)
    {
        super(context);
    }

    public HorizontalScrollViewFilters(Context context, AttributeSet attributes)
    {
        super(context, attributes);
    }

    public HorizontalScrollViewFilters(Context context, AttributeSet attributes, int style)
    {
        super(context, attributes, style);
    }

    public void setImage(ImageView image)
    {
        this.imageViewHelpCategories = image;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        System.out.println("SCROLLED!");
        if (imageViewHelpCategories != null)
        {
            System.out.println("INVISIBLE");
            imageViewHelpCategories.setVisibility(View.GONE);
        }
        System.out.println("DONE!");
    }
}
