package com.patron.model;

import java.lang.Cloneable;
import java.lang.CloneNotSupportedException;
import java.io.Serializable;

public class Selection implements Serializable, Cloneable
{
	private static final long serialVersionUID = 1L;

	// Properties
	private Attribute attribute;
	private Option option;

	// Constructor
	public Selection(Attribute attribute, Option option)
	{
		this.attribute = attribute;
		this.option = option;
	}

	public Selection clone() throws CloneNotSupportedException
	{
		Attribute newAttribute = getAttribute().clone();
		Option newOption = getOption().clone();
		Selection newSelection = new Selection(newAttribute, newOption);
		return newSelection;
	}

	// Setters
	public void setAttribute(Attribute attribute)
	{
		this.attribute = attribute;
	}

	public void setOption(Option option)
	{
		this.option = option;
	}

	// Getters
	public Attribute getAttribute()
	{
		return attribute;
	}

	public Option getOption()
	{
		return option;
	}
}
