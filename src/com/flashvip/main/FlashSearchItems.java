package com.flashvip.main;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.flashvip.bind.ProductBinder;
import com.flashvip.listeners.ListItemMenuAddListener;
import com.flashvip.model.Item;
import com.flashvip.sort.ProductSorter;
import com.flashvip.system.Globals;
import com.flashvip.system.Loadable;

public class FlashSearchItems extends ActionBarActivity implements Loadable
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
		viewMain = inflater.inflate(R.layout.layout_search_items, null);
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
		listMain = (ListView) viewMain.findViewById(R.id.searchItemsListMain);
		editTextMain = (EditText) viewMain.findViewById(R.id.searchItemsEditTextMain);
		editTextMain.setText("");
		
		// Search Bar Listener
		editTextMain.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				List<Item> items = ProductSorter.getBySearch(Globals.getVendor().getItems(), text);
				Globals.getVendor().setFilteredItems(items);
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
		
		// Main List Listener
		listMain.setOnItemClickListener(new ListItemMenuAddListener());
		
		load();
	}
	
	public void load()
	{
		endLoading();
	}
	
	public void endLoading()
	{
		Globals.getVendor().setFilteredItems(Globals.getVendor().getItems());
		update();
	}

	public void update()
	{
		// Set main list items to a list of drinks.
    	if (Globals.getVendor() != null && Globals.getVendor().getFilteredItems() != null)
    	{
    		List<Map<String, String>> products = new ArrayList<Map<String, String>>();
    		
    		String[] from = {"name",
    				"price",
    				"categories",
    				"toggleButtonFavorite",
    				"layout"};
    		
    		int[] to = {R.id.productListItemTextName,
    				R.id.productListItemTextPrice,
    				R.id.productListItemTextCategories,
    				R.id.productListItemToggleButtonFavorite,
    				R.id.productListItemLayout};
    		
    		for (int i = 0; i < Globals.getVendor().getFilteredItems().size(); i++)
    		{	
        		Map<String, String> mapping = new HashMap<String, String>();
    			Item item = Globals.getVendor().getFilteredItems().get(i);
    			NumberFormat formatter = NumberFormat.getCurrencyInstance();
    			String price = formatter.format(item.getPrice());
    			String categoriesText = "";
    			for (int j = 0; j < item.getCategories().size(); j++)
    			{
    				if (!categoriesText.equals(""))
    				{
    					categoriesText = categoriesText + "\n";
    				}
    				categoriesText = categoriesText + item.getCategories().get(j).getName();
    			}
    			mapping.put("name", item.getName());
    			mapping.put("price", price);
    			mapping.put("categories", categoriesText);
    			mapping.put("toggleButtonFavorite", item.getItemId());
    			mapping.put("layout", item.getItemId());
    			products.add(mapping);
    		}
    		SimpleAdapter adapter = new SimpleAdapter(this, products, R.layout.list_item_product, from, to);
    		adapter.setViewBinder(new ProductBinder());
    		listMain.setAdapter(adapter);
    	}
	}
	
	public void message(String msg)
	{
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
