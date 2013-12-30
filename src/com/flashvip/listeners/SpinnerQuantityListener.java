package com.flashvip.listeners;

import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.flashvip.model.Fragment;
import com.flashvip.model.Order;
import com.flashvip.system.Globals;
import com.flashvip.system.Loadable;

public class SpinnerQuantityListener implements OnItemSelectedListener 
{
	private Fragment fragment;
	private Loadable activity;
	private boolean firstCallHappened;
	
	public SpinnerQuantityListener(Fragment fragment, Loadable activity)
	{
		this.fragment = fragment;
		this.activity = activity;
		firstCallHappened = false;
	}
	
	public void onItemSelected(AdapterView<?> adapter, View view,
			int position, long id)
	{
		Order order = Globals.getOrder();
		for (int i = 0; i < order.getFragments().size(); i++)
		{
			if (order.getFragments().get(i) == fragment)
			{
				fragment.setQuantity(position + 1);
				List<Fragment> fragments = order.getFragments();
				fragments.set(i, fragment);
				order.setFragments(fragments);
				Globals.setOrder(order);
				if (firstCallHappened)
				{
					activity.update();
				}
				firstCallHappened = true;
				break;
			}
		}
		firstCallHappened = true;
	}

	public void onNothingSelected(AdapterView<?> adapter)
	{
		
	}
}
