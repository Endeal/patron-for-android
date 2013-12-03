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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.flashvip.db.ProductConnector;
import com.flashvip.db.LocationConnector;
import com.flashvip.lists.ListLinks;

public class FlashLocations extends ActionBarActivity
{
	// The layout elements.
	private ListView listLocations;
	
	// Activity methods.
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initializeLayout();
		setContentView(R.layout.misc_loading);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	@Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	super.onWindowFocusChanged(hasFocus);
    	initializeLayout();
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
		LocationConnector dbconnector = new LocationConnector(this);
		URL url = null;
		try
		{
			try
			{
				url = new URL(ListLinks.LINK_GET_SERVERS);

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
    	if (Globals.getLocations() != null && !Globals.getLocations().isEmpty())
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
    		
    		for (int i = 0; i < Globals.getLocations().size(); i++)
    		{
    			Map<String, String> mapping = new HashMap<String, String>();
    			Location currentLocation = Globals.getLocations().get(i);
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
    		adapter.setViewBinder(new LocationBinder());
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
		private FlashLocations activity;
		
		public LocationItemListener(FlashLocations activity)
		{
			this.activity = activity;
		}
		
		public void onItemClick(AdapterView<?> adapter, View v, int item,
				long row)
		{
			Location server = Globals.getLocations().get(item);
			Globals.setLocationName(server.getName());
			Globals.setCurrentLocation(server);
			ProductConnector dbdrink = new ProductConnector(activity);
			try
			{
				URL url = new URL(ListLinks.LINK_GET_DRINKS);
				dbdrink.execute(url);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
	}
}
