package com.flashvip.listeners;

import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.flashvip.main.Globals;
import com.flashvip.model.Attribute;
import com.flashvip.model.Fragment;
import com.flashvip.model.Order;
import com.flashvip.model.Selection;

public class SpinnerAttributeListener implements OnItemSelectedListener 
{
	private Fragment fragment;
	private Attribute attribute;
	
	public SpinnerAttributeListener(Fragment fragment, Attribute attribute)
	{
		this.fragment = fragment;
		this.attribute = attribute;
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
				break;
			}
		}
	}

	public void onNothingSelected(AdapterView<?> adapter)
	{
		
	}
}
