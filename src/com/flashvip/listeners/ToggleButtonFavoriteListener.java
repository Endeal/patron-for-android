package com.flashvip.listeners;

import com.flashvip.main.Globals;

import com.flashvip.model.Item;
import com.flashvip.model.Vendor;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ToggleButtonFavoriteListener implements OnClickListener
{
	// The favorite product if a product toggle button.
	private Item item;
	
	// The favorite location if a location toggle button.
	private Vendor location;
	
	public ToggleButtonFavoriteListener(Item item)
	{
		this.item = item;
		this.location = null;
	}
	
	public ToggleButtonFavoriteListener(Vendor location)
	{
		this.item = null;
		this.location = location;
	}
	
	@Override
	public void onClick(View buttonView)
	{
		// A product was selected.
		if (item != null)
		{
			if (Globals.getFavoriteItems() != null && Globals.getFavoriteItems().contains(item))
			{
				Toast toast = Toast.makeText(buttonView.getContext(), "Removed from favorites.", Toast.LENGTH_SHORT);
				toast.show();
				if (Globals.getFavoriteItems() != null && Globals.getFavoriteItems().size() > 0)
				{
					for (int i = 0; i < Globals.getFavoriteItems().size(); i++)
					{
						if (Globals.getFavoriteItems().get(i) == item)
						{
							Globals.getFavoriteItems().remove(i);
						}
					}
				}
			}
			else
			{
				Toast toast = Toast.makeText(buttonView.getContext(), "Added to favorites.", Toast.LENGTH_SHORT);
				toast.show();
				Globals.getFavoriteItems().add(item);
			}
		}
		
		// A location was selected.
		else
		{
			if (Globals.getFavoriteVendors() != null && Globals.getFavoriteVendors().contains(location))
			{
				Toast toast = Toast.makeText(buttonView.getContext(), "Removed from favorites.", Toast.LENGTH_SHORT);
				toast.show();
				if (Globals.getFavoriteVendors() != null && Globals.getFavoriteVendors().size() > 0)
				{
					for (int i = 0; i < Globals.getFavoriteVendors().size(); i++)
					{
						if (Globals.getFavoriteVendors().get(i) == location)
						{
							Globals.getFavoriteVendors().remove(i);
						}
					}
				}
			}
			else
			{
				Toast toast = Toast.makeText(buttonView.getContext(), "Added to favorites.", Toast.LENGTH_SHORT);
				toast.show();
				Globals.getFavoriteVendors().add(location);
			}
		}
	}
}