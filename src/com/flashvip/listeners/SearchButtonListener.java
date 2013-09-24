package com.flashvip.listeners;

import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Toast;
import com.flashvip.lists.ListScreens;
import com.flashvip.main.Globals;
import com.flashvip.main.FlashClient;


public class SearchButtonListener implements OnClickListener
{

	public void onClick(View view)
	{
		if (Globals.getCurrentScreen() == ListScreens.SCREEN_CHOOSE_BAR ||
				Globals.getCurrentScreen() == ListScreens.SCREEN_SEARCH_SERVERS)
		{
			if (Globals.getServers() != null &&
					!Globals.getServers().isEmpty())
			{
				if (Globals.getCurrentScreen() != ListScreens.SCREEN_SEARCH_SERVERS)
					Globals.setCurrentScreen(ListScreens.SCREEN_SEARCH_SERVERS);
				FlashClient.updateListViewAdapter();
			}
			else
			{
				Toast error = Toast.makeText(Globals.getContext(),
						"Failed to retrieve list of servers.",
						Toast.LENGTH_SHORT);
				error.show();
			}
		}
		else
		{
			if (Globals.getProducts() != null &&
					!Globals.getProducts().isEmpty())
			{
				if (Globals.getCurrentScreen() != ListScreens.SCREEN_SEARCH_DRINKS)
					Globals.setCurrentScreen(ListScreens.SCREEN_SEARCH_DRINKS);
				FlashClient.updateListViewAdapter();
			}
			else
			{
				Toast error = Toast.makeText(Globals.getContext(),
						"Failed to retrieve list of drinks.",
						Toast.LENGTH_SHORT);
				error.show();
			}
		}
	}
	
}
