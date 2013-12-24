package com.flashvip.listeners;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.flashvip.main.FlashMenu;
import com.flashvip.main.Globals;
import com.flashvip.main.R;
import com.flashvip.model.Item;
import com.flashvip.sort.ProductSorter;

public class ButtonFavoritesListener implements OnClickListener
{
	FlashMenu activity;
	
	public ButtonFavoritesListener(FlashMenu activity)
	{
		
		this.activity = activity;
	}
	
	public void onClick(View v)
	{
		if (v.getBackground().getConstantState() ==
				v.getContext().getResources().getDrawable(
						R.drawable.mainbutton).getConstantState())
		{
			// Set backgrounds to unselected for other buttons.
			if (activity.getButtonCategories() != null &&
					!activity.getButtonCategories().isEmpty())
			{
				for (int i = 0; i < activity.getButtonCategories().size(); i++)
				{
					Button button = activity.getButtonCategories().get(i);
					button.setBackgroundResource(R.drawable.mainbutton);
				}
			}

			v.setBackgroundResource(R.drawable.mainbuttonselected);
			ArrayList<Item> items = Globals.getVendor().getItems();
			ArrayList<Item> newItems = ProductSorter.getByFavorites(items, true);
			Globals.getVendor().setFilteredItems(newItems);
			activity.updateListViewMenu();
		}
		else
		{
			v.setBackgroundResource(R.drawable.mainbutton);
			Globals.getVendor().setFilteredItems(Globals.getVendor().getItems());
			activity.updateListViewMenu();
		}
	}
}
