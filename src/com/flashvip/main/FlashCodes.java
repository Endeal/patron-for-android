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
import com.flashvip.db.CodeConnector;
import com.flashvip.lists.ListLinks;
import com.flashvip.model.Code;
import com.flashvip.model.Order;

public class FlashCodes extends ActionBarActivity
{
	// The layout elements.
	private ListView listCodes;
	
	// Activity methods.
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_codes);
		listCodes = (ListView) findViewById(R.id.codesList);
		updateList();
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
    }
	
	// Main methods.
	public void loadedMenu()
	{
		Intent intent = new Intent(this, FlashMenu.class);
		startActivity(intent);
	}
	
	private void updateCodes()
	{
		CodeConnector dbconnector = new CodeConnector();
		URL url = null;
		try
		{
			try
			{
				url = new URL(ListLinks.LINK_GET_CODES);

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
		listCodes = (ListView) findViewById(R.id.codesList);
	}
	
	public void updateList()
	{
		ArrayList<Code> testCodes = new ArrayList<Code>();
    	/*if (testCodes() != null && !Globals.getCodes().isEmpty())
    	{*/
    		//setContentView(R.layout.layout_codes);
    		List<Map<String, String>> codes = new ArrayList<Map<String, String>>();
    		
    		String[] from = {"textTime",
    				"textStatus",
    				"textOrders"};
    		
    		int[] to = {R.id.codeListItemTextTime,
    				R.id.codeListItemTextStatus,
    				R.id.codeListItemTextProducts};
    		
    		for (int i = 0; i < testCodes.size(); i++)
    		{
    			Map<String, String> mapping = new HashMap<String, String>();
    			Code code = testCodes.get(i);
    			mapping.put("textTime", code.getTimestampText());
    			mapping.put("textStatus", Order.getStatusText(code.getOrder().getStatus()));
    			mapping.put("textOrders", code.getOrdersText());
    			codes.add(mapping);
    		}
    		SimpleAdapter adapter = new SimpleAdapter(this,
    				codes, R.layout.list_item_code, from, to);
    		adapter.setViewBinder(new VendorBinder());
    		if (listCodes == null)
    			initializeLayout();
    		listCodes.setAdapter(adapter);
    		listCodes.setOnItemClickListener(new LocationItemListener(this));
    		adapter.notifyDataSetChanged();
    	/*}
    	else
    	{
    		setContentView(R.layout.misc_no_locations);
    	}*/
	}
	
	public static class LocationItemListener implements OnItemClickListener
	{
		//private FlashCodes activity;
		
		public LocationItemListener(FlashCodes activity)
		{
			//this.activity = activity;
		}
		
		public void onItemClick(AdapterView<?> adapter, View v, int item,
				long row)
		{
			/*
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
			}*/
		}
	}
}
