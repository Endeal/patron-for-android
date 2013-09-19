/**
 * The Cart screen.
 */
package com.flashvip.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FlashCart extends ActionBarActivity
{
	// The layout elements.
	Button buttonFinish;
	
	// Activity Methods
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_cart);
		initializeLayout();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	// Getters & Setters
	public Activity getActivity()
	{
		return this;
	}
	
	// Layout
	private void initializeLayout()
	{
		buttonFinish = (Button) findViewById(R.id.cartButtonFinish);
		
		initializeButtonListeners();
	}
	
	private void initializeButtonListeners()
	{
		buttonFinish.setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), FlashClient.class);
				getActivity().startActivity(intent);
			}
		});
	}
}
