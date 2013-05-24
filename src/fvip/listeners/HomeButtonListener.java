package fvip.listeners;

import fvip.lists.ListScreens;

import fvip.main.Globals;
import fvip.main.FlashClient;
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
