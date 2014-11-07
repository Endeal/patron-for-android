package com.patron.model;

public class BankAccount extends Funder
{
	private String routing;
	private boolean creditable;
	private boolean debitable;

	public BankAccount(String id, String name, String bankName, String number, String type, String routing,
		String address, String city, String state, String postalCode, String href, String createdAt,
		boolean creditable, boolean debitable)
	{
		setId(id);
		setName(name);
		setBankName(bankName);
		setNumber(number);
		setType(type);
		setAddress(address);
		setCity(city);
		setState(state);
		setPostalCode(postalCode);
		setHref(href);
		setCreatedAt(createdAt);
		this.routing = routing;
		this.creditable = creditable;
		this.debitable = debitable;
	}

	// Setters
	public void setRouting(String routing)
	{
		this.routing = routing;
	}

	public void setCreditable(boolean creditable)
	{
		this.creditable = creditable;
	}

	public void setDebitable(boolean debitable)
	{
		this.debitable = debitable;
	}

	// Getters
	public String getRouting()
	{
		return routing;
	}

	public boolean getCreditable()
	{
		return creditable;
	}

	public boolean getDebitable()
	{
		return debitable;
	}
}