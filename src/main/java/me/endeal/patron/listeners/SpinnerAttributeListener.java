package me.endeal.patron.listeners;

import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.model.Attribute;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Order;
import me.endeal.patron.model.Selection;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.Loadable;

public class SpinnerAttributeListener implements OnItemSelectedListener
{
	private Fragment fragment;
	private Attribute attribute;
	private OnApiExecutedListener listener;
	private boolean firstCallHappened;

	public SpinnerAttributeListener(Fragment fragment, Attribute attribute, OnApiExecutedListener listener)
	{
		this.fragment = fragment;
		this.attribute = attribute;
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