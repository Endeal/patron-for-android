package com.patron.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.appboy.AppboyGcmReceiver;

import com.patron.main.FlashScan;

public class FlashIntentReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{

        String packageName = context.getPackageName();
        String pushReceivedAction = packageName + ".intent.APPBOY_PUSH_RECEIVED";
        String pushOpenedAction = packageName + ".intent.APPBOY_NOTIFICATION_OPENED";
		String action = intent.getAction();

        if (action.equals(pushOpenedAction))
        {
            String orderId = intent.getExtras().getString("orderId");
            System.out.println(orderId);
            Bundle extras = new Bundle();
            extras.putString("orderId", orderId);
            Intent scanIntent = new Intent(context, FlashScan.class);
            context.startActivity(scanIntent, extras);
        }
    }
}
