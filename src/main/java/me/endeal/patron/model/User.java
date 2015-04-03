package me.endeal.patron.model;

import java.util.List;
import java.util.ArrayList;
import java.lang.Exception;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import me.endeal.patron.system.FlashCipher;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.Patron;

public class User
{
	private String id;
	private String firstName;
	private String lastName;
	private String birthday;
	private String balancedId;
	private String facebookId;
	private String twitterId;
	private String googlePlusId;
	private List<Funder> funders;
    private List<String> vendors;
    private List<String> items;
    private static String provider = "";

	public User(String id, String firstName, String lastName, String birthday,
		String balancedId, String facebookId, String twitterId, String googlePlusId,
		List<Funder> funders, List<String> vendors, List<String> items)
	{
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
		setBirthday(birthday);
		setBalancedId(balancedId);
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

	public void setId(String id)
	{
		this.id = id;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public void setEmail(String email)
	{
        Globals.setEmail(email);
	}

	public void setPassword(String password)
	{
        Globals.setPassword(password);
	}

	public void setProvider(String provider)
	{
        Globals.setProvider(provider);
	}

	public void setBirthday(String birthday)
	{
		this.birthday = birthday;
	}

	public void setBalancedId(String balancedId)
	{
		this.balancedId = balancedId;
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

	public String getId()
	{
		return id;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getEmail()
	{
        return Globals.getEmail();
	}

	public String getPassword()
	{
        return Globals.getPassword();
	}

	public String getProvider()
	{
        return Globals.getProvider();
	}

	public String getBirthday()
	{
		return birthday;
	}

	public String getBalancedId()
	{
		return balancedId;
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
