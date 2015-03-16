package com.patron.listeners;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.patron.model.Item;
import com.patron.model.Vendor;
import com.patron.system.ApiExecutor;
import com.patron.system.Globals;
import com.google.gson.Gson;

public class ToggleButtonFavoriteListener implements OnClickListener
{
	// The favorite product if a product toggle button.
	private Item item;

	// The favorite location if a location toggle button.
	private Vendor vendor;

	public ToggleButtonFavoriteListener(Item item)
	{
		this.item = item;
		this.vendor = null;
	}

	public ToggleButtonFavoriteListener(Vendor vendor)
	{
		this.item = null;
		this.vendor = vendor;
	}

	@Override
	public void onClick(View buttonView)
	{
        final Context context = buttonView.getContext();
		// A product was selected.
		if (item != null)
		{
			ApiExecutor executor = new ApiExecutor();
			if (Globals.getUser().hasFavoriteItem(item.getId()))
			{
				executor.removeFavoriteItem(item);
			}
			else
			{
				executor.addFavoriteItem(item);
			}
		}

		// A location was selected.
		else
		{
            ApiExecutor executor = new ApiExecutor();
            if (Globals.getUser().hasFavoriteVendor(vendor.getId()))
            {
                executor.removeFavoriteVendor(vendor);
            }
            else
            {
                executor.addFavoriteVendor(vendor);
            }
		}
	}

}
