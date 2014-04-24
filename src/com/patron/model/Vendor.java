package com.patron.model;

import java.util.ArrayList;
import java.util.List;

public class Vendor
{
	private String vendorId;
	private String name;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	
	private List<Item> items;
	private List<Item> filteredItems;
	private List<Item> recommendations;
	
	// CONSTRUCTORS
	
	public Vendor()
	{
		name = "Jack & Jill's";
		items = new ArrayList<Item>();
	}
	
	public Vendor(String vendorId, String name, String address, String city,
			String state, String zip, String phone, List<Item> items, List<Item> recommendations)
	{
		this.vendorId = vendorId;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone = phone;
		this.items = items;
		this.recommendations = recommendations;
		this.filteredItems = items;
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
	
	public void setItems(List<Item> items)
	{
		this.items = items;
	}
	
	public void setFilteredItems(List<Item> filteredItems)
	{
		this.filteredItems = filteredItems;
	}
	
	public void setRecommendations(List<Item> d)
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
	
	public List<Item> getItems()
	{
		return items;
	}
	
	public List<Item> getFilteredItems()
	{
		return filteredItems;
	}
	
	public List<Item> getRecommendations()
	{
		return recommendations;
	}
}
