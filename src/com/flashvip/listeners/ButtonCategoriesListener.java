package com.flashvip.listeners;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.flashvip.main.FlashMenu;
import com.flashvip.main.Globals;
import com.flashvip.main.R;
import com.flashvip.model.Category;
import com.flashvip.model.Item;
import com.flashvip.sort.ProductSorter;

public class ButtonCategoriesListener implements OnClickListener
{
	FlashMenu activity;
	Category category;
	
	public ButtonCategoriesListener(FlashMenu activity, Category category)
	{
		this.activity = activity;
		this.category = category;
	}
	
	public void onClick(View v)
	{
		if (v.getBackground().getConstantState() ==
				v.getContext().getResources().getDrawable(
						R.drawable.mainbutton).getConstantState())
		{
			// Set other backgrounds to be unselected
			if (activity.getButtonFavorites() != null)
			{
				activity.getButtonFavorites().setBackgroundResource(
						R.drawable.mainbutton);
			}
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
			ArrayList<Item> newItems = ProductSorter.getByCategory(items, category, true);
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
