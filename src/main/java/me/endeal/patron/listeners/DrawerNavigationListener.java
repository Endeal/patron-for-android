package com.endeal.patron.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.SimpleDrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;

import com.endeal.patron.activity.MenuActivity;
import com.endeal.patron.activity.ReviewActivity;
import com.endeal.patron.R;

public class DrawerNavigationListener extends ActionBarDrawerToggle
{
    private Activity activity;
    private Intent intent;

    public DrawerNavigationListener(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes)
    {
        super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.activity = activity;
        this.intent = null;
    }

    public DrawerNavigationListener(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes)
    {
        super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.activity = activity;
        this.intent = null;
    }

    public void setIntent(Intent intent)
    {
        this.intent = intent;
    }

    @Override
    public void onDrawerClosed(View view)
    {
        super.onDrawerClosed(view);
        if (intent != null)
        {
            Context context = view.getContext();
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, view, "NavigationListItem");
            Bundle bundle = options.toBundle();
            activity.finish();
            //ActivityCompat.startActivity(activity, intent, bundle);
            activity.startActivity(intent);
            intent = null;
        }
    }

    @Override
    public void onDrawerOpened(View view)
    {
        super.onDrawerOpened(view);
    }

    @Override
    public void onDrawerSlide(View view, float offset)
    {
        super.onDrawerSlide(view, offset);
        Activity activity = (Activity)view.getContext();
        FloatingActionButton fab;
        float alpha = 1 - offset;
        if (activity.getClass() == MenuActivity.class)
        {
            fab = (FloatingActionButton)activity.findViewById(R.id.menuFloatingActionButtonFilter);
            fab.setAlpha(alpha);
        }
        else if (activity.getClass() == ReviewActivity.class)
        {
            fab = (FloatingActionButton)activity.findViewById(R.id.reviewFloatingActionButtonPay);
            fab.setAlpha(alpha);
        }
    }

    @Override
    public void onDrawerStateChanged(int newState)
    {
        super.onDrawerStateChanged(newState);
    }
}
