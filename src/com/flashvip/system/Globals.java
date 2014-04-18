package com.flashvip.system;

import java.util.ArrayList;
import java.util.List;

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
	private static List<Vendor> vendors = new ArrayList<Vendor>();
	private static List<Vendor> filteredVendors = new ArrayList<Vendor>();
	private static List<Category> categories = new ArrayList<Category>();
	private static List<Code> codes = new ArrayList<Code>();
	private static Order order;
	private static String deviceId;
	private static List<Vendor> favoriteVendors = new ArrayList<Vendor>();
	private static List<Item> favoriteItems =  new ArrayList<Item>();
	private static Bitmap scan;
	
	// MAIN METHODS
	public static Item getItemById(String itemId)
	{
		Item item = null;
		List<Item> items = vendor.getItems();
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

	public static void setVendors(List<Vendor> vendors)
	{
		Globals.vendors = vendors;
	}
	
	public static void setFilteredVendors(List<Vendor> filteredVendors)
	{
		Globals.filteredVendors = filteredVendors;
	}
	
	public static void setCategories(List<Category> categories)
	{
		Globals.categories = categories;
	}

	public static void setCodes(List<Code> codes)
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

	public static void setFavoriteVendors(List<Vendor> favoriteVendors)
	{
		Globals.favoriteVendors = favoriteVendors;
	}

	public static void setFavoriteItems(List<Item> favoriteItems)
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
	public static List<Vendor> getVendors()
	{
		return vendors;
	}
	
	public static List<Vendor> getFilteredVendors()
	{
		return filteredVendors;
	}
	
	public static List<Category> getCategories()
	{
		return categories;
	}

	public static List<Code> getCodes()
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

	public static List<Vendor> getFavoriteVendors()
	{
		return favoriteVendors;
	}

	public static List<Item> getFavoriteItems()
	{
		return favoriteItems;
	}
	
	public static Bitmap getScan()
	{
		return scan;
	}
}