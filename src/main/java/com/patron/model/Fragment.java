package com.patron.model;

import java.lang.Cloneable;
import java.lang.CloneNotSupportedException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Fragment implements Cloneable
{
	// Properties
	private String id;
	private Item item;
	private List<Selection> selections;
	private List<Supplement> supplements;
	private int quantity;

	// Constructor
	public Fragment(String id, Item item, List<Selection> selections,
			List<Supplement> supplements, int quantity)
	{
		setId(id);
		setItem(item);
		setSelections(selections);
		setSupplements(supplements);
		setQuantity(quantity);
	}

	// Cloneable
	public Fragment clone() throws CloneNotSupportedException
	{
		String newId = new String(getId());
		Item newItem = item.clone();
		List<Selection> newSelections = new ArrayList<Selection>();
		List<Supplement> newSupplements = new ArrayList<Supplement>();
		int newQuantity = new Integer(getQuantity());

		// Fill Selections
		for (int i = 0; i < getSelections().size(); i++)
		{
			Selection newSelection = getSelections().get(i).clone();
			newSelections.add(newSelection);
		}

		// Fill Supplements
		for (int i = 0; i < getSupplements().size(); i++)
		{
			Supplement newSupplement = getSupplements().get(i).clone();
			newSupplements.add(newSupplement);
		}

		Fragment fragment = new Fragment(newId, newItem, newSelections, newSupplements, newQuantity);
		return fragment;
	}

	// Convenience
	public boolean hasSupplement(Supplement supplement)
	{
		for (int i = 0; i < supplements.size(); i++)
		{
			Supplement has = supplements.get(i);
			if (has.getId().equals(supplement.getId()))
			{
				return true;
			}
		}
		return false;
	}

	// Main Methods
	public BigDecimal getPrice()
	{
		BigDecimal total = new BigDecimal(0);
		total = total.add(item.getPrice());
		if (selections != null && selections.size() > 0)
		for (int i = 0; i < selections.size(); i++)
		{
			total = total.add(selections.get(i).getOption().getPrice());
		}
		if (supplements != null && selections.size() > 0)
		for (int i = 0; i < supplements.size(); i++)
		{
			total = total.add(supplements.get(i).getPrice());
		}
		total = total.multiply(new BigDecimal(quantity));
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
	public String getId()
	{
		return id;
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
