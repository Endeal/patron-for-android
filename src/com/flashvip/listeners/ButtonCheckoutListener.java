package com.flashvip.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.flashvip.main.FlashCart;

public class ButtonCheckoutListener implements OnClickListener
{
	Activity activity;
	
	public ButtonCheckoutListener(Activity activity)
	{
		this.activity = activity;
	}
	
	public void onClick(View v)
	{
		Intent intent = new Intent(activity, FlashCart.class);
		activity.startActivity(intent);
	}
}
