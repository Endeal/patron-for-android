package com.flashvip.listeners;

import com.flashvip.main.Globals;
import com.flashvip.main.Product;
import com.flashvip.main.Location;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ToggleButtonFavoriteListener implements OnClickListener
{
	// The favorite product if a product toggle button.
	private Product product;
	
	// The favorite location if a location toggle button.
	private Location location;
	
	public ToggleButtonFavoriteListener(Product product)
	{
		this.product = product;
		this.location = null;
	}
	
	public ToggleButtonFavoriteListener(Location location)
	{
		this.product = null;
		this.location = location;
	}
	
	@Override
	public void onClick(View buttonView)
	{
		// A product was selected.
		if (product != null)
		{
			if (Globals.getFavoriteProducts() != null && Globals.getFavoriteProducts().contains(product))
			{
				Toast toast = Toast.makeText(buttonView.getContext(), "Removed from favorites.", Toast.LENGTH_SHORT);
				toast.show();
				if (Globals.getFavoriteProducts() != null && Globals.getFavoriteProducts().size() > 0)
				{
					for (int i = 0; i < Globals.getFavoriteProducts().size(); i++)
					{
						if (Globals.getFavoriteProducts().get(i) == product)
						{
							Globals.removeFavoriteProduct(i);
						}
					}
				}
			}
			else
			{
				Toast toast = Toast.makeText(buttonView.getContext(), "Added to favorites.", Toast.LENGTH_SHORT);
				toast.show();
				Globals.addFavoriteProduct(product);
			}
		}
		
		// A location was selected.
		else
		{
			if (Globals.getFavoriteLocations() != null && Globals.getFavoriteLocations().contains(location))
			{
				Toast toast = Toast.makeText(buttonView.getContext(), "Removed from favorites.", Toast.LENGTH_SHORT);
				toast.show();
				if (Globals.getFavoriteLocations() != null && Globals.getFavoriteLocations().size() > 0)
				{
					for (int i = 0; i < Globals.getFavoriteLocations().size(); i++)
					{
						if (Globals.getFavoriteLocations().get(i) == location)
						{
							Globals.removeFavoriteLocation(i);
						}
					}
				}
			}
			else
			{
				Toast toast = Toast.makeText(buttonView.getContext(), "Added to favorites.", Toast.LENGTH_SHORT);
				toast.show();
				Globals.addFavoriteLocation(location);
			}
		}
	}
}