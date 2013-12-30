package com.flashvip.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.flashvip.bind.VendorBinder;
import com.flashvip.main.FlashVendors.LocationItemListener;
import com.flashvip.model.Vendor;
import com.flashvip.sort.LocationSorter;
import com.flashvip.system.Globals;
import com.flashvip.system.Loadable;

public class FlashSearchVendors extends ActionBarActivity implements Loadable
{
	// The layout elements.
	private ListView listMain;
	private EditText editTextMain;
	private View viewMain;
	
	// Activity
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		viewMain = inflater.inflate(R.layout.layout_search_vendors, null);
		setContentView(viewMain);
		beginLoading();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_main, menu);
	    return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
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
	
	// Loading
	public void beginLoading()
	{
		listMain = (ListView) viewMain.findViewById(R.id.searchVendorsListMain);
		editTextMain = (EditText) viewMain.findViewById(R.id.searchVendorsEditTextMain);
		editTextMain.setText("");
		editTextMain.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				ArrayList<Vendor> vendors = LocationSorter.getBySearch(Globals.getVendors(), text);
				Globals.setFilteredVendors(vendors);
				System.out.println("Text changed: " + vendors.size());
				update();
			}
			
			@Override
			public void beforeTextChanged(CharSequence text, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		update();
	}
	
	public void load()
	{
		
	}
	
	public void endLoading()
	{
		
	}
	
	public void update()
	{
		// Update the list to be the filtered vendors.
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
    		if (Globals.getFilteredVendors() != null)
    		{
    			for (int i = 0; i < Globals.getFilteredVendors().size(); i++)
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
    		}
    		SimpleAdapter adapter = new SimpleAdapter(this,
    				locations, R.layout.list_item_location, from, to);
    		adapter.setViewBinder(new VendorBinder());
    		listMain.setAdapter(adapter);
    		listMain.setOnItemClickListener(new LocationItemListener());
    		adapter.notifyDataSetChanged();
    	}
	}
	
	public void message(String msg)
	{
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	// List item listener.
	public static class CodeItemListener implements OnItemClickListener
	{
		private Activity activity;
		
		public CodeItemListener(Activity activity)
		{
			this.activity = activity;
		}
		
		public void onItemClick(AdapterView<?> adapter, View v, int item, long row)
		{
			Intent intent = new Intent(activity, FlashScan.class);
			intent.putExtra("orderRow", "" + item);
			activity.startActivity(intent);
		}
	}
}
