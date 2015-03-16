package com.patron.model;

import java.lang.Cloneable;
import java.lang.CloneNotSupportedException;
import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

public class Supplement implements Cloneable
{
	// Properties
	private String id;
	private String name;
	private BigDecimal price;
	private int supply;

	// Constructor
	public Supplement(String id, String name, BigDecimal price, int supply)
	{
		setId(id);
		setName(name);
		setPrice(price);
		setSupply(supply);
	}

    public Supplement(JSONObject rawSupplement) throws JSONException
    {
        setId(rawSupplement.getString("supplementId"));
        setName(rawSupplement.getString("name"));
        setPrice(new BigDecimal(rawSupplement.getString("price")));
        setSupply(rawSupplement.getInt("supply"));
    }

		public Supplement clone() throws CloneNotSupportedException
		{
			String newId = new String(getId());
			String newName = new String(getName());
			BigDecimal newPrice = new BigDecimal(getPrice().toString());
			int newSupply = new Integer(getSupply());
			Supplement newSupplement = new Supplement(newId, newName, newPrice, newSupply);
			return newSupplement;
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
