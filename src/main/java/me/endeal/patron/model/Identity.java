package me.endeal.patron.model;

import java.io.Serializable;
import java.util.List;

public class Identity implements Serializable
{
    private String firstName;
    private String lastName;
    private List<Credential> credentials;
    private long birthday;
    private List<Location> locations;

    public Identity(String firstName, String lastName, List<Credential> credentials, long birthday, List<Location> locations)
    {
        setFirstName(firstName);
        setLastName(lastName);
        setCredentials(credentials);
        setBirthday(birthday);
        setLocations(locations);
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setCredentials(List<Credential> credentials)
    {
        this.credentials = credentials;
    }

    public void setBirthday(long birthday)
    {
        this.birthday = birthday;
    }

    public void setLocations(List<Location> locations)
    {
        this.locations = locations;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public List<Credential> getCredentials()
    {
        return this.credentials;
    }

    public long getBirthday()
    {
        return this.birthday;
    }

    public List<Location> getLocations()
    {
        return this.locations;
    }
}
