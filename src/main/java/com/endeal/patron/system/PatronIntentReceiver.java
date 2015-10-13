package com.endeal.patron.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.util.Log;

import com.appboy.AppboyGcmReceiver;

import com.endeal.patron.activity.LoginActivity;
import com.endeal.patron.activity.OrdersActivity;
import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.model.ApiResult;

public class PatronIntentReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(final Context context, Intent intent)
	{
        String packageName = context.getPackageName();
        String pushReceivedAction = packageName + ".intent.APPBOY_PUSH_RECEIVED";
        String pushOpenedAction = packageName + ".intent.APPBOY_NOTIFICATION_OPENED";
        String action = intent.getAction();
        if (action.equals(pushOpenedAction))
        {
            Bundle extras = intent.getExtras().getBundle("extra");
            final String orderId = extras.getString("orderId");

            if (orderId == null || orderId.length() == 0)
            {
                Intent loginIntent = new Intent(context.getApplicationContext(), LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(loginIntent);
                return;
            }

            // Go to order screen and open order dialog if there is a patron
            if (Globals.getPatron() != null)
            {
                Intent orderIntent = new Intent(context.getApplicationContext(), OrdersActivity.class);
                orderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                orderIntent.putExtra("orderId", orderId);
                context.startActivity(orderIntent);
            }
            else
            {
                if (Globals.getCredential() != null && Globals.getCredential().getIdentifier() != null &&
                        Globals.getCredential().getVerifier() != null &&
                        !Globals.getCredential().getIdentifier().equals("") &&
                        !Globals.getCredential().getVerifier().equals(""))
                {
                    ApiExecutor api = new ApiExecutor();
                    api.login(Globals.getCredential(), new OnApiExecutedListener() {
                        @Override
                        public void onExecuted(ApiResult result)
                        {
                            if (Globals.getPatron() == null)
                            {
                                Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            }
                            else
                            {
                                Intent orderIntent = new Intent(context.getApplicationContext(), OrdersActivity.class);
                                orderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                orderIntent.putExtra("orderId", orderId);
                                context.startActivity(orderIntent);
                            }
                        }
                    });
                }
            }
        }
        else if (action.equals(pushReceivedAction))
        {
            try
            {
                // Play the notification sound upon receiving.
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
                ringtone.play();

                // Refresh the orders page if on it already
                Bundle extras = intent.getExtras().getBundle("extra");
                final String orderId = extras.getString("orderId");
                if (PatronApplication.isOrdersActivityVisible())
                {
                    Intent orderIntent = new Intent(context.getApplicationContext(), OrdersActivity.class);
                    orderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    orderIntent.putExtra("orderId", orderId);
                    context.startActivity(orderIntent);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
