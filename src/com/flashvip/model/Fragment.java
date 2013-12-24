package com.flashvip.model;

import java.math.BigDecimal;
import java.util.List;

public class Fragment
{
	// Properties
	private String fragmentId;
	private Item item;
	private List<Selection> selections;
	private List<Supplement> supplements;
	private int quantity;
	
	// Constructor
	public Fragment(String fragmentId, Item item, List<Selection> selections,
			List<Supplement> supplements, int quantity)
	{
		this.fragmentId = fragmentId;
		this.item = item;
		this.selections = selections;
		this.supplements = supplements;
		this.quantity = quantity;
	}
	
	// Main Methods
	public BigDecimal getPrice()
	{
		BigDecimal total = new BigDecimal(0);
		total.add(item.getPrice());
		for (int i = 0; i < selections.size(); i++)
		{
			total.add(selections.get(i).getOption().getPrice());
		}
		for (int i = 0; i < supplements.size(); i++)
		{
			total.add(supplements.get(i).getPrice());
		}
		return total;
	}

	// Setters
	public void setFragmentId(String fragmentId)
	{
		this.fragmentId = fragmentId;
	}

	public void setItem(Item item)
	{
		this.item = item;
	}

	public void setSelections(List<Selection> selections)
	{
		this.selections = selections;
	}

	public void setSupplements(List<Supplement> supplements)
	{
		this.supplements = supplements;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	// Getters
	public String getFragmentId()
	{
		return fragmentId;
	}

	public Item getItem()
	{
		return item;
	}

	public List<Selection> getSelections()
	{
		return selections;
	}

	public List<Supplement> getSupplements()
	{
		return supplements;
	}

	public int getQuantity()
	{
		return quantity;
	}
}
