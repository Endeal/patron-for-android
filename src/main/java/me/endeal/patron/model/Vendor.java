package me.endeal.patron.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Vendor implements Serializable
{
    private static final long serialVersionUID = 1L;

	private String id;
	private String name;
    private String picture;
    private Franchise franchise;
    private Location location;
    private Contact contact;
    private boolean selfServe;
	private double taxRate;
    private double range;
    private int favorites;
    private int facebookPoints;
    private int twitterPoints;
    private int googlePlusPoints;

	private List<Item> items;
	private List<Item> filteredItems;
	private List<Station> stations;
    private List<Locale> locales;
    private List<Query> queries;
	private Map<String, Integer> rewardItems;
	private Map<String, Integer> rewardOptions;

    public Vendor(String id, String name, String picture, Franchise franchise, Location location, Contact contact,
            double taxRate, double range, int favorites, int facebookPoints, int twitterPoints,
            int googlePlusPoints, List<Item> items, List<Station> stations, List<Locale> locales, boolean selfServe,
            List<Query> queries)
    {
        setId(id);
        setName(name);
        setPicture(picture);
        setFranchise(franchise);
        setLocation(location);
        setContact(contact);
        setTaxRate(taxRate);
        setRange(range);
        setFavorites(favorites);
        setFacebookPoints(facebookPoints);
        setTwitterPoints(twitterPoints);
        setGooglePlusPoints(googlePlusPoints);
        setItems(items);
        setStations(stations);
        setLocales(locales);
        setSelfServe(selfServe);
        setQueries(queries);
    }

	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    public void setFranchise(Franchise franchise)
    {
        this.franchise = franchise;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public void setContact(Contact contact)
    {
        this.contact = contact;
    }

	public void setTaxRate(double taxRate)
	{
		this.taxRate = taxRate;
	}

    public void setRange(double range)
    {
        this.range = range;
    }

    public void setFavorites(int favorites)
    {
        this.favorites = favorites;
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

	public void setItems(List<Item> items)
	{
		this.items = items;
	}

	public void setFilteredItems(List<Item> filteredItems)
	{
		this.filteredItems = filteredItems;
	}

	public void setStations(List<Station> stations)
	{
		this.stations = stations;
	}

    public void setLocales(List<Locale> locales)
    {
        this.locales = locales;
    }

    public void setSelfServe(boolean selfServe)
    {
        this.selfServe = selfServe;
    }

    public void setQueries(List<Query> queries)
    {
        this.queries = queries;
    }

	public void setRewardItems(Map<String, Integer> rewardItems)
	{
		this.rewardItems = rewardItems;
	}

	public void setRewardOptions(Map<String, Integer> rewardOptions)
	{
		this.rewardOptions = rewardOptions;
	}

	public String getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

    public String getPicture()
    {
        return this.picture;
    }

    public Franchise getFranchise()
    {
        return this.franchise;
    }

    public Location getLocation()
    {
        return this.location;
    }

    public Contact getContact()
    {
        return this.contact;
    }

	public double getTaxRate()
	{
		return this.taxRate;
	}

    public double getRange()
    {
        return this.range;
    }

    public int getFavorites()
    {
        return this.favorites;
    }

    public int getFacebookPoints()
    {
        return this.facebookPoints;
    }

    public int getTwitterPoints()
    {
        return this.twitterPoints;
    }

    public int getGooglePlusPoints()
    {
        return this.googlePlusPoints;
    }

	public List<Item> getItems()
	{
		return this.items;
	}

	public List<Item> getFilteredItems()
	{
		return this.filteredItems;
	}

	public List<Station> getStations()
	{
		return this.stations;
	}

    public List<Locale> getLocales()
    {
        return this.locales;
    }

    public boolean getSelfServe()
    {
        return this.selfServe;
    }

    public List<Query> getQueries()
    {
        return this.queries;
    }

	public Map<String, Integer> getRewardItems()
	{
		return this.rewardItems;
	}

	public Map<String, Integer> getRewardOptions()
	{
		return this.rewardOptions;
	}
}
