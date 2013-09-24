/**
 * The Menu screen.
 * @author James Whiteman
 */

package com.flashvip.main;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flashvip.listeners.ButtonCheckoutListener;
import com.flashvip.listeners.ButtonFavoritesListener;
import com.flashvip.listeners.ButtonTypesListener;
import com.flashvip.listeners.ListItemMenuAddListener;
import com.flashvip.lists.ListFonts;
import com.flashvip.main.Product.Type;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
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
		
		updateListViewMenu();
		initializeButtonListeners();
	}
	
	public void updateListViewMenu()
	{
		// Set main list items to a list of drinks.
    	if (Globals.getCurrentProducts() != null && !Globals.getCurrentProducts().isEmpty())
    	{
    		List<Map<String, String>> products = new ArrayList<Map<String, String>>();
    		
    		String[] from = {"name",
    				"price",
    				"alcohol",
    				"type",
    				"orders",
    				"spinnerAlcohol",
    				"spinnerQuantity",
    				"toggleButtonFavorite"};
    		
    		int[] to = {R.id.productListItemTextName,
    				R.id.productListItemTextPrice,
    				R.id.productListItemTextAlcohol,
    				R.id.productListItemTextType,
    				R.array.array_quantity,
    				R.id.productListItemSpinnerAlcohol,
    				R.id.productListItemSpinnerQuantity,
    				R.id.productListItemToggleButtonFavorite};
    		
    		for (int i = 0; i < Globals.getCurrentProducts().size(); i++)
    		{	
        		Map<String, String> mapping = new HashMap<String, String>();
    			Product currentProduct = Globals.getCurrentProducts().get(i);
    			NumberFormat formatter = NumberFormat.getCurrencyInstance();
    			String price = formatter.format(currentProduct.getPrice());
    			mapping.put("name", currentProduct.getName());
    			mapping.put("price", price);
    			mapping.put("type", Product.getTypeName(currentProduct.getType()));
    			mapping.put("alcohol", Product.getAlcoholName(currentProduct.getAlcohol()));
    			mapping.put("orders", currentProduct.getId().toString());
    			mapping.put("spinnerAlcohol", currentProduct.getId());
    			mapping.put("spinnerQuantity", currentProduct.getId());
    			mapping.put("toggleButtonFavorite", currentProduct.getId());
    			products.add(mapping);
    		}
    		SimpleAdapter adapter = new SimpleAdapter(this,
    				products, R.layout.list_item_product, from, to);
    		adapter.setViewBinder(new ProductBinder());
    		listMenu.setAdapter(adapter);
    		
    	}
    	else
    	{
    		setContentView(R.layout.misc_no_items);
    	}
	}
	
	private void initializeButtonListeners()
	{
		listMenu.setOnItemClickListener(new ListItemMenuAddListener());
		
		buttonCheckout.setOnClickListener(new ButtonCheckoutListener(this));
		buttonFavorites.setOnClickListener(new ButtonFavoritesListener(this));
		buttonBeer.setOnClickListener(new ButtonTypesListener(this, Type.DOMESTIC_BEER));
		buttonCocktails.setOnClickListener(new ButtonTypesListener(this, Type.COCKTAIL));
		buttonShots.setOnClickListener(new ButtonTypesListener(this, Type.SHOT));
		buttonMartinis.setOnClickListener(new ButtonTypesListener(this, Type.MARTINI));
		buttonWine.setOnClickListener(new ButtonTypesListener(this, Type.WINE));
	}
}
