package com.flashvip.listeners;

import com.flashvip.main.Globals;
import com.flashvip.main.Product;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ToggleButtonFavoriteListener implements OnClickListener
{
	private Product product;
	
	public ToggleButtonFavoriteListener(Product product)
	{
		this.product = product;
	}
	
	@Override
	public void onClick(View buttonView)
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
}