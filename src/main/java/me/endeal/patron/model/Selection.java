package me.endeal.patron.model;

import java.io.Serializable;

public class Selection implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Attribute attribute;
	private Option option;

	public Selection(Attribute attribute, Option option)
	{
        setAttribute(attribute);
        setOption(option);
	}

	public void setAttribute(Attribute attribute)
	{
		this.attribute = attribute;
	}

	public void setOption(Option option)
	{
		this.option = option;
	}

	public Attribute getAttribute()
	{
		return attribute;
	}

	public Option getOption()
	{
		return option;
	}
}
