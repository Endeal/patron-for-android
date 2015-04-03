package me.endeal.patron.model;

import java.io.Serializable;
import java.lang.Cloneable;
import java.lang.CloneNotSupportedException;

import org.json.JSONException;
import org.json.JSONObject;

public class Category implements Serializable
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

    public Category(Category category)
    {
        setId(category.getId());
        setName(category.getName());
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
