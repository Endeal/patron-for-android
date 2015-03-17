package com.patron.listeners;

import java.lang.CloneNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.patron.model.Supplement;
import com.patron.model.Funder;
import com.patron.model.Vendor;
import com.patron.system.Globals;
import com.patron.system.Patron;

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
			for (int i = 0; i < relativeLayout.getChildCount(); i++)
			{
				View view = (View)relativeLayout.getChildAt(i);
				if (view.getTag() != null && view.getTag().equals("attribute"))
				{
					Spinner spinner = (Spinner)view;
					spinnerAttributes.add(spinner);
				}
			}
		}

		// Gets the item for this given row.
		Fragment oldFragment = Globals.getFragments().get(row);
		Fragment fragment = new Fragment(oldFragment.getId(), oldFragment.getItem(),
			oldFragment.getSelections(), oldFragment.getSupplements(), oldFragment.getQuantity());
			

		Item item = fragment.getItem();
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
		fragment.setSelections(selections);
		fragment.setQuantity(quantity);
		/*
		Fragment fragment = new Fragment(null, item, selections,
				supplements, quantity);*/

		// Adds the fragment that was just created to the order.
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

            // Get defaults from shared preferences.
            Context context = Patron.getContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences("com.patron", Context.MODE_PRIVATE);
            String defaultPayment = sharedPreferences.getString("payment", "-1");
            String defaultTip = sharedPreferences.getString("tip", "0.00");

			if (Globals.getVendor().getStations() != null && Globals.getVendor().getStations().size() > 0)
			{
				station = Globals.getVendor().getStations().get(0);
			}
			if (Globals.getUser().getFunders() != null && Globals.getUser().getFunders().size() > 0)
			{
                List<Funder> funders = Globals.getUser().getFunders();
								funder = Globals.getUser().getFunders().get(0);
                for (int i = 0; i < funders.size(); i++)
                {
                    Funder tempFunder = funders.get(i);
                    if (tempFunder.getId().equals(defaultPayment) && !defaultPayment.equals("-1"))
                    {
                        funder = tempFunder;
                    }
                }
			}
			try
			{
				Vendor vendor = (Vendor)Globals.getVendor().clone();
				vendor.setItems(null);
				vendor.setFilteredItems(null);
				vendor.setRecommendations(null);
				order = new Order(null, vendor, Globals.getUser(), fragments, Order.Status.WAITING,
					station, funder, tip, coupons, comment);
                BigDecimal price = order.getPrice();
                BigDecimal newTip = price.multiply(new BigDecimal(defaultTip));
                order.setTip(newTip);
			}
			catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
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
		Toast toast = Toast.makeText(v.getContext(),
				"Added " + item.getName() + " to order", Toast.LENGTH_SHORT);
		toast.show();
	}
}
