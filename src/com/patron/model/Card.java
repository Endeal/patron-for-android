package com.patron.model;

public class Card extends Funder
{
	private String expirationMonth;
	private String expirationYear;
	private boolean verified;

	public Card(String id, String name, String number, String expirationMonth, String expirationYear, String type, String bankName,
		String address, String city, String state, String postalCode, String href, String createdAt, boolean verified)
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
		setExpirationMonth(expirationMonth);
		setExpirationYear(expirationYear);
		setVerified(verified);
	}

	// Setters
	public void setExpirationMonth(String expirationMonth)
	{
		this.expirationMonth = expirationMonth;
	}
	
	public void setExpirationYear(String expirationYear)
	{
		this.expirationYear = expirationYear;
	}

	public void setVerified(boolean verified)
	{
		this.verified = verified;
	}

	// Getters
	public String getExpirationMonth()
	{
		return expirationMonth;
	}

	public String getExpirationYear()
	{
		return expirationYear;
	}

	public boolean getVerified()
	{
		return verified;
	}
}