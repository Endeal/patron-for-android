package me.endeal.patron.listeners;

import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Order;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.Loadable;

public class SpinnerQuantityListener implements OnItemSelectedListener
{
	private Fragment fragment;
    private OnApiExecutedListener listener;
	private boolean firstCallHappened;

	public SpinnerQuantityListener(Fragment fragment, OnApiExecutedListener listener)
	{
		this.fragment = fragment;
        this.listener = listener;
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
                    listener.onExecuted();
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
