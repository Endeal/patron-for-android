package com.flashvip.system;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.flashvip.model.Category;
import com.flashvip.model.Code;
import com.flashvip.model.Item;
import com.flashvip.model.Order;
import com.flashvip.model.Vendor;

public class Globals
{
	// Properties
	private static Vendor vendor;
	private static ArrayList<Vendor> vendors = new ArrayList<Vendor>();
	private static ArrayList<Vendor> filteredVendors = new ArrayList<Vendor>();
	private static ArrayList<Category> categories = new ArrayList<Category>();
	private static ArrayList<Code> codes = new ArrayList<Code>();
	private static Order order;
	private static String deviceId;
	private static ArrayList<Vendor> favoriteVendors = new ArrayList<Vendor>();
	private static ArrayList<Item> favoriteItems =  new ArrayList<Item>();
	private static Bitmap scan;
	
	// MAIN METHODS
	public static Item getItemById(String itemId)
	{
		Item item = null;
		ArrayList<Item> items = vendor.getItems();
		for (int j = 0; j < items.size(); j++)
		{
			if (items.get(j).getItemId().equals(itemId))
				item = items.get(j);
		}
		return item;
	}
	
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}
	
	public static float convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}

	// Setters
	public static void setVendor(Vendor vendor)
	{
		Globals.vendor = vendor;
	}

	public static void setVendors(ArrayList<Vendor> vendors)
	{
		Globals.vendors = vendors;
	}
	
	public static void setFilteredVendors(ArrayList<Vendor> filteredVendors)
	{
		Globals.filteredVendors = filteredVendors;
	}
	
	public static void setCategories(ArrayList<Category> categories)
	{
		Globals.categories = categories;
	}

	public static void setCodes(ArrayList<Code> codes)
	{
		Globals.codes = codes;
	}

	public static void setOrder(Order order)
	{
		Globals.order = order;
	}
	
	public static void setDeviceId(String deviceId)
	{
		Globals.deviceId = deviceId;
	}

	public static void setFavoriteVendors(ArrayList<Vendor> favoriteVendors)
	{
		Globals.favoriteVendors = favoriteVendors;
	}

	public static void setFavoriteItems(ArrayList<Item> favoriteItems)
	{
		Globals.favoriteItems = favoriteItems;
	}
	
	public static void setScan(Bitmap codeImage)
	{
		Globals.scan = codeImage;
	}

	// Getters
	public static Vendor getVendor()
	{
		return vendor;
	}
	public static ArrayList<Vendor> getVendors()
	{
		return vendors;
	}
	
	public static ArrayList<Vendor> getFilteredVendors()
	{
		return filteredVendors;
	}
	
	public static ArrayList<Category> getCategories()
	{
		return categories;
	}

	public static ArrayList<Code> getCodes()
	{
		return codes;
	}

	public static Order getOrder()
	{
		return order;
	}
	
	public static String getDeviceId()
	{
		return deviceId;
	}

	public static ArrayList<Vendor> getFavoriteVendors()
	{
		return favoriteVendors;
	}

	public static ArrayList<Item> getFavoriteItems()
	{
		return favoriteItems;
	}
	
	public static Bitmap getScan()
	{
		return scan;
	}
}
