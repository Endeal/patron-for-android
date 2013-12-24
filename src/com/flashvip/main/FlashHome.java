package com.flashvip.main;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class FlashHome extends ActionBarActivity
{
	// Layout elements.
	public static TextView textLocation; // The bar's name.
	public static TextView textUser; // The user's name.
	public static Button buttonHomeMenu; // Go to Server Menu
	public static Button buttonHomeCart; // Go to Cart
	public static Button buttonHomeCodes; // Go to Codes
	public static Button buttonHomeFavorites; // Go to Favorites
	
	// Activity methods.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Flash VIP");
        setContentView(R.layout.layout_home);
        initializeLayout();
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        
        AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
        UAirship.takeOff(this.getApplication(), options);
        PushManager.enablePush();
        
        String apid = PushManager.shared().getAPID();
        Globals.setDeviceId(apid);
        Logger.info("My Application onCreate - App APID: " + apid);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	super.onWindowFocusChanged(hasFocus);
    	buttonHomeMenu.getLayoutParams().height = buttonHomeMenu.getWidth();
    	buttonHomeCart.getLayoutParams().height = buttonHomeCart.getWidth();
    	buttonHomeCodes.getLayoutParams().height = buttonHomeCodes.getWidth();
    	buttonHomeFavorites.getLayoutParams().height = buttonHomeFavorites.getWidth();
    	buttonHomeMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				Intent intent = new Intent(getActivity(), FlashMenu.class);
				getActivity().startActivity(intent);
			}
    	});
    	buttonHomeCodes.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				Intent intent = new Intent(getActivity(), FlashCodes.class);
				getActivity().startActivity(intent);
			}
    	});
    	ImageView locations = (ImageView)findViewById(R.id.homeImageProfileServer);
    	locations.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), FlashVendors.class);
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
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    
    // Layout methods.
    public void initializeLayout()
    {
    	// Main XML Elements
    	textLocation = (TextView) findViewById(R.id.homeTextLocation);
    	textUser = (TextView) findViewById(R.id.homeTextUser);
    	
    	// Profile Screen
    	buttonHomeMenu = (Button) findViewById(R.id.homeButtonMenu);
    	buttonHomeCart = (Button) findViewById(R.id.homeButtonCart);
    	buttonHomeCodes = (Button) findViewById(R.id.homeButtonCodes);
    	buttonHomeFavorites = (Button) findViewById(R.id.homeButtonFavorites);
    	
    	updateLayout();
    }
    
    public void updateLayout()
    {
    	if (Globals.getVendor() != null &&
    			Globals.getVendor().getName() != null)
    	textLocation.setText(Globals.getVendor().getName());
    }
}