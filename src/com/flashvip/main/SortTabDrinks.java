package com.flashvip.main;

import java.util.ArrayList;

public class SortTabDrinks
{
	public static ArrayList<Product> getByName(ArrayList<Product> products, boolean ascending)
	{
		if (products != null & !products.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < products.size(); i++)
				{
					if ( (ascending == true &&
							products.get(i).getName().compareTo(products.get(0).getName()) < 0) ||
							(ascending == false &&
							products.get(i).getName().compareTo(products.get(0).getName()) > 0) )
					{
						changed = true;
						Product tempDrink = products.get(i);
						products.remove(i);
						products.add(0, tempDrink);
					}
				}
			}
		}
		return products;
	}
	
	public static ArrayList<Product> getByPrice(ArrayList<Product> products, boolean ascending)
	{
		if (products != null & !products.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < products.size(); i++)
				{
					if ( (ascending == true &&
							products.get(i).getPrice() < products.get(0).getPrice()) ||
							(ascending == false &&
							products.get(i).getPrice() > products.get(0).getPrice()) )
					{
						changed = true;
						Product tempDrink = products.get(i);
						products.remove(i);
						products.add(0, tempDrink);
					}
				}
			}
		}
		return products;
	}
	
	public static ArrayList<Product> getByType(ArrayList<Product> products, boolean ascending)
	{
		if (products != null & !products.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < products.size(); i++)
				{
					if ( (ascending == true &&
							products.get(i).getType().name().compareTo(products.get(0).getType().name()) < 0) ||
							(ascending == false &&
							products.get(i).getType().name().compareTo(products.get(0).getType().name()) > 0) )
					{
						changed = true;
						Product tempDrink = products.get(i);
						products.remove(i);
						products.add(0, tempDrink);
					}
				}
			}
		}
		return products;
	}
}