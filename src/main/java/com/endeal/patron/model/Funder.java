package com.endeal.patron.model;

import java.io.Serializable;

public class Funder implements Serializable
{
    private static final long serialVersionUID = 1L;

	private String id;
    private String object;
    private String last4;
    private String brand;
    private String funding;
    private String country;
    private String exp_month;
    private String exp_year;
    private String cvc;

    public Funder(String id, String object, String last4, String brand, String funding,
            String country, String month, String year, String cvc)
    {
        setId(id);
        setObject(object);
        setLast4(last4);
        setBrand(brand);
        setFunding(funding);
        setCountry(country);
        setExpirationMonth(month);
        setExpirationYear(year);
        setCvc(cvc);
    }

	public void setId(String id)
	{
		this.id = id;
	}

    public void setObject(String object)
    {
        this.object = object;
    }

	public void setLast4(String number)
	{
		this.last4 = number;
	}

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public void setFunding(String funding)
    {
        this.funding = funding;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public void setExpirationMonth(String month)
    {
        this.exp_month = month;
    }

    public void setExpirationYear(String year)
    {
        this.exp_year = year;
    }

    public void setCvc(String cvc)
    {
        this.cvc = cvc;
    }

    public String getId()
    {
        return this.id;
    }

    public String getObject()
    {
        return this.object;
    }

    public String getLast4()
    {
        return this.last4;
    }

    public String getBrand()
    {
        return this.brand;
    }

    public String getFunding()
    {
        return this.funding;
    }

    public String getCountry()
    {
        return this.country;
    }

    public String getExpirationMonth()
    {
        return this.exp_month;
    }

    public String getExpirationYear()
    {
        return this.exp_year;
    }

    public String getCvc()
    {
        return this.cvc;
    }

    @Override
    public String toString()
    {
        return getBrand() + " " + getLast4();
    }
}
