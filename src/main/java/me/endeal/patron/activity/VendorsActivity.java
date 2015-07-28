package com.endeal.patron.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appboy.Appboy;

import com.appsee.Appsee;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.ArrayList;

import com.endeal.patron.adapters.VendorAdapter;
import com.endeal.patron.adapters.NavigationAdapter;
import com.endeal.patron.listeners.ButtonFindNearestListener;
import com.endeal.patron.listeners.DrawerNavigationListener;
import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.model.ApiResult;
import com.endeal.patron.model.Contact;
import com.endeal.patron.model.Location;
import com.endeal.patron.model.Vendor;
import com.endeal.patron.R;
import com.endeal.patron.system.ApiExecutor;
import com.endeal.patron.system.Globals;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VendorsActivity extends AppCompatActivity
{
    private DrawerNavigationListener drawerToggle;
    private CoordinatorLayout coordinatorLayout;
    private boolean submitting = false;

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
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.vendorsCoordinatorLayoutMain);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.vendorsDrawerNavigation);
        drawerToggle = new DrawerNavigationListener(this, drawerLayoutNavigation, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayoutNavigation.setDrawerListener(drawerToggle);
        drawerLayoutNavigation.setScrimColor(getResources().getColor(R.color.scrim));
        final RecyclerView recyclerViewNavigation = (RecyclerView)findViewById(R.id.navigationRecyclerViewNavigation);
        final TextView textViewDrawerTitle = (TextView)findViewById(R.id.navigationTextViewDrawerTitle);
        final TextView textViewDrawerSubtitle = (TextView)findViewById(R.id.navigationTextViewDrawerSubtitle);
        final ImageView imageViewDrawerVendor = (ImageView)findViewById(R.id.navigationImageViewDrawerVendor);
        textViewDrawerTitle.setText(Globals.getPatron().getIdentity().getFirstName() + " " + Globals.getPatron().getIdentity().getLastName());
        if (Globals.getVendor() != null)
        {
            textViewDrawerSubtitle.setText(Globals.getVendor().getName());
            Picasso.with(this).load(Globals.getVendor().getPicture()).into(imageViewDrawerVendor);
        }
        else
        {
            textViewDrawerSubtitle.setText("No vendor selected");
        }
        NavigationAdapter navigationAdapter = new NavigationAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerViewNavigation.setLayoutManager(layoutManager);
        recyclerViewNavigation.setAdapter(navigationAdapter);
        drawerToggle.syncState();

        // Set up recycler view.
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.vendorsRecyclerViewMain);
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(manager);
        final VendorAdapter adapter = new VendorAdapter(this, Globals.getVendors());
        recyclerView.setAdapter(adapter);
        final ApiExecutor apiExecutor = new ApiExecutor();
        int startOffset = (int)Globals.convertDpToPixel(10, this);
        int endOffset = (int)Globals.convertDpToPixel(50, this);
        swipeRefreshLayoutVendors.setProgressViewOffset(false, startOffset, endOffset);
				swipeRefreshLayoutVendors.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        OnRefreshListener refreshListener = new OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                swipeRefreshLayoutVendors.setRefreshing(true);
                apiExecutor.getVendors(new OnApiExecutedListener() {
                    @Override
                    public void onExecuted(ApiResult result)
                    {
                        swipeRefreshLayoutVendors.setRefreshing(false);
                        adapter.setVendors(Globals.getVendors());
                        adapter.notifyDataSetChanged();
                        if (result.getStatusCode() != 200)
                        {
                            Snackbar.make(coordinatorLayout, result.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        swipeRefreshLayoutVendors.setOnRefreshListener(refreshListener);
        refreshListener.onRefresh();
	}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(VendorsActivity.this).openSession(VendorsActivity.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(VendorsActivity.this).closeSession(VendorsActivity.this);
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
        if (item.getItemId() == R.id.findNearest && !submitting)
        {
            // Find Nearest Vendor
            submitting = true;
            final Activity activity = this;
            final ApiExecutor executor = new ApiExecutor();
            executor.selectNearestVendor(this, new OnApiExecutedListener() {
                @Override
                public void onExecuted(ApiResult result)
                {
                    submitting = false;
                    if (result != null && result.getStatusCode() == 200)
                    {
                        activity.finish();
                    }
                    else if (result != null && result.getMessage() != null && result.getStatusCode() != 202)
                    {
                        Snackbar.make(coordinatorLayout, result.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                    else if (result != null && result.getStatusCode() != 202)
                    {
                        Snackbar.make(coordinatorLayout, "Failed to find nearest vendor", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
