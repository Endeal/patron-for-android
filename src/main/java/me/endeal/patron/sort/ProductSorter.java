package me.endeal.patron.sort;

import java.util.ArrayList;
import java.util.List;

import me.endeal.patron.model.Category;
import me.endeal.patron.model.Item;
import me.endeal.patron.system.Globals;

public class ProductSorter
{
	public static List<Item>getByName(List<Item>items, boolean ascending)
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
	
	public static List<Item> getByCategory(List<Item> items, Category category, boolean ascending)
	{
		List<Item> newItems = new ArrayList<Item>();
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
					if (itemCategory.getId().equals(category.getId()))
					{
						newItems.add(item);
						break;
					}
				}
			}
		}
		return newItems;
	}
	
	public static List<Item> getByFavorites(List<Item> products, boolean ascending)
	{
		List<Item> newItems = new ArrayList<Item>();
		if (products != null && !products.isEmpty())
		{
			for (int i = 0; i < products.size(); i++)
			{
				Item item = products.get(i);
				if (Globals.getFavoriteItems() != null)
				{
					for (int j = 0; j < Globals.getFavoriteItems().size(); j++)
					{
						Item favoriteItem = Globals.getFavoriteItems().get(j);
						if (favoriteItem.getId().equals(item.getId()))
						{
							if (ascending)
								newItems.add(item);
							else
								newItems.add(0, item);
						}
					}
				}
			}
		}
		return newItems;
	}
	
	public static List<Item> getBySearch(List<Item> items, CharSequence text)
	{
		List<Item> newItems = new ArrayList<Item>();
		if (items != null && !items.isEmpty())
		{
			for (int i = 0; i < items.size(); i++)
			{
				Item currentItem = items.get(i);
				// Check if the name or price contains the query.
				if (currentItem.getName().toLowerCase().contains(text.toString().toLowerCase()) ||
						currentItem.getPrice().toString().toLowerCase().contains(text.toString().toLowerCase()))
				{
					newItems.add(items.get(i));
				}
				// Check if the categories contain the query.
				else
				{
					for (int j = 0; j < currentItem.getCategories().size(); j++)
					{
						Category category = currentItem.getCategories().get(j);
						if (category.getName().toLowerCase().contains(text.toString().toLowerCase()))
						{
							newItems.add(items.get(i));
							break;
						}
					}
				}
			}
		}
		return newItems;
	}
}