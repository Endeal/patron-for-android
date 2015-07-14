package me.endeal.patron.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.endeal.patron.main.FlashLogin;
import me.endeal.patron.model.Category;
import me.endeal.patron.model.Code;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Item;
import me.endeal.patron.model.Order;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.model.Patron;
import me.endeal.patron.view.ButtonFilter;

public class Globals
{
	private static final Logger logger = LoggerFactory.getLogger(Globals.class);

	// Properties
	private static Patron user;
	private static Vendor vendor;
	private static List<Vendor> vendors = new ArrayList<Vendor>();
	private static List<Vendor> filteredVendors = new ArrayList<Vendor>();
	private static List<Category> categories = new ArrayList<Category>();
	private static List<Order> orders = new ArrayList<Order>();
	private static List<Fragment> fragments = new ArrayList<Fragment>();
	private static Order order;
	private static String deviceId;
	private static List<Vendor> favoriteVendors = new ArrayList<Vendor>();
	private static List<Item> favoriteItems =  new ArrayList<Item>();
	private static Bitmap scan;
  private static Map<String, Integer> points = new HashMap<String, Integer>();
	private static ButtonFilter buttonFilter;

    // Login
    private static String provider = "";

	// MAIN METHODS
	public static Item getItemById(String itemId)
	{
		Item item = null;
		List<Item> items = vendor.getItems();
		for (int j = 0; j < items.size(); j++)
		{
			if (items.get(j).getId().equals(itemId))
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

    public static boolean hasUser()
    {
        return Globals.user != null;
    }

	// Setters
	public static void setUser(Patron user)
	{
		Globals.user = user;
	}

	public static void setVendor(Vendor vendor)
	{
		if (vendor == null)
		{
			logger.error("Null vendor set.", vendor);
		}
        Globals.order = null;
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

    public static void filterCategories(List<Item> items)
    {
        /*
        // Add the categories from every item
        Map<String, Category> map = new HashMap<String, Category>();
        for (int i = 0; i < items.size(); i++)
        {
            Item item = items.get(i);
            for (int j = 0; j < item.getCategories().size(); i++)
            {
                Category category = item.getCategories().get(j);
                if (!map.containsKey(category.getId()))
                {
                    map.put(category.getId(), category);
                }
            }
        }

        // Set List of categories for vendor
        List<Category> categories = new ArrayList<Category>();
        for (Map.Entry<String, Category> entry : map.entrySet())
        {
            categories.add(entry.getValue());
        }
        Globals.setCategories(categories);
        */
        SortedSet<Category> set = new TreeSet<Category>();
        for (int i = 0; i < items.size(); i++)
        {
            Item item = items.get(i);
            for (int j = 0; j < item.getCategories().size(); i++)
            {
                Category category = item.getCategories().get(j);
                set.add(category);
            }
        }
        Category[] raw = Arrays.copyOf(set.toArray(), set.toArray().length, Category[].class);
        List<Category> list = Arrays.asList(raw);
        Globals.setCategories(list);
    }

	public static void setCategories(List<Category> categories)
	{
		Globals.categories = categories;
	}

	public static void setOrders(List<Order> orders)
	{
		Globals.orders = orders;
	}

	public static void setFragments(List<Fragment> fragments)
	{
		Globals.fragments = fragments;
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

    public static void setPoints(Map<String, Integer> points)
    {
        Globals.points = points;
    }

public static void setButtonFilter(ButtonFilter buttonFilter)
{
	Globals.buttonFilter = buttonFilter;
}

	// Getters
	public static Patron getUser()
	{
        /*
		if (user == null)
		{
			Context context = Patron.getContext();
			Intent intent = new Intent(context, FlashLogin.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
        */
		return user;
	}

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

	public static List<Order> getOrders()
	{
		return orders;
	}

	public static List<Fragment> getFragments()
	{
		return fragments;
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

    public static Map<String, Integer> getPoints()
    {
        return points;
    }

    public static int getPoints(String vendorId)
    {
        if (points.get(vendorId) == null)
            return -1;
        return points.get(vendorId);
    }

		public static ButtonFilter getButtonFilter()
		{
			return buttonFilter;
		}

	public static void setEmail(String email)
	{
		try
		{
			Context context = PatronApplication.getContext();
			SharedPreferences sharedPreferences = context.getSharedPreferences("me.endeal.patron", Context.MODE_PRIVATE);
        	Editor editor = sharedPreferences.edit();
        	editor.putString("email", email);
        	editor.commit();
    	}
    	catch (Exception e)
    	{
            e.printStackTrace();
    	}
	}

	public static void setPassword(String password)
	{
		try
		{
            String encryptedPassword;
            Context context = PatronApplication.getContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences("me.endeal.patron", Context.MODE_PRIVATE);
            if (password == null)
            {
                encryptedPassword = null;
            }
            else
            {
                encryptedPassword = FlashCipher.encrypt(password);
            }
	        Editor editor = sharedPreferences.edit();
	        editor.putString("password", encryptedPassword);
	        editor.commit();
	    }
	    catch (Exception e)
	    {
            e.printStackTrace();
	    }
	}

	public static void setProvider(String provider)
	{
		try
		{
			Context context = PatronApplication.getContext();
			SharedPreferences sharedPreferences = context.getSharedPreferences("me.endeal.patron", Context.MODE_PRIVATE);
        	Editor editor = sharedPreferences.edit();
        	editor.putString("provider", provider);
        	editor.commit();
    	}
    	catch (Exception e)
    	{
            e.printStackTrace();
    	}
	}

	public static String getEmail()
	{
		try
		{
			Context context = PatronApplication.getContext();
			SharedPreferences sharedPreferences = context.getSharedPreferences("me.endeal.patron", Context.MODE_PRIVATE);
			String email = sharedPreferences.getString("email", "");
	        return email;
	    }
	    catch (Exception e)
	    {
	    	return null;
	    }
	}

	public static String getPassword()
	{
		try
		{
			Context context = PatronApplication.getContext();
			SharedPreferences sharedPreferences = context.getSharedPreferences("me.endeal.patron", Context.MODE_PRIVATE);
			String password = sharedPreferences.getString("password", "");
	        password = FlashCipher.decrypt(password);
	        return password;
	    }
	    catch (Exception e)
	    {
	    	return null;
	    }
	}

	public static String getProvider()
	{
		try
		{
			Context context = PatronApplication.getContext();
			SharedPreferences sharedPreferences = context.getSharedPreferences("me.endeal.patron", Context.MODE_PRIVATE);
			String provider = sharedPreferences.getString("provider", "");
	        return provider;
	    }
	    catch (Exception e)
	    {
	    	return null;
	    }
	}

}
