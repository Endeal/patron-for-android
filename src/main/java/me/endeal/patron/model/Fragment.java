package me.endeal.patron.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Fragment implements Serializable
{
    private static final long serialVersionUID = 7526472945622987L;

	private String id;
	private Item item;
	private List<Option> options;
	private List<Selection> selections;
	private int quantity;

	public Fragment(String id, Item item, List<Option> options, List<Selection> selections, int quantity)
	{
		setId(id);
		setItem(item);
        setOptions(options);
		setSelections(selections);
		setQuantity(quantity);
	}

    public Fragment(Fragment fragment)
    {
        setId(fragment.getId());
        setItem(fragment.getItem());
        setOptions(fragment.getOptions());
        setSelections(fragment.getSelections());
        setQuantity(fragment.getQuantity());
    }

	// Convenience
	public boolean hasOption(Option option)
	{
		for (int i = 0; i < options.size(); i++)
		{
			Option has = options.get(i);
			if (has.getId().equals(option.getId()))
			{
				return true;
			}
		}
		return false;
	}

	// Main Methods
	public Price getPrice()
	{
        Price total = new Price(0, "USD");
        total.add(item.getPrice());
		if (selections != null && selections.size() > 0)
		for (int i = 0; i < selections.size(); i++)
		{
			total.add(selections.get(i).getOption().getPrice());
		}
		if (options != null && options.size() > 0)
		for (int i = 0; i < options.size(); i++)
		{
			total.add(options.get(i).getPrice());
		}
		total.multiply(quantity);
		return total;
	}

	// Setters
	public void setId(String id)
	{
		this.id = id;
	}

	public void setItem(Item item)
	{
		this.item = item;
	}

    public void setOptions(List<Option> options)
    {
        this.options = options;
    }

	public void setSelections(List<Selection> selections)
	{
		this.selections = selections;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	// Getters
	public String getId()
	{
		return this.id;
	}

	public Item getItem()
	{
		return this.item;
	}

    public List<Option> getOptions()
    {
        return this.options;
    }

	public List<Selection> getSelections()
	{
		return this.selections;
	}

	public int getQuantity()
	{
		return this.quantity;
	}
}
