package com.flashvip.listeners;

import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Toast;
import com.flashvip.lists.ListScreens;
import com.flashvip.main.FlashClient;
import com.flashvip.main.Globals;

public class ButtonDeleteListener implements OnClickListener
{	
	public void onClick(View v)
	{
		String position = (String)v.getTag();
		if (Globals.getTabDrinks().size() <= 1 &&
				Globals.getCurrentScreen() == ListScreens.SCREEN_TAB)
			Globals.setCurrentScreen(ListScreens.SCREEN_MAIN);
		Globals.removeOrderFromTab(Integer.parseInt(position));
		FlashClient.updateAll();
		Toast toast = Toast.makeText(Globals.getContext(),
				"Drink removed from tab.", Toast.LENGTH_SHORT);
		toast.show();
	}
}