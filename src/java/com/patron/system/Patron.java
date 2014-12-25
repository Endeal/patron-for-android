package com.patron.system;

import android.app.Application;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;

import com.parse.Parse;
import com.parse.PushService;

import com.patron.main.FlashLogin;
import com.patron.main.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Patron extends Application
{
	private static Context context;

	@Override
	public void onCreate()
	{
		super.onCreate();
		CalligraphyConfig.initDefault("fonts/Quicksand-Regular.otf", R.attr.fontPath);
		//context = this;
		context = getApplicationContext();
		Parse.initialize(this, "l6vtkQv0R6uPJrXSCn2fiAMRjMFrH6iSA3FQtvkN", "TFzHYXvM4LbuyfqtQsigWaz1j9stCuvcho1zZyge");
		// Also in this method, specify a default Activity to handle push notifications
  		PushService.setDefaultPushCallback(this, FlashLogin.class);
	}

	public static Context getContext()
	{
		return context;
	}
}
