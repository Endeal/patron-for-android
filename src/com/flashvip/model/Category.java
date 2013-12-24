package com.flashvip.model;

public class Category
{
	// Properties
	private String categoryId;
	private String name;
	
	// Constructor
	public Category(String categoryId, String name)
	{
		this.categoryId = categoryId;
		this.name = name;
	}
	
	// Setters
	public void setCategoryId(String categoryId)
	{
		this.categoryId = categoryId;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	// Getters
	public String getCategoryId()
	{
		return categoryId;
	}
	
	public String getName()
	{
		return name;
	}
}
