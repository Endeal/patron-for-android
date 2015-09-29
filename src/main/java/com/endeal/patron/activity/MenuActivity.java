/**
 * The Menu screen.
 * @author James Whiteman
 */

package com.endeal.patron.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.HorizontalScrollView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Runnable;

import com.appboy.Appboy;

import com.appsee.Appsee;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.ActionItemBadgeAdder;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import com.squareup.picasso.Picasso;

import com.endeal.patron.adapters.FragmentAdapter;
import com.endeal.patron.adapters.NavigationAdapter;
import com.endeal.patron.decor.GridSpacingItemDecoration;
import com.endeal.patron.listeners.FilterButtonListener;
import com.endeal.patron.listeners.DrawerNavigationListener;
import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.lists.ListFonts;
import com.endeal.patron.lists.ListLinks;
import com.endeal.patron.model.*;
import com.endeal.patron.R;
import com.endeal.patron.system.ApiExecutor;
import com.endeal.patron.system.Globals;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MenuActivity extends AppCompatActivity
{
    private List<Button> buttonCategories;
    private CoordinatorLayout coordinatorLayout;
    private DrawerNavigationListener drawerToggle;
    private OnRefreshListener refreshListener;
    private boolean activityChanged;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu);
        activityChanged = false;

        // Set up toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.menuToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.menuDrawerNavigation);
        drawerToggle = new DrawerNavigationListener(this, drawerLayoutNavigation, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayoutNavigation.setDrawerListener(drawerToggle);
        drawerLayoutNavigation.setScrimColor(getResources().getColor(R.color.scrim));

        // Find navigation drawer elements
        final RecyclerView recyclerViewNavigation = (RecyclerView)findViewById(R.id.navigationRecyclerViewNavigation);
        final TextView textViewDrawerTitle = (TextView)findViewById(R.id.navigationTextViewDrawerTitle);
        final TextView textViewDrawerSubtitle = (TextView)findViewById(R.id.navigationTextViewDrawerSubtitle);
        final ImageView imageViewDrawerVendor = (ImageView)findViewById(R.id.navigationImageViewDrawerVendor);
        final GridLayoutManager navigationLayoutManager = new GridLayoutManager(getApplicationContext(), 1);

        // Find the views.
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.menuCoordinatorLayoutMain);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.menuRecyclerViewItems);
        final FragmentAdapter adapter = new FragmentAdapter(recyclerView.getContext(), new ArrayList<Item>());
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        final SwipeRefreshLayout swipeRefreshLayoutItems = (SwipeRefreshLayout) findViewById(R.id.menuSwipeRefreshLayoutItems);
        swipeRefreshLayoutItems.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
            android.R.color.holo_orange_light, android.R.color.holo_red_light);
        final FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.menuFloatingActionButtonFilter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 20, true));

        final Context context = this;
        final ApiExecutor executor = new ApiExecutor();
        refreshListener = new OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                swipeRefreshLayoutItems.setRefreshing(true);
                // Get items listener
                final OnApiExecutedListener itemsListener = new OnApiExecutedListener() {
                    @Override
                    public void onExecuted(ApiResult result)
                    {
                        if (result.getStatusCode() != 200)
                        {
                            Snackbar.make(coordinatorLayout, result.getMessage(), Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        adapter.setItems(Globals.getVendor().getItems());
                        Globals.filterCategories(Globals.getVendor().getItems());
                        adapter.notifyDataSetChanged();
                        fab.setOnClickListener(new FilterButtonListener(adapter));
                        swipeRefreshLayoutItems.setRefreshing(false);
                    }
                };

                // Find vendor first if there is none, then get items
                if (Globals.getVendor() == null)
                {
                    executor.selectNearestVendor(context, new OnApiExecutedListener() {
                        @Override
                        public void onExecuted(ApiResult result)
                        {
                            if (activityChanged)
                            {
                                swipeRefreshLayoutItems.setRefreshing(false);
                                return;
                            }
                            if (Globals.getVendor() == null)
                            {
                                activityChanged = true;
                                Intent intent = new Intent(context, VendorsActivity.class);
                                context.startActivity(intent);
                            }
                            else
                            {
                                executor.getItems(itemsListener);

                                // Update the navigation drawer
                                textViewDrawerTitle.setText(Globals.getPatron().getIdentity().getFirstName() + " " + Globals.getPatron().getIdentity().getLastName());
                                if (Globals.getVendor() != null)
                                {
                                    textViewDrawerSubtitle.setText(Globals.getVendor().getName());
                                    Picasso.with(context).load(Globals.getVendor().getPicture()).into(imageViewDrawerVendor);
                                }
                                else
                                {
                                    textViewDrawerSubtitle.setText("No vendor selected");
                                }
                                NavigationAdapter navigationAdapter = new NavigationAdapter(context);
                                recyclerViewNavigation.setLayoutManager(navigationLayoutManager);
                                recyclerViewNavigation.setAdapter(navigationAdapter);
                                drawerToggle.syncState();
                            }
                        }
                    });
                }
                else
                {
                    executor.getItems(itemsListener);
                }
            }
        };
        swipeRefreshLayoutItems.setOnRefreshListener(refreshListener);
	}

    @Override
    protected void onResume()
    {
        super.onResume();
        if (Globals.getPatron() == null)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        invalidateOptionsMenu();
        activityChanged = false;
        refreshListener.onRefresh();

		// Refresh the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.menuDrawerNavigation);
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
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    public List<Button> getButtonCategories()
    {
        return buttonCategories;
    }

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(MenuActivity.this).openSession(MenuActivity.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(MenuActivity.this).closeSession(MenuActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            return true;
        if (Globals.getOrder() != null && Globals.getOrder().getFragments() != null &&
                Globals.getOrder().getFragments().size() > 0) {
            ActionItemBadge.update(this, menu.findItem(R.id.review),
                    GoogleMaterial.Icon.gmd_shopping_basket,
                    ActionItemBadge.BadgeStyles.DARK_GREY, Globals.getOrder().getFragments().size());
        } else {
            ActionItemBadge.hide(menu.findItem(R.id.review));
        }
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
        if (item.getItemId() == R.id.vendors)
        {
            Intent intent = new Intent(this, VendorsActivity.class);
            startActivity(intent);
            activityChanged = true;
        }
        else if (item.getItemId() == R.id.review)
        {
            if (Globals.getOrder() == null || Globals.getOrder().getFragments() == null || Globals.getOrder().getFragments().size() <= 0)
            {
                Snackbar.make(coordinatorLayout, "Your basket is empty", Snackbar.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            Intent intent = new Intent(this, ReviewActivity.class);
            startActivity(intent);
            activityChanged = true;
        }
        return super.onOptionsItemSelected(item);
    }

	// Calligraphy
	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
