package com.patron.model;

public class Category
{
	// Properties
	private String id;
	private String name;
	
	// Constructor
	public Category(String id, String name)
	{
		setId(id);
		setName(name);
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
	
	// Getters
	public String getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
}
