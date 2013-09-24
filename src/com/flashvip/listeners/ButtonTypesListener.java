package com.flashvip.listeners;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;

import com.flashvip.main.FlashMenu;
import com.flashvip.main.Globals;
import com.flashvip.main.Product;
import com.flashvip.main.Product.Type;
import com.flashvip.main.ProductSorter;

public class ButtonTypesListener implements OnClickListener
{
	FlashMenu activity;
	Type type;
	
	public ButtonTypesListener(FlashMenu activity, Type type)
	{
		this.activity = activity;
		this.type = type;
	}
	
	public void onClick(View v)
	{
		ArrayList<Product> currentProducts = Globals.getProducts();
		ArrayList<Product> newProducts = ProductSorter.getByType(currentProducts, type, true);
		Globals.setCurrentProducts(newProducts);
		activity.updateListViewMenu();
	}
}
