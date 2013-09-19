package com.flashvip.main;

import java.util.ArrayList;

public class Server
{
	private String id;
	private String name;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private ArrayList<Drink> orders;
	private ArrayList<Drink> topOrders;
	
	// CONSTRUCTORS
	
	public Server()
	{
		name = "John's";
		orders = new ArrayList<Drink>();
	}
	
	public Server(String name)
	{
		this.name = name;
		orders = new ArrayList<Drink>();
	}
	
	public Server(String name, String id)
	{
		this.name = name;
		this.id = id;
	}
	
	public Server(String name, ArrayList<Drink> orders)
	{
		this.name = name;
		this.orders = orders;
	}
	
	public Server(String name, ArrayList<Drink> orders, ArrayList<Drink> top)
	{
		this.name = name;
		this.orders = orders;
		topOrders = top;
	}
	
	public Server(String name, String id, String address, String city, String state,
			String zip, String phone)
	{
		this.name = name;
		this.id = id;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone = phone;
	}
	
	public Server(String name, String id, String address, String city, String state,
			String zip, String phone, ArrayList<Drink> orders, ArrayList<Drink> top)
	{
		this.name = name;
		this.id = id;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone = phone;
		this.orders = orders;
		this.topOrders = top;
	}
	
	// SETTER METHODS
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setName(String s)
	{
		name = s;
	}
	
	public void setAddress(String s)
	{
		address = s;
	}
	
	public void setCity(String s)
	{
		city = s;
	}
	
	public void setState(String s)
	{
		state = s;
	}
	
	public void setZip(String s)
	{
		zip = s;
	}
	
	public void setPhone(String s)
	{
		phone = s;
	}
	
	public void setDrinks(ArrayList<Drink> d)
	{
		orders = d;
	}
	
	public void setTopDrinks(ArrayList<Drink> d)
	{
		topOrders = d;
	}
	
	// ACCESSOR METHODS
	
	public String getId()
	{
		return id;
	}
	
	public String getCustomId()
	{
		return "" + (Integer.parseInt(name + address + city + state + zip + phone));
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String getCity()
	{
		return city;
	}
	
	public String getState()
	{
		return state;
	}
	
	public String getZip()
	{
		return zip;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public ArrayList<Drink> getDrinks()
	{
		return orders;
	}
	
	public ArrayList<Drink> getTopDrinks()
	{
		return topOrders;
	}
}
