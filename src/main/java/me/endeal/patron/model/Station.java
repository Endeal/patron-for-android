package me.endeal.patron.model;

public class Station
{
	private String id;
	private String name;

	public Station(String id, String name)
	{
		setId(id);
		setName(name);
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}
}