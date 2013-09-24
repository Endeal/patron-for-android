package com.flashvip.listeners;

import com.flashvip.lists.ListScreens;

import com.flashvip.lists.ListSort;
import com.flashvip.main.Globals;
import com.flashvip.main.ProductSorter;
import com.flashvip.main.SortServers;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SortServerListener implements OnItemSelectedListener
{

	public void onItemSelected(AdapterView<?> adapter, View view, int position,
			long id)
	{
//		if (Globals.getCurrentScreen() == ListScreens.SCREEN_CHOOSE_BAR ||
//				Globals.getCurrentScreen() == ListScreens.SCREEN_SEARCH_SERVERS)
//		{
//			switch (position)
//			{
//			case ListSort.BY_NAME:
//				Globals.setLocations(SortServers.getByName(Globals.getServers(), true));
//				break;
//			case ListSort.BY_ADDRESS:
//				Globals.setLocations(SortServers.getByAddress(Globals.getServers(), true));
//				break;
//			case ListSort.BY_CITY:
//				Globals.setLocations(SortServers.getByCity(Globals.getServers(), true));
//				break;
//			case ListSort.BY_STATE:
//				Globals.setLocations(SortServers.getByState(Globals.getServers(), true));
//				break;
//			case ListSort.BY_ZIP:
//				Globals.setLocations(SortServers.getByZip(Globals.getServers(), true));
//				break;
//			case ListSort.BY_PHONE:
//				Globals.setLocations(SortServers.getByPhone(Globals.getServers(), true));
//				break;
//			}
//		}
//		else if (Globals.getCurrentScreen() == ListScreens.SCREEN_BROWSE_DRINKS ||
//					Globals.getCurrentScreen() == ListScreens.SCREEN_BAR_TOP ||
//					Globals.getCurrentScreen() == ListScreens.SCREEN_MY_TOP ||
//					Globals.getCurrentScreen() == ListScreens.SCREEN_SEARCH_DRINKS)
//		{
//			switch (position)
//			{
//			case ListSort.BY_NAME:
//				Globals.setProducts(ProductSorter.getByName(Globals.getProducts(), true));
//				break;
//			case ListSort.BY_PRICE:
//				Globals.setProducts(ProductSorter.getByPrice(Globals.getProducts(), true));
//				break;
//			case ListSort.BY_TYPE:
//				//Globals.setProducts(ProductSorter.getByType(Globals.getProducts(),  true));
//				break;
//			}
//		}
//		else if (Globals.getCurrentScreen() == ListScreens.SCREEN_TAB)
//		{
//		}
	}

	public void onNothingSelected(AdapterView<?> adapter)
	{
		
	}
}