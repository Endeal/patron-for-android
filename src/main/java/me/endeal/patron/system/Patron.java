package me.endeal.patron.system;

import android.app.Application;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;

import me.endeal.patron.main.FlashLogin;
import me.endeal.patron.R;
import me.endeal.patron.social.SocialExecutor;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Patron extends Application
{
	private static Context context;
    public static boolean DEBUGGING_OFFLINE = false;

	@Override
	public void onCreate()
	{
		super.onCreate();
        SocialExecutor.onCreate(this, "zzj56RtJAssnuE9sSUl1NoeeT", "4QOqpgbwzZ15SLGINwUI8RNLyT98c7pZRbW3K7xt96aPx6mRdE");
		CalligraphyConfig.initDefault("fonts/Quicksand-Regular.otf", R.attr.fontPath);
		//context = this;
		context = getApplicationContext();
		//Parse.initialize(this, "l6vtkQv0R6uPJrXSCn2fiAMRjMFrH6iSA3FQtvkN", "TFzHYXvM4LbuyfqtQsigWaz1j9stCuvcho1zZyge");
		// Also in this method, specify a default Activity to handle push notifications
  		//PushService.setDefaultPushCallback(this, FlashLogin.class);
	}

	public static Context getContext()
	{
		return context;
	}
}
