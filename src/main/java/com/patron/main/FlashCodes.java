package com.patron.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.appboy.Appboy;

import com.patron.bind.CodeBinder;
import com.patron.db.CodeConnector;
import com.patron.listeners.DrawerNavigationListener;
import com.patron.listeners.OnApiExecutedListener;
import com.patron.listeners.OnCodesRefreshListener;
import com.patron.lists.ListLinks;
import com.patron.model.Code;
import com.patron.model.Order;
import com.patron.R;
import com.patron.system.ApiExecutor;
import com.patron.system.Globals;
import com.patron.system.Loadable;
import com.patron.view.NavigationListView;
import static com.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashCodes extends Activity implements Loadable
{
	// The layout elements.
	private ListView listCodes;
	private View viewLoading;
	private View viewCodes;

	// Activity
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_codes);


		// Set up the navigation drawer.
        final SwipeRefreshLayout swipeRefreshLayoutCodes = (SwipeRefreshLayout) findViewById(R.id.codesSwipeRefreshLayoutCodes);
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.codesDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) findViewById(R.id.codesListNavigation);
		DrawerNavigationListener drawerNavigationListener = new DrawerNavigationListener(this);
		drawerLayoutNavigation.setDrawerListener(drawerNavigationListener);
		listNavigation.setHierarchy(drawerNavigationListener, drawerLayoutNavigation, Hierarchy.ORDERS);
		ListView listCodes = (ListView)findViewById(R.id.codesListViewMain);

        // Loading indicator
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.codesRelativeLayoutContent);
        final ProgressBar progressIndicator = new ProgressBar(this);
        progressIndicator.setBackgroundColor(Color.TRANSPARENT);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        final OnApiExecutedListener removeViewListener = new OnApiExecutedListener() {
            @Override
            public void onExecuted()
            {
                layout.removeView(progressIndicator);
            }
        };
        final OnApiExecutedListener stopRefreshListener = new OnApiExecutedListener() {
            @Override
            public void onExecuted()
            {
                swipeRefreshLayoutCodes.setRefreshing(false);
            }
        };

        // Manually refreshing
		final ApiExecutor executor = new ApiExecutor();
		final OnCodesRefreshListener listener = new OnCodesRefreshListener(listCodes);
        swipeRefreshLayoutCodes.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
            android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayoutCodes.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                swipeRefreshLayoutCodes.setRefreshing(true);
                executor.getCodes(listener, stopRefreshListener);
            }
        });

		// Get codes initially.
        layout.addView(progressIndicator, params);
		executor.getCodes(listener, removeViewListener);
	}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(FlashCodes.this).openSession(FlashCodes.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(FlashCodes.this).closeSession(FlashCodes.this);
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_main, menu);
	    return super.onCreateOptionsMenu(menu);
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
    	default:
    		return false;
    	}
    }

	@Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	super.onWindowFocusChanged(hasFocus);
    }

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}

	// Loading
	public void beginLoading()
	{
		//listCodes = (ListView) viewCodes.findViewById(R.id.codesList);

		load();
	}

	public void load()
	{
		URL url = null;
		try
		{
			try
			{
				url = new URL(ListLinks.LINK_GET_CODES);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			CodeConnector codeConnector = new CodeConnector(this);
			codeConnector.execute(url);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void endLoading()
	{
		if (Globals.getCodes() != null && Globals.getCodes().size() > 0)
		{
			setContentView(viewCodes);
		}
		else
		{
			setContentView(R.layout.misc_no_codes);
		}
		update();
	}

	public void update()
	{

	}

	public void message(String msg)
	{
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
