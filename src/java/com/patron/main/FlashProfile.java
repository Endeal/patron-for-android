package com.patron.main;

import java.lang.Exception;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.widget.EditText;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.patron.listeners.DrawerNavigationListener;
import com.patron.R;
import com.patron.system.Loadable;
import com.patron.system.Globals;
import com.patron.view.NavigationListView;
import static com.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashProfile extends Activity implements Loadable
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		View viewProfile = inflater.inflate(R.layout.layout_profile, null);
		setContentView(viewProfile);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) viewProfile.findViewById(R.id.profileDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) viewProfile.findViewById(R.id.profileListNavigation);
		DrawerNavigationListener drawerNavigationListener = new DrawerNavigationListener(this);
		drawerLayoutNavigation.setDrawerListener(drawerNavigationListener);
		listNavigation.setHierarchy(drawerNavigationListener, drawerLayoutNavigation, Hierarchy.PROFILE);

		init();
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
    	if (item.getItemId() == android.R.id.home)
    	{
        	Intent intent = new Intent(this, FlashHome.class);
        	this.finish();
        	startActivity(intent);
        	return true;
    	}
    	return super.onOptionsItemSelected(item);
	}

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}

	@Override
	public void beginLoading()
	{
	}

	@Override
	public void load()
	{
	}

	@Override
	public void endLoading()
	{
	}

	@Override
	public void update()
	{
	}

	@Override
	public void message(String msg)
	{
	}

	// Button actions
	public void init()
	{
		// Get the layout elements.
		//ListView listOptions = (ListView)findViewById(R.id.paymentListOptions);
		Button buttonLogout = (Button)findViewById(R.id.profileButtonLogout);
		Button buttonAddPayment = (Button)findViewById(R.id.profileButtonAddPayment);
		Button buttonRemovePayment = (Button)findViewById(R.id.profileButtonRemovePayment);
		buttonLogout.setOnClickListener(new ButtonProfileLogoutListener(this));
		buttonRemovePayment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(view.getContext(), FlashRemovePayment.class);
				view.getContext().startActivity(intent);
			}
		});
		buttonAddPayment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(view.getContext(), FlashPayment.class);
				view.getContext().startActivity(intent);
			}
		});
	}

	private class ButtonProfileLogoutListener implements OnClickListener
	{
		private FlashProfile activity;

		public ButtonProfileLogoutListener(FlashProfile activity)
		{
			this.activity = activity;
		}

		@Override
		public void onClick(View view)
		{
			Intent intent = new Intent(view.getContext(), FlashLogin.class);
			activity.finish();
			view.getContext().startActivity(intent);
			Globals.setUser(null);
		}
	}
}
