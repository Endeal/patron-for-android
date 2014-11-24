package com.patron.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.patron.bind.VendorBinder;
import com.patron.db.VendorConnector;
import com.patron.listeners.ListItemVendorListener;
import com.patron.lists.ListLinks;
import com.patron.model.Vendor;
import com.patron.system.Globals;
import com.patron.system.Loadable;
import com.patron.system.Parser;

public class FlashVendors extends ActionBarActivity implements Loadable
{
	// The layout elements.
	private ListView listLocations;
	private View viewLoading;
	private View viewVendors;
	private View viewNone;
	private List<Vendor> retrievedVendors;
	
	// Message to be displayed.
	private Activity activity;
	private CharSequence message;
	
	// Activity methods.
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		viewLoading = inflater.inflate(R.layout.misc_loading, null);
		viewVendors = inflater.inflate(R.layout.layout_locations, null);
		viewNone = inflater.inflate(R.layout.misc_no_locations, null);
		setContentView(viewLoading);
		beginLoading();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	    // Inflate the menu items for use in the action bar
	    getMenuInflater().inflate(R.menu.menu_search, menu);
	    return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.menuItemSearch:
			if (Globals.getVendors() != null && Globals.getVendors().size() > 0)
			{
				Intent intent = new Intent(this, FlashSearchVendors.class);
				startActivity(intent);

			}
			else
			{
				message("There are no vendors to search.");
			}
			return true;
		case R.id.menuItemSettings:
			Intent intentSettings = new Intent(this, FlashSettings.class);
			startActivity(intentSettings);
			return true;
		case R.id.menuItemHelp:
			Intent intentHelp = new Intent(this, FlashHelp.class);
			startActivity(intentHelp);
			return true;
		default:
			return false;
		}
	}
	
	@Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	super.onWindowFocusChanged(hasFocus);
    }
	
	// Main methods.
	public void beginLoading()
	{
		listLocations = (ListView) viewVendors.findViewById(R.id.locationsList);
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		String json = preferences.getString("favoriteVendors", "");
		try
		{
			JSONArray rawVendors = new JSONArray(json);
			retrievedVendors = Parser.getVendors(rawVendors);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		load();
	}
	
	public void load()
	{
		VendorConnector dbconnector = new VendorConnector(this);
		URL url = null;
		try
		{
			try
			{
				url = new URL(ListLinks.LINK_GET_VENDORS);

			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			dbconnector.execute(url);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void endLoading()
	{
		if (Globals.getVendors() != null &&
				!Globals.getVendors().isEmpty())
		{
			setContentView(viewVendors);
			// Check if all the items have the favorited ones from the preferences.
			ArrayList<Vendor> vendors = new ArrayList<Vendor>(); 
			if (retrievedVendors != null && !retrievedVendors.isEmpty())
			{
				for (int i = 0; i < Globals.getVendors().size(); i++)
				{
					Vendor vendor = Globals.getVendors().get(i);
					for (int j = 0; j < retrievedVendors.size(); j++)
					{
						Vendor favoriteVendor = retrievedVendors.get(j);
						if (favoriteVendor.getId().equals(vendor.getId()))
						{
							vendors.add(vendor);
						}
					}
				}
				Globals.setFavoriteVendors(vendors);
			}
		}
		else
		{
			setContentView(viewNone);
		}
		update();
	}
	
	public void update()
	{
    	if (Globals.getVendors() != null && !Globals.getVendors().isEmpty())
    	{
    		List<Map<String, String>> locations = new ArrayList<Map<String, String>>();
    		
    		String[] from = {"textName",
    				"textPhone",
    				"textAddress",
    				"toggleButtonFavorite"};
    		
    		int[] to = {R.id.locationListItemTextName,
    				R.id.locationListItemTextPhone,
    				R.id.locationListItemTextAddress,
    				R.id.locationListItemToggleButtonFavorite};
    		
    		for (int i = 0; i < Globals.getVendors().size(); i++)
    		{
    			Map<String, String> mapping = new HashMap<String, String>();
    			Vendor currentLocation = Globals.getVendors().get(i);
    			mapping.put("textName", currentLocation.getName());
    			mapping.put("textPhone", currentLocation.getPhone());
    			mapping.put("textAddress", currentLocation.getAddress() +
    					", " + currentLocation.getCity() +
    					", " + currentLocation.getState() +
    					currentLocation.getZip());
    			mapping.put("toggleButtonFavorite", "" + i);
    			locations.add(mapping);
    		}
    		SimpleAdapter adapter = new SimpleAdapter(this,
    				locations, R.layout.list_item_location, from, to);
    		adapter.setViewBinder(new VendorBinder());
    		listLocations.setAdapter(adapter);
    		listLocations.setOnItemClickListener(new ListItemVendorListener());
    		adapter.notifyDataSetChanged();
    	}
	}
	
	public void message(String msg)
	{
		message = msg;
		activity = this;
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
				toast.show();
			}
		};
		runOnUiThread(thread);
	}
}
