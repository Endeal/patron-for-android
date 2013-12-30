package com.flashvip.model;

import java.text.DateFormat;
import java.util.Date;

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
