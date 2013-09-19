package com.flashvip.main;

import java.util.ArrayList;

public class SortDrinks
{
	public static ArrayList<Drink> getByName(ArrayList<Drink> drinks, boolean ascending)
	{
		if (drinks != null & !drinks.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < drinks.size(); i++)
				{
					if ( (ascending == true &&
							drinks.get(i).getName().compareTo(drinks.get(0).getName()) < 0) ||
							(ascending == false &&
							drinks.get(i).getName().compareTo(drinks.get(0).getName()) > 0) )
					{
						changed = true;
						Drink tempDrink = drinks.get(i);
						drinks.remove(i);
						drinks.add(0, tempDrink);
					}
				}
			}
		}
		return drinks;
	}
	
	public static ArrayList<Drink> getByPrice(ArrayList<Drink> drinks, boolean ascending)
	{
		if (drinks != null & !drinks.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < drinks.size(); i++)
				{
					if ( (ascending == true &&
							drinks.get(i).getPrice() < drinks.get(0).getPrice()) ||
							(ascending == false &&
							drinks.get(i).getPrice() > drinks.get(0).getPrice()) )
					{
						changed = true;
						Drink tempDrink = drinks.get(i);
						drinks.remove(i);
						drinks.add(0, tempDrink);
					}
				}
			}
		}
		return drinks;
	}
	
	public static ArrayList<Drink> getByType(ArrayList<Drink> drinks, boolean ascending)
	{
		if (drinks != null & !drinks.isEmpty())
		{
			boolean changed = true;
			while (changed == true)
			{
				changed = false;
				for (int i = 0; i < drinks.size(); i++)
				{
					if ( (ascending == true &&
							drinks.get(i).getType().name().compareTo(drinks.get(0).getType().name()) < 0) ||
							(ascending == false &&
							drinks.get(i).getType().name().compareTo(drinks.get(0).getType().name()) > 0) )
					{
						changed = true;
						Drink tempDrink = drinks.get(i);
						drinks.remove(i);
						drinks.add(0, tempDrink);
					}
				}
			}
		}
		return drinks;
	}
}