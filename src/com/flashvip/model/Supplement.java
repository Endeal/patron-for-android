package com.flashvip.model;

import java.math.BigDecimal;

public class Supplement
{
	// Properties
	private String supplementId;
	private String name;
	private BigDecimal price;
	
	// Constructor
	public Supplement(String supplementId, String name, BigDecimal price)
	{
		this.supplementId = supplementId;
		this.name = name;
		this.price = price;
	}

	// Setters
	public void setSupplementId(String supplementId)
	{
		this.supplementId = supplementId;
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
	public String getSupplementId()
	{
		return supplementId;
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
