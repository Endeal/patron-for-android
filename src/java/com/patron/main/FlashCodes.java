package com.patron.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.patron.bind.CodeBinder;
import com.patron.db.CodeConnector;
import com.patron.listeners.DrawerNavigationListener;
import com.patron.lists.ListLinks;
import com.patron.model.Code;
import com.patron.model.Order;
import com.patron.system.Globals;
import com.patron.system.Loadable;
import com.patron.view.NavigationListView;
import static com.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashCodes extends Activity implements Loadable
{
	// The layout elements.
	private ListView listCodes;
	private View viewLoading;
	private View viewCodes;

	// Activity
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		viewLoading = inflater.inflate(R.layout.misc_loading, null);
		viewCodes = inflater.inflate(R.layout.layout_codes, null);
		setContentView(viewLoading);
		beginLoading();
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

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}

	// Loading
	public void beginLoading()
	{
		listCodes = (ListView) viewCodes.findViewById(R.id.codesList);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) viewCodes.findViewById(R.id.codesDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) viewCodes.findViewById(R.id.codesListNavigation);
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
				url = new URL(ListLinks.LINK_GET_CODES);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			CodeConnector codeConnector = new CodeConnector(this);
			codeConnector.execute(url);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void endLoading()
	{
		if (Globals.getCodes() != null && Globals.getCodes().size() > 0)
		{
			setContentView(viewCodes);
		}
		else
		{
			setContentView(R.layout.misc_no_codes);
		}
		update();
	}

	public void update()
	{
		List<Code> codes = Globals.getCodes();
		List<Map<String, String>> codesMap = new ArrayList<Map<String, String>>();

		String[] from = {"textTime",
				"textStatus",
				"textOrders"};

		int[] to = {R.id.codeListItemTextTime,
				R.id.codeListItemTextStatus,
				R.id.codeListItemTextProducts};

		// Don't update the list if the codes are empty.
		if (codes == null || codes.isEmpty())
			return;

		// Map the values to the elements of the list item.
		for (int i = 0; i < codes.size(); i++)
		{
			Map<String, String> mapping = new HashMap<String, String>();
			Code code = codes.get(i);
			mapping.put("textTime", code.getTimestampText());
			mapping.put("textStatus", Order.getStatusText(code.getOrder().getStatus()));
			mapping.put("textOrders", code.getOrder().getOrderText());
			codesMap.add(mapping);
		}

		// Bind the values to the elements of the list item.
		SimpleAdapter adapter = new SimpleAdapter(this,
				codesMap, R.layout.list_item_code, from, to);
		adapter.setViewBinder(new CodeBinder());
		listCodes.setAdapter(adapter);
		listCodes.setOnItemClickListener(new CodeItemListener(this));
		adapter.notifyDataSetChanged();
	}

	public void message(String msg)
	{
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	// List item listener.
	public static class CodeItemListener implements OnItemClickListener
	{
		private Activity activity;

		public CodeItemListener(Activity activity)
		{
			this.activity = activity;
		}

		public void onItemClick(AdapterView<?> adapter, View v, int item, long row)
		{
			Intent intent = new Intent(activity, FlashScan.class);
			intent.putExtra("orderRow", "" + item);
			activity.startActivity(intent);
		}
	}
}
