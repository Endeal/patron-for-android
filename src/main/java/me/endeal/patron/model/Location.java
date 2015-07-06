package me.endeal.patron.model;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String address;
    private String city;
    private String state;
    private String zip;
    private double longitude;
    private double latitude;

    public Location(String address, String city, String state, String zip, double longitude, double latitude)
    {
        setAddress(address);
        setCity(city);
        setState(state);
        setZip(zip);
        setLongitude(longitude);
        setLatitude(latitude);
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

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
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

    public String getZip()
    {
        return this.zip;
    }

    public double getLongitude()
    {
        return this.longitude;
    }

    public double getLatitude()
    {
        return this.latitude;
    }
}
