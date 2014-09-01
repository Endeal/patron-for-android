package com.patron.main;

import java.lang.Exception;
import java.util.ArrayList;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
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
import android.view.MenuItem;

import com.patron.system.Loadable;
import com.patron.system.Globals;

public class FlashProfile extends ActionBarActivity implements Loadable
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_profile);
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
