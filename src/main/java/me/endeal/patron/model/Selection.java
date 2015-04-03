package me.endeal.patron.model;

import java.io.Serializable;

public class Selection implements Serializable
{
	private static final long serialVersionUID = 1L;

	// Properties
	private Attribute attribute;
	private Option option;

	// Constructor
	public Selection(Attribute attribute, Option option)
	{
        setAttribute(attribute);
        setOption(option);
	}

    public Selection(Selection selection)
    {
        setAttribute(selection.getAttribute());
        setOption(selection.getOption());
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
