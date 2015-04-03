package me.endeal.patron.listeners;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import me.endeal.patron.listeners.OnMenuRefreshListener;
import me.endeal.patron.main.FlashMenu;
import me.endeal.patron.model.Category;
import me.endeal.patron.model.Item;
import me.endeal.patron.R;
import me.endeal.patron.sort.ProductSorter;
import me.endeal.patron.system.Globals;

public class ButtonCategoriesListener// implements OnClickListener
{
	/*
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
						R.drawable.button_category_unpressed).getConstantState())
		{
			// Set other backgrounds to be unselected
			if (activity.getButtonFavorites() != null)
			{
				activity.getButtonFavorites().setBackgroundResource(
						R.drawable.button_category_unpressed);
			}
			if (activity.getButtonCategories() != null &&
					!activity.getButtonCategories().isEmpty())
			{
				for (int i = 0; i < activity.getButtonCategories().size(); i++)
				{
					Button button = activity.getButtonCategories().get(i);
					button.setBackgroundResource(R.drawable.button_category_unpressed);
				}
			}

			v.setBackgroundResource(R.drawable.button_category_pressed);
			List<Item> items = Globals.getVendor().getItems();
			List<Item> newItems = ProductSorter.getByCategory(items, category, true);
			Globals.getVendor().setFilteredItems(newItems);
            listener.onExecuted();
		}
		else
		{
			v.setBackgroundResource(R.drawable.button_category_unpressed);
			Globals.getVendor().setFilteredItems(Globals.getVendor().getItems());
            listener.onExecuted();
		}
	}
	*/
}
