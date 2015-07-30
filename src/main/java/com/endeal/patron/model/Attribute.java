package com.endeal.patron.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Attribute implements Serializable
{
    private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private List<Option> options;

	public Attribute(String id, String name, List<Option> options)
	{
		setId(id);
		setName(name);
		setOptions(options);
	}

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
