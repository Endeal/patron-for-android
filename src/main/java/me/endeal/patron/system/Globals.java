package me.endeal.patron.system;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.IndexOutOfBoundsException;
import java.lang.Exception;
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

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.endeal.patron.model.Credential;
import me.endeal.patron.model.Category;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Item;
import me.endeal.patron.model.Order;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.model.Patron;

public class Globals
{
	private static final Logger logger = LoggerFactory.getLogger(Globals.class);

	// Properties
	private static Patron patron;
	private static Vendor vendor;
	private static List<Vendor> vendors = new ArrayList<Vendor>();
	private static List<Vendor> filteredVendors = new ArrayList<Vendor>();
	private static List<Category> categories = new ArrayList<Category>();
	private static List<Order> orders = new ArrayList<Order>();
	private static List<Fragment> fragments = new ArrayList<Fragment>();
	private static Order order;
	private static Bitmap scan;
    private static Credential credential;

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

    public static Object deepClone(Object object)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

	// Setters
	public static void setPatron(Patron patron)
	{
		Globals.patron = patron;
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
        // Add the categories from every item
        Map<String, Category> map = new HashMap<String, Category>();
        for (int i = 0; i < items.size(); i++)
        {
            Item item = items.get(i);
            for (int j = 0; j < item.getCategories().size(); j++)
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
        SortedSet<Category> set = new TreeSet<Category>();
        for (int i = 0; i < items.size(); i++)
        {
            Item item = items.get(i);
            for (int j = 0; j < item.getCategories().size(); j++)
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

	public static void setScan(Bitmap codeImage)
	{
		Globals.scan = codeImage;
	}

    public static void setCredential(Credential credential)
    {
        Gson gson = new Gson();
        SharedPreferences shared = PatronApplication.getContext().getSharedPreferences("patron", Context.MODE_PRIVATE);
        String json = gson.toJson(credential);
        Editor editor = shared.edit();
        editor.putString("credential", json);
        editor.commit();
    }

	// Getters
	public static Patron getPatron()
	{
        /*
		if (patron == null)
		{
			Context context = Patron.getContext();
			Intent intent = new Intent(context, FlashLogin.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
        */
		return patron;
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

	public static Bitmap getScan()
	{
		return scan;
	}

    public static Credential getCredential()
    {
        Gson gson = new Gson();
        SharedPreferences shared = PatronApplication.getContext().getSharedPreferences("patron", Context.MODE_PRIVATE);
        String json = shared.getString("credential", "");
        Credential credential = gson.fromJson(json, Credential.class);
        return credential;
    }
}
