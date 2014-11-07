package com.patron.model;

import java.util.List;
import java.util.ArrayList;

public class User
{
	private String patronId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String birthday;
	private String balancedId;
	private String facebookId;
	private String twitterId;
	private String googlePlusId;
	private List<Funder> funders;

	public User(String patronId, String firstName, String lastName, String email, String password, String birthday,
		String balancedId, String facebookId, String twitterId, String googlePlusId,
		List<Funder> funders)
	{
		this.patronId = patronId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.birthday = birthday;
		this.balancedId = balancedId;
		this.facebookId = facebookId;
		this.twitterId = twitterId;
		this.googlePlusId = googlePlusId;
		this.funders = funders;
	}

	// Setters

	public void setPatronId(String patronId)
	{
		this.patronId = patronId;
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
		this.email = email;
	}

	public void setPassword(String password)
	{
		this.password = password;
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

	// Getters

	public String getPatronId()
	{
		return patronId;
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
		return email;
	}

	public String getPassword()
	{
		return password;
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
}