package com.flashvip.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.flashvip.main.FlashHome;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class NotificationsReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context aContext, Intent aIntent) {

		String action = aIntent.getAction();
		if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {
			/* User opened the notification so we launch the application.
				This intent is what will be used to launch the activity
				in our application */
			Intent lLaunch = new Intent(Intent.ACTION_MAIN);

			/* Main.class can be substituted any activity in your android
				project that you wish to be launched when the user selects
				the notification from the Notifications drop down */
			lLaunch.setClass(UAirship.shared().getApplicationContext(),
					FlashHome.class);
			lLaunch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			/* Copy the intent data from the incoming intent to the intent
				that we are going to launch */
			lLaunch.setData(aIntent.getData());
			UAirship.shared().getApplicationContext().startActivity(lLaunch);
		}
		// Push notification received.
		else if(action.equals(PushManager.ACTION_PUSH_RECEIVED))
		{
		}

	}
}
