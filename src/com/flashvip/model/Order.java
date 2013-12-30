/**
 * @author James Whiteman
 * 
 * Device ID:
 * The device ID is the APID generated by Urban Airship.
 * It uniquely identifies the device running the application.
 * 
 * Device Type:
 * The device type specifies what kind of device is placing
 * the order. This information is needed by Urban Airship.
 * 0 = iPhone
 * 1 = Android
 * 2 = Windows 8
 * 3 = Windows Mobile
 * 4 = Blackberry
 *
 */

package com.flashvip.model;

import java.math.BigDecimal;
import java.util.List;

import com.flashvip.system.Globals;

public class Order
{	
	// Enumeration
	public enum Status
	{
		WAITING, READY, SCANNED, COMPLETED, REJECTED
	}
	
	// Properties
	private String orderId;
	private String vendorId;
	private String deviceId;
	private int deviceType;
	private List<Fragment> fragments;
	private Status status;
	
	// Constructor
	public Order(String orderId, String vendorId, List<Fragment> fragments, Status status)
	{
		this.orderId = orderId;
		this.vendorId = vendorId;
		this.deviceId = Globals.getDeviceId();
		this.deviceType = 1;
		this.fragments = fragments;
		this.status = status;
	}
	
	// Main Methods
	public BigDecimal getPrice()
	{
		BigDecimal total = new BigDecimal(0);
		for (int i = 0; i < fragments.size(); i++)
		{
			total = total.add(fragments.get(i).getPrice());
		}
		return total;
	}

	public static Status getIntStatus(int i)
	{
		switch (i)
		{
		case 0:
			return Status.WAITING;
		case 1:
			return Status.READY;
		case 2:
			return Status.SCANNED;
		case 3:
			return Status.COMPLETED;
		default:
			return Status.REJECTED;
		}
	}

	public static int getStatusInt(Status status)
	{
		switch (status)
		{
		case WAITING:
			return 0;
		case READY:
			return 1;
		case SCANNED:
			return 2;
		case COMPLETED:
			return 3;
		default:
			return 4;
		}
	}
	
	public static String getStatusText(Status status)
	{
		switch (status)
		{
		case WAITING:
			return "Waiting";
		case READY:
			return "Ready";
		case SCANNED:
			return "Scanned";
		case COMPLETED:
			return "Completed";
		default:
			return "Rejected";
		}
	}

	public String getOrderText()
	{
		String s = "";
		for (int i = 0; i < fragments.size(); i++)
		{
			if (i > 0)
			{
				s = s + "\n";
			}
			Fragment fragment = fragments.get(i);
			int quantity = fragments.get(i).getQuantity();
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
			s = s + quantity  +
					"\u0009" +
					name;
			for (int j = 0; j < fragment.getSelections().size(); j++)
			{
				if (j == 0)
				{
					s = s + " with ";
				}
				else
				{
					s = ", ";
				}
				String optionName = fragment.getSelections().get(j).getOption().getName();
				s = s + optionName;
			}
		}
		return s;
	}
	
	// Setters
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
	
	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}
	
	public void setFragments(List<Fragment> fragments)
	{
		this.fragments = fragments;
	}
	
	public void setStatus(Status status)
	{
		this.status = status;
	}
	
	// Getters
	public String getOrderId()
	{
		return orderId;
	}
	
	public String getVendorId()
	{
		return vendorId;
	}
	
	public String getDeviceId()
	{
		return deviceId;
	}
	
	public int getDeviceType()
	{
		return deviceType;
	}
	
	public List<Fragment> getFragments()
	{
		return fragments;
	}
	
	public Status getStatus()
	{
		return status;
	}
}