/**
 * The Menu screen.
 * @author James Whiteman
 */

package com.patron.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Runnable;

import com.patron.bind.NavigationBinder;
import com.patron.bind.ProductBinder;
import com.patron.db.ItemConnector;
import com.patron.listeners.ButtonCategoriesListener;
import com.patron.listeners.ButtonCheckoutListener;
import com.patron.listeners.ButtonFavoritesListener;
import com.patron.listeners.DrawerNavigationListener;
import com.patron.listeners.ListItemMenuAddListener;
import com.patron.listeners.OnMenuRefreshListener;
import com.patron.listeners.OnApiExecutedListener;
import com.patron.lists.ListFonts;
import com.patron.lists.ListLinks;
import com.patron.model.Item;
import com.patron.R;
import com.patron.system.ApiExecutor;
import com.patron.system.Globals;
import com.patron.system.Loadable;
import com.patron.view.NavigationListView;
import static com.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashMenu extends Activity
{
    private List<Button> buttonCategories;

	// Activity Methods
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_menu);

		// Set up the drawer layout.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.menuDrawerNavigation);
        DrawerNavigationListener drawerNavigationListener = new DrawerNavigationListener(this);
		drawerLayoutNavigation.setDrawerListener(drawerNavigationListener);
		final NavigationListView listNavigation = (NavigationListView) findViewById(R.id.menuListNavigation);
		listNavigation.setHierarchy(drawerNavigationListener, drawerLayoutNavigation, Hierarchy.BUY);

		// Find the views.
		ListView listMenu = (ListView) findViewById(R.id.menuListItems);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.menuLayoutTypes);
        Button buttonSelectVendor = (Button) findViewById(R.id.menuButtonSelectVendor);
		Button buttonCheckout = (Button) findViewById(R.id.menuButtonCheckout);
		Button buttonFavorites = (Button) findViewById(R.id.menuButtonFavorites);
		final SwipeRefreshLayout swipeRefreshLayoutItems = (SwipeRefreshLayout) findViewById(R.id.menuSwipeRefreshLayoutItems);
		swipeRefreshLayoutItems.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
			android.R.color.holo_orange_light, android.R.color.holo_red_light);


        // Listener to refresh the page on fetching the items for the vendor.
        final OnApiExecutedListener refreshListener = new OnMenuRefreshListener(swipeRefreshLayoutItems,
                listMenu, linearLayout, buttonSelectVendor);

        // Update the items on manual refresh.
        final ApiExecutor apiExecutor = new ApiExecutor();
        int startOffset = (int)Globals.convertDpToPixel(10, this);
        int endOffset = (int)Globals.convertDpToPixel(50, this);
        swipeRefreshLayoutItems.setProgressViewOffset(false, startOffset, endOffset);
		swipeRefreshLayoutItems.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                if (Globals.getVendor() == null)
                {
                    apiExecutor.selectNearestVendor(swipeRefreshLayoutItems.getContext(), new OnApiExecutedListener() {
                        @Override
                        public void onExecuted()
                        {
                            apiExecutor.getItems(Globals.getVendor().getId(), refreshListener);
                            swipeRefreshLayoutItems.setRefreshing(false);
                        }
                    });
                }
                else
                {
                    apiExecutor.getItems(Globals.getVendor().getId(), refreshListener);
                    swipeRefreshLayoutItems.setRefreshing(false);
                }
            }
        });

        // Update the items upon activity creation.
        if (Globals.getVendor() == null)
        {
            apiExecutor.selectNearestVendor(this, new OnApiExecutedListener() {
                @Override
                public void onExecuted()
                {
                    listNavigation.getTextViewHeader().setText(Globals.getVendor().getName() + "\n" + Globals.getPoints(Globals.getVendor().getId()) + " Points");
                    apiExecutor.getItems(Globals.getVendor().getId(), refreshListener);
                }
            });
        }
        else
        {
            apiExecutor.getItems(Globals.getVendor().getId(), refreshListener);
        }

        // Set up listeners
		buttonFavorites.setOnClickListener(new ButtonFavoritesListener(this, refreshListener));
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
                ActivityCompat.startActivity(activity, intent, bundle);
                activity.finish();
            }
        });
	}

    public List<Button> getButtonCategories()
    {
        return buttonCategories;
    }

    public void setButtonCategories(List<Button> buttonCategories)
    {
        this.buttonCategories = buttonCategories;
    }

    public Button getButtonFavorites()
    {
        return (Button)findViewById(R.id.menuButtonFavorites);
    }

	// Calligraphy
	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
