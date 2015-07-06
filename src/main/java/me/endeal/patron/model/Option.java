package me.endeal.patron.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Option implements Serializable
{
    private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private Price price;

	public Option(String id, String name, Price price)
	{
		setId(id);
		setName(name);
		setPrice(price);
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setPrice(Price price)
	{
		this.price = price;
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public Price getPrice()
	{
		return price;
	}
}
