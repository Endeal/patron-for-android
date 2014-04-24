package com.patron.listeners;

import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.patron.model.Attribute;
import com.patron.model.Fragment;
import com.patron.model.Order;
import com.patron.model.Selection;
import com.patron.system.Globals;
import com.patron.system.Loadable;

public class SpinnerAttributeListener implements OnItemSelectedListener
{
	private Fragment fragment;
	private Attribute attribute;
	private Loadable activity;
	private boolean firstCallHappened;
	
	public SpinnerAttributeListener(Fragment fragment, Attribute attribute, Loadable activity)
	{
		this.fragment = fragment;
		this.attribute = attribute;
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
				for (int j = 0; j < fragment.getSelections().size(); j++)
				{
					if (fragment.getSelections() != null &&
							fragment.getSelections().get(j) != null &&
							fragment.getSelections().get(j).getAttribute() != null)
					{
						if (fragment.getSelections().get(j).getAttribute() == attribute)
						{
							Selection selection = new Selection(attribute,
									attribute.getOptions().get(position));
							List<Selection> selections = fragment.getSelections();
							selections.set(j, selection);
							fragment.setSelections(selections);
						}
					}
				}
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