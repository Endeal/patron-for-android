package com.flashvip.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.flashvip.main.FlashCart;
import com.flashvip.main.Globals;

public class ButtonCheckoutListener implements OnClickListener
{
	Activity activity;
	
	public ButtonCheckoutListener(Activity activity)
	{
		this.activity = activity;
	}
	
	public void onClick(View v)
	{
		if (Globals.getOrder() != null &&
				Globals.getOrder().getFragments() != null &&
				!Globals.getOrder().getFragments().isEmpty())
		{
			Intent intent = new Intent(activity, FlashCart.class);
			activity.startActivity(intent);
		}
		else
		{
			Toast toast = Toast.makeText(v.getContext(),
					"No items have been added to your cart.",
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
