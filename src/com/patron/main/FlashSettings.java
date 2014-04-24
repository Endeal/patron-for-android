package com.patron.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class FlashSettings extends ActionBarActivity
{
	// Layout elements.
	public static View viewMain;
	
	// Activity methods.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        viewMain = inflater.inflate(R.layout.layout_settings, null);
        setContentView(viewMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
    	{
    	case R.id.menuItemHelp:
    		Intent intent = new Intent(this, FlashHelp.class);
    		startActivity(intent);
    		return true;
    	default:
    		return false;
    	}
    }
	
	@Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	super.onWindowFocusChanged(hasFocus);
    }
    
}