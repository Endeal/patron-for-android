package com.flashvip.sort;

import java.util.ArrayList;



import com.flashvip.main.Globals;
import com.flashvip.model.Category;
import com.flashvip.model.Item;

public class ProductSorter
{
	public static ArrayList<Item>getByName(ArrayList<Item>items, boolean ascending)
	{
		int i = 1;
		while (i < items.size())
		{
			if (ascending)
			{
				if (items.get(i).getName().compareTo(items.get(i - 1).getName()) < 0)
				{
					Item temp = items.get(i - 1);
					items.set(i - 1, items.get(i));
					items.set(i, temp);
					i = 0;
				}
			}
			else
			{
				if (items.get(i).getName().compareTo(items.get(i - 1).getName()) > 0)
				{
					Item temp = items.get(i - 1);
					items.set(i - 1, items.get(i));
					items.set(i, temp);
					i = 0;
				}
			}
			i++;
		}
		return items;
	}
	
	public static ArrayList<Item> getByCategory(ArrayList<Item> items, Category category, boolean ascending)
	{
		ArrayList<Item> newItems = new ArrayList<Item>();
		if (items != null & !items.isEmpty())
		{
			for (int i = 0; i < items.size(); i++)
			{
				// Go through each item in the menu
				Item item = items.get(i);
				for (int j = 0; j < item.getCategories().size(); j++)
				{
					// Go through each category in the item
					Category itemCategory = item.getCategories().get(j);
					if (itemCategory.getCategoryId().equals(category.getCategoryId()))
					{
						newItems.add(item);
						break;
					}
				}
			}
		}
		return newItems;
	}
	
	public static ArrayList<Item> getByFavorites(ArrayList<Item> products, boolean ascending)
	{
		ArrayList<Item> newItems = new ArrayList<Item>();
		if (products != null && !products.isEmpty())
		{
			for (int i = 0; i < products.size(); i++)
			{
				Item item = products.get(i);
				if (Globals.getFavoriteItems() != null && Globals.getFavoriteItems().contains(item))
				{
					if (ascending)
						newItems.add(item);
					else
						newItems.add(0, item);
				}
			}
		}
		return newItems;
	}
}