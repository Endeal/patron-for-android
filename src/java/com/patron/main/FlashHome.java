package com.patron.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.patron.listeners.DialogTutorialListener;
import com.patron.system.Globals;
import com.patron.system.Loadable;

public class FlashHome extends ActionBarActivity implements Loadable
{
	// Layout elements.
	public static TextView textLocation; // The bar's name.
	public static TextView textUser; // The user's name.
	public static Button buttonHomeVendors; // Go to Vendors
	public static Button buttonHomeMenu; // Go to Menu
	public static Button buttonHomeCodes; // Go to Codes
	public static Button buttonHomeProfile; // Go to Profile
	public static View viewMain;

    // Location services
    private GoogleApiClient googleApiClient;
    private final String TAG = "Patron";
    private LocationRequest locationRequest;

	// Activity methods.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        viewMain = inflater.inflate(R.layout.layout_home, null);
        setContentView(viewMain);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
		/*
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
				*/
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
    		Intent intentSettings = new Intent(this, FlashSettings.class);
    		startActivity(intentSettings);
    		return true;
    	case R.id.menuItemHelp:
    		Intent intentHelp = new Intent(this, FlashHelp.class);
    		startActivity(intentHelp);
    		return true;
    	}
        return true;
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
    	buttonHomeProfile = (Button) viewMain.findViewById(R.id.homeButtonProfile);

    	buttonHomeVendors.getLayoutParams().height = buttonHomeVendors.getWidth();
    	buttonHomeMenu.getLayoutParams().height = buttonHomeMenu.getWidth();
    	buttonHomeCodes.getLayoutParams().height = buttonHomeCodes.getWidth();
    	buttonHomeProfile.getLayoutParams().height = buttonHomeProfile.getWidth();

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
        // Go to profile/login
        buttonHomeProfile.setOnClickListener(new OnClickListener() {
            public void onClick(View view)
            {
                Intent intent;/*
                if (Globals.getEmail() == null || Globals.getPassword() == null)
                {
                    intent = new Intent(view.getContext(), FlashLogin.class);
                }
                else
                {*/
                    intent = new Intent(view.getContext(), FlashProfile.class);
                //}
                view.getContext().startActivity(intent);
            }
        });

    	endLoading();
    }

    public void endLoading()
    {
    	SharedPreferences preferences = getSharedPreferences("firstLaunch", MODE_PRIVATE);
    	if (preferences.getBoolean("firstLaunch", false) == false)
    	{
    		DialogInterface.OnClickListener dialogClickListener =new DialogTutorialListener(this);
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setMessage("I see this is the first time you've used this app.\n"
    			+ "Do you want to view the tutorial?").setPositiveButton("Yes",
    			dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    		Editor editor = preferences.edit();
    		editor.putBoolean("firstLaunch", true);
    		editor.commit();
    	}
    	update();
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

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        googleApiClient.disconnect();
        super.onStop();
    }
}
