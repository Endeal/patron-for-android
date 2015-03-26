package com.patron.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.patron.listeners.ButtonFindNearestListener;
import com.patron.listeners.DrawerNavigationListener;
import com.patron.listeners.OnApiExecutedListener;
import com.patron.listeners.OnVendorRefreshListener;
import com.patron.R;
import com.patron.system.ApiExecutor;
import com.patron.system.Globals;
import com.patron.view.NavigationListView;
import static com.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashVendors extends Activity
{
	// Activity methods.
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_vendors);

        // Get layout elements
		ListView listVendors = (ListView) findViewById(R.id.vendorsListViewVendors);
        final SwipeRefreshLayout swipeRefreshLayoutVendors = (SwipeRefreshLayout) findViewById(R.id.vendorsSwipeRefreshLayoutVendors);
        Button buttonFindNearest = (Button) findViewById(R.id.vendorsButtonFindNearest);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.locationsDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) findViewById(R.id.locationsListNavigation);
		DrawerNavigationListener drawerNavigationListener = new DrawerNavigationListener(this);
		drawerLayoutNavigation.setDrawerListener(drawerNavigationListener);
		listNavigation.setHierarchy(drawerNavigationListener, drawerLayoutNavigation, Hierarchy.BUY);

        // Loading indicator
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.vendorsRelativeLayoutContent);
        final ProgressBar progressIndicator = new ProgressBar(this);
        progressIndicator.setBackgroundColor(Color.TRANSPARENT);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        final OnApiExecutedListener removeViewListener = new OnApiExecutedListener() {
            @Override
            public void onExecuted()
            {
                swipeRefreshLayoutVendors.setRefreshing(false);
                layout.removeView(progressIndicator);
            }
        };

        // Refreshing the page.
        final ApiExecutor apiExecutor = new ApiExecutor();
        int startOffset = (int)Globals.convertDpToPixel(10, this);
        int endOffset = (int)Globals.convertDpToPixel(50, this);
        swipeRefreshLayoutVendors.setProgressViewOffset(false, startOffset, endOffset);
				swipeRefreshLayoutVendors.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
						android.R.color.holo_orange_light, android.R.color.holo_red_light);
        final OnVendorRefreshListener vendorRefreshListener = new OnVendorRefreshListener(listVendors, buttonFindNearest);
        layout.addView(progressIndicator, params);
        apiExecutor.getVendors(vendorRefreshListener, removeViewListener);
        swipeRefreshLayoutVendors.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                layout.addView(progressIndicator, params);
                swipeRefreshLayoutVendors.setRefreshing(true);
                apiExecutor.getVendors(vendorRefreshListener, removeViewListener);
            }
        });

        // Find nearest vendor button.
        buttonFindNearest.setOnClickListener(new ButtonFindNearestListener());
	}

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
