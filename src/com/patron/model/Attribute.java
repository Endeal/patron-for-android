package com.patron.model;

import java.util.List;

public class Attribute
{
	// Properties
	private String id;
	private String name;
	private List<Option> options;
	
	// Constructor
	public Attribute(String id, String name, List<Option> options)
	{
		setId(id);
		setName(name);
		setOptions(options);
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
	public void setOptions(List<Option> options)
	{
		this.options = options;
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
	public List<Option> getOptions()
	{
		return options;
	}
}
