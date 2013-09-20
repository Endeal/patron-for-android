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

import com.flashvip.lists.ListFonts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
    			NumberFormat formatter = NumberFormat.getCurrencyInstance();
    			String price = formatter.format(currentDrink.getPrice());
    			mapping.put("name", currentDrink.getName());
    			mapping.put("price", price);
    			mapping.put("type", Drink.getTypeName(currentDrink.getType()));
    			mapping.put("alcohol", Drink.getAlcoholName(currentDrink.getAlcohol()));
    			mapping.put("orders", currentDrink.getId().toString());
    			mapping.put("spinnerAlcohol", currentDrink.getId());
    			mapping.put("spinnerQuantity", currentDrink.getId());
    			drinks.add(mapping);
    		}
    		SimpleAdapter adapter = new SimpleAdapter(this,
    				drinks, R.layout.list_item_product, from, to);
    		adapter.setViewBinder(new DrinkBinder());
    		listMenu.setAdapter(adapter);
    		if (adapter.areAllItemsEnabled())
    			System.out.println("ALLGOOD");
    		else
    			System.out.println("LOOK OUT!!!");
    		
    	}
    	else
    	{
    		setContentView(R.layout.misc_no_items);
    	}
	}
	
	private void initializeButtonListeners()
	{
		listMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int row,
					long rowId)
			{
				// Get the layout containing this view, the 'Add' button
				RelativeLayout rl = (RelativeLayout) v;
				
				// Get the spinners, which are the 4th and 5th view in the list_item_drink.xml layout file.
				Spinner spinnerAlcohol = (Spinner) rl.getChildAt(5);
				Spinner spinnerQuantity = (Spinner) rl.getChildAt(6);
				
				// Gets the drink for this given row.
				Drink d = Globals.getAllOrders().get(row);
				
				// Creates a TabDrink that has the given drink, gets the spinners position, and quantities position.
				int alcoholRow;
				
				if (spinnerAlcohol == null)
				{
					System.out.println("NO ALCOHOL SPINNER");
				}
				if (spinnerQuantity == null)
				{
					System.out.println("NO QUANTITY SPINNER");
				}
				
				if (spinnerAlcohol.getVisibility() == View.INVISIBLE)
					alcoholRow = 0;
				else
					alcoholRow = spinnerAlcohol.getSelectedItemPosition();
				
				TabDrink td = new TabDrink(d, alcoholRow,
						spinnerQuantity.getSelectedItemPosition());
				
				// Adds the TabDrink that was just created to the tab.
				Globals.addOrderToTab(td);
				
				// Creates a Toast that informs the user that the drink has been added to the tab.
				Toast toast = Toast.makeText(Globals.getContext(),
						"Drink added to tab.", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		
		buttonCheckout.setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), FlashCart.class);
				getActivity().startActivity(intent);
			}
		});
	}
}
