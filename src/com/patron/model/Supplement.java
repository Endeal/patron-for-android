package com.patron.model;

import java.math.BigDecimal;

public class Supplement
{
	// Properties
	private String id;
	private String name;
	private BigDecimal price;
	
	// Constructor
	public Supplement(String id, String name, BigDecimal price)
	{
		setId(id);
		setName(name);
		setPrice(price);
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
}
