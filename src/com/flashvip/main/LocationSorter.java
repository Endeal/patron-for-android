package com.flashvip.main;

import java.util.ArrayList;

public class LocationSorter
{
	
	public static ArrayList<Location> getByFavorites(ArrayList<Location> locations, boolean ascending)
	{
		ArrayList<Location> newLocations = new ArrayList<Location>();
		if (locations != null && !locations.isEmpty())
		{
			for (int i = 0; i < locations.size(); i++)
			{
				Location currentLocation = locations.get(i);
				if (Globals.getFavoriteLocations() != null && Globals.getFavoriteLocations().contains(currentLocation))
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