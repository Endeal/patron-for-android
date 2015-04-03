package me.endeal.patron.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.DateTimeZone;

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
        DateTimeZone timezone = DateTimeZone.getDefault();
        long offset = timezone.getOffset(getTimestamp());
        DateTime date = new DateTime(getTimestamp() + offset);
        String s = date.toString("hh:mma, E MM/dd/yyyy");
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
