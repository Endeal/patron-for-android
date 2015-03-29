package com.patron.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Item implements Serializable
{
    private static final long serialVersionUID = 752647290209837L;

	// Properties
	private String id;
	private String name;
	private BigDecimal price;
	private int maxSupplements;
	private int supply;
	private List<Category> categories;
	private List<Attribute> attributes;
	private List<Supplement> supplements;

	// Constructor
	public Item(String id, String name, BigDecimal price, int maxSupplements, int supply,
			List<Category> categories, List<Attribute> attributes,
			List<Supplement> supplements)
	{
		setId(id);
		setName(name);
		setPrice(price);
		setMaxSupplements(maxSupplements);
		setSupply(supply);
		setCategories(categories);
		setAttributes(attributes);
		setSupplements(supplements);
	}

    public Item(Item item)
    {
        setId(item.getId());
        setName(item.getName());
        setPrice(item.getPrice());
        setMaxSupplements(item.getMaxSupplements());
        setSupply(item.getSupply());
        setCategories(item.getCategories());
        setAttributes(item.getAttributes());
        setSupplements(item.getSupplements());
    }
    public Item(JSONObject rawItem) throws JSONException
    {
        categories = new ArrayList<Category>();
        attributes = new ArrayList<Attribute>();
        supplements = new ArrayList<Supplement>();

        setId(rawItem.getString("itemId"));
        setName(rawItem.getString("name"));
        setPrice(new BigDecimal(rawItem.getString("price")));
        setMaxSupplements(rawItem.getInt("maxSupplements"));
        setSupply(rawItem.getInt("supply"));
        JSONArray rawCategories = rawItem.getJSONArray("categories");
        JSONArray rawAttributes = rawItem.getJSONArray("attributes");
        JSONArray rawSupplements = rawItem.getJSONArray("supplements");
        for (int i = 0; i < rawCategories.length(); i++)
        {
            JSONObject rawCategory = rawCategories.getJSONObject(i);
            Category category = new Category(rawCategory);
            categories.add(category);
        }
        for (int i = 0; i < rawAttributes.length(); i++)
        {
            JSONObject rawAttribute = rawAttributes.getJSONObject(i);
            Attribute attribute = new Attribute(rawAttribute);
            attributes.add(attribute);
        }
        for (int i = 0; i < rawSupplements.length(); i++)
        {
            JSONObject rawSupplement = rawSupplements.getJSONObject(i);
            Supplement supplement = new Supplement(rawSupplement);
            supplements.add(supplement);
        }
    }

	// Setters
	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public void setMaxSupplements(int maxSupplements)
	{
		this.maxSupplements = maxSupplements;
	}

	public void setSupply(int supply)
	{
		this.supply = supply;
	}

	public void setCategories(List<Category> categories)
	{
		this.categories = categories;
	}

	public void setAttributes(List<Attribute> attributes)
	{
		this.attributes = attributes;
	}

	public void setSupplements(List<Supplement> supplements)
	{
		this.supplements = supplements;
	}

	// Getters
	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public int getMaxSupplements()
	{
		return maxSupplements;
	}

	public int getSupply()
	{
		return supply;
	}

	public List<Category> getCategories()
	{
		return categories;
	}

	public List<Attribute> getAttributes()
	{
		return attributes;
	}

	public List<Supplement> getSupplements()
	{
		return supplements;
	}
}
