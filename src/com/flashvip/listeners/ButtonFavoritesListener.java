package com.flashvip.listeners;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;

import com.flashvip.main.FlashMenu;
import com.flashvip.main.Globals;
import com.flashvip.main.Product;
import com.flashvip.main.ProductSorter;

public class ButtonFavoritesListener implements OnClickListener
{
	FlashMenu activity;
	
	public ButtonFavoritesListener(FlashMenu activity)
	{
		this.activity = activity;
	}
	
	public void onClick(View v)
	{
		ArrayList<Product> currentProducts = Globals.getProducts();
		ArrayList<Product> newProducts = ProductSorter.getByFavorites(currentProducts, true);
		Globals.setCurrentProducts(newProducts);
		activity.updateListViewMenu();
	}
}
