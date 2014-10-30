package com.patron.model;

public class BankAccount
{
	private String name;
	private String bankName;
	private String number;
	private String type;
	private String routing;
	private String address;
	private String city;
	private String state;
	private String postalCode;
	private String href;
	private String createdAt;
	private boolean creditable;
	private boolean debitable;

	public BankAccount(String name, String bankName, String number, String type, String routing,
		String address, String city, String state, String postalCode, String href, String createdAt,
		boolean creditable, boolean debitable)
	{
		this.name = name;
		this.bankName = bankName;
		this.number = number;
		this.type = type;
		this.routing = routing;
		setAddress(address);
		setCity(city);
		setState(state);
		setPostalCode(postalCode);
		this.href = href;
		this.createdAt = createdAt;
		this.creditable = creditable;
		this.debitable = debitable;
	}

	// Setters
	public void setName(String name)
	{
		this.name = name;
	}
	public void setBankName(String bankName)
	{
		this.bankName = bankName;
	}
	public void setNumber(String number)
	{
		this.number = number;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public void setRouting(String routing)
	{
		this.routing = routing;
	}
	public void setAddress(String address)
	{
		if (address.equals("null"))
			this.address = null;
		else
			this.address = address;
	}
	public void setCity(String city)
	{
		if (city.equals("null"))
			this.city = null;
		else
			this.city = city;
	}
	public void setState(String state)
	{
		if (state.equals("null"))
			this.state = null;
		else
			this.state = state;
	}
	public void setPostalCode(String postalCode)
	{
		if (postalCode.equals("null"))
			this.postalCode = null;
		else
			this.postalCode = postalCode;
	}
	public void setHref(String href)
	{
		this.href = href;
	}
	public void setCreatedAt(String createdAt)
	{
		this.createdAt = createdAt;
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
	public String getName()
	{
		return name;
	}
	public String getBankName()
	{
		return bankName;
	}
	public String getNumber()
	{
		return number;
	}
	public String getType()
	{
		return type;
	}
	public String getRouting()
	{
		return routing;
	}
	public String getAddress()
	{
		return address;
	}
	public String getCity()
	{
		return city;
	}
	public String getState()
	{
		return state;
	}
	public String getPostalCode()
	{
		return postalCode;
	}
	public String getHref()
	{
		return href;
	}
	public String getCreatedAt()
	{
		return createdAt;
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