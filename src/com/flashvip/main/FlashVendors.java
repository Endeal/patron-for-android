package com.flashvip.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.flashvip.bind.VendorBinder;
import com.flashvip.db.ItemConnector;
import com.flashvip.db.VendorConnector;
import com.flashvip.lists.ListLinks;
import com.flashvip.model.Vendor;

public class FlashVendors extends ActionBarActivity
{
	// The layout elements.
	private ListView listLocations;
	
	// Activity methods.
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.misc_loading);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_home, menu);
	    return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	super.onWindowFocusChanged(hasFocus);
    	updateLocations();
    }
	
	// Main methods.
	public void loadedMenu()
	{
		Intent intent = new Intent(this, FlashMenu.class);
		startActivity(intent);
	}
	
	private void updateLocations()
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
	
	// Layout
	private void initializeLayout()
	{
		listLocations = (ListView) findViewById(R.id.locationsList);
	}
	
	public void updateList()
	{
    	if (Globals.getVendors() != null && !Globals.getVendors().isEmpty())
    	{
    		setContentView(R.layout.layout_locations);
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
    		if (listLocations == null)
    			initializeLayout();
    		listLocations.setAdapter(adapter);
    		listLocations.setOnItemClickListener(new LocationItemListener(this));
    		adapter.notifyDataSetChanged();
    	}
    	else
    	{
    		setContentView(R.layout.misc_no_locations);
    	}
	}
	
	public static class LocationItemListener implements OnItemClickListener
	{
		private FlashVendors activity;
		
		public LocationItemListener(FlashVendors activity)
		{
			this.activity = activity;
		}
		
		public void onItemClick(AdapterView<?> adapter, View v, int item,
				long row)
		{
			Vendor vendor = Globals.getVendors().get(item);
			Globals.setVendor(vendor);
			ItemConnector itemConnector = new ItemConnector(activity);
			try
			{
				URL itemsUrl = new URL(ListLinks.LINK_GET_ITEMS);
				itemConnector.execute(itemsUrl);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
	}
}
