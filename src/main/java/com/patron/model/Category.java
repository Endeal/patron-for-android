package com.patron.model;

import org.json.JSONException;
import org.json.JSONObject;

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

    public Category(JSONObject rawCategory) throws JSONException
    {
        setId(rawCategory.getString("categoryId"));
        setName(rawCategory.getString("name"));
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
