package com.flashvip.listeners;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.flashvip.model.Attribute;
import com.flashvip.model.Fragment;
import com.flashvip.model.Item;
import com.flashvip.model.Option;
import com.flashvip.model.Order;
import com.flashvip.model.Selection;
import com.flashvip.system.Globals;

public class ListItemMenuAddListener implements OnItemClickListener
{	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int row,
			long rowId)
	{
		// Get the layout containing this view, the 'Add' button
		RelativeLayout relativeLayout = (RelativeLayout) v;
		
		// Get the spinners.
		ArrayList<Spinner> spinnerAttributes = new ArrayList<Spinner>();
		Spinner spinnerQuantity = (Spinner) relativeLayout.getChildAt(1);
		if (relativeLayout.getChildCount() > 5)
		{
			for (int i = 0; i < relativeLayout.getChildCount() - 5; i++)
			{
				Spinner spinner = (Spinner)relativeLayout.getChildAt(5 + i);
				spinnerAttributes.add(spinner);
			}
		}
		
		// Gets the item for this given row.
		Item item = Globals.getVendor().getFilteredItems().get(row);
		ArrayList<Selection> selections = new ArrayList<Selection>(); 
		for (int i = 0; i < spinnerAttributes.size(); i++)
		{
			Spinner spinner = spinnerAttributes.get(i);
			int selectedOption = spinner.getSelectedItemPosition();
			Attribute attribute = item.getAttributes().get(i);
			Option option = attribute.getOptions().get(selectedOption);
			Selection selection = new Selection(attribute, option);
			selections.add(selection);
		}
		int quantity = spinnerQuantity.getSelectedItemPosition() + 1;
		Fragment fragment = new Fragment(null, item, selections,
				null, quantity);
		
		// Adds the TabDrink that was just created to the tab.
		Order order = Globals.getOrder();
		List<Fragment> fragments = null;
		if (order == null)
		{
			order = new Order(null, Globals.getVendor().getVendorId(),
					fragments, Order.Status.WAITING);
		}
		if (order.getFragments() != null &&
				!order.getFragments().isEmpty())
		{
			fragments = order.getFragments();
		}
		else
		{
			fragments = new ArrayList<Fragment>();
		}
		fragments.add(fragment);
		order.setFragments(fragments);
		Globals.setOrder(order);
		
		// Creates a Toast that informs the user that the drink has been added to the tab.
		System.out.println("SHOWING TOAST");
		Toast toast = Toast.makeText(v.getContext(),
				"Added " + item.getName() + ".", Toast.LENGTH_SHORT);
		toast.show();
	}
}