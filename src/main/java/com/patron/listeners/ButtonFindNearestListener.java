package com.patron.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.View.OnClickListener;

import com.patron.main.FlashMenu;
import com.patron.system.Globals;

public class ButtonFindNearestListener implements OnClickListener
{
    @Override
    public void onClick(View view)
    {
        Globals.setVendor(null);
        Activity activity = (Activity)view.getContext();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity, view, "buttonFindNearest");
        Bundle bundle = options.toBundle();
        Intent intent = new Intent(activity, FlashMenu.class);
        ActivityCompat.startActivity(activity, intent, bundle);
        activity.finish();
    }
}