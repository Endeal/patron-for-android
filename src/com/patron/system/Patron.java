package com.patron.system;

import com.parse.Parse;
import com.parse.PushService;

import android.app.Application;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;

import com.noveogroup.android.log.Log;

import com.patron.main.FlashLogin;

public class Patron extends Application
{
	private static Context context;
	/**
	Initialize the library with your Mixpanel project token, MIXPANEL_TOKEN, and a reference
 	to your application context.
 	*/

	@Override
	public void onCreate()
	{
		super.onCreate();
		context = this;
		Parse.initialize(this, "l6vtkQv0R6uPJrXSCn2fiAMRjMFrH6iSA3FQtvkN", "TFzHYXvM4LbuyfqtQsigWaz1j9stCuvcho1zZyge");
		// Also in this method, specify a default Activity to handle push notifications
  		PushService.setDefaultPushCallback(this, FlashLogin.class);
	}

	public static Context getContext()
	{
		return context;
	}
}
