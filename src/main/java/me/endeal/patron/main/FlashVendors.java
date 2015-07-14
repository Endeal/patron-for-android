package me.endeal.patron.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.appboy.Appboy;

import com.appsee.Appsee;

import java.util.List;
import java.util.ArrayList;

import me.endeal.patron.listeners.ButtonFindNearestListener;
import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.listeners.OnVendorRefreshListener;
import me.endeal.patron.model.Contact;
import me.endeal.patron.model.Location;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.view.NavigationListView;
import static me.endeal.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashVendors extends AppCompatActivity
{
    private DrawerNavigationListener drawerToggle;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_vendors);
        Toolbar toolbar = (Toolbar)findViewById(R.id.vendorsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get layout elements
        final SwipeRefreshLayout swipeRefreshLayoutVendors = (SwipeRefreshLayout) findViewById(R.id.vendorsSwipeRefreshLayoutVendors);
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.vendorsRecyclerViewMain);
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(manager);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.locationsDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) findViewById(R.id.locationsListNavigation);
        drawerToggle = new DrawerNavigationListener(this, drawerLayoutNavigation, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayoutNavigation.setDrawerListener(drawerToggle);
		listNavigation.setHierarchy(drawerToggle, drawerLayoutNavigation, Hierarchy.BUY);
        drawerLayoutNavigation.setScrimColor(getResources().getColor(R.color.scrim));
        drawerToggle.syncState();

        // Set up recycler view.
        OnVendorRefreshListener refreshListener = new OnVendorRefreshListener(swipeRefreshLayoutVendors, recyclerView);

        // Loading indicator
        /*
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.vendorsRelativeLayoutContent);
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
                swipeRefreshLayoutVendors.setRefreshing(false);
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
                swipeRefreshLayoutVendors.setRefreshing(true);
                apiExecutor.getVendors(vendorRefreshListener, stopRefreshListener);
            }
        });
        */

        // Add all vendors
        Location location = new Location("144 Olympic Place", "Napa", "CA", "94552", 72.283, 29.993);
        Location location2 = new Location("19 Yellow Drive", "San Rafael", "CA", "98532", 56.115, 31.334);
        Contact contact = new Contact("(805) 442-4921", "johngalt@gmail.com", "http://facebook.com", "http://twitter.com", "http://plus.google.com");
        Contact contact2 = new Contact("(235) 482-9288", "frandrescher@gmail.com", "http://facebook.com", "http://twitter.com", "http://plus.google.com");
        Vendor vendor = new Vendor("", "Mike's Place", "http://rabda2.s3.amazonaws.com/images/images/180/large/Morimoto_Andaz_Maui___1_.jpg",
                null, location, contact, 0.0833, 4.0, 72, 25, 25, 25, null, null, null, true, null);
        Vendor vendor2 = new Vendor("", "Panda Express", "https://musicalcities.files.wordpress.com/2012/03/outside-arcade-low-res.jpg",
                null, location2, null, 0.0833, 4.0, 72, 25, 25, 25, null, null, null, false, null);
        List<Vendor> vendors = new ArrayList<Vendor>();
        vendors.add(vendor);
        vendors.add(vendor2);
        Globals.setVendors(vendors);
        refreshListener.onExecuted();

        // Find nearest vendor button.
        //buttonFindNearest.setOnClickListener(new ButtonFindNearestListener());
	}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(FlashVendors.this).openSession(FlashVendors.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(FlashVendors.this).closeSession(FlashVendors.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_vendors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        // Handle your other action bar items...
        if (item.getItemId() == R.id.findNearest)
        {
            // Find Nearest Vendor
            Intent intent = new Intent(this, FlashMenu.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
