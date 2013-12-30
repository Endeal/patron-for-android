package com.flashvip.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flashvip.system.Globals;
import com.flashvip.system.Loadable;

public class FlashHome extends ActionBarActivity implements Loadable
{
	// Layout elements.
	public static TextView textLocation; // The bar's name.
	public static TextView textUser; // The user's name.
	public static Button buttonHomeVendors; // Go to Vendors
	public static Button buttonHomeMenu; // Go to Menu
	public static Button buttonHomeCodes; // Go to Codes
	public static Button buttonHomeFavorites; // Go to Favorite Orders
	public static View viewMain;
	
	// Activity methods.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Flash VIP");
        LayoutInflater inflater = LayoutInflater.from(this);
        viewMain = inflater.inflate(R.layout.layout_home, null);
        setContentView(viewMain);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	beginLoading();
    	super.onWindowFocusChanged(hasFocus);
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
    	{
    	case R.id.menuItemSettings:
    		message("Settings.");
    		return true;
    	case R.id.menuItemHelp:
    		message("Help");
    		return true;
    	default:
    		return false;
    	}
    }
    
    // Loadable methods
    public void beginLoading()
    {
    	// Main XML Elements
    	textLocation = (TextView) viewMain.findViewById(R.id.homeTextLocation);
    	textUser = (TextView) viewMain.findViewById(R.id.homeTextUser);

    	buttonHomeVendors = (Button) viewMain.findViewById(R.id.homeButtonVendors);
    	buttonHomeMenu = (Button) viewMain.findViewById(R.id.homeButtonMenu);
    	buttonHomeCodes = (Button) viewMain.findViewById(R.id.homeButtonCodes);
    	buttonHomeFavorites = (Button) viewMain.findViewById(R.id.homeButtonFavorites);
    	
    	buttonHomeVendors.getLayoutParams().height = buttonHomeVendors.getWidth();
    	buttonHomeMenu.getLayoutParams().height = buttonHomeMenu.getWidth();
    	buttonHomeCodes.getLayoutParams().height = buttonHomeCodes.getWidth();
    	buttonHomeFavorites.getLayoutParams().height = buttonHomeFavorites.getWidth();
    	
    	load();
    }
    
    // Layout methods.
    public void load()
    {
    	// Go to change vendor.
    	buttonHomeVendors.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), FlashVendors.class);
				v.getContext().startActivity(intent);
			}
		});
    	
    	// Go to buy items
    	buttonHomeMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				Intent intent = new Intent(view.getContext(), FlashMenu.class);
				view.getContext().startActivity(intent);
			}
    	});
    	
    	// Go to codes
    	buttonHomeCodes.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				Intent intent = new Intent(view.getContext(), FlashCodes.class);
				view.getContext().startActivity(intent);
			}
    	});
    	
    	update();
    }
    
    public void endLoading()
    {
    	
    }
    
    public void update()
    {
    	if (Globals.getVendor() != null &&
    			Globals.getVendor().getName() != null)
    	textLocation.setText(Globals.getVendor().getName());
    }
    
    public void message(String msg)
    {
    	Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
    	toast.show();
    }
}