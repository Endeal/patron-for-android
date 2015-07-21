package me.endeal.patron.activity;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
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

import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.listeners.OrderRefreshListener;
import me.endeal.patron.lists.ListLinks;
import me.endeal.patron.model.*;
import me.endeal.patron.R;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.Loadable;
import me.endeal.patron.view.NavigationListView;
import static me.endeal.patron.view.NavigationListView.Hierarchy;
import static me.endeal.patron.model.Order.Status;
import static me.endeal.patron.model.Retrieval.Method;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrdersActivity extends AppCompatActivity
{
	// Activity
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_orders);
		final Context context = this;
        Toolbar toolbar = (Toolbar)findViewById(R.id.ordersToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Set up the navigation drawer.
		final DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.ordersDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) findViewById(R.id.ordersListNavigation);
        DrawerNavigationListener drawerToggle = new DrawerNavigationListener(this, drawerLayoutNavigation, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayoutNavigation.setDrawerListener(drawerToggle);
		listNavigation.setHierarchy(drawerToggle, drawerLayoutNavigation, Hierarchy.ORDERS);
        drawerLayoutNavigation.setScrimColor(getResources().getColor(R.color.scrim));
        drawerToggle.syncState();

        // Set up the user interface elements.
        final SwipeRefreshLayout swipeRefreshLayoutOrders = (SwipeRefreshLayout)findViewById(R.id.ordersSwipeRefreshLayoutOrders);
		final RecyclerView recycler = (RecyclerView)findViewById(R.id.ordersRecyclerViewMain);
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        recycler.setLayoutManager(manager);
        OrderRefreshListener listener = new OrderRefreshListener(swipeRefreshLayoutOrders, recycler);

        // Set up orders
        List<Order> orders = new ArrayList<Order>();
        List<Fragment> fragments = new ArrayList<Fragment>();
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
        options.add(new Option("3327", "Ice", new Price(10, "USD")));
        List<Option> aOptions = new ArrayList<Option>();
        aOptions.add(new Option("6764", "Grey Goose", new Price(400, "USD")));
        Option option = new Option("34455", "Absolute", new Price(325, "USD"));
        aOptions.add(option);
        Attribute attribute = new Attribute("2334", "Vodka", aOptions);
        attributes.add(attribute);
        Item item = new Item("82639", "Corona", "A beverage.", "http://www.fuelyourcreativity.com/files/Screen-shot-2010-08-31-at-12.45.11-AM-600x336.jpg",
                price, categories, options, attributes, nutrition, supply);
        List<Selection> selections = new ArrayList<Selection>();
        Selection selection = new Selection(attribute, option);
        selections.add(selection);
        Fragment fragment = new Fragment("86663", item, options, selections, 7);
        fragments.add(fragment);
        Price tip = new Price(223, "USD");
        long time = 1436843933;
        String comment = "Your mom!";
        Retrieval retrieval = new Retrieval(Method.Pickup, new Station("287782", "Front Counter"), null, null);
        Status status = Order.getIntStatus(0);
        Funder funder = new Card("92873", "VISA", "98729837962986", "05", "2016", "DEBIT", "Wells Fargo",
                "144 Place Dr.", "Simi Valley", "CA", "93393", "google.com-href", "998723", false);
        Location location = new Location("19 Yellow Drive", "San Rafael", "CA", "98532", 56.115, 31.334);
        Contact contact = new Contact("(805) 442-4921", "johngalt@gmail.com", "http://facebook.com", "http://twitter.com", "http://plus.google.com");
        Vendor vendor = new Vendor("", "Mike's Place", "http://rabda2.s3.amazonaws.com/images/images/180/large/Morimoto_Andaz_Maui___1_.jpg",
                null, location, contact, 0.0833, 4.0, 72, 25, 25, 25, null, null, null, true, null);
        String code = "https://upload.wikimedia.org/wikipedia/commons/6/65/QR_code_for_QRpedia.png";

        Order order = new Order("387", fragments, null, tip, comment, retrieval, time, status,
                funder, vendor, code);

        orders.add(order);

        Globals.setOrders(orders);
        listener.onExecuted();

        // Loading indicator
        /*
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
        */
	}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(OrdersActivity.this).openSession(OrdersActivity.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(OrdersActivity.this).closeSession(OrdersActivity.this);
    }
	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
