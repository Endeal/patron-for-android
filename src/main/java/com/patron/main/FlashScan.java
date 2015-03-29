package com.patron.main;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.appboy.Appboy;

import com.patron.db.ScanConnector;
import com.patron.listeners.DrawerNavigationListener;
import com.patron.lists.ListLinks;
import com.patron.model.Order;
import com.patron.R;
import com.patron.system.Globals;
import com.patron.system.Loadable;
import com.patron.view.NavigationListView;
import static com.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashScan extends Activity implements Loadable
{
	// The layout elements.
	private ImageView imageCode;
	private View viewLoading;
	private View viewScan;
	private Order order;

	// Activity
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			String orderRow = bundle.getString("orderRow");
			int row = Integer.parseInt(orderRow);
			order = Globals.getCodes().get(row).getOrder();
		}
		LayoutInflater inflater = LayoutInflater.from(this);
		viewLoading = inflater.inflate(R.layout.misc_loading, null);
		viewScan = inflater.inflate(R.layout.layout_scan, null);
		setContentView(viewLoading);
		beginLoading();
	}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(FlashScan.this).openSession(FlashScan.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(FlashScan.this).closeSession(FlashScan.this);
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_main, menu);
	    return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
    	{
    	case R.id.menuItemSettings:
    		Intent intentSettings = new Intent(this, FlashSettings.class);
    		startActivity(intentSettings);
    		return true;
    	case R.id.menuItemHelp:
    		Intent intentHelp = new Intent(this, FlashHelp.class);
    		startActivity(intentHelp);
    		return true;
    	default:
    		return false;
    	}
	}

	@Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	super.onWindowFocusChanged(hasFocus);
    }

	// Loading
	public void beginLoading()
	{
		imageCode = (ImageView) viewScan.findViewById(R.id.codeImageImageViewCode);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) viewScan.findViewById(R.id.scanDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) viewScan.findViewById(R.id.scanListNavigation);
		DrawerNavigationListener drawerNavigationListener = new DrawerNavigationListener(this);
		drawerLayoutNavigation.setDrawerListener(drawerNavigationListener);
		listNavigation.setHierarchy(drawerNavigationListener, drawerLayoutNavigation, Hierarchy.ORDERS);

		load();
	}

	public void load()
	{
		URL url = null;
		try
		{
			try
			{
				url = new URL(ListLinks.LINK_GET_SCAN);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			ScanConnector codeImageConnector = new ScanConnector(this, order);
			codeImageConnector.execute(url);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void endLoading()
	{
		if (Globals.getScan() != null)
		{
			setContentView(viewScan);

			ViewTreeObserver vto = viewScan.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout()
				{
					// Set and resize the image.
					int margin = 20;
					imageCode.setImageBitmap(Globals.getScan());
					LinearLayout layout = (LinearLayout)viewScan.findViewById(R.id.scanLayoutMain);
					LayoutParams params = new LayoutParams(layout.getWidth() - margin,
							layout.getWidth() - margin);
					params.setMargins(margin, margin, margin, margin);
					imageCode.setLayoutParams(params);

					// Set the order text.
					String text = order.getOrderText();
					TextView textView = (TextView)viewScan.findViewById(R.id.scanTextOrder);
					textView.setText(text);

                    // Set the status text.
                    text = Order.getStatusText(order.getStatus());
                    TextView textViewStatus = (TextView)viewScan.findViewById(R.id.scanTextViewStatus);
                    textViewStatus.setText(text);
				}
			});
		}
		else
		{
			setContentView(R.layout.misc_no_scan);
		}
		update();
	}

	public void update()
	{
	}

	public void message(String msg)
	{
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	// Calligraphy
	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
