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
    private List<String> vendors;
    private List<String> items;

    public Identity(String firstName, String lastName, List<Credential> credentials, long birthday, List<Location> locations,
            List<String> vendors, List<String> items)
    {
        setFirstName(firstName);
        setLastName(lastName);
        setCredentials(credentials);
        setBirthday(birthday);
        setLocations(locations);
        setVendors(vendors);
        setItems(items);
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

    public void setVendors(List<String> vendors)
    {
        this.vendors = vendors;
    }

    public void setItems(List<String> items)
    {
        this.items = items;
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

    public List<String> getVendors()
    {
        return this.vendors;
    }

    public List<String> getItems()
    {
        return this.items;
    }
}
