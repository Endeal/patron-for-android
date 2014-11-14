package com.patron.model;

import java.util.List;
import java.util.ArrayList;

public class User
{
	private String id;
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

	public User(String id, String firstName, String lastName, String email, String password, String birthday,
		String balancedId, String facebookId, String twitterId, String googlePlusId,
		List<Funder> funders)
	{
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setPassword(password);
		setBirthday(birthday);
		setBalancedId(balancedId);
		setFacebookId(facebookId);
		setTwitterId(twitterId);
		setGooglePlusId(googlePlusId);
		setFunders(funders);
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