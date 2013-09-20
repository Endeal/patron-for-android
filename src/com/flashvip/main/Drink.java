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
		BRANDY, GIN, HOUSE_LIQUOR, NON_ALCOHOLIC, NONE, OTHER, TEQUILA, VODKA, WHISKEY;
	}
	public static enum Type
	{
		DOMESTIC_BEER, IMPORTED_BEER, COCKTAIL, MARTINI, MIXED, NONE, OTHER, PREMIUM, SHOT, SODA;
	}
	
	private String id;
	private String name;
	private Type type;
	private Alcohol alcohol;
	private float price;
	private boolean topOrder;
	
	public Drink()
	{
		id = "" + Integer.parseInt(name) + Integer.parseInt(alcohol.toString());
		name = "";
		type = Type.DOMESTIC_BEER;
		alcohol = Alcohol.HOUSE_LIQUOR;
		price = 0;
		topOrder = false;
	}
	
	public Drink(String id, String name, Type type, Alcohol alcohol, float price)
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
	
	public Drink(String id, String name, String type, String alcohol, float price)
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
		s = s.toLowerCase(Locale.US);
		Alcohol a;

		/* Switch statements with strings only allowed in Java 1.7 & up.
		 	Android needs Java 1.5 or 1.6. Keep the if-else. */
		if (s.equals("brandy"))
			a = Alcohol.BRANDY;
		else if (s.equals("gin"))
			a = Alcohol.GIN;
		else if (s.equals("house liquor"))
			a = Alcohol.HOUSE_LIQUOR;
		else if (s.equals("non-alcoholic"))
			a = Alcohol.NON_ALCOHOLIC;
		else if (s.equals("tequila"))
			a = Alcohol.TEQUILA;
		else if (s.equals("vodka"))
			a = Alcohol.VODKA;
		else if (s.equals("whiskey"))
			a = Alcohol.WHISKEY;
		else if (s.equals("other"))
			a = Alcohol.OTHER;
		else
			a = Alcohol.NONE;

		return a;
	}
	
	public Type decodeType(String s)
	{
		s = s.toLowerCase(Locale.US);
		Type t;

		/* Switch statements with strings only allowed in Java 1.7 & up.
		 	Android needs Java 1.5 or 1.6. Keep the if-else. */
		if (s.equals("domestic beer"))
			t = Type.DOMESTIC_BEER;
		else if (s.equals("imported beer"))
			t = Type.IMPORTED_BEER;
		else if (s.equals("cocktail"))
			t = Type.COCKTAIL;
		else if (s.equals("martini"))
			t = Type.MARTINI;
		else if (s.equals("mixed"))
			t = Type.MIXED;
		else if (s.equals("premium liquor"))
			t = Type.PREMIUM;
		else if (s.equals("shot"))
			t = Type.SHOT;
		else if (s.equals("soda"))
			t = Type.SODA;
		else
			t = Type.OTHER;

		return t;
	}
	
	public static String getAlcoholName(Alcohol a)
	{
		String s;

		/* Switch statements with strings only allowed in Java 1.7 & up.
		 	Android needs Java 1.5 or 1.6. Keep the if-else. */
		if (a == Alcohol.BRANDY)
			s = "Brandy";
		else if (a == Alcohol.GIN)
			s = "Gin";
		else if (a == Alcohol.HOUSE_LIQUOR)
			s = "House Liquor";
		else if (a == Alcohol.NON_ALCOHOLIC)
			s = "Non-Alcoholic";
		else if (a == Alcohol.TEQUILA)
			s = "Tequila";
		else if (a == Alcohol.VODKA)
			s = "Vodka";
		else if (a == Alcohol.WHISKEY)
			s = "Whiskey";
		else if (a == Alcohol.OTHER)
			s = "Other";
		else
			s = "";

		return s;
	}
	
	public static String getTypeName(Type t)
	{
		String s;
		
		/* Switch statements with strings only allowed in Java 1.7 & up.
		 	Android needs Java 1.5 or 1.6. Keep the if-else. */
		if (t == Type.DOMESTIC_BEER)
			s = "Domestic Beer";
		else if (t == Type.IMPORTED_BEER)
			s = "Imported Beer";
		else if (t == Type.COCKTAIL)
			s = "Cocktail";
		else if (t == Type.MARTINI)
			s = "Martini";
		else if (t == Type.MIXED)
			s = "Mixed Drink";
		else if (t == Type.PREMIUM)
			s = "Premium Liquor";
		else if (t == Type.SHOT)
			s = "Shot";
		else if (t == Type.SODA)
			s = "Soda";
		else
			s = "Other";
		
		return s;
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
	
	public void setPrice(float price)
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
