package com.patron.system;

import com.parse.Parse;
import com.parse.PushService;

import android.app.Application;
import android.content.Context;

import com.patron.main.FlashScan;

public class Patron extends Application
{
	private static Context context;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		context = this;
		Parse.initialize(this, "l6vtkQv0R6uPJrXSCn2fiAMRjMFrH6iSA3FQtvkN", "TFzHYXvM4LbuyfqtQsigWaz1j9stCuvcho1zZyge");

		// Also in this method, specify a default Activity to handle push notifications
  		PushService.setDefaultPushCallback(this, FlashScan.class);
	}
	
	public static Context getContext()
	{
		return context;
	}
}
