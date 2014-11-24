package com.patron.bind;

import android.graphics.Typeface;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.patron.listeners.ToggleButtonFavoriteListener;
import com.patron.lists.ListFonts;
import com.patron.main.R;
import com.patron.model.Vendor;
import com.patron.system.Globals;

public class VendorBinder implements SimpleAdapter.ViewBinder
{
	public boolean setViewValue(View view, Object data, String textRepresentation)
    {
		// Change the font of any text.
		if (view.getId() == R.id.locationListItemTextName)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(),
					ListFonts.FONT_MAIN_BOLD);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
		}
		else if (view.getId() == R.id.locationListItemTextAddress ||
				view.getId() == R.id.locationListItemTextPhone)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(),
					ListFonts.FONT_MAIN_REGULAR);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
		}
		
		// Set the toggled state of the favorite button.
		else if (view.getId() == R.id.locationListItemToggleButtonFavorite)
		{
			int row = Integer.parseInt(data.toString());
			ToggleButton toggle = (ToggleButton) view;
			Vendor vendor = Globals.getVendors().get(row);
			ToggleButtonFavoriteListener listener = new ToggleButtonFavoriteListener(vendor);
			toggle.setOnClickListener(listener);
			
			toggle.setChecked(false);
			for (int i = 0; i < Globals.getFavoriteVendors().size(); i++)
			{
				Vendor favoriteVendor = Globals.getFavoriteVendors().get(i);
				if (favoriteVendor.getId().equals(vendor.getId()))
				{
					toggle.setChecked(true);
				}
			}
			
        	return true;
		}
        return false;
    }
}