package com.flashvip.listeners;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.flashvip.main.FlashCart;
import com.flashvip.system.Globals;

public class ButtonFinishListener implements View.OnClickListener
{
    
	FlashCart activity;
	
	public ButtonFinishListener(FlashCart activity)
	{
		this.activity = activity;
	}
	
	public void onClick(View view)
	{
		if (Globals.getOrder() != null &&
				Globals.getOrder().getFragments() != null &&
				!Globals.getOrder().getFragments().isEmpty())
		{
			Toast success = Toast.makeText(view.getContext(),
					"Success",
					Toast.LENGTH_SHORT);
			success.show();
		}
		else
		{
			Toast error = Toast.makeText(view.getContext(),
					"No drinks added to tab yet.",
					Toast.LENGTH_SHORT);
			error.show();
		}
	}
}
