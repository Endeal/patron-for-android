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

	private static ArrayList<Location> locations = new ArrayList<Location>();
	private static Location currentServer;
	private static String serverName;
	
	private static int currentScreen = 0;
	private static ArrayList<Integer> previousScreen = new ArrayList<Integer>();
	private static int screenPosition = 0;
	
	private static ArrayList<Product> products = new ArrayList<Product>();
	private static ArrayList<Product> currentProducts = new ArrayList<Product>();
	private static ArrayList<Product> alcohols = new ArrayList<Product>();
	private static ArrayList<Product> serverTopOrders = new ArrayList<Product>();
	private static ArrayList<Product> favoriteProducts =  new ArrayList<Product>();
	private static ArrayList<TabProduct> tabProducts = new ArrayList<TabProduct>();
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
	
	public static void addOrderToTab(TabProduct order)
	{
		tabProducts.add(order);
	}
	
	public static void removeOrderFromTab(int position)
	{
		tabProducts.remove(position);
	}
	
	public static void addFavoriteProduct(Product order)
	{
		if (favoriteProducts == null)
			favoriteProducts = new ArrayList<Product>();
		if (order != null)
			favoriteProducts.add(order);
	}
	
	public static void removeFavoriteProduct(int position)
	{
		favoriteProducts.remove(position);
	}
	
	public static void addAlcohol(Product alcohol)
	{
		alcohols.add(alcohol);
		mapSpinnerToAlcohol.put(alcohols.size() - 1, alcohol.getId());
	}
	
	public static void removeAllAlcohols()
	{
		alcohols.clear();
		mapSpinnerToAlcohol.clear();
	}
	
	public static void addServerTopOrder(Product order)
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
		for (int i = 0; i < tabProducts.size(); i++)
		{
			total += tabProducts.get(i).getPrice();
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
	
	public static Product getProductById(String id)
	{
		Product d = null;
		for (int j = 0; j < products.size(); j++)
		{
			if (products.get(j).getId().equals(id))
				d = products.get(j);
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
	
	public static void setServers(ArrayList<Location> newLocations)
	{
		locations = newLocations;
	}
	
	public static void setProducts(ArrayList<Product> newProducts)
	{
		products = newProducts;
	}
	
	public static void setCurrentProducts(ArrayList<Product> newCurrentProducts)
	{
		currentProducts = newCurrentProducts;
	}
	
	public static void setAlcohols(ArrayList<Product> newAlcohols)
	{
		alcohols = newAlcohols;
	}
	
	public static void setServerTopOrders(ArrayList<Product> orders)
	{
		serverTopOrders = orders;
	}
	
	public static void setFavoriteProducts(ArrayList<Product> orders)
	{
		favoriteProducts = orders;
	}
	
	public static void setTabProducts(ArrayList<TabProduct> orders)
	{
		tabProducts = orders;
	}
	
	public static void setTabTotal(double total)
	{
		tabTotal = total;
	}
	
	public static void setCurrentServer(Location s)
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
	
	public static ArrayList<Location> getServers()
	{
		return locations;
	}
	
	public static ArrayList<Location> getSearchServers()
	{
		String s = FlashClient.search_drinks.getText().toString().toLowerCase(Locale.US);
		ArrayList<Location> items = new ArrayList<Location>();
		for (int i = 0; i < locations.size(); i++)
		{
			if (locations.get(i).getName().toLowerCase(Locale.US).contains(s))
				items.add(locations.get(i));
		}
		return items;
	}
	
	public static ArrayList<Product> getProducts()
	{
		return products;
	}
	
	public static ArrayList<Product> getCurrentProducts()
	{
		return currentProducts;
	}
	
	public static ArrayList<Product> getSearchOrders()
	{
		String s = FlashClient.search_drinks.getText().toString().toLowerCase(Locale.US);
		ArrayList<Product> items = new ArrayList<Product>();
		for (int i = 0; i < products.size(); i++)
		{
			if (products.get(i).getName().toLowerCase(Locale.US).contains(s))
				items.add(products.get(i));
		}
		return items;
	}
	
	public static ArrayList<Product> getServerTopOrders()
	{
		return serverTopOrders;
	}
	
	public static ArrayList<Product> getFavoriteProducts()
	{
		return favoriteProducts;
	}
	
	public static ArrayList<TabProduct> getTabProducts()
	{
		return tabProducts;
	}
	
	public static ArrayList<TabProduct> getSearchTabDrinks()
	{
		String s = FlashClient.search_drinks.getText().toString().toLowerCase(Locale.US);
		ArrayList<TabProduct> items = new ArrayList<TabProduct>();
		for (int i = 0; i < tabProducts.size(); i++)
		{
			if (tabProducts.get(i).getDrink().getName().toLowerCase(Locale.US).contains(s))
				items.add(tabProducts.get(i));
		}
		return items;
	}
	
	public static ArrayList<Product> getAlcohols()
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
	
	public static Location getCurrentServer()
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
