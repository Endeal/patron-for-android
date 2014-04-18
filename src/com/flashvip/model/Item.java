package com.flashvip.model;

import java.math.BigDecimal;
import java.util.List;

public class Item
{
	// Properties
	private String itemId;
	private String name;
	private BigDecimal price;
	private int maxSupplements;
	private List<Category> categories;
	private List<Attribute> attributes;
	private List<Supplement> supplements;
	
	// Constructor
	public Item(String itemId, String name, BigDecimal price, int maxSupplements,
			List<Category> categories, List<Attribute> attributes,
			List<Supplement> supplements)
	{
		this.itemId = itemId;
		this.name = name;
		this.price = price;
		this.maxSupplements = maxSupplements;
		this.categories = categories;
		this.attributes = attributes;
		this.supplements = supplements;
	}
	
	// Setters
	public void setItemId(String itemId)
	{
		this.itemId = itemId;
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
	
	public void setAttributes(List<Category> categories)
	{
		this.categories = categories;
	}
	
	public void setCategories(List<Attribute> attributes)
	{
		this.attributes = attributes;
	}
	
	public void setSupplements(List<Supplement> supplements)
	{
		this.supplements = supplements;
	}
	
	// Getters
	public String getItemId()
	{
		return itemId;
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