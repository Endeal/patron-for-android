package com.patron.model;

public class User
{
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String birthday;

	public User(String firstName, String lastName, String email, String password, String birthday)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.birthday = birthday;
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

	// Getters

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
}