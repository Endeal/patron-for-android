package com.flashvip.listeners;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.flashvip.main.FlashCart;
import com.flashvip.main.Globals;
import com.flashvip.model.Fragment;

public class ButtonRemoveListener implements OnClickListener
{	
	private FlashCart activity;
	private Fragment fragment;
	
	public ButtonRemoveListener(FlashCart activity, Fragment fragment)
	{
		this.activity = activity;
		this.fragment = fragment;
	}
	
	public void onClick(View v)
	{
		if (Globals.getOrder() != null &&
				Globals.getOrder().getFragments() != null)
		{
			Globals.getOrder().getFragments().remove(fragment);
			Toast toast = Toast.makeText(activity,
					"Item removed from order.", Toast.LENGTH_SHORT);
			toast.show();
		}
		activity.updateListViewCart();
	}
}