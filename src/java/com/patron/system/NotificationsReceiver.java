package com.patron.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.patron.main.FlashHome;

public class NotificationsReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context aContext, Intent aIntent)
	{
		String action = aIntent.getAction();
	}
}
