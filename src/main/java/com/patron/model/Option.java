package com.patron.model;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Option
{
	// Properties
	private String id;
	private String name;
	private BigDecimal price;
	private int supply;

	// Constructor
	public Option(String id, String name, BigDecimal price, int supply)
	{
		setId(id);
		setName(name);
		setPrice(price);
		setSupply(supply);
	}

    public Option(JSONObject rawOption) throws JSONException
    {
        setId(rawOption.getString("optionId"));
        setName(rawOption.getString("name"));
        setPrice(new BigDecimal(rawOption.getString("price")));
        setSupply(rawOption.getInt("supply"));
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

	public void setSupply(int supply)
	{
		this.supply = supply;
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

	public int getSupply()
	{
		return supply;
	}
}
