package com.patron.model;

import java.lang.Cloneable;
import java.lang.CloneNotSupportedException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Attribute implements Cloneable
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

    public Attribute(JSONObject rawAttribute) throws JSONException
    {
        options = new ArrayList<Option>();

        setId(rawAttribute.getString("attributeId"));
        setName(rawAttribute.getString("name"));
        JSONArray rawOptions = rawAttribute.getJSONArray("options");
        for (int i = 0; i < rawOptions.length(); i++)
        {
            JSONObject rawOption = rawOptions.getJSONObject(i);
            Option option = new Option(rawOption);
            options.add(option);
        }
    }

	public Attribute clone() throws CloneNotSupportedException
	{
		String newId = new String(getId());
		String newName = new String(getName());
		List<Option> newOptions = new ArrayList<Option>();
		for (int i = 0; i < getOptions().size(); i++)
		{
			Option newOption = getOptions().get(i).clone();
			newOptions.add(newOption);
		}
		Attribute newAttribute = new Attribute(newId, newName, newOptions);
		return newAttribute;
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
