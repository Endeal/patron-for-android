package me.endeal.patron.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.appboy.Appboy;

import com.appsee.Appsee;

import me.endeal.patron.bind.CodeBinder;
import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.listeners.OnCodesRefreshListener;
import me.endeal.patron.lists.ListLinks;
import me.endeal.patron.model.Code;
import me.endeal.patron.model.Order;
import me.endeal.patron.R;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.Loadable;
import me.endeal.patron.view.NavigationListView;
import static me.endeal.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashOrders extends Activity
{
	// Activity
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_orders);
		final Context context = this;

		// Set up the navigation drawer.
        final SwipeRefreshLayout swipeRefreshLayoutOrders = (SwipeRefreshLayout)findViewById(R.id.ordersSwipeRefreshLayoutOrders);
		final DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.ordersDrawerNavigation);
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
		executor.getOrders(listener, removeViewListener, new OnApiExecutedListener() {
			@Override
			public void onExecuted()
			{
				// Open the navigation drawer on first viewing
				try
				{
					SharedPreferences sharedPreferences = context.getSharedPreferences("me.endeal.patron", Context.MODE_PRIVATE);
					boolean drawerOpened = sharedPreferences.getBoolean("drawerDiscovered", false);
					if (!drawerOpened)
					{
						drawerLayoutNavigation.openDrawer(Gravity.LEFT);
						Editor editor = sharedPreferences.edit();
						editor.putBoolean("drawerDiscovered", true);
						editor.commit();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
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
