/**
 * An order has a name, a type of alcohol,
 * an identification number, a price, and a
 * boolean variable indicating whether or not
 * it is in the top drinks of the user.
 */

package com.flashvip.main;

import java.util.Locale;

public class Drink
{
	public static enum Alcohol
	{
		BRANDY, GIN, HOUSE_LIQUOR, NONE, OTHER, TEQUILA, VODKA, WHISKEY;
	}
	public static enum Type
	{
		BEER, COCKTAIL, MARTINI, MIXED, NONE, OTHER, PREMIUM, SHOT;
	}
	
	private String id;
	private String name;
	private Type type;
	private Alcohol alcohol;
	private double price;
	private boolean topOrder;
	
	public Drink()
	{
		id = "" + Integer.parseInt(name) + Integer.parseInt(alcohol.toString());
		name = "";
		type = Type.BEER;
		alcohol = Alcohol.HOUSE_LIQUOR;
		price = 0;
		topOrder = false;
	}
	
	public Drink(String id, String name, Type type, Alcohol alcohol, double price)
	{
		this.id = id;
		this.name = name;
		this.alcohol = alcohol;
		this.type = type;
		this.price = price;
		topOrder = false;
		int counter = Globals.getMyTopOrders().size();
		for (int i = 0; i < counter; i++)
		{
			if (Globals.getMyTopOrders().get(i).getId() == id)
			{
				topOrder = true;
				break;
			}
		}
	}
	
	public Drink(String id, String name, String type, String alcohol, double price)
	{
		this.id = id;
		this.name = name;
		this.alcohol = decodeAlcohol(alcohol);
		this.type = decodeType(type);
		this.price = price;
		topOrder = false;
		if (Globals.getMyTopOrders() != null)
		{
			int counter = Globals.getMyTopOrders().size();
			for (int i = 0; i < counter; i++)
			{
				if (Globals.getMyTopOrders().get(i).getId() == id)
				{
					topOrder = true;
					break;
				}
			}
		}
	}
	
	// MAIN METHODS
	
	public void generateId()
	{
		id = "" + Integer.parseInt(name) + Integer.parseInt(alcohol.toString());
	}
	
	public void toggleTopOrder()
	{
		if (topOrder == false)
			topOrder = true;
		else
			topOrder = false;
	}
	
	public Alcohol decodeAlcohol(String s)
	{
		Alcohol a;
		if (s.equals("Brandy"))
			a = Alcohol.BRANDY;
		else if (s.equals("Gin"))
			a = Alcohol.GIN;
		else if (s.equals("House"))
			a = Alcohol.HOUSE_LIQUOR;
		else if (s.equals("Tequila"))
			a = Alcohol.TEQUILA;
		else if (s.equals("Vodka"))
			a = Alcohol.VODKA;
		else if (s.equals("Whiskey"))
			a = Alcohol.WHISKEY;
		else if (s.equals("Other"))
			a = Alcohol.OTHER;
		else
			a = Alcohol.NONE;
		return a;
	}
	
	public Type decodeType(String s)
	{
		s = s.toLowerCase(Locale.US);
		Type t;
		
		// Switching Strings isn't permitted until Android 1.7. Keep the if-else.
		if (s.equals("beer"))
			t = Type.BEER;
		else if (s.equals("cocktail"))
			t = Type.COCKTAIL;
		else if (s.equals("martini"))
			t = Type.MARTINI;
		else if (s.equals("mixed"))
			t = Type.MIXED;
		else if (s.equals("premium"))
			t = Type.PREMIUM;
		else if (s.equals("shot"))
			t = Type.SHOT;
		else
			t = Type.OTHER;
		return t;
	}
	
	// SETTER METHODS
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setType(Type type)
	{
		this.type = type;
	}
	
	public void setAlcohol(Alcohol alcohol)
	{
		this.alcohol = alcohol;
	}
	
	public void setPrice(double price)
	{
		this.price = price;
	}
	
	public void setTopOrder(boolean topOrder)
	{
		this.topOrder = topOrder;
	}
	
	// ACCESSOR METHODS
	public String getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public Alcohol getAlcohol()
	{
		return alcohol;
	}
	
	public double getPrice()
	{
		return price;
	}
	
	public boolean getTopOrder()
	{
		return topOrder;
	}
}
