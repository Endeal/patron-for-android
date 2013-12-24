package com.flashvip.model;

import java.util.List;

public class Attribute
{
	// Properties
	private String attributeId;
	private String name;
	private List<Option> options;
	
	// Constructor
	public Attribute(String attributeId, String name, List<Option> options)
	{
		this.attributeId = attributeId;
		this.name = name;
		this.options = options;
	}
	
	// Setters
	public void setAttributeId(String attributeId)
	{
		this.attributeId = attributeId;
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
	public String getAttributeId()
	{
		return attributeId;
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
