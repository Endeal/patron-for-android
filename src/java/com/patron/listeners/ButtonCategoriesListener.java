package com.patron.listeners;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.patron.listeners.OnMenuRefreshListener;
import com.patron.main.FlashMenu;
import com.patron.model.Category;
import com.patron.model.Item;
import com.patron.R;
import com.patron.sort.ProductSorter;
import com.patron.system.Globals;

public class ButtonCategoriesListener implements OnClickListener
{
	FlashMenu activity;
	Category category;
    OnMenuRefreshListener listener;

	public ButtonCategoriesListener(FlashMenu activity, Category category, OnMenuRefreshListener listener)
	{
		this.activity = activity;
		this.category = category;
        this.listener = listener;
	}

	public void onClick(View v)
	{
		if (v.getBackground().getConstantState() ==
				v.getContext().getResources().getDrawable(
						R.drawable.button_filter_off).getConstantState())
		{
			// Set other backgrounds to be unselected
			if (activity.getButtonFavorites() != null)
			{
				activity.getButtonFavorites().setBackgroundResource(
						R.drawable.button_filter_off);
			}
			if (activity.getButtonCategories() != null &&
					!activity.getButtonCategories().isEmpty())
			{
				for (int i = 0; i < activity.getButtonCategories().size(); i++)
				{
					Button button = activity.getButtonCategories().get(i);
					button.setBackgroundResource(R.drawable.button_filter_off);
				}
			}

			v.setBackgroundResource(R.drawable.button_filter_on);
			List<Item> items = Globals.getVendor().getItems();
			List<Item> newItems = ProductSorter.getByCategory(items, category, true);
			Globals.getVendor().setFilteredItems(newItems);
            listener.onExecuted();
		}
		else
		{
			v.setBackgroundResource(R.drawable.button_filter_off);
			Globals.getVendor().setFilteredItems(Globals.getVendor().getItems());
            listener.onExecuted();
		}
	}
}
