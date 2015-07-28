package com.endeal.patron.model;

import java.io.Serializable;
import java.util.List;

public class Profile implements Serializable
{
    private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private String birthday;
	private String stripeId;
	private String facebookId;
	private String twitterId;
	private String googlePlusId;
	private List<Funder> funders;
    private List<String> vendors;
    private List<String> items;

    public Profile(String firstName, String lastName, String birthday, String stripeId,
            String facebookId, String twitterId, String googlePlusId, List<Funder> funders,
            List<String> vendors, List<String> items)
    {
        setFirstName(firstName);
        setLastName(lastName);
        setBirthday(birthday);
        setStripeId(stripeId);
        setFacebookId(facebookId);
        setTwitterId(twitterId);
        setGooglePlusId(googlePlusId);
        setFunders(funders);
        setVendors(vendors);
        setItems(items);
    }

    // Convenience
    public boolean hasFavoriteVendor(String vendorId)
    {
        if (getVendors() != null && getVendors().size() > 0)
        {
            for (int i = 0; i < getVendors().size(); i++)
            {
                if (vendorId.equals(getVendors().get(i)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasFavoriteItem(String itemId)
    {
        if (getItems() != null && getItems().size() > 0)
        {
            for (int i = 0; i < getItems().size(); i++)
            {
                if (itemId.equals(getItems().get(i)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    // Setters
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public void setBirthday(String birthday)
	{
		this.birthday = birthday;
	}

	public void setStripeId(String stripeId)
	{
		this.stripeId = stripeId;
	}

	public void setFacebookId(String facebookId)
	{
		this.facebookId = facebookId;
	}

	public void setTwitterId(String twitterId)
	{
		this.twitterId = twitterId;
	}

	public void setGooglePlusId(String googlePlusId)
	{
		this.googlePlusId = googlePlusId;
	}

	public void setFunders(List<Funder> funders)
	{
		this.funders = funders;
	}

    public void setVendors(List<String> vendors)
    {
        this.vendors = vendors;
    }

    public void setItems(List<String> items)
    {
        this.items = items;
    }

    // Getters
	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getBirthday()
	{
		return birthday;
	}

	public String getStripeId()
	{
		return stripeId;
	}

	public String getFacebookId()
	{
		return facebookId;
	}

	public String getTwitterId()
	{
		return twitterId;
	}

	public String getGooglePlusId()
	{
		return googlePlusId;
	}

	public List<Funder> getFunders()
	{
		return funders;
	}

    public List<String> getVendors()
    {
        return vendors;
    }

    public List<String> getItems()
    {
        return items;
    }
}
