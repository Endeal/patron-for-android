package com.flashvip.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.flashvip.bind.VendorBinder;
import com.flashvip.db.VendorConnector;
import com.flashvip.lists.ListLinks;
import com.flashvip.model.Vendor;
import com.flashvip.system.Globals;
import com.flashvip.system.Loadable;
import com.flashvip.system.Parser;

public class FlashVendors extends ActionBarActivity implements Loadable
{
	// The layout elements.
	private ListView listLocations;
	private View viewLoading;
	private View viewVendors;
	private View viewNone;
	private ArrayList<Vendor> retrievedVendors; 
	
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
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_search, menu);
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
				message("There are no vendors to search through.");
			}
			return true;
    	case R.id.menuItemSettings:
    		message("Settings.");
    		return true;
    	case R.id.menuItemHelp:
    		message("Help");
    		return true;
		default:
			message("Menu item tapped.");
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
			ArrayList<Vendor> vendors = new ArrayList<Vendor>(); 
			for (int i = 0; i < rawVendors.length(); i++)
			{
				JSONObject rawVendor = rawVendors.getJSONObject(i);
				Vendor vendor = Parser.getVendor(rawVendor);
				vendors.add(vendor);
			}
			retrievedVendors = vendors;
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
						if (favoriteVendor.getVendorId().equals(vendor.getVendorId()))
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
    		listLocations.setOnItemClickListener(new LocationItemListener());
    		adapter.notifyDataSetChanged();
    	}
	}
	
	public void message(String msg)
	{
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public static class LocationItemListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> adapter, View v, int item,
				long row)
		{
			Vendor vendor = Globals.getVendors().get(item);
			Globals.setVendor(vendor);
			Activity activity = (Activity)v.getContext();
			Intent intent = new Intent(v.getContext(), FlashMenu.class);
			activity.startActivity(intent);
		}
	}
}
