package com.flashvip.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.flashvip.listeners.ListMainListeners;
import com.flashvip.lists.ListMain;
import com.flashvip.lists.ListScreens;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class FlashClient extends ActionBarActivity
{
	// Main XML Elements
	public static LinearLayout layout_list; // Layout holding everything.
	public static ListView list_main; // Main list
	public static TextView text_server_name; // Title
	public static Button button_tab; // Tab Button
	public static EditText search_drinks; // Search Bar
	public static ImageButton imgbut_search; // Search Button
	
	// Profile Menu Elements
	public static Button buttonProfileMenu; // Go to Server Menu
	public static Button buttonProfileCart; // Go to Cart
	public static Button buttonProfileCodes; // Go to Codes
	public static Button buttonProfileFavorites; // Go to Favorites
	
	// Shortcut Buttons
	public static ImageButton imgbut_beer; // Beers button
	public static ImageButton imgbut_cocktail; // Cocktails button
	public static ImageButton imgbut_shot; // Shots button
	public static ImageButton imgbut_martini; // Martinis button
	
	// Loading Widgets
	public static ProgressBar progress_main; // Main HTTP connection progress bar.
	
	// Sorting Widgets
	public static Spinner drop_sort_servers;
	public static Spinner drop_sort_order;
	
	// List Adapters
	public static ArrayAdapter<String> adapter_text; // adapter for plain text.
	public static ArrayAdapter<CharSequence> adapter_spinner_drinks; // Adapter for spinners of drink properties.
	public static ArrayAdapter<CharSequence> adapter_spinner_sort; // Adapter for spinners of sorting widgets.
	public static SimpleAdapter adapter_drinks; // Adapter for a list of drinks.
	public static SimpleAdapter adapter_servers; // Adapter for a list of servers.
	
	// Drinks List Adapter Layout Elements
	public static ImageButton imgbut_add; // Image Button used to add drink to tab.
	public static TextView text_name; // Name text of drink list item.
	public static TextView text_price; // Price text of drink list item.
	public static ToggleButton cbox_favorite; // Checkbox to Favorite a drink list item.
	public static Spinner spinner_alcohol; // Spinner to select alcohol for a drink list item.
	public static Spinner spinner_quantity; // Spinner to select quantity of a drink list item.
	public static TextView text_alcohol; // Alcohol text of a drink list item.
	public static TextView text_type; // Type of drink text of a drink list item.
	

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Flash VIP");
        Globals.setContext(this);
        setContentView(R.layout.layout_home);
        setXmlElements();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        
        AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
        UAirship.takeOff(this.getApplication(), options);
        PushManager.enablePush();
        
        String apid = PushManager.shared().getAPID();
        Logger.info("My Application onCreate - App APID: " + apid);
        //updateAll();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	super.onWindowFocusChanged(hasFocus);
    	buttonProfileMenu.getLayoutParams().height = buttonProfileMenu.getWidth();
    	buttonProfileCart.getLayoutParams().height = buttonProfileCart.getWidth();
    	buttonProfileCodes.getLayoutParams().height = buttonProfileCodes.getWidth();
    	buttonProfileFavorites.getLayoutParams().height = buttonProfileFavorites.getWidth();
    	buttonProfileMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				Intent intent = new Intent(getActivity(), FlashMenu.class);
				getActivity().startActivity(intent);
			}
    	});
    	ImageView locations = (ImageView)findViewById(R.id.homeImageProfileServer);
    	locations.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), FlashLocations.class);
				getActivity().startActivity(intent);
			}
    	});
    }
    
    public Activity getActivity()
    {
    	return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	if (keyCode == KeyEvent.KEYCODE_BACK)
    	{
    		if (Globals.getCurrentScreen() != 0)
    		{
    			Globals.goToPreviousScreen();
    			updateAll();
    		}
    		else
    			super.onKeyDown(keyCode, event);
    	}
    	else if (keyCode == KeyEvent.KEYCODE_SEARCH && (
    			Globals.getCurrentScreen() == ListScreens.SCREEN_MAIN ||
    			Globals.getCurrentScreen() == ListScreens.SCREEN_CHOOSE_BAR
    			))
    	{
    		
    		if (!FlashClient.search_drinks.isFocused())
    		{
    			FlashClient.search_drinks.requestFocus();
    			InputMethodManager imm = (InputMethodManager)getSystemService(
    					Context.INPUT_METHOD_SERVICE);
    			imm.showSoftInput(search_drinks, 0);
    		}
    	}
    	return false;
    }
    
    public void setXmlElements()
    {
    	// Main XML Elements
    	layout_list = (LinearLayout) findViewById(R.id.linearLayout2);
    	list_main = (ListView) findViewById(R.id.list_home);;
    	text_server_name = (TextView) findViewById(R.id.bar_name);
    	search_drinks = (EditText) findViewById(R.id.SearchBar1);
    	imgbut_search = (ImageButton) findViewById(R.id.imgbut_search);
    	button_tab = (Button) findViewById(R.id.tab_button);
    	
    	// Profile Screen
    	buttonProfileMenu = (Button) findViewById(R.id.homeButtonMenu);
    	buttonProfileCart = (Button) findViewById(R.id.homeButtonCart);
    	buttonProfileCodes = (Button) findViewById(R.id.homeButtonCodes);
    	buttonProfileFavorites = (Button) findViewById(R.id.homeButtonFavorites);
    	
    	// Shortcut Buttons
    	imgbut_beer = (ImageButton) findViewById(R.id.beer_imgbut);
    	imgbut_cocktail = (ImageButton) findViewById(R.id.cocktail_imgbut);
    	imgbut_shot = (ImageButton) findViewById(R.id.shot_imgbut);
    	imgbut_martini = (ImageButton) findViewById(R.id.martini_imgbut);
    	
    	// Loading Widgets
    	progress_main = (ProgressBar) findViewById(R.id.progress_bar_1);
    	
    	// Spinners
    	drop_sort_servers = (Spinner) findViewById(R.id.drop_sort_servers);
    	drop_sort_order = (Spinner) findViewById(R.id.drop_sort_order);
    	
    	// Drink Item XML Elements
    	text_name = (TextView) ((Activity)Globals.getContext()).findViewById(R.id.productListItemTextName);
    	text_price = (TextView) ((Activity)Globals.getContext()).findViewById(R.id.productListItemTextPrice);
    	cbox_favorite = (ToggleButton) ((Activity)Globals.getContext()).findViewById(R.id.productListItemToggleButtonFavorite);
    	spinner_alcohol = (Spinner) ((Activity)Globals.getContext()).findViewById(R.id.productListItemSpinnerAlcohol);
    	text_alcohol = (TextView) ((Activity)Globals.getContext()).findViewById(R.id.productListItemTextAlcohol);
    	text_type = (TextView) ((Activity)Globals.getContext()).findViewById(R.id.productListItemTextType);
    	
//    	imgbut_search.setOnClickListener(new SearchButtonListener());
//    	button_tab.setOnClickListener(new ButtonTabListener());
//    	text_server_name.setOnClickListener(new HomeButtonListener());
    }
    
    public static void createAdapters()
    { 
    	adapter_spinner_drinks = ArrayAdapter.createFromResource(Globals.getContext(),
    			R.array.array_default_alcohols, R.layout.spinner_alcohol_layout);
    }
    
    public static void setXmlDrinkItemElements()
    {
    	if (spinner_alcohol != null && spinner_quantity != null)
    	{
    		spinner_alcohol.setAdapter(adapter_spinner_drinks);
    		spinner_quantity.setAdapter(adapter_spinner_drinks);
    	}
    }
    
    public static void beginLoading()
    {
    	list_main.setVisibility(ListView.GONE);
		progress_main.setVisibility(ListView.VISIBLE);
		updateAll();
    }
    
    public static void endLoading()
    {
    	progress_main.setVisibility(ListView.GONE);
    	list_main.setVisibility(ListView.VISIBLE);
    	updateAll();
    }
    
    public static void updateAll()
    {
    	updateListViewAdapter();
    	updateSorters();
    	text_server_name.setText(Globals.getServerName());
    }
    
    public static void updateListViewAdapter()
    {
    	switch (Globals.getCurrentScreen())
    	{
    	case ListScreens.SCREEN_CHOOSE_BAR:
    		updateServersAdapter();
    		list_main.setOnItemClickListener(ListMainListeners.getMainListener());
    		break;
    	case ListScreens.SCREEN_SEARCH_SERVERS:
    		updateServersAdapter();
    		list_main.setOnItemClickListener(ListMainListeners.getMainListener());
    		break;
    	case ListScreens.SCREEN_BROWSE_DRINKS:
    		updateDrinksAdapter();
    		list_main.setOnItemClickListener(ListMainListeners.getMainListener());
    		break;
    	case ListScreens.SCREEN_MY_TOP:
    		updateDrinksAdapter();
    		list_main.setOnItemClickListener(ListMainListeners.getMainListener());
    		break;
    	case ListScreens.SCREEN_TAB:
    		updateTabAdapter();
    		list_main.setOnItemClickListener(ListMainListeners.getMainListener());
    		break;
    	case ListScreens.SCREEN_BAR_TOP:
    		updateDrinksAdapter();
    		list_main.setOnItemClickListener(ListMainListeners.getMainListener());
    		break;
    	default:
    		updateTextAdapter();
    		list_main.setOnItemClickListener(ListMainListeners.getMainListener());
    		break;
    	}
    	setXmlDrinkItemElements();
    }
    
    public static void updateTextAdapter()
    {
    	/* Set main list adapter.
    	adapter_text = new ArrayAdapter<String>(Globals.getContext(),
    			R.layout.list_main_layout, R.id.list_main_text,
    			ListMain.getItems(Globals.getCurrentScreen()));
    	list_main.setAdapter(adapter_text);*/
    }
    
    public static void updateServersAdapter()
    {
    	// Set main list items to a list of locations.
    	if (Globals.getServers() != null && !Globals.getServers().isEmpty())
    	{
    		List<Map<String, String>> servers = new ArrayList<Map<String, String>>();
    		String[] from = {"name", "phone", "address"};
    		int[] to = {R.id.locationListItemTextName, R.id.locationListItemTextPhone,
    				R.id.locationListItemTextAddress};
    		for (int i = 0; i < Globals.getServers().size(); i++)
    		{
    			Map<String, String> mapping = new HashMap<String, String>();
    			Server currentServer = Globals.getServers().get(i);
    			mapping.put("name", currentServer.getName());
    			mapping.put("phone", currentServer.getPhone());
    			mapping.put("address", currentServer.getAddress() + ", " +
    					currentServer.getCity() + ", " + currentServer.getState() +
    					currentServer.getZip());
    			servers.add(mapping);
    		}
    		adapter_servers = new SimpleAdapter(Globals.getContext(),
    				servers, R.layout.list_item_location, from, to);
    		list_main.setAdapter(adapter_servers);
    	}
    }
    
    public static void updateDrinksAdapter()
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
    				"spinnerAlcohol"};
    		
    		int[] to = {R.id.productListItemTextName,
    				R.id.productListItemTextPrice,
    				R.id.productListItemTextAlcohol,
    				R.id.productListItemTextType,
    				R.array.array_quantity,
    				R.id.productListItemSpinnerAlcohol};
    		
    		for (int i = 0; i < Globals.getAllOrders().size(); i++)
    		{
        		Map<String, String> mapping = new HashMap<String, String>();
    			Drink currentDrink = Globals.getAllOrders().get(i);
    			mapping.put("name", currentDrink.getName());
    			mapping.put("price", "$" + currentDrink.getPrice());
    			mapping.put("type", currentDrink.getType().toString());
    			mapping.put("alcohol", currentDrink.getAlcohol().toString());
    			mapping.put("orders", currentDrink.getId().toString());
    			mapping.put("spinnerAlcohol", currentDrink.getId());
    			mapping.put("spinnerQuantity", currentDrink.getId());
    			drinks.add(mapping);
    		}
    		adapter_drinks = new SimpleAdapter(Globals.getContext(),
    				drinks, R.layout.list_item_product, from, to);
    		adapter_drinks.setViewBinder(new DrinkBinder());
    		list_main.setAdapter(adapter_drinks);
    	}
    }
    
    public static void updateTabAdapter()
    {
    	// Set main list items to a list of drinks.
    	if (Globals.getTabDrinks() != null && !Globals.getTabDrinks().isEmpty())
    	{
    		List<Map<String, String>> drinks = new ArrayList<Map<String, String>>();
    		String[] from = {"name", "price", "type", "alcohol", "delete", "alcohol_pos",
    				"quantity_pos"};
    		int[] to = {R.id.cartListItemTextName, R.id.cartListItemTextPrice, R.id.cartListItemTextType,
    				R.id.cartListItemTextAlcohol, R.id.cartListItemImageButtonDelete, R.id.cartListItemSpinnerAlcohol,
    				R.id.cartListItemSpinnerQuantity};
    		for (int i = 0; i < Globals.getTabDrinks().size(); i++)
    		{
        		Map<String, String> mapping = new HashMap<String, String>();
    			TabDrink currentDrink = Globals.getTabDrinks().get(i);
    			mapping.put("name", currentDrink.getDrink().getName());
    			mapping.put("price", "$" + currentDrink.getDrink().getPrice());
    			mapping.put("type", currentDrink.getDrink().getType().toString());
    			mapping.put("alcohol", currentDrink.getDrink().getAlcohol().name().toLowerCase());
    			mapping.put("delete", String.valueOf(i));
    			mapping.put("alcohol_pos", currentDrink.getDrink().getId() + ";" + i);
    			mapping.put("quantity_pos", "" + i);
    			drinks.add(mapping);
    		}
    		adapter_drinks = new SimpleAdapter(Globals.getContext(),
    				drinks, R.layout.list_item_cart, from, to);
    		adapter_drinks.setViewBinder(new TabDrinkBinder());
    		list_main.setAdapter(adapter_drinks);
    	}
    }
    
    public static void updateSorters()
    {
    	// Set up sorting spinners if the screens require it.
    	if (Globals.getCurrentScreen() == ListScreens.SCREEN_CHOOSE_BAR ||
    			Globals.getCurrentScreen() == ListScreens.SCREEN_SEARCH_SERVERS)
    	{
    		ArrayAdapter<CharSequence> sortServersAdapter = ArrayAdapter.createFromResource(
    				Globals.getContext(), R.array.array_sort_servers, R.layout.spinner_layout);
    		ArrayAdapter<CharSequence> sortOrderAdapter = ArrayAdapter.createFromResource(
    				Globals.getContext(), R.array.array_sort_order, R.layout.spinner_layout);
    		drop_sort_servers.setAdapter(sortServersAdapter);
    		drop_sort_order.setAdapter(sortOrderAdapter);
    		drop_sort_servers.setVisibility(View.VISIBLE);
    		drop_sort_order.setVisibility(View.VISIBLE);
    	}
    	else if (Globals.getCurrentScreen() == ListScreens.SCREEN_MY_TOP ||
    			Globals.getCurrentScreen() == ListScreens.SCREEN_SEARCH_DRINKS ||
    			Globals.getCurrentScreen() == ListScreens.SCREEN_BROWSE_DRINKS ||
    			Globals.getCurrentScreen() == ListScreens.SCREEN_TAB)
    	{
    		ArrayAdapter<CharSequence> sortDrinksAdapter = ArrayAdapter.createFromResource(
    				Globals.getContext(), R.array.array_sort_drinks, R.layout.spinner_layout);
    		ArrayAdapter<CharSequence> sortOrderAdapter = ArrayAdapter.createFromResource(
    				Globals.getContext(), R.array.array_sort_order, R.layout.spinner_layout);
    		drop_sort_servers.setAdapter(sortDrinksAdapter);
    		drop_sort_order.setAdapter(sortOrderAdapter);
    		drop_sort_servers.setVisibility(View.VISIBLE);
    		drop_sort_order.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		drop_sort_servers.setVisibility(View.GONE);
    		drop_sort_order.setVisibility(View.GONE);
    	}
    }
    
    public static void updateListViewChoiceMode(Context context, int mode)
    {
    	adapter_text = new ArrayAdapter<String>(context,
    			mode, ListMain.getItems(Globals.getCurrentScreen()));
    	list_main.setAdapter(adapter_text);
    	list_main.setOnItemClickListener(ListMainListeners.getMainListener());
    }
}