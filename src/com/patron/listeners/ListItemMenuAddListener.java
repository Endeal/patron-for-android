package com.patron.listeners;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.patron.model.Attribute;
import com.patron.model.Fragment;
import com.patron.model.Item;
import com.patron.model.Option;
import com.patron.model.Order;
import com.patron.model.Selection;
import com.patron.model.Station;
import com.patron.model.Funder;
import com.patron.model.Vendor;
import com.patron.system.Globals;

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
			if (item.getAttributes() != null && !item.getAttributes().isEmpty())
			{
				Attribute attribute = item.getAttributes().get(i);
				if (attribute.getOptions() != null && !attribute.getOptions().isEmpty())
				{
					Option option = attribute.getOptions().get(selectedOption);
					Selection selection = new Selection(attribute, option);
					selections.add(selection);
				}
			}
		}
		int quantity = spinnerQuantity.getSelectedItemPosition() + 1;
		Fragment fragment = new Fragment(null, item, selections,
				null, quantity);
		
		// Adds the TabDrink that was just created to the tab.
		Order order = Globals.getOrder();
		List<Fragment> fragments = null;
		if (order == null)
		{
			// Create default order info
			Station station = null;
			Funder funder = null;
			BigDecimal tip = new BigDecimal("0.00");
			List<Object> coupons = new ArrayList<Object>();
			String comment = "";
			if (Globals.getVendor().getStations() != null && Globals.getVendor().getStations().size() > 0)
			{
				station = Globals.getVendor().getStations().get(0);
			}
			if (Globals.getUser().getFunders() != null && Globals.getUser().getFunders().size() > 0)
			{
				funder = Globals.getUser().getFunders().get(0);
			}

			order = new Order(null, Globals.getVendor().getVendorId(), fragments, Order.Status.WAITING,
				station, funder, tip, coupons, comment);
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