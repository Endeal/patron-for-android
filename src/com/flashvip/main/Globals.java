package com.flashvip.main;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;

public class Globals
{	
	public static final String DEVICE_ID = "jww93";
	private static ArrayList<String> orderCodes = new ArrayList<String>();
	
	private static Context context;

	private static ArrayList<Server> servers = new ArrayList<Server>();
	private static Server currentServer;
	private static String serverName;
	
	private static int currentScreen = 0;
	private static ArrayList<Integer> previousScreen = new ArrayList<Integer>();
	private static int screenPosition = 0;
	
	private static ArrayList<Drink> orders = new ArrayList<Drink>();
	private static ArrayList<Drink> alcohols = new ArrayList<Drink>();
	private static ArrayList<Drink> serverTopOrders = new ArrayList<Drink>();
	private static ArrayList<Drink> myTopOrders =  new ArrayList<Drink>();
	private static ArrayList<TabDrink> tabDrinks = new ArrayList<TabDrink>();
	private static SparseArray<String> mapSpinnerToAlcohol = new SparseArray<String>();
	private static SparseIntArray mapArrayToAlcohol = new SparseIntArray();
	private static SparseIntArray mapArrayToQuantity = new SparseIntArray();
	
	private static double tabTotal;
	
	// MAIN METHODS
	
	public static void addOrderCode(String code)
	{
		orderCodes.add(code);
	}
	
	public static void removeAllOrderCodes()
	{
		orderCodes.clear();
	}
	
	public static void addOrderToTab(TabDrink order)
	{
		tabDrinks.add(order);
	}
	
	public static void removeOrderFromTab(int position)
	{
		tabDrinks.remove(position);
	}
	
	public static void addMyTopOrder(Drink order)
	{
		myTopOrders.add(order);
	}
	
	public static void removeMyTopOrder(int position)
	{
		myTopOrders.remove(position);
	}
	
	public static void addAlcohol(Drink alcohol)
	{
		alcohols.add(alcohol);
		mapSpinnerToAlcohol.put(alcohols.size() - 1, alcohol.getId());
	}
	
	public static void removeAllAlcohols()
	{
		alcohols.clear();
		mapSpinnerToAlcohol.clear();
	}
	
	public static void addServerTopOrder(Drink order)
	{
		serverTopOrders.add(order);
	}
	
	public static void removeAllServerTopOrders()
	{
		serverTopOrders.clear();
	}
	
	public static void calculateTabTotal()
	{
		double total = 0.0;
		for (int i = 0; i < tabDrinks.size(); i++)
		{
			total += tabDrinks.get(i).getPrice();
		}
		tabTotal = total;
	}
	
	public static void goToPreviousScreen()
	{
		if (screenPosition > 0)
		{
			screenPosition--;
			currentScreen = previousScreen.get(screenPosition);
			previousScreen.remove(previousScreen.size() - 1);
		}
		else
			currentScreen = 0;
		FlashClient.updateAll();
	}
	
	public static void goToMainScreen()
	{
		screenPosition = 0;
		currentScreen = 0;
		previousScreen.clear();
		FlashClient.updateAll();
	}
	
	public static Drink getOrderById(String id)
	{
		Drink d = null;
		for (int j = 0; j < orders.size(); j++)
		{
			if (orders.get(j).getId().equals(id))
				d = orders.get(j);
		}
		return d;
	}
	
	public static void removeAllArrayToValues()
	{
		mapArrayToAlcohol.clear();
		mapArrayToQuantity.clear();
	}
	
	// SETTER METHODS
	
	public static void setOrderCodes(ArrayList<String> codes)
	{
		orderCodes = codes;
	}
	
	public static void setContext(Context c)
	{
		context = c;
	}
	
	public static void setServerName(String s)
	{
		serverName = s;
	}
	
	public static void setCurrentScreen(int i)
	{
		addPreviousScreen(currentScreen);
		currentScreen = i;
		screenPosition = previousScreen.size();
	}
	
	public static void addPreviousScreen(int i)
	{
		if (previousScreen.size() >= 30)
			previousScreen.remove(0);
		previousScreen.add(i);
	}
	
	public static void setServers(ArrayList<Server> s)
	{
		servers = s;
	}
	
	public static void setOrders(ArrayList<Drink> o)
	{
		orders = o;
	}
	
	public static void setAlcohols(ArrayList<Drink> a)
	{
		alcohols = a;
	}
	
	public static void setServerTopOrders(ArrayList<Drink> orders)
	{
		serverTopOrders = orders;
	}
	
	public static void setMyTopOrders(ArrayList<Drink> orders)
	{
		myTopOrders = orders;
	}
	
	public static void setTabDrinks(ArrayList<TabDrink> orders)
	{
		tabDrinks = orders;
	}
	
	public static void setTabTotal(double total)
	{
		tabTotal = total;
	}
	
	public static void setCurrentServer(Server s)
	{
		currentServer = s;
	}
	
	public static void setArrayToAlcohol(int listPosition, int selection)
	{
		mapArrayToAlcohol.put(listPosition, selection);
	}
	
	public static void setArrayToQuantity(int listPosition, int selection)
	{
		mapArrayToQuantity.put(listPosition, selection);
	}
	
	// ACCESSOR METHODS
	
	public static Context getContext()
	{
		return context;
		
	}
	
	public static String getServerName()
	{
		if (serverName != null && serverName.length() > 0)
			return serverName;
		else
			return "(Select Bar)";
	}
	
	public static int getCurrentScreen()
	{
		return currentScreen;
	}
	
	public static ArrayList<Integer> getPreviousScreen()
	{
		return previousScreen;
	}
	
	public static int getScreenPosition()
	{
		return screenPosition;
	}
	
	public static ArrayList<Server> getServers()
	{
		return servers;
	}
	
	public static ArrayList<Server> getSearchServers()
	{
		String s = FlashClient.search_drinks.getText().toString().toLowerCase(Locale.US);
		ArrayList<Server> items = new ArrayList<Server>();
		for (int i = 0; i < servers.size(); i++)
		{
			if (servers.get(i).getName().toLowerCase(Locale.US).contains(s))
				items.add(servers.get(i));
		}
		return items;
	}
	
	public static ArrayList<Drink> getAllOrders()
	{
		return orders;
	}
	
	public static ArrayList<Drink> getSearchOrders()
	{
		String s = FlashClient.search_drinks.getText().toString().toLowerCase(Locale.US);
		ArrayList<Drink> items = new ArrayList<Drink>();
		for (int i = 0; i < orders.size(); i++)
		{
			if (orders.get(i).getName().toLowerCase(Locale.US).contains(s))
				items.add(orders.get(i));
		}
		return items;
	}
	
	public static ArrayList<Drink> getServerTopOrders()
	{
		return serverTopOrders;
	}
	
	public static ArrayList<Drink> getMyTopOrders()
	{
		return myTopOrders;
	}
	
	public static ArrayList<TabDrink> getTabDrinks()
	{
		return tabDrinks;
	}
	
	public static ArrayList<TabDrink> getSearchTabDrinks()
	{
		String s = FlashClient.search_drinks.getText().toString().toLowerCase(Locale.US);
		ArrayList<TabDrink> items = new ArrayList<TabDrink>();
		for (int i = 0; i < tabDrinks.size(); i++)
		{
			if (tabDrinks.get(i).getDrink().getName().toLowerCase(Locale.US).contains(s))
				items.add(tabDrinks.get(i));
		}
		return items;
	}
	
	public static ArrayList<Drink> getAlcohols()
	{
		return alcohols;
	}
	
	public static String getAlcoholIdForSpinnerPosition(int position)
	{
		String alcohol_id = mapSpinnerToAlcohol.get(position);
		return alcohol_id;
	}
	
	public static double getTabTotal()
	{
		return tabTotal;
	}
	
	public static Server getCurrentServer()
	{
		return currentServer;
	}
	
	public static int getAlcoholSelectionForKey(int listPosition)
	{
		return mapArrayToAlcohol.get(listPosition);
	}
	
	public static int getQuantitySelectionForKey(int listPosition)
	{
		return mapArrayToQuantity.get(listPosition);
	}
	
	public static ArrayList<String> getOrderCodes()
	{
		return orderCodes;
	}
}
