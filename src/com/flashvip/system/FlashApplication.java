package com.flashvip.system;

import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

import android.app.Application;
import android.content.Context;

public class FlashApplication extends Application
{
	private static Context context;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		context = this;
		
		AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
        UAirship.takeOff(this, options);
        PushManager.enablePush();
        String apid = PushManager.shared().getAPID();
        Globals.setDeviceId(apid);
        Logger.info("My Application onCreate - App APID: " + apid);
		PushManager.shared().setIntentReceiver(FlashIntentReceiver.class);
	}
	
	public static Context getContext()
	{
		return context;
	}
}
