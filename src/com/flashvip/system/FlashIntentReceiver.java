package com.flashvip.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.flashvip.db.DataConnector;
import com.flashvip.main.FlashScan;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class FlashIntentReceiver extends BroadcastReceiver implements Loadable
{
	private String orderId;
	private String msg; 

	@Override   
	public void onReceive(Context context, Intent intent)
	{   
		this.orderId = intent.getExtras().getString("orderId");
		String action = intent.getAction();   

		// if a notification is received ...   
		if (action.equals(PushManager.ACTION_PUSH_RECEIVED))
		{
			//beginLoading();
			System.out.println(msg + ": 1");
		}
		// if the notification is opened/clicked ...
		else if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED))
		{
			beginLoading();
			System.out.println(msg + ": 2");
			
			// here you can get the extras from the intent.   
			// and then you can use it as you wish.   
//			msg = intent.getStringExtra(PushManager.EXTRA_ALERT);   

			// for example, you can start an activity and send the msg as an extra.   
//			Intent launch = new Intent(Intent.ACTION_MAIN);   
//			launch.setClass(UAirship.shared().getApplicationContext(), FlashCodes.class);   
//			launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
//			launch.putExtra("notification", msg);   

//			UAirship.shared().getApplicationContext().startActivity(launch);   

			// save the APID in a shared preferences ...   
			// it'll be sent to the server ...   
		}
		else if (action.equals(PushManager.ACTION_REGISTRATION_FINISHED))
		{   
			//beginLoading();
			System.out.println(msg + ": 3");
			// to log the APID ...  
			Log.i("Log", "Registration complete. APID:"  
					+ intent.getStringExtra(PushManager.EXTRA_APID)  
					+ ". Valid: "  
					+ intent.getBooleanExtra(  
							PushManager.EXTRA_REGISTRATION_VALID, false));  

			// if registration is successful ...   
			if(intent.getBooleanExtra(PushManager.EXTRA_REGISTRATION_VALID, false))
			{
				// Do whatever you want ....   
			}   
			else
			{   
				// register again ...   
			}   
		}
	}
	
	public void beginLoading()
	{
		load();
	}
	
	public void load()
	{
		System.out.println("orderId: " + orderId);
		if (Globals.getCodes() == null)
			System.out.println("NULL CODES");
		if (Globals.getVendor() == null)
			System.out.println("NULL VENDOR");
		if (orderId != null &&
				(Globals.getCodes() == null ||
				Globals.getVendor() == null))
		{
			DataConnector dataConnector = new DataConnector(this);
			dataConnector.execute(orderId);
			System.out.println("DATA CONNECTOR FOR VENDOR: " + orderId);
		}
		else
		{
			endLoading();
		}
	}
	
	public void endLoading()
	{
		Intent scanIntent = new Intent(UAirship.shared().getApplicationContext(), FlashScan.class);
		scanIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		System.out.println("ENDED LOADING ORDER: " + orderId);
		if (orderId != null &&
				Globals.getCodes() != null &&
				!Globals.getCodes().isEmpty())
		{
			for (int i = 0; i < Globals.getCodes().size(); i++)
			{
				if (Globals.getCodes().get(i).getOrder().getOrderId().equals(orderId))
				{
					scanIntent.putExtra("orderRow", "" + i);
					System.out.println("PUT DOWN ORDER FOR ROW: " + i);
					break;
				}
			}
		}
		UAirship.shared().getApplicationContext().startActivity(scanIntent);
	}
	
	public void update()
	{
		
	}
	
	public void message(String msg)
	{
		
	}
}  