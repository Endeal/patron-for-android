/**
 * The Menu screen.
 * @author James Whiteman
 */

package com.patron.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Runnable;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.content.Context;

import com.patron.bind.ProductBinder;
import com.patron.db.ItemConnector;
import com.patron.listeners.ButtonCategoriesListener;
import com.patron.listeners.ButtonCheckoutListener;
import com.patron.listeners.ButtonFavoritesListener;
import com.patron.listeners.ListItemMenuAddListener;
import com.patron.lists.ListFonts;
import com.patron.lists.ListLinks;
import com.patron.model.Item;
import com.patron.system.Globals;
import com.patron.system.Loadable;

public class FlashMenu extends ActionBarActivity implements Loadable
{
	// The layout elements.
	private ListView listMenu;
	private Button buttonCheckout;
	private Button buttonFavorites;
	private ArrayList<Button> buttonCategories = new ArrayList<Button>(); 
	private View viewLoading;
	private View viewMenu;
	private View viewNone;

	// Setters
	public void setListMenu(ListView listMenu)
	{
		this.listMenu = listMenu;
	}

	public void setButtonCheckout(Button buttonCheckout)
	{
		this.buttonCheckout = buttonCheckout;
	}

	public void setButtonFavorites(Button buttonFavorites)
	{
		this.buttonFavorites = buttonFavorites;
	}

	public void setButtonCategories(ArrayList<Button> buttonCategories)
	{
		this.buttonCategories = buttonCategories;
	}
	
	// Getters
	public ListView getListMenu()
	{
		return listMenu;
	}

	public Button getButtonCheckout()
	{
		return buttonCheckout;
	}

	public Button getButtonFavorites()
	{
		return buttonFavorites;
	}

	public ArrayList<Button> getButtonCategories()
	{
		return buttonCategories;
	}
	
	// Activity Methods
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		viewLoading = inflater.inflate(R.layout.misc_loading, null);
		viewMenu = inflater.inflate(R.layout.layout_menu, null);
		viewNone = inflater.inflate(R.layout.misc_no_items, null);
		setContentView(viewLoading);
		beginLoading();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menuItemSearch:
			if (Globals.getVendor() != null &&
			Globals.getVendor().getItems() != null &&
			!Globals.getVendor().getItems().isEmpty())
			{
				Intent intent = new Intent(this, FlashSearchItems.class);
				startActivity(intent);
			}
			else
			{
				message("There are no items to search.");
			}
			return true;
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
	
	// Loadable
	public void beginLoading()
	{
		// Find the views.
		listMenu = (ListView) viewMenu.findViewById(R.id.menuListItems);
		buttonCheckout = (Button) viewMenu.findViewById(R.id.menuButtonCheckout);
		buttonFavorites = (Button) viewMenu.findViewById(R.id.menuButtonFavorites);
		
		// Set the custom fonts.
		Typeface typeface = Typeface.createFromAsset(getAssets(), ListFonts.FONT_MAIN_BOLD);
		buttonCheckout.setTypeface(typeface);
		buttonFavorites.setTypeface(typeface);
		
		// Set button listeners
		listMenu.setOnItemClickListener(new ListItemMenuAddListener());
		buttonCheckout.setOnClickListener(new ButtonCheckoutListener(this));
		buttonFavorites.setOnClickListener(new ButtonFavoritesListener(this));
		
		load();
	}
	
	public void load()
	{
		ItemConnector itemConnector = new ItemConnector(this,
				getSharedPreferences("favoriteItems", MODE_PRIVATE));
		try
		{
			URL url = new URL(ListLinks.LINK_GET_ITEMS);
			itemConnector.execute(url);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}

	public void endLoading()
	{
		// Update the categories
		if (Globals.getCategories() != null && !Globals.getCategories().isEmpty())
		{
			LinearLayout linearLayout = (LinearLayout)viewMenu.findViewById(R.id.menuLayoutTypes);

			for (int i = 0; i < Globals.getCategories().size(); i++)
			{
				Button button = new Button(linearLayout.getContext());
				button.setBackgroundResource(R.drawable.button_filter_off);
				button.setTextAppearance(this, R.style.StyleMenuButtonFavorites);
				float width = Globals.convertDpToPixel(60, this);
				float height = Globals.convertDpToPixel(60, this);
				LayoutParams params = new LayoutParams((int)width, (int)height);
				params.gravity = Gravity.CENTER;
				params.setMargins(10, 10, 10, 10);
				button.setLayoutParams(params);
				button.setText(Globals.getCategories().get(i).getName());
				button.setOnClickListener(new ButtonCategoriesListener(this, Globals.getCategories().get(i)));

				linearLayout.addView(button);
				buttonCategories.add(button);
			}
		}

		// Update the list view
		if (Globals.getVendor() != null &&
				Globals.getVendor().getItems() != null &&
				!Globals.getVendor().getItems().isEmpty())
		{
			setContentView(viewMenu);
		}
		else
		{
			setContentView(viewNone);
		}
		update();
	}
	
	public void update()
	{	
		// Set main list items to a list of drinks.
		//Globals.getVendor().setFilteredItems(Globals.getVendor().getItems());
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
    			mapping.put("toggleButtonFavorite", item.getId());
    			mapping.put("layout", item.getId());
    			products.add(mapping);
    		}
    		SimpleAdapter adapter = new SimpleAdapter(this, products, R.layout.list_item_product, from, to);
    		adapter.setViewBinder(new ProductBinder());
    		listMenu.setAdapter(adapter);
    	}
    	else
    	{
    		setContentView(viewNone);
    	}
	}
	
	public void message(String msg)
	{
		final String message = msg;
		final Context context = (Context)this;
		runOnUiThread(new Runnable() {
			public void run()
			{
				Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}
}
