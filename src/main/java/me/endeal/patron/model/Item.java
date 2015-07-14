package me.endeal.patron.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Item implements Serializable
{
    private static final long serialVersionUID = 1L;

	private String id;
	private String name;
    private String description;
    private String picture;
	private Price price;
	private List<Category> categories;
    private List<Option> options;
	private List<Attribute> attributes;
    private Nutrition nutrition;
	private int supply;

	public Item(String id, String name, String description, String picture, Price price, List<Category> categories,
            List<Option> options, List<Attribute> attributes, Nutrition nutrition, int supply)
	{
		setId(id);
		setName(name);
        setDescription(description);
        setPicture(picture);
		setPrice(price);
		setCategories(categories);
        setOptions(options);
		setAttributes(attributes);
        setNutrition(nutrition);
		setSupply(supply);
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

	public void setPrice(Price price)
	{
		this.price = price;
	}

	public void setCategories(List<Category> categories)
	{
		this.categories = categories;
	}

    public void setOptions(List<Option> options)
    {
        this.options = options;
    }

	public void setAttributes(List<Attribute> attributes)
	{
		this.attributes = attributes;
	}

    public void setNutrition(Nutrition nutrition)
    {
        this.nutrition = nutrition;
    }

	public void setSupply(int supply)
	{
		this.supply = supply;
	}

	public String getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

    public String getDescription()
    {
        return this.description;
    }

    public String getPicture()
    {
        return this.picture;
    }

	public Price getPrice()
	{
		return this.price;
	}

	public List<Category> getCategories()
	{
		return this.categories;
	}

    public List<Option> getOptions()
    {
        return this.options;
    }

	public List<Attribute> getAttributes()
	{
		return this.attributes;
	}

    public Nutrition getNutrition()
    {
        return this.nutrition;
    }

	public int getSupply()
	{
		return this.supply;
	}
}
