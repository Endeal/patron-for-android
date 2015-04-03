package me.endeal.patron.model;

public abstract class Funder
{
    public static enum Type
    {
        Card, BankAccount
    }

	// Properties
	private String id;
	private String name;
	private String bankName;
	private String number;
	private String type;
	private String address;
	private String city;
	private String state;
	private String postalCode;
	private String href;
	private String createdAt;

    // Convenience for checking type.
    public Type getFunderType()
    {
        if (this.type.toLowerCase().equals("credit") || this.type.toLowerCase().equals("debit"))
        {
            return Type.Card;
        }
        else
        {
            return Type.BankAccount;
        }
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

	public void setAddress(String address)
	{
		this.address = address;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public void setPostalCode(String postalCode)
	{
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

	// Getters
	public String getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

	public String getBankName()
	{
		return this.bankName;
	}

	public String getNumber()
	{
		return this.number;
	}

	public String getType()
	{
		return this.type;
	}

	public String getAddress()
	{
		return this.address;
	}

	public String getCity()
	{
		return this.city;
	}

	public String getState()
	{
		return this.state;
	}

	public String getPostalCode()
	{
		return this.postalCode;
	}

	public String getHref()
	{
		return this.href;
	}

	public String getCreatedAt()
	{
		return this.createdAt;
	}
}
