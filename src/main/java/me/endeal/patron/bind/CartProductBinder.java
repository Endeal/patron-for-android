package me.endeal.patron.bind;

import java.text.NumberFormat;
import java.util.List;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.TextView;

import me.endeal.patron.listeners.ButtonRemoveListener;
import me.endeal.patron.listeners.ButtonSupplementsListener;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.listeners.SpinnerAttributeListener;
import me.endeal.patron.listeners.SpinnerQuantityListener;
import me.endeal.patron.lists.ListFonts;
import me.endeal.patron.main.FlashCart;
import me.endeal.patron.model.Attribute;
import me.endeal.patron.model.Option;
import me.endeal.patron.model.Selection;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Item;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.Loadable;

public class CartProductBinder implements ViewBinder
{
    private OnApiExecutedListener listener;

	public CartProductBinder(OnApiExecutedListener listener)
	{
        this.listener = listener;
	}

	public boolean setViewValue(View view, Object data, String textRepresentation)
	{
		// Change the font of the name and price.
		if (view.getId() == R.id.cartListItemTextName ||
				view.getId() == R.id.cartListItemTextPrice)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_BOLD);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
			Fragment fragment = (Fragment)data;
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			String price = formatter.format(fragment.getItem().getPrice());
			if (view.getId() == R.id.cartListItemTextName)
				text.setText(fragment.getItem().getName());
			else
				text.setText(price);
			return true;
		}

        // Set the supplements button
		else if (view.getId() == R.id.cartListItemButtonSupplements)
		{
			Fragment fragment = (Fragment)data;
			Button button = (Button)view;
			Item item = fragment.getItem();
            /*
			if (item.getSupplements() != null && item.getSupplements().size() > 0)
			{
				ButtonSupplementsListener listener = new ButtonSupplementsListener(fragment);
				button.setOnClickListener(listener);
				button.setVisibility(View.VISIBLE);
			}
			else
			{
				button.setVisibility(View.GONE);
			}
            */
			return true;
		}

		// Add the tag to each Remove Button.
		else if (view.getId() == R.id.cartListItemButtonRemove)
		{
			Fragment fragment = (Fragment)data;
            ButtonRemoveListener removeListener = new ButtonRemoveListener(listener, fragment);
            ((Button)view).setOnClickListener(removeListener);
            return true;
        }

		// Add the attributes
		else if (view.getId() == R.id.cartListItemLayout)
		{
			RelativeLayout relativeLayout = (RelativeLayout)view;
			Fragment fragment = (Fragment)data;
			List<Selection> selections = fragment.getSelections();

			// Remove excess attributes.
			if (relativeLayout.getChildCount() > 5)
				relativeLayout.removeViews(5, relativeLayout.getChildCount() - 5);

			// Create the attributes
			if (selections != null &&
					!selections.isEmpty())
			{
				Spinner lastSpinner = null;
				for (int i = 0; i < selections.size(); i++)
				{
					Attribute attribute = selections.get(i).getAttribute();
					Option option = selections.get(i).getOption();
					int selectedIndex = 0;
					String[] optionNames = new String[attribute.getOptions().size()];
					for (int j = 0; j < attribute.getOptions().size(); j++)
					{
						optionNames[j] = attribute.getOptions().get(j).getName();
						if (attribute.getOptions().get(j).getId().equals(
								option.getId()))
							selectedIndex = j;
					}
					ArrayAdapter<String>arrayAdapter = new
							ArrayAdapter<String>(view.getContext(),
									android.R.layout.simple_spinner_item,
									optionNames);

					// Create the spinner.
					Spinner spinner = new Spinner(view.getContext());
					spinner.setAdapter(arrayAdapter);
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					float marginBottom = Globals.convertDpToPixel(10, view.getContext());
					float marginRight = Globals.convertDpToPixel(10, view.getContext());
                    params.setMargins(0, 0, (int)marginBottom, (int)marginRight);
					params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					if (lastSpinner == null)
					{
						params.addRule(RelativeLayout.BELOW,
								R.id.cartListItemTextPrice);
					}
					else
					{
						params.addRule(RelativeLayout.BELOW, lastSpinner.getId());

					}
					spinner.setLayoutParams(params);
					spinner.setSelection(selectedIndex);
					SpinnerAttributeListener attributeListener = new SpinnerAttributeListener(fragment, attribute, listener);
					spinner.setOnItemSelectedListener(attributeListener);

					// Add the attribute to the view.
					relativeLayout.addView(spinner);
					spinner.setId(1 + i);
					lastSpinner = spinner;
				}
			}
			return true;
		}

        // Set up the quantity spinner.
        else if (view.getId() == R.id.cartListItemSpinnerQuantity)
        {
        	Fragment fragment = (Fragment)data;
        	System.out.println("Quantity: " + fragment.getQuantity());
        	((Spinner)view).setSelection(fragment.getQuantity() - 1);
        	SpinnerQuantityListener quantityListener = new SpinnerQuantityListener(fragment, listener);
        	((Spinner)view).setOnItemSelectedListener(quantityListener);
        	return true;
        }
        return false;
    }
}
