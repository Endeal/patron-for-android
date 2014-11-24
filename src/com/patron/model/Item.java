package com.patron.model;

import java.math.BigDecimal;
import java.util.List;

public class Item
{
	// Properties
	private String id;
	private String name;
	private BigDecimal price;
	private int maxSupplements;
	private int supply;
	private List<Category> categories;
	private List<Attribute> attributes;
	private List<Supplement> supplements;
	
	// Constructor
	public Item(String id, String name, BigDecimal price, int maxSupplements, int supply,
			List<Category> categories, List<Attribute> attributes,
			List<Supplement> supplements)
	{
		setId(id);
		setName(name);
		setPrice(price);
		setMaxSupplements(maxSupplements);
		setSupply(supply);
		setCategories(categories);
		setAttributes(attributes);
		setSupplements(supplements);
	}
	
	// Setters
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}
	
	public void setMaxSupplements(int maxSupplements)
	{
		this.maxSupplements = maxSupplements;
	}

	public void setSupply(int supply)
	{
		this.supply = supply;
	}
	
	public void setCategories(List<Category> categories)
	{
		this.categories = categories;
	}
	
	public void setAttributes(List<Attribute> attributes)
	{
		this.attributes = attributes;
	}
	
	public void setSupplements(List<Supplement> supplements)
	{
		this.supplements = supplements;
	}
	
	// Getters
	public String getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public BigDecimal getPrice()
	{
		return price;
	}
	
	public int getMaxSupplements()
	{
		return maxSupplements;
	}

	public int getSupply()
	{
		return supply;
	}
	
	public List<Category> getCategories()
	{
		return categories;
	}
	
	public List<Attribute> getAttributes()
	{
		return attributes;
	}
	
	public List<Supplement> getSupplements()
	{
		return supplements;
	}
}
