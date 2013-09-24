package com.flashvip.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.flashvip.db.ProductConnector;
import com.flashvip.db.ServerConnector;
import com.flashvip.lists.ListLinks;

public class FlashLocations extends ActionBarActivity
{
	// The layout elements.
	private ListView list;
	
	// Activity methods.
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.misc_loading);

		updateLocations();
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
		ServerConnector dbconnector = new ServerConnector(this);
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
	
	private Activity getActivity()
	{
		return this;
	}
	
	// Layout
	private void initializeLayout()
	{
		list = (ListView) findViewById(R.id.locationsList);
	}
	
	public void updateList()
	{
    	if (Globals.getServers() != null && !Globals.getServers().isEmpty())
    	{
    		setContentView(R.layout.layout_locations);
    		initializeLayout();
    		List<Map<String, String>> servers = new ArrayList<Map<String, String>>();
    		String[] from = {"name", "phone", "address"};
    		int[] to = {R.id.locationListItemTextName, R.id.locationListItemTextPhone,
    				R.id.locationListItemTextAddress};
    		for (int i = 0; i < Globals.getServers().size(); i++)
    		{
    			Map<String, String> mapping = new HashMap<String, String>();
    			Location currentServer = Globals.getServers().get(i);
    			mapping.put("name", currentServer.getName());
    			mapping.put("phone", currentServer.getPhone());
    			mapping.put("address", currentServer.getAddress() + ", " +
    					currentServer.getCity() + ", " + currentServer.getState() +
    					currentServer.getZip());
    			servers.add(mapping);
    		}
    		SimpleAdapter adapter = new SimpleAdapter(this,
    				servers, R.layout.list_item_location, from, to);
    		list.setAdapter(adapter);
    		list.setOnItemClickListener(new LocationItemListener(this));
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
			Location server = Globals.getServers().get(item);
			Globals.setServerName(server.getName());
			Globals.setCurrentServer(server);
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
