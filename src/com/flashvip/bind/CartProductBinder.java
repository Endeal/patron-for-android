package com.flashvip.bind;

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

import com.flashvip.listeners.ButtonRemoveListener;
import com.flashvip.listeners.SpinnerAttributeListener;
import com.flashvip.listeners.SpinnerQuantityListener;
import com.flashvip.lists.ListFonts;
import com.flashvip.main.FlashCart;
import com.flashvip.main.R;
import com.flashvip.model.Attribute;
import com.flashvip.model.Option;
import com.flashvip.model.Selection;
import com.flashvip.model.Fragment;
import com.flashvip.model.Item;
import com.flashvip.system.Loadable;

public class CartProductBinder implements ViewBinder
{
	private Loadable activity;
	
	public CartProductBinder(Loadable activity)
	{
		this.activity = activity;
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

		// Set the categories text and font.
		else if (view.getId() == R.id.cartListItemTextCategories)
		{
			Fragment fragment = (Fragment)data;
			Item item = fragment.getItem();
			String name = "";
			for (int i = 0; i < item.getCategories().size(); i++)
			{
				if (!name.equals(""))
					name = name + "\n";
				name = name + item.getCategories().get(i).getName();
			}
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_BOLD);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
			text.setText(name);
			return true;
		}

		// Add the tag to each Remove Button.
		else if (view.getId() == R.id.cartListItemButtonRemove)
		{
			Fragment fragment = (Fragment)data;
            ButtonRemoveListener listener = new ButtonRemoveListener(
            		(FlashCart)view.getContext(), fragment);
            ((Button)view).setOnClickListener(listener);
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
						if (attribute.getOptions().get(j).getOptionId().equals(
								option.getOptionId()))
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
					SpinnerAttributeListener listener = new
							SpinnerAttributeListener(fragment, attribute, activity);
					spinner.setOnItemSelectedListener(listener);

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
        	SpinnerQuantityListener listener = new
        			SpinnerQuantityListener(fragment, activity);
        	((Spinner)view).setOnItemSelectedListener(listener);
        	return true;
        }
        return false;
    }
}