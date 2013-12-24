package com.flashvip.bind;

import com.flashvip.listeners.ToggleButtonFavoriteListener;
import com.flashvip.lists.ListFonts;
import com.flashvip.main.Globals;
import com.flashvip.main.R;
import com.flashvip.model.Attribute;
import com.flashvip.model.Item;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ProductBinder implements SimpleAdapter.ViewBinder
{
	public boolean setViewValue(View view, Object data, String textRepresentation)
    {
		// Change the font of the name and price.
		if (view.getId() == R.id.productListItemTextName ||
				view.getId() == R.id.productListItemTextPrice)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_BOLD);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
		}
		
		// Set the categories text and font.
		else if (view.getId() == R.id.productListItemTextCategories)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_BOLD);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
		}
		
		// Set the toggled state of the favorite button.
		else if (view.getId() == R.id.productListItemToggleButtonFavorite)
		{
			ToggleButton toggle = (ToggleButton) view;
			Item item = Globals.getItemById(data.toString());
			ToggleButtonFavoriteListener listener = new ToggleButtonFavoriteListener(item);
			toggle.setOnClickListener(listener);
			
			if (Globals.getFavoriteItems() != null && Globals.getFavoriteItems().contains(item))
				toggle.setChecked(true);
			else
				toggle.setChecked(false);
			
        	return true;
		}
		
		else if (view.getId() == R.id.productListItemLayout)
		{
			RelativeLayout relativeLayout = (RelativeLayout)view;
			Item item = Globals.getItemById(data.toString());

			// Remove excess attributes.
			if (relativeLayout.getChildCount() > 5)
				relativeLayout.removeViews(5, relativeLayout.getChildCount() - 5);

			// Create the attributes
			if (item.getAttributes() != null &&
					!item.getAttributes().isEmpty())
			{
				Spinner lastSpinner = null;
				for (int i = 0; i < item.getAttributes().size(); i++)
				{
					Attribute attribute = item.getAttributes().get(i);
					String[] optionNames = new String[attribute.getOptions().size()];
					for (int j = 0; j < attribute.getOptions().size(); j++)
					{
						optionNames[j] = attribute.getOptions().get(j).getName(); 
					}
					ArrayAdapter<String>arrayAdapter = new
							ArrayAdapter<String>(view.getContext(),
									android.R.layout.simple_spinner_item,
									optionNames);
					Spinner spinner = new Spinner(view.getContext());
					spinner.setAdapter(arrayAdapter);
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					if (lastSpinner == null)
					{
						params.addRule(RelativeLayout.BELOW,
								R.id.productListItemTextPrice);
					}
					else
					{
						params.addRule(RelativeLayout.BELOW, lastSpinner.getId());
						
					}
					spinner.setLayoutParams(params);

					// Add the attribute to the view.
					relativeLayout.addView(spinner);
					spinner.setId(1 + i);
					lastSpinner = spinner;
				}
			}
			return true;
		}
		
		// Set the quantity spinner.
		/*
        else if (view.getId() == R.id.productListItemSpinnerQuantity)
        {
        	// Set the listener for the spinner
        	final int drink_id = Integer.parseInt(data.toString());
        	((Spinner)view).setOnItemSelectedListener(new OnItemSelectedListener() {

        		public void onItemSelected(AdapterView<?> adapter, View view,
        				int position, long id)
        		{
        			Globals.setArrayToAlcohol(drink_id, position);
        		}

        		public void onNothingSelected(AdapterView<?> arg0)
        		{
        		}
        	});
        	return true;
        }*/
        return false;
    }
}