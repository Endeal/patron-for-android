package com.endeal.patron.activity;

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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.appboy.Appboy;

import com.appsee.Appsee;

import com.squareup.picasso.Picasso;

import com.endeal.patron.adapters.NavigationAdapter;
import com.endeal.patron.adapters.OrderAdapter;
import com.endeal.patron.dialogs.OrderDialog;
import com.endeal.patron.listeners.DrawerNavigationListener;
import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.listeners.OrderRefreshListener;
import com.endeal.patron.lists.ListLinks;
import com.endeal.patron.model.*;
import com.endeal.patron.R;
import com.endeal.patron.system.ApiExecutor;
import com.endeal.patron.system.Globals;
import static com.endeal.patron.model.Order.Status;
import static com.endeal.patron.model.Retrieval.Method;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrdersActivity extends AppCompatActivity
{
    private CoordinatorLayout coordinatorLayout;
    private DrawerNavigationListener drawerToggle;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

        // Check Intent
        Bundle extras = getIntent().getExtras();
        CharSequence rawOrderId = extras.getCharSequence("orderId", "");
        final String orderId = rawOrderId.toString();

		setContentView(R.layout.layout_orders);
		final Context context = this;
        Toolbar toolbar = (Toolbar)findViewById(R.id.ordersToolbar);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.ordersCoordinatorLayoutMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.ordersDrawerNavigation);
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

        // Set up the user interface elements.
        final TextView empty = (TextView)findViewById(R.id.ordersTextViewEmpty);
        final SwipeRefreshLayout swipeRefreshLayoutOrders = (SwipeRefreshLayout)findViewById(R.id.ordersSwipeRefreshLayoutOrders);
		final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.ordersRecyclerViewMain);
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(manager);

        // Set up recycler view
        final OrderAdapter adapter = new OrderAdapter(this, Globals.getOrders());
        recyclerView.setAdapter(adapter);
        final ApiExecutor apiExecutor = new ApiExecutor();
        int startOffset = (int)Globals.convertDpToPixel(10, this);
        int endOffset = (int)Globals.convertDpToPixel(50, this);
        swipeRefreshLayoutOrders.setProgressViewOffset(false, startOffset, endOffset);
        swipeRefreshLayoutOrders.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        OnRefreshListener refreshListener = new OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                swipeRefreshLayoutOrders.setRefreshing(true);
                apiExecutor.getOrders(new OnApiExecutedListener() {
                    @Override
                    public void onExecuted(ApiResult result)
                    {
                        swipeRefreshLayoutOrders.setRefreshing(false);
                        if (Globals.getOrders() == null || Globals.getOrders().size() == 0)
                        {
                            empty.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            empty.setVisibility(View.GONE);
                        }
                        if (result.getStatusCode() == 200)
                        {
                            adapter.setOrders(Globals.getOrders());
                            adapter.notifyDataSetChanged();
                            if (orderId != null && !orderId.equals(""))
                            {
                                Order order = null;
                                for (int i = 0; i < Globals.getOrders().size(); i++)
                                {
                                    Order currentOrder = Globals.getOrders().get(i);
                                    if (currentOrder.getId().equals(orderId))
                                    {
                                        order = currentOrder;
                                        break;
                                    }
                                }
                                if (order != null)
                                {
                                    OrderDialog dialog = new OrderDialog(context, order);
                                    dialog.show();
                                }
                            }
                        }
                        else
                        {
                            Snackbar.make(coordinatorLayout, result.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        swipeRefreshLayoutOrders.setOnRefreshListener(refreshListener);
        refreshListener.onRefresh();
	}

    @Override
    protected void onResume()
    {
        super.onResume();
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
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
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
