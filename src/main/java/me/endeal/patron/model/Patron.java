package me.endeal.patron.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.lang.Exception;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import me.endeal.patron.system.FlashCipher;
//import me.endeal.patron.system.Patron;

public class Patron implements Serializable
{
    private static final long serialVersionUID = 1L;

	private String id;
    /*
    private String email;
    private Profile profile;
    private static String provider = "";
    */

    private Identity identity;
    private List<Funder> funders;
    private List<Franchise> franchises;
    private List<Vendor> vendors;
    private List<Item> items;

    public Patron(Identity identity, List<Funder> funders, List<Franchise> franchises, List<Vendor> vendors, List<Item> items)
    {
        setIdentity(identity);
        setFunders(funders);
        setFranchises(franchises);
        setVendors(vendors);
        setItems(items);
    }

    public void setIdentity(Identity identity)
    {
        this.identity = identity;
    }

    public void setFunders(List<Funder> funders)
    {
        this.funders = funders;
    }

    public void setFranchises(List<Franchise> franchises)
    {
        this.franchises = franchises;
    }

    public void setVendors(List<Vendor> vendors)
    {
        this.vendors = vendors;
    }

    public void setItems(List<Item> items)
    {
        this.items = items;
    }

    public Identity getIdentity()
    {
        return this.identity;
    }

    public List<Funder> getFunders()
    {
        return this.funders;
    }

    public List<Franchise> getFranchises()
    {
        return this.franchises;
    }

    public List<Vendor> getVendors()
    {
        return this.vendors;
    }

    public List<Item> getItems()
    {
        return this.items;
    }

    /*
	public User(String id, String email, Profile profile)
	{
		setId(id);
        setEmail(email);
        setProfile(profile);
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setEmail(String email)
	{
        this.email = email;
	}

    public void setProfile(Profile profile)
    {
        this.profile = profile;
    }


	public String getId()
	{
		return id;
	}

	public String getEmail()
	{
        return email;
	}

    public Profile getProfile()
    {
        return profile;
    }
    */
}
