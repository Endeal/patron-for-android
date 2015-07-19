package me.endeal.patron.listeners;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.IndexOutOfBoundsException;
import java.lang.Exception;
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

import me.endeal.patron.model.Attribute;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Item;
import me.endeal.patron.model.Option;
import me.endeal.patron.model.Order;
import me.endeal.patron.model.Price;
import me.endeal.patron.model.Selection;
import me.endeal.patron.model.Station;
import me.endeal.patron.model.Retrieval;
import me.endeal.patron.model.Funder;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.PatronApplication;
import static me.endeal.patron.model.Retrieval.Method;

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
        for (int i = 0; i < relativeLayout.getChildCount(); i++)
        {
            View view = (View)relativeLayout.getChildAt(i);
            if (view.getTag() != null && view.getTag().equals("attribute"))
            {
                Spinner spinner = (Spinner)view;
                spinnerAttributes.add(spinner);
            }
        }

		// Gets the item for this given row.
		Fragment oldFragment = Globals.getFragments().get(row);
        final Fragment fragment = (Fragment)deepClone(oldFragment);

        // Get the selections for the item.
		Item item = fragment.getItem();
		ArrayList<Selection> selections = new ArrayList<Selection>();
        try
        {
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
        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            Toast.makeText(v.getContext(), "Failed to add item to order", Toast.LENGTH_SHORT).show();
            return;
        }

		// Adds the fragment that was just created to the order.
        List<Fragment> fragments = new ArrayList<Fragment>();
		Order order = Globals.getOrder();
		if (order == null)
		{
			// Create default order info
			Station station = null;
			Funder funder = null;
            Price tip = new Price(0, "USD");
			List<Object> coupons = new ArrayList<Object>();
			String comment = "";

            // Get defaults from shared preferences.
            Context context = PatronApplication.getContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences("me.endeal.patron", Context.MODE_PRIVATE);
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

            long time = System.currentTimeMillis() / 1000;
            Retrieval retrieval = new Retrieval(Method.Pickup, null, null, null);
            order = new Order(null, fragments, null, tip, comment, retrieval, time,
                    Order.Status.WAITING, funder, Globals.getVendor(), null);
		}
		if (order.getFragments() != null &&
				!order.getFragments().isEmpty())
		{
			fragments = order.getFragments();
		}
		fragments.add(fragment);
		order.setFragments(fragments);
		Globals.setOrder(order);

		// Creates a Toast that informs the user that the drink has been added to the tab.
		Toast toast = Toast.makeText(v.getContext(), "Added " + item.getName() + " to order", Toast.LENGTH_SHORT);
		toast.show();
	}

    public static Object deepClone(Object object)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
 }
}
