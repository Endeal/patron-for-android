package com.flashvip.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.flashvip.main.FlashCart;
import com.flashvip.main.Globals;

public class SpinnerAlcoholListener implements OnItemSelectedListener 
{
	private FlashCart activity;
	private int row;
	private boolean firstCall;
	
	public SpinnerAlcoholListener(FlashCart activity, int row)
	{
		this.activity = activity;
		this.row = row;
		firstCall = true;
	}
	
	public void onItemSelected(AdapterView<?> adapter, View view,
			int position, long id)
	{
		Globals.getCartProducts().get(row).setAlcohol(position);
		Globals.getCartProducts().get(row).updatePrice();

		if (firstCall)
			firstCall = false;
		else
			activity.updateListViewCart();
	}

	public void onNothingSelected(AdapterView<?> adapter)
	{

	}
}
