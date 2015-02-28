package com.patron.sort;

import java.util.ArrayList;

import com.patron.model.Item;

public class SortTabDrinks
{
	public static ArrayList<Item> getByName(ArrayList<Item> items, boolean ascending)
	{
		if (items != null & !items.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < items.size(); i++)
				{
					if ( (ascending == true &&
							items.get(i).getName().compareTo(items.get(0).getName()) < 0) ||
							(ascending == false &&
							items.get(i).getName().compareTo(items.get(0).getName()) > 0) )
					{
						changed = true;
						Item tempDrink = items.get(i);
						items.remove(i);
						items.add(0, tempDrink);
					}
				}
			}
		}
		return items;
	}
	
	public static ArrayList<Item> getByPrice(ArrayList<Item> items, boolean ascending)
	{
		if (items != null & !items.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < items.size(); i++)
				{
					if ( (ascending == true &&
							items.get(i).getPrice().compareTo(items.
									get(0).getPrice()) == -1) ||
							(ascending == false &&
							items.get(i).getPrice().compareTo(items.
									get(0).getPrice()) == 1) )
					{
						changed = true;
						Item tempDrink = items.get(i);
						items.remove(i);
						items.add(0, tempDrink);
					}
				}
			}
		}
		return items;
	}
	
	/*public static ArrayList<Item> getByType(ArrayList<Item> items, boolean ascending)
	{
		if (items != null & !items.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < items.size(); i++)
				{
					if ( (ascending == true &&
							items.get(i).getCategories().name().compareTo(items.get(0).getType().name()) < 0) ||
							(ascending == false &&
							items.get(i).getType().name().compareTo(items.get(0).getType().name()) > 0) )
					{
						changed = true;
						Item tempDrink = items.get(i);
						items.remove(i);
						items.add(0, tempDrink);
					}
				}
			}
		}
		return items;
	}*/
}