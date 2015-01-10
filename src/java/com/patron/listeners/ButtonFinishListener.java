package com.patron.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.patron.listeners.OnApiExecutedListener;
import com.patron.main.FlashCart;
import com.patron.main.FlashCodes;
import com.patron.system.ApiExecutor;
import com.patron.system.Globals;

public class ButtonFinishListener implements OnClickListener
{
    @Override
	public void onClick(View tempView)
	{
        final View view = tempView;
        ApiExecutor apiExecutor = new ApiExecutor();
        apiExecutor.addOrder(Globals.getOrder(), view.getContext(), new OnApiExecutedListener() {
            @Override
            public void onExecuted()
            {
                Activity activity = (Activity)view.getContext();
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, view, "buttonFinish");
                Bundle bundle = options.toBundle();
                Intent intent = new Intent(activity, FlashCodes.class);
                ActivityCompat.startActivity(activity, intent, bundle);
                activity.finish();
            }
        });
	}
}
