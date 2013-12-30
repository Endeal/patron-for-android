package com.flashvip.model;

import java.util.ArrayList;

public class Vendor
{
	private String vendorId;
	private String name;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String paypal;
	
	private ArrayList<Item> items;
	private ArrayList<Item> filteredItems;
	private ArrayList<Item> recommendations;
	
	// CONSTRUCTORS
	
	public Vendor()
	{
		name = "John's";
		items = new ArrayList<Item>();
	}
	
	public Vendor(String vendorId, String name, String address, String city,
			String state, String zip, String phone, String paypal, 
			ArrayList<Item> items, ArrayList<Item> recommendations)
	{
		this.vendorId = vendorId;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone = phone;
		this.paypal = paypal;
		this.items = items;
		this.recommendations = recommendations;
	}
	
	// SETTER METHODS
	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public void setCity(String city)
	{
		this.city = city;
	}
	
	public void setState(String state)
	{
		this.state = state;
	}
	
	public void setZip(String zip)
	{
		this.zip = zip;
	}
	
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
	public void setPaypal(String paypal)
	{
		this.paypal = paypal;
	}
	
	public void setItems(ArrayList<Item> items)
	{
		this.items = items;
	}
	
	public void setFilteredItems(ArrayList<Item> filteredItems)
	{
		this.filteredItems = filteredItems;
	}
	
	public void setRecommendations(ArrayList<Item> d)
	{
		recommendations = d;
	}
	
	// ACCESSOR METHODS
	
	public String getVendorId()
	{
		return vendorId;
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
	
	public String getPaypal()
	{
		return paypal;
	}
	
	public ArrayList<Item> getItems()
	{
		return items;
	}
	
	public ArrayList<Item> getFilteredItems()
	{
		return filteredItems;
	}
	
	public ArrayList<Item> getRecommendations()
	{
		return recommendations;
	}
}
