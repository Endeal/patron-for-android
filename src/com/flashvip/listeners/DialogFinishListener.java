package com.flashvip.listeners;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.Toast;

import com.flashvip.db.AddOrderConnector;
import com.flashvip.lists.ListLinks;
import com.flashvip.main.R;

public class DialogFinishListener implements DialogInterface.OnClickListener
{
	private Activity activity;
	private ButtonFinishListener listener;
	
	public DialogFinishListener(Activity activity, ButtonFinishListener listener)
	{
		this.activity = activity;
		this.listener = listener;
	}
	
	public void onClick(DialogInterface dialog, int which) {/*
		switch (which){
		case DialogInterface.BUTTON_POSITIVE:
			activity.setContentView(R.layout.misc_loading);
			AddOrderConnector aoconnect = new AddOrderConnector(listener);
			URL url;
			try
			{
				url = new URL(ListLinks.LINK_ADD_ORDER);
				aoconnect.execute(url);
			}
			catch (MalformedURLException e)
			{
				Toast toast = Toast.makeText(activity, 
						"Connection error.", Toast.LENGTH_SHORT);
				toast.show();
			}
			break;

		case DialogInterface.BUTTON_NEGATIVE:
			break;
		}*/
	}
}
