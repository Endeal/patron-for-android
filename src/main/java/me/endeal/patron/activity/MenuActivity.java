/**
 * The Menu screen.
 * @author James Whiteman
 */

package me.endeal.patron.activity;

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

import com.squareup.picasso.Picasso;

import me.endeal.patron.adapters.FragmentAdapter;
import me.endeal.patron.decor.GridSpacingItemDecoration;
import me.endeal.patron.listeners.FilterButtonListener;
import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.lists.ListFonts;
import me.endeal.patron.lists.ListLinks;
import me.endeal.patron.model.*;
import me.endeal.patron.R;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.Loadable;
import me.endeal.patron.view.NavigationListView;
import static me.endeal.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MenuActivity extends AppCompatActivity
{
    private List<Button> buttonCategories;
    private CoordinatorLayout coordinatorLayout;
    private DrawerNavigationListener drawerToggle;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu);

        // Set up toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.menuToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.menuDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) findViewById(R.id.menuListNavigation);
        drawerToggle = new DrawerNavigationListener(this, drawerLayoutNavigation, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
		listNavigation.setHierarchy(drawerToggle, drawerLayoutNavigation, Hierarchy.BUY);
        drawerLayoutNavigation.setDrawerListener(drawerToggle);
        drawerLayoutNavigation.setScrimColor(getResources().getColor(R.color.scrim));
        drawerToggle.syncState();

        // Find the views.
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.menuCoordinatorLayoutMain);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.menuRecyclerViewItems);
        final GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        final SwipeRefreshLayout swipeRefreshLayoutItems = (SwipeRefreshLayout) findViewById(R.id.menuSwipeRefreshLayoutItems);
        swipeRefreshLayoutItems.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
            android.R.color.holo_orange_light, android.R.color.holo_red_light);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.menuFloatingActionButtonFilter);

        // Add items
        List<Item> items = new ArrayList<Item>();
        List<Category> categories = new ArrayList<Category>();
        List<Option> options = new ArrayList<Option>();
        List<Attribute> attributes = new ArrayList<Attribute>();
        Price price = new Price(200, "USD");
        List<String> ingredients = new ArrayList<String>();
        ingredients.add("Flour");
        ingredients.add("Milk");
        ingredients.add("High-Fructose Corn Syrup");
        Nutrition nutrition = new Nutrition(22, 58, 82, 92, 22, 888, 90, ingredients, false, false, false, false,
                false, false, false, false, false);
        int supply = 52;
        categories.add(new Category("827", "Domestic Beer"));
        categories.add(new Category("273", "Solvent"));
        categories.add(new Category("11234", "Fresco"));
        List<Category> categories2 = new ArrayList<Category>();
        options.add(new Option("3327", "Ice", new Price(10, "USD")));
        List<Option> aOptions = new ArrayList<Option>();
        aOptions.add(new Option("6764", "Grey Goose", new Price(400, "USD")));
        aOptions.add(new Option("34455", "Absolute", new Price(325, "USD")));
        attributes.add(new Attribute("2334", "Vodka", aOptions));
        Item item1 = new Item("82639", "Corona", "A beverage.", "http://www.fuelyourcreativity.com/files/Screen-shot-2010-08-31-at-12.45.11-AM-600x336.jpg",
                price, categories, options, attributes, nutrition, supply);
        Item item2 = new Item("8263", "Your Mom", "Juicy!", "http://mikatorestaurant.com/images/Mojito-Cocktail1.jpg",
                price, categories2, options, attributes, nutrition, supply);
        Item item3 = new Item("263", "Drinkypie", "It sure does taste.", "http://talentanarchy.com/wp-content/uploads/2013/06/apple-pie.jpg",
                price, categories, options, attributes, nutrition, supply);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        for (int i = 0; i < 8; i++)
        {
            items.add(item2);
        }
        Vendor vendor = new Vendor("", "Mike's Place", "https://musicalcities.files.wordpress.com/2012/03/outside-arcade-low-res.jpg",
                null, null, null, 0.0833, 4.0, 72, 25, 25, 25, items, null, null, false, null);
        vendor.setItems(items);
        vendor.setFilteredItems(items);
        Globals.setVendor(vendor);

        // Set up loading indicator
        /*
        final ProgressBar progressIndicator = new ProgressBar(this);
        progressIndicator.setBackgroundColor(Color.TRANSPARENT);
        final CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(200,200);
        CoordinatorLayout.Behavior behavior = new CoordinatorLayout.Behavior();
        params.setBehavior(behavior);
        */

        List<Fragment> fragments = new ArrayList<Fragment>();
        for (int i = 0; i < Globals.getVendor().getFilteredItems().size(); i++)
        {
            // Create default fragment
            Item item = Globals.getVendor().getFilteredItems().get(i);
            List<Selection> selections = new ArrayList<Selection>();
            if (item.getAttributes() != null && item.getAttributes().size() > 0)
            for (int j = 0; j < item.getAttributes().size(); j++)
            {
              Attribute attribute = item.getAttributes().get(j);
              if (attribute.getOptions() != null && attribute.getOptions().size() > 0)
              {
                Option option = attribute.getOptions().get(0);
                Selection selection = new Selection(attribute, option);
                selections.add(selection);
              }
            }
            options = new ArrayList<Option>();
            Fragment fragment = new Fragment("", item, options, selections, 1);
            fragments.add(fragment);
        }
        Globals.setFragments(fragments);
        FragmentAdapter adapter = new FragmentAdapter(recyclerView.getContext(), fragments);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 20, true));

        Globals.filterCategories(Globals.getVendor().getItems());
        fab.setOnClickListener(new FilterButtonListener(adapter));

        // Listener to refresh the page on fetching the items for the vendor.
        //final OnApiExecutedListener refreshListener = new OnMenuRefreshListener(swipeRefreshLayoutItems, recyclerView);
        //refreshListener.onExecuted();
        /*
        final OnApiExecutedListener removeIndicatorListener = new OnApiExecutedListener() {
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
                swipeRefreshLayoutItems.setRefreshing(false);
            }
        };

        // Update the items on manual refresh.
        final ApiExecutor apiExecutor = new ApiExecutor();
        int startOffset = (int)Globals.convertDpToPixel(10, this);
        int endOffset = (int)Globals.convertDpToPixel(50, this);
        swipeRefreshLayoutItems.setProgressViewOffset(false, startOffset, endOffset);
		    swipeRefreshLayoutItems.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                // Add a loading indicator.
                swipeRefreshLayoutItems.setRefreshing(true);
                if (Globals.getVendor() == null)
                {
                    apiExecutor.selectNearestVendor(swipeRefreshLayoutItems.getContext(), new OnApiExecutedListener() {
                        @Override
                        public void onExecuted()
                        {
                            if (Globals.getVendor() == null)
                            {
                              Activity activity = (Activity)swipeRefreshLayoutItems.getContext();
                              //Activity activity = (Activity)view.getContext();
                              //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                               //   activity, view, "findFailedSelectVendor");
                              //Bundle bundle = options.toBundle();
                              Intent intent = new Intent(activity, FlashVendors.class);
                              //ActivityCompat.startActivity(activity, intent, bundle);
                              activity.startActivity(intent);
                              swipeRefreshLayoutItems.setRefreshing(false);
                              return;
                            }
                            apiExecutor.getItems(Globals.getVendor().getId(), refreshListener, stopRefreshListener);
                        }
                    });
                }
                else
                {
                    apiExecutor.getItems(Globals.getVendor().getId(), refreshListener, stopRefreshListener);
                }
            }
        });
    */

        // Update the items upon activity creation.
        //layout.addView(progressIndicator, params);
        /*
        if (Globals.getVendor() == null)
        {
            apiExecutor.selectNearestVendor(this, new OnApiExecutedListener() {
                @Override
                public void onExecuted()
                {
                  if (Globals.getVendor() != null)
                  {
                    listNavigation.getTextViewHeader().setText(Globals.getVendor().getName() + "\n" + Globals.getPoints(Globals.getVendor().getId()) + " Points");
                    //apiExecutor.getItems(Globals.getVendor().getId(), refreshListener, removeIndicatorListener);
                  }
                  else
                  {
                      //removeIndicatorListener.onExecuted();
                    Activity activity = (Activity)swipeRefreshLayoutItems.getContext();
                    Intent intent = new Intent(activity, FlashVendors.class);
                    //ActivityCompat.startActivity(activity, intent, bundle);
                    //activity.startActivity(intent);
                  }
                }
            });
        }
        else
        {
            //apiExecutor.getItems(Globals.getVendor().getId(), refreshListener, removeIndicatorListener);
        }
        */

        // Set up listeners
        /*
        buttonSearch.setListener(refreshListener);
        buttonFavorites.setListener(refreshListener);
		    buttonCheckout.setOnClickListener(new ButtonCheckoutListener(this));
        buttonSelectVendor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Activity activity = (Activity)view.getContext();
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, view, "selectVendorButton");
                Bundle bundle = options.toBundle();
                Intent intent = new Intent(activity, FlashVendors.class);
                //ActivityCompat.startActivity(activity, intent, bundle);
                activity.startActivity(intent);
            }
        });

        // Create animation to show horizontal movement
        final ImageView imageViewHelpCategories = (ImageView) findViewById(R.id.menuImageViewHelpCategories);
        int images[] = { R.drawable.help_categories};
        animate(imageViewHelpCategories, images, 0,true);

        // Get rid of helper image once scrolled enough.
        HorizontalScrollViewFilters scrollViewFilters = (HorizontalScrollViewFilters)findViewById(R.id.menuHorizontalScrollViewTypes);
        scrollViewFilters.setImage(imageViewHelpCategories);
        */
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
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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
        }
        else if (item.getItemId() == R.id.review)
        {
            if (Globals.getOrder() == null || Globals.getOrder().getFragments() == null || Globals.getOrder().getFragments().size() <= 0)
            {
                Snackbar.make(coordinatorLayout, "Your order is empty", Snackbar.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            Intent intent = new Intent(this, ReviewActivity.class);
            startActivity(intent);
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
