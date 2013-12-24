package com.flashvip.sort;

import java.util.ArrayList;

import com.flashvip.main.Globals;
import com.flashvip.model.Vendor;

public class LocationSorter
{
	
	public static ArrayList<Vendor> getByFavorites(ArrayList<Vendor> vendors, boolean ascending)
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
}