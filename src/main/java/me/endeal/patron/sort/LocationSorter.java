package me.endeal.patron.sort;

import java.util.ArrayList;
import java.util.List;

import me.endeal.patron.model.Vendor;
import me.endeal.patron.system.Globals;

public class LocationSorter
{
	
	public static List<Vendor> getByFavorites(List<Vendor> vendors, boolean ascending)
	{
		ArrayList<Vendor> newLocations = new ArrayList<Vendor>();
		if (vendors != null && !vendors.isEmpty())
		{
			for (int i = 0; i < vendors.size(); i++)
			{
				Vendor currentLocation = vendors.get(i);
				if (Globals.getFavoriteVendors() != null && Globals.getFavoriteVendors().contains(currentLocation))
				{
					if (ascending)
						newLocations.add(currentLocation);
					else
						newLocations.add(0, currentLocation);
				}
			}
		}
		return newLocations;
	}
	
	public static List<Vendor> getBySearch(List<Vendor> vendors, CharSequence text)
	{
		ArrayList<Vendor> newVendors = new ArrayList<Vendor>();
		if (vendors != null && !vendors.isEmpty())
		{
			for (int i = 0; i < vendors.size(); i++)
			{
				Vendor currentVendor = vendors.get(i);
				if (currentVendor.getName().toLowerCase().contains(text.toString().toLowerCase()) ||
						currentVendor.getAddress().toLowerCase().contains(text.toString().toLowerCase()) ||
						currentVendor.getCity().toLowerCase().contains(text.toString().toLowerCase()) ||
						currentVendor.getState().toLowerCase().contains(text.toString().toLowerCase()) ||
						currentVendor.getZip().toLowerCase().contains(text.toString().toLowerCase()) ||
						currentVendor.getPhone().toLowerCase().contains(text.toString().toLowerCase()))
				{
					newVendors.add(vendors.get(i));
				}
			}
		}
		return newVendors;
	}
}