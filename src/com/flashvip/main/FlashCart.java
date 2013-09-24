/**
 * The Cart screen.
 */
package com.flashvip.main;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flashvip.listeners.ButtonFinishListener;

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
        getMenuInflater().inflate(R.menu.activity_main, menu);
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
    	if (Globals.getCartProducts() != null && !Globals.getCartProducts().isEmpty())
    	{
    		List<Map<String, String>> products = new ArrayList<Map<String, String>>();
    		
    		String[] from = {"name",
    				"price",
    				"alcohol",
    				"type",
    				"orders",
    				"spinnerAlcohol",
    				"spinnerQuantity",
    				"buttonRemove"};
    		
    		int[] to = {R.id.cartListItemTextName,
    				R.id.cartListItemTextPrice,
    				R.id.cartListItemTextAlcohol,
    				R.id.cartListItemTextType,
    				R.array.array_quantity,
    				R.id.cartListItemSpinnerAlcohol,
    				R.id.cartListItemSpinnerQuantity,
    				R.id.cartListItemButtonRemove};
    		
    		for (int i = 0; i < Globals.getCartProducts().size(); i++)
    		{	
        		Map<String, String> mapping = new HashMap<String, String>();
    			CartProduct currentProduct = Globals.getCartProducts().get(i);
    			NumberFormat formatter = NumberFormat.getCurrencyInstance();
    			String price = formatter.format(currentProduct.getPrice());
    			mapping.put("name", currentProduct.getDrink().getName());
    			mapping.put("price", price);
    			mapping.put("type", Product.getTypeName(currentProduct.getDrink().getType()));
    			mapping.put("alcohol", Product.getAlcoholName(currentProduct.getDrink().getAlcohol()));
    			mapping.put("orders", currentProduct.getDrink().getId().toString());
    			mapping.put("spinnerAlcohol", i + "");
    			mapping.put("spinnerQuantity", i + "");
    			mapping.put("buttonRemove", i + "");
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
