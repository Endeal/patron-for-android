package com.patron.model;

import java.math.BigDecimal;

public class Option
{
	// Properties
	private String optionId;
	private String name;
	private BigDecimal price;
	
	// Constructor
	public Option(String optionId, String name, BigDecimal price)
	{
		this.optionId = optionId;
		this.name = name;
		this.price = price;
	}

	// Setters
	public void setOptionId(String optionId)
	{
		this.optionId = optionId;
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
	public String getOptionId()
	{
		return optionId;
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
