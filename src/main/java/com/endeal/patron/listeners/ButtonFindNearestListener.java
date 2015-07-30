package com.endeal.patron.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.View.OnClickListener;

import com.endeal.patron.activity.MenuActivity;
import com.endeal.patron.system.Globals;

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
        Intent intent = new Intent(activity, MenuActivity.class);
        //ActivityCompat.startActivity(activity, intent, bundle);
        view.getContext().startActivity(intent);
        activity.finish();
    }
}
