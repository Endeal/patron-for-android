package com.endeal.patron.system;

import android.app.Application;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;

import com.endeal.patron.activity.LoginActivity;
import com.endeal.patron.activity.OrdersActivity;
import com.endeal.patron.R;
import com.endeal.patron.social.SocialExecutor;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class PatronApplication extends Application
{
	private static Context context;
    public static boolean DEBUGGING_OFFLINE = false;
    public static OrdersActivity ordersActivity;

	@Override
	public void onCreate()
	{
		super.onCreate();
        SocialExecutor.onCreate(this, "zzj56RtJAssnuE9sSUl1NoeeT", "4QOqpgbwzZ15SLGINwUI8RNLyT98c7pZRbW3K7xt96aPx6mRdE");
		CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf", R.attr.fontPath);
		context = getApplicationContext();
	}

	public static Context getContext()
	{
		return context;
	}

    public static void ordersActivityResumed(OrdersActivity ordersActivity)
    {
        PatronApplication.ordersActivity = ordersActivity;
    }

    public static void ordersActivityPaused()
    {
        PatronApplication.ordersActivity = null;
    }

    public static boolean isOrdersActivityVisible()
    {
        return (PatronApplication.ordersActivity != null);
    }
}
