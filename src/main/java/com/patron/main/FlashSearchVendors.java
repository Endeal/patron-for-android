package com.patron.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.patron.bind.VendorBinder;
import com.patron.listeners.ListItemVendorListener;
import com.patron.model.Vendor;
import com.patron.R;
import com.patron.sort.LocationSorter;
import com.patron.system.Globals;
import com.patron.system.Loadable;

public class FlashSearchVendors extends Activity implements Loadable
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
		switch (item.getItemId())
    	{
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

	// Loading
	public void beginLoading()
	{
		listMain = (ListView) viewMain.findViewById(R.id.searchVendorsListMain);
		editTextMain = (EditText) viewMain.findViewById(R.id.searchVendorsEditTextMain);
		editTextMain.setText("");

		// Set the Search Listener
		editTextMain.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				List<Vendor> vendors = LocationSorter.getBySearch(Globals.getVendors(), text);
				Globals.setFilteredVendors(vendors);
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

		// Set the Vendor List Item Listener
		listMain.setOnItemClickListener(new ListItemVendorListener());
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
    		adapter.notifyDataSetChanged();
    	}
	}

	public void message(String msg)
	{
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
