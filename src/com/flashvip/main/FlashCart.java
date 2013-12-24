/**
 * The Cart screen.
 */
package com.flashvip.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flashvip.bind.CartProductBinder;
import com.flashvip.listeners.ButtonFinishListener;
import com.flashvip.model.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FlashCart extends ActionBarActivity
{
	// The layout elements.
	Button buttonFinish;
	ListView listCart;
	
	// Activity Methods
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_cart);
		initializeLayout();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
	
	// Getters & Setters
	public Activity getActivity()
	{
		return this;
	}
	
	// Layout
	private void initializeLayout()
	{
		listCart = (ListView) findViewById(R.id.cartListItems);
		buttonFinish = (Button) findViewById(R.id.cartButtonFinish);
		
		updateListViewCart();
		initializeButtonListeners();
	}
	
	private void initializeButtonListeners()
	{
		buttonFinish.setOnClickListener(new ButtonFinishListener(this));
	}
	
	public void updateListViewCart()
	{
		// Set main list items to a list of drinks.
    	if (Globals.getOrder().getFragments() != null &&
    			!Globals.getOrder().getFragments().isEmpty())
    	{
    		List<Map<String, Fragment>> products = new ArrayList<Map<String, Fragment>>();
    		
    		String[] from = {"name",
    				"price",
    				"quantity",
    				"categories",
    				"buttonRemove",
    				"layout"};
    		
    		int[] to = {R.id.cartListItemTextName,
    				R.id.cartListItemTextPrice,
    				R.id.cartListItemSpinnerQuantity,
    				R.id.cartListItemTextCategories,
    				R.id.cartListItemButtonRemove,
    				R.id.cartListItemLayout};
    		
    		for (int i = 0; i < Globals.getOrder().getFragments().size(); i++)
    		{	
        		Map<String, Fragment> mapping = new HashMap<String, Fragment>();
    			Fragment fragment = Globals.getOrder().getFragments().get(i);
    			mapping.put("name", fragment);
    			mapping.put("price", fragment);
    			mapping.put("quantity", fragment);
    			mapping.put("categories", fragment);
    			mapping.put("buttonRemove", fragment);
    			mapping.put("layout", fragment);
    			products.add(mapping);
    		}
    		SimpleAdapter adapter = new SimpleAdapter(this,
    				products, R.layout.list_item_cart, from, to);
    		adapter.setViewBinder(new CartProductBinder());
    		listCart.setAdapter(adapter);
    	}
    	else
    	{
    		setContentView(R.layout.misc_no_items_cart);
    	}
	}
}
