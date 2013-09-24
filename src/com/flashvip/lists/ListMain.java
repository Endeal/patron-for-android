package com.flashvip.lists;

import java.util.ArrayList;

import java.util.Collections;

import com.flashvip.main.Globals;

public class ListMain
{
	public static ArrayList<String> items;
	
	public static ArrayList<String> getItemSet(int set)
	{
		items = new ArrayList<String>();
		int counter;
		switch (set)
		{
		case ListScreens.SCREEN_MAIN: // Main Screen
			items.add("Choose Bar");
			items.add("Browse Drinks");
			items.add("Bar's Top 7");
			items.add("My Top 7");
			items.add("Add Random Drink to Tab");
			items.add("Purchase Codes");
			items.add("Settings");
			break;
		case ListScreens.SCREEN_CHOOSE_BAR: // Choose Bar Screen
			counter = Globals.getServers().size();
			for (int i = 0; i < counter; i++)
			{
				items.add(Globals.getServers().get(i).getName());
			}
			Collections.sort(items);
			break;
		case ListScreens.SCREEN_BROWSE_DRINKS: // Browse Drinks Screen
			counter = Globals.getProducts().size();
			for (int i = 0; i < counter; i++)
			{
				items.add(Globals.getProducts().get(i).getName());
			}
			Collections.sort(items);
			break;
		case ListScreens.SCREEN_SEARCH_SERVERS: // Search Servers Screen
			counter = Globals.getSearchServers().size();
			for (int i = 0; i < counter; i++)
			{
				items.add(Globals.getSearchServers().get(i).getName());
			}
			Collections.sort(items);
			break;
		case ListScreens.SCREEN_SEARCH_DRINKS: // Search Drinks Screen
			counter = Globals.getSearchOrders().size();
			for (int i = 0; i < counter; i++)
			{
				items.add(Globals.getSearchOrders().get(i).getName());
			}
			Collections.sort(items);
			break;
		case ListScreens.SCREEN_BAR_TOP: // Server's top drinks Screen
			break;
		case ListScreens.SCREEN_MY_TOP: // My top drinks Screen
			break; 
		case ListScreens.SCREEN_TAB: // The Tab Screen
			for (int i = 0; i < Globals.getTabProducts().size(); i++)
			{
				items.add(Globals.getTabProducts().get(i).getDrink().getName());
			}
			Collections.sort(items);
			break;
		case ListScreens.SCREEN_CODES: // The Purchased QR Codes Screen
			for (int i = 0; i < Globals.getOrderCodes().size(); i++)
			{
				items.add(Globals.getOrderCodes().get(i));
			}
		}
		return items;
	}
	
	public static ArrayList<String> getItems(int set)
	{
		return getItemSet(set);
	}
}
