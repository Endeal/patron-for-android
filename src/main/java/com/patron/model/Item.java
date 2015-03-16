package com.patron.model;

import java.lang.Cloneable;
import java.lang.CloneNotSupportedException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Item implements Cloneable
{
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

	public Item clone() throws CloneNotSupportedException
	{
		String newId = new String(getId());
		String newName = new String(getName());
		BigDecimal newPrice = new BigDecimal(getPrice().toString());
		int newMaxSupplements = new Integer(getMaxSupplements());
		int newSupply = new Integer(getSupply());

		List<Category> newCategories = new ArrayList<Category>();
		List<Attribute> newAttributes = new ArrayList<Attribute>();
		List<Supplement> newSupplements = new ArrayList<Supplement>();

		// Fill categories
		for (int i = 0; i < getCategories().size(); i++)
		{
			Category newCategory = getCategories().get(i).clone();
			newCategories.add(newCategory);
		}

		// Fill attributes
		for (int i = 0; i < getAttributes().size(); i++)
		{
			Attribute newAttribute = getAttributes().get(i).clone();
			newAttributes.add(newAttribute);
		}

		// Fill supplements
		for (int i = 0; i < getSupplements().size(); i++)
		{
			Supplement newSupplement = getSupplements().get(i).clone();
			newSupplements.add(newSupplement);
		}

		Item newItem = new Item(newId, newName, newPrice, newMaxSupplements, newSupply,
				newCategories, newAttributes, newSupplements);
		return newItem;
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
