/**
 * The Menu screen.
 * @author James Whiteman
 */

package com.flashvip.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flashvip.lists.ListFonts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FlashMenu extends ActionBarActivity
{
	// The layout elements.
	ListView listMenu;
	Button buttonCheckout;
	Button buttonFavorites;
	Button buttonBeer;
	Button buttonShots;
	Button buttonCocktails;
	Button buttonMartinis;
	Button buttonWine;
	
	// Activity Methods
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_menu);
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
		Typeface typeface = Typeface.createFromAsset(getAssets(), ListFonts.FONT_MAIN_BOLD);
		
		// Find the views.
		listMenu = (ListView) findViewById(R.id.menuListItems);
		buttonCheckout = (Button) findViewById(R.id.menuButtonCheckout);
		buttonFavorites = (Button) findViewById(R.id.menuButtonFavorites);
		buttonBeer = (Button) findViewById(R.id.menuButtonBeer);
		buttonShots = (Button) findViewById(R.id.menuButtonShots);
		buttonCocktails = (Button) findViewById(R.id.menuButtonCocktails);
		buttonMartinis = (Button) findViewById(R.id.menuButtonMartinis);
		buttonWine = (Button) findViewById(R.id.menuButtonWine);
		
		// Set the custom fonts.
		
		buttonCheckout.setTypeface(typeface);
		buttonFavorites.setTypeface(typeface);
		buttonBeer.setTypeface(typeface);
		buttonShots.setTypeface(typeface);
		buttonCocktails.setTypeface(typeface);
		buttonMartinis.setTypeface(typeface);
		buttonWine.setTypeface(typeface);
		
		initializeMenu();
		initializeButtonListeners();
	}
	
	private void initializeMenu()
	{
		// Set main list items to a list of drinks.
    	if (Globals.getAllOrders() != null && !Globals.getAllOrders().isEmpty())
    	{
    		List<Map<String, String>> drinks = new ArrayList<Map<String, String>>();
    		
    		String[] from = {"name",
    				"price",
    				"alcohol",
    				"type",
    				"orders",
    				"spinnerAlcohol",
    				"spinnerQuantity"};
    		
    		int[] to = {R.id.productListItemTextName,
    				R.id.productListItemTextPrice,
    				R.id.productListItemTextAlcohol,
    				R.id.productListItemTextType,
    				R.array.array_quantity,
    				R.id.productListItemSpinnerAlcohol,
    				R.id.productListItemSpinnerQuantity};
    		
    		for (int i = 0; i < Globals.getAllOrders().size(); i++)
    		{
        		Map<String, String> mapping = new HashMap<String, String>();
    			Drink currentDrink = Globals.getAllOrders().get(i);
    			mapping.put("name", currentDrink.getName());
    			mapping.put("price", "$" + currentDrink.getPrice());
    			mapping.put("type", currentDrink.getType().toString());
    			mapping.put("alcohol", currentDrink.getAlcohol().toString());
    			mapping.put("orders", currentDrink.getId().toString());
    			mapping.put("add", currentDrink.getId());
    			mapping.put("spinnerAlcohol", currentDrink.getId());
    			mapping.put("spinnerQuantity", currentDrink.getId());
    			drinks.add(mapping);
    		}
    		SimpleAdapter adapter = new SimpleAdapter(Globals.getContext(),
    				drinks, R.layout.list_item_product, from, to);
    		adapter.setViewBinder(new DrinkBinder());
    		listMenu.setAdapter(adapter);
    	}
    	else
    	{
    		setContentView(R.layout.misc_no_items);
    	}
	}
	
	private void initializeButtonListeners()
	{
		buttonCheckout.setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), FlashCart.class);
				getActivity().startActivity(intent);
			}
		});
	}
	
}
