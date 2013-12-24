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

import com.flashvip.bind.ProductBinder;
import com.flashvip.listeners.ButtonCheckoutListener;
import com.flashvip.listeners.ButtonFavoritesListener;
import com.flashvip.listeners.ButtonCategoriesListener;
import com.flashvip.listeners.ListItemMenuAddListener;
import com.flashvip.lists.ListFonts;
import com.flashvip.model.Item;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.LinearLayout.LayoutParams;

public class FlashMenu extends ActionBarActivity
{
	// The layout elements.
	private ListView listMenu;
	private Button buttonCheckout;
	private Button buttonFavorites;
	private ArrayList<Button> buttonCategories = new ArrayList<Button>(); 

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
		setContentView(R.layout.layout_menu);
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
	
	// )Layout
	private void initializeLayout()
	{
		Typeface typeface = Typeface.createFromAsset(getAssets(), ListFonts.FONT_MAIN_BOLD);
		
		// Set category items to the list of categories.
		if (Globals.getCategories() != null && !Globals.getCategories().isEmpty())
		{
			LinearLayout linearLayout = (LinearLayout)findViewById(R.id.menuLayoutTypes);

			for (int i = 0; i < Globals.getCategories().size(); i++)
			{
				Button button = new Button(linearLayout.getContext());
				button.setBackgroundResource(R.drawable.mainbutton);
				button.setTextAppearance(this, R.style.StyleMenuButtonBeer);
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
		
		// Find the views.
		listMenu = (ListView) findViewById(R.id.menuListItems);
		buttonCheckout = (Button) findViewById(R.id.menuButtonCheckout);
		buttonFavorites = (Button) findViewById(R.id.menuButtonFavorites);
		
		// Set the custom fonts.
		buttonCheckout.setTypeface(typeface);
		buttonFavorites.setTypeface(typeface);
		
		updateListViewMenu();
		initializeButtonListeners();
	}
	
	public void updateListViewMenu()
	{	
		// Set main list items to a list of drinks.
    	if (Globals.getVendor() != null && Globals.getVendor().getFilteredItems() != null &&
    			!Globals.getVendor().getFilteredItems().isEmpty())
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
	}
}
