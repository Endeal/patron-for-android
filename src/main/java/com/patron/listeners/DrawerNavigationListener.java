package com.patron.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.SimpleDrawerListener;
import android.view.View;

public class DrawerNavigationListener extends SimpleDrawerListener
{
    private Activity activity;
    private Intent intent;

    public DrawerNavigationListener(Activity activity)
    {
        this.activity = activity;
    }

    public void setIntent(Intent intent)
    {
        this.intent = intent;
    }

    @Override
    public void onDrawerClosed(View view)
    {
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

    }

    @Override
    public void onDrawerSlide(View view, float offset)
    {

    }

    @Override
    public void onDrawerStateChanged(int newState)
    {

    }
}
