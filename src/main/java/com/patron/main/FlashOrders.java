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

public class FlashOrders extends Activity
{
	// Activity
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_orders);


		// Set up the navigation drawer.
        final SwipeRefreshLayout swipeRefreshLayoutOrders = (SwipeRefreshLayout)findViewById(R.id.ordersSwipeRefreshLayoutOrders);
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.ordersDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) findViewById(R.id.ordersListNavigation);
		DrawerNavigationListener drawerNavigationListener = new DrawerNavigationListener(this);
		drawerLayoutNavigation.setDrawerListener(drawerNavigationListener);
		listNavigation.setHierarchy(drawerNavigationListener, drawerLayoutNavigation, Hierarchy.ORDERS);

        // Set up the user interface elements.
		final ListView listOrders = (ListView)findViewById(R.id.ordersListViewMain);

        // Loading indicator
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.ordersRelativeLayoutContent);
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
                swipeRefreshLayoutOrders.setRefreshing(false);
            }
        };

        // Manually refreshing
		final ApiExecutor executor = new ApiExecutor();
		final OnCodesRefreshListener listener = new OnCodesRefreshListener(listOrders);
        swipeRefreshLayoutOrders.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
            android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayoutOrders.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                swipeRefreshLayoutOrders.setRefreshing(true);
                executor.getOrders(listener, stopRefreshListener);
            }
        });

		// Get codes initially.
        layout.addView(progressIndicator, params);
		executor.getOrders(listener, removeViewListener);
	}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(FlashOrders.this).openSession(FlashOrders.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(FlashOrders.this).closeSession(FlashOrders.this);
    }
	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
