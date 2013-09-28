package com.flashvip.main;

import com.flashvip.listeners.ToggleButtonFavoriteListener;
import com.flashvip.lists.ListFonts;

import android.graphics.Typeface;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LocationBinder implements SimpleAdapter.ViewBinder
{
	public boolean setViewValue(View view, Object data, String textRepresentation)
    {
		// Change the font of any text.
		if (view.getId() == R.id.locationListItemTextName)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_BOLD);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
		}
		else if (view.getId() == R.id.locationListItemTextAddress ||
				view.getId() == R.id.locationListItemTextPhone)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_REGULAR);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
		}
		
		// Set the toggled state of the favorite button.
		else if (view.getId() == R.id.productListItemToggleButtonFavorite)
		{
			int row = Integer.parseInt(data.toString());
			ToggleButton toggle = (ToggleButton) view;
			Location location = Globals.getCurrentLocations().get(row);
			ToggleButtonFavoriteListener listener = new ToggleButtonFavoriteListener(location);
			toggle.setOnClickListener(listener);
			
			if (Globals.getFavoriteProducts() != null && Globals.getFavoriteProducts().contains(location))
				toggle.setChecked(true);
			else
				toggle.setChecked(false);
			
        	return true;
		}
        return false;
    }
}