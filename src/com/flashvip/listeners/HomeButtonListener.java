package com.flashvip.listeners;

import com.flashvip.lists.ListScreens;
import com.flashvip.main.Globals;
import com.flashvip.main.FlashClient;
import android.view.View;
import android.view.View.OnClickListener;

public class HomeButtonListener implements OnClickListener
{

	public void onClick(View v)
	{
		if (Globals.getCurrentScreen() != ListScreens.SCREEN_MAIN)
		{
			Globals.setCurrentScreen(ListScreens.SCREEN_MAIN);
			FlashClient.updateAll();
		}
	}
	
}
