package com.patron.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.patron.model.Station;

public class Vendor
{
	private String id;
	private String name;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private double taxRate;
	private double latitude;
	private double longitude;
    private String facebook;
    private String twitter;
    private String googlePlus;
    private int facebookPoints;
    private int twitterPoints;
    private int googlePlusPoints;

	private List<Item> items;
	private List<Item> filteredItems;
	private List<Item> recommendations;
	private List<Station> stations;
	private Map<String, Integer> rewardItems;
	private Map<String, Integer> rewardOptions;
	private Map<String, Integer> rewardSupplements;

	// CONSTRUCTORS

	public Vendor()
	{
		name = "";
		items = new ArrayList<Item>();
	}

	public Vendor(String id, String name, String address, String city,
			String state, String zip, String phone, List<Item> items,
			List<Item> recommendations, List<Station> stations,
			double taxRate, double latitude, double longitude,
            String facebook, String twitter, String googlePlus,
            int facebookPoints, int twitterPoints, int googlePlusPoints)
	{
		setId(id);
		setName(name);
		setAddress(address);
		setCity(city);
		setState(state);
		setZip(zip);
		setPhone(phone);
		setItems(items);
		setRecommendations(recommendations);
		setStations(stations);
		setLatitude(latitude);
		setLongitude(longitude);
		setTaxRate(taxRate);
        setFacebook(facebook);
        setTwitter(twitter);
        setGooglePlus(googlePlus);
        setFacebookPoints(facebookPoints);
        setTwitterPoints(twitterPoints);
        setGooglePlusPoints(googlePlusPoints);
	}

    public Vendor(Vendor vendor)
    {
        setId(vendor.getId());
        setName(vendor.getName());
        setAddress(vendor.getAddress());
        setCity(vendor.getCity());
        setState(vendor.getState());
        setZip(vendor.getZip());
        setPhone(vendor.getPhone());
        setItems(vendor.getItems());
        setRecommendations(vendor.getRecommendations());
        setStations(vendor.getStations());
        setLatitude(vendor.getLatitude());
        setLongitude(vendor.getLongitude());
        setTaxRate(vendor.getTaxRate());
    }


	// SETTER METHODS
	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public void setTaxRate(double taxRate)
	{
		this.taxRate = taxRate;
	}

	public void setItems(List<Item> items)
	{
		this.items = items;
	}

	public void setFilteredItems(List<Item> filteredItems)
	{
		this.filteredItems = filteredItems;
	}

	public void setRecommendations(List<Item> recommendations)
	{
		this.recommendations = recommendations;
	}

	public void setStations(List<Station> stations)
	{
		this.stations = stations;
	}

	public void setRewardItems(Map<String, Integer> rewardItems)
	{
		this.rewardItems = rewardItems;
	}

	public void setRewardOptions(Map<String, Integer> rewardOptions)
	{
		this.rewardOptions = rewardOptions;
	}

	public void setRewardSupplements(Map<String, Integer> rewardSupplements)
	{
		this.rewardSupplements = rewardSupplements;
	}

    public void setFacebook(String facebook)
    {
        this.facebook = facebook;
    }

    public void setTwitter(String twitter)
    {
        this.twitter = twitter;
    }

    public void setGooglePlus(String googlePlus)
    {
        this.googlePlus = googlePlus;
    }

    public void setFacebookPoints(int facebookPoints)
    {
        this.facebookPoints = facebookPoints;
    }

    public void setTwitterPoints(int twitterPoints)
    {
        this.twitterPoints = twitterPoints;
    }

    public void setGooglePlusPoints(int googlePlusPoints)
    {
        this.googlePlusPoints = googlePlusPoints;
    }

	// ACCESSOR METHODS

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getAddress()
	{
		return address;
	}

	public String getCity()
	{
		return city;
	}

	public String getState()
	{
		return state;
	}

	public String getZip()
	{
		return zip;
	}

	public String getPhone()
	{
		return phone;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public double getTaxRate()
	{
		return taxRate;
	}

	public List<Item> getItems()
	{
		return items;
	}

	public List<Item> getFilteredItems()
	{
		return filteredItems;
	}

	public List<Item> getRecommendations()
	{
		return recommendations;
	}

	public List<Station> getStations()
	{
		return stations;
	}

	public Map<String, Integer> getRewardItems()
	{
		return rewardItems;
	}

	public Map<String, Integer> getRewardOptions()
	{
		return rewardOptions;
	}

	public Map<String, Integer> getRewardSupplements()
	{
		return rewardSupplements;
	}

    public String getFacebook()
    {
        return facebook;
    }

    public String getTwitter()
    {
        return twitter;
    }

    public String getGooglePlus()
    {
        return googlePlus;
    }

    public int getFacebookPoints()
    {
        return facebookPoints;
    }

    public int getTwitterPoints()
    {
        return twitterPoints;
    }

    public int getGooglePlusPoints()
    {
        return googlePlusPoints;
    }
}
