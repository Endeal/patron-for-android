package com.patron.view;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.widget.TextView;

public class NavigationDrawerLayout extends DrawerLayout
{
    public NavigationDrawerLayout(Context context)
    {
        super(context);
        init();
    }

    public NavigationDrawerLayout(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init();
    }

    public NavigationDrawerLayout(Context context, AttributeSet attributeSet, int i)
    {
        super(context, attributeSet, i);
        init();
    }

    public void init()
    {
        TextView textAppTitle = new TextView(getContext());
        textAppTitle.setText("Flash VIP");
        //addView(textAppTitle);
    }
}
