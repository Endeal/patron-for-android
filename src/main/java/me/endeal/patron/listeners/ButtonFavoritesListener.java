package me.endeal.patron.listeners;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.main.FlashMenu;
import me.endeal.patron.model.Item;
import me.endeal.patron.R;
import me.endeal.patron.sort.ProductSorter;
import me.endeal.patron.system.Globals;

public class ButtonFavoritesListener implements OnClickListener
{
	FlashMenu activity;
    OnApiExecutedListener listener;

	public ButtonFavoritesListener(FlashMenu activity, OnApiExecutedListener listener)
	{
		this.activity = activity;
        this.listener = listener;
	}

	public void onClick(View v)
	{
		if (v.getBackground().getConstantState() ==
				v.getContext().getResources().getDrawable(
						R.drawable.button_filter_favorites_unpressed).getConstantState())
		{
			// Set backgrounds to unselected for other buttons.
			if (activity.getButtonCategories() != null &&
					!activity.getButtonCategories().isEmpty())
			{
				for (int i = 0; i < activity.getButtonCategories().size(); i++)
				{
					Button button = activity.getButtonCategories().get(i);
					button.setBackgroundResource(R.drawable.button_filter_favorites_unpressed);
				}
			}

			v.setBackgroundResource(R.drawable.button_filter_favorites_pressed);
			List<Item> items = Globals.getVendor().getItems();
			List<Item> newItems = ProductSorter.getByFavorites(items, true);
			Globals.getVendor().setFilteredItems(newItems);
            listener.onExecuted();
		}
		else
		{
			v.setBackgroundResource(R.drawable.button_filter_favorites_pressed);
			Globals.getVendor().setFilteredItems(Globals.getVendor().getItems());
            listener.onExecuted();
		}
	}
}
