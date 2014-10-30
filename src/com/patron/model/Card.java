package com.patron.model;

public class Card
{
	private String name;
	private String number;
	private String expirationMonth;
	private String expirationYear;
	private String type;
	private String bankName;
	private String address;
	private String city;
	private String state;
	private String postalCode;
	private String href;
	private String createdAt;
	private boolean verified;

	public Card(String name, String number, String expirationMonth, String expirationYear, String type, String bankName,
		String address, String city, String state, String postalCode, String href, String createdAt, boolean verified)
	{
		this.name = name;
		this.number = number;
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
		this.type = type;
		this.bankName = bankName;
		setAddress(address);
		setCity(city);
		setState(state);
		setPostalCode(postalCode);
		this.href = href;
		this.createdAt = createdAt;
		this. verified = verified;
	}

	// Setters
	public void setName(String name)
	{
		this.name = name;
	}
	public void setNumber(String number)
	{
		this.number = number;
	}
	public void setExpirationMonth(String expirationMonth)
	{
		this.expirationMonth = expirationMonth;
	}
	public void setExpirationYear(String expirationYear)
	{
		this.expirationYear = expirationYear;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public void setBankName(String bankName)
	{
		this.bankName = bankName;
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
	public void setVerified(boolean verified)
	{
		this.verified = verified;
	}

	// Getters
	public String getName()
	{
		return name;
	}
	public String getNumber()
	{
		return number;
	}
	public String getExpirationMonth()
	{
		return expirationMonth;
	}
	public String getExpirationYear()
	{
		return expirationYear;
	}
	public String getType()
	{
		return type;
	}
	public String getBankName()
	{
		return bankName;
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
	public boolean getVerified()
	{
		return verified;
	}
}