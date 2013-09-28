package com.flashvip.listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.flashvip.main.FlashCart;
import com.flashvip.main.FlashHome;
import com.flashvip.main.Globals;

public class ButtonFinishListener implements View.OnClickListener
{
	FlashCart activity;
	
	public ButtonFinishListener(FlashCart activity)
	{
		this.activity = activity;
	}
	
	public void onClick(View view)
	{
		if (Globals.getCartProducts() != null &&
				!Globals.getCartProducts().isEmpty())
		{
			OnClickListener dialogClickListener =new DialogFinishListener(activity, this);
			AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
			builder.setMessage("Are you sure?").setPositiveButton("Yes",
					dialogClickListener).setNegativeButton("No", dialogClickListener).show();
		}
		else
		{
			Toast error = Toast.makeText(view.getContext(),
					"No drinks added to tab yet.",
					Toast.LENGTH_SHORT);
			error.show();
		}
	}
	
	public void orderFinished()
	{
		Intent intent = new Intent(activity, FlashHome.class);
		activity.startActivity(intent);
		
		Toast toast = Toast.makeText(activity,
				"Order placed.",
				Toast.LENGTH_SHORT);
		toast.show();
	}

	public Activity getActivity()
	{
		return activity;
	}
}
