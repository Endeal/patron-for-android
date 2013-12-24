package com.flashvip.model;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class Code
{
	// Properties
	private long timestamp;
	private Order order;
	
	// Constructor
	public Code(long timestamp, Order order)
	{
		this.timestamp = timestamp;
		this.order = order;
	}
	
	// Main methods
	
	public String getTimestampText()
	{
		Date date = new Date(timestamp);
		String s = DateFormat.getDateTimeInstance().format(date);
		return s;
	}
	
	public String getOrdersText()
	{
		String s = "";
		List<Fragment> fragments = order.getFragments();
		for (int i = 0; i < fragments.size(); i++)
		{
			if (i > 0)
			{
				s = s + "\n";
			}
			int quantity = fragments.get(i).getQuantity() + 1;
			String name;
			if (fragments.get(i).getItem() != null &&
					fragments.get(i).getItem().getName() != null)
			{
				name = fragments.get(i).getItem().getName();
			}
			else
			{
				name = "";
			}
			/*
			if (fragments.get(i).getAlcohol() != null &&
					fragments.get(i).getAlcohol().getName() != null)
			{
				alcohol = fragments.get(i).getAlcohol().getName();
			}
			else
			{
				alcohol = "";
			}*/
			
			
			s = s + quantity  +
					//FlashApplication.getContext().getResources().getString(R.string.tab) +
					"\u0009" +
					name/* +
					"\u0009" +
					alcohol*/;
		}
		return s;
	}
	
	// Setters
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public void setOrder(Order order)
	{
		this.order = order;
	}
	
	// Getters
	public long getTimestamp()
	{
		return timestamp;
	}
	
	public Order getOrder()
	{
		return order;
	}
}
