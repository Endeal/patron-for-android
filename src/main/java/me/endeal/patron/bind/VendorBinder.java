package me.endeal.patron.bind;

import android.graphics.Typeface;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import me.endeal.patron.listeners.ToggleButtonFavoriteListener;
import me.endeal.patron.lists.ListFonts;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

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
            if (view.getId() == R.id.locationListItemTextPhone)
            {
                String phone = (String)data;
                if (phone.length() == 10)
                {
                    phone = "(" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + "-" + phone.substring(6, 10);
                    text.setText(phone);
                    return true;
                }
            }
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
            if (Globals.getUser().hasFavoriteVendor(vendor.getId()))
            {
                toggle.setChecked(true);
            }

        	return true;
		}
        return false;
    }
}