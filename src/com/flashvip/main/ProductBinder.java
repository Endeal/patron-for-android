package com.flashvip.main;

import java.util.ArrayList;

import com.flashvip.listeners.ToggleButtonFavoriteListener;
import com.flashvip.lists.ListFonts;

import android.graphics.Typeface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ProductBinder implements SimpleAdapter.ViewBinder
{
	public boolean setViewValue(View view, Object data, String textRepresentation)
    {
		// Change the font of any text.
		if (view.getId() == R.id.productListItemTextName ||
				view.getId() == R.id.productListItemTextPrice)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_BOLD);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
		}
		else if (view.getId() == R.id.productListItemTextType ||
				view.getId() == R.id.productListItemTextAlcohol)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_REGULAR);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
		}
		
		// Set the toggled state of the favorite button.
		else if (view.getId() == R.id.productListItemToggleButtonFavorite)
		{
			ToggleButton toggle = (ToggleButton) view;
			Product product = Globals.getProductById(data.toString());
			ToggleButtonFavoriteListener listener = new ToggleButtonFavoriteListener(product);
			toggle.setOnClickListener(listener);
			
			if (Globals.getFavoriteProducts() != null && Globals.getFavoriteProducts().contains(product))
				toggle.setChecked(true);
			else
				toggle.setChecked(false);
			
        	return true;
		}
		
        // Set up the alcohol spinner.
		else if (view.getId() == R.id.productListItemSpinnerAlcohol)
        {
        	ArrayList<String> names = new ArrayList<String>();
        	
        	// Add alcohols based on current drink's alcohol type.
        	Product currentDrink = Globals.getProductById(data.toString());
        	for (int i = 0; i < Globals.getAlcohols().size(); i++)
        	{
        		if (Globals.getAlcohols().get(i).getAlcohol() == currentDrink.getAlcohol())
        			names.add(Globals.getAlcohols().get(i).getName());
        	}
        	
        	// Set to invisible if empty.
        	if (names.size() == 0)
        	{
        		view.setVisibility(View.INVISIBLE);
        	}
        	else
        	{
        		// Set to visible if not empty.
        		view.setVisibility(View.VISIBLE);
        		
        		// Bind the adapter to the names.
        		ArrayAdapter<String> alcohol_adapter = new ArrayAdapter<String>(view.getContext(),
        				android.R.layout.simple_spinner_item, names);
        		((Spinner)view).setAdapter(alcohol_adapter);
        	
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
        	}
        	return true;
        }
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
        }
        return false;
    }
}