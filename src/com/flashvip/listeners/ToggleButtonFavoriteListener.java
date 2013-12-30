package com.flashvip.listeners;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;

import com.flashvip.model.Item;
import com.flashvip.model.Vendor;
import com.flashvip.system.Globals;
import com.google.gson.Gson;

public class ToggleButtonFavoriteListener implements OnClickListener
{
	// The favorite product if a product toggle button.
	private Item item;

	// The favorite location if a location toggle button.
	private Vendor vendor;

	public ToggleButtonFavoriteListener(Item item)
	{
		this.item = item;
		this.vendor = null;
	}

	public ToggleButtonFavoriteListener(Vendor vendor)
	{
		this.item = null;
		this.vendor = vendor;
	}

	@Override
	public void onClick(View buttonView)
	{
		// A product was selected.
		if (item != null)
		{
			if (Globals.getFavoriteItems() != null && !Globals.getFavoriteItems().isEmpty())
			{
				boolean isFavorite = false;
				for (int i = 0; i < Globals.getFavoriteItems().size(); i++)
				{
					Item favoriteItem = Globals.getFavoriteItems().get(i);
					if (favoriteItem.getItemId().equals(item.getItemId()))
					{
						isFavorite = true;
						removeFavoriteItem(buttonView, i);
						break;
					}
				}
				if (!isFavorite)
				{
					addFavoriteItem(buttonView);
				}
			}
			else
			{
				addFavoriteItem(buttonView);
			}
		}

		// A location was selected.
		else
		{
			if (Globals.getFavoriteVendors() != null && !Globals.getFavoriteVendors().isEmpty())
			{
				boolean isFavorite = false;
				for (int i = 0; i < Globals.getFavoriteVendors().size(); i++)
				{
					Vendor favoriteVendor = Globals.getFavoriteVendors().get(i);
					if (favoriteVendor.getVendorId().equals(vendor.getVendorId()))
					{
						isFavorite = true;
						removeFavoriteVendor(buttonView, i);
						break;
					}
				}
				if (!isFavorite)
				{
					addFavoriteVendor(buttonView);
				}
			}
			else
			{
				addFavoriteVendor(buttonView);
			}
		}
	}

	public void addFavoriteVendor(View buttonView)
	{
		ArrayList<Vendor> favorites = Globals.getFavoriteVendors();
		favorites.add(vendor);
		Gson gson = new Gson();
		String json = gson.toJson(favorites);
		Activity activity = (Activity)buttonView.getContext();
		SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("favoriteVendors", json);
		editor.commit();
		Globals.setFavoriteVendors(favorites);
	}
	
	public void addFavoriteItem(View buttonView)
	{
		ArrayList<Item> favorites = Globals.getFavoriteItems();
		favorites.add(item);
		Gson gson = new Gson();
		String json = gson.toJson(favorites);
		Activity activity = (Activity)buttonView.getContext();
		SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("favoriteItems", json);
		editor.commit();
		Globals.setFavoriteItems(favorites);
	}
	
	public void removeFavoriteVendor(View buttonView, int i)
	{
		ArrayList<Vendor> favorites = Globals.getFavoriteVendors();
		favorites.remove(i);
		Gson gson = new Gson();
		String json = gson.toJson(favorites);
		Activity activity = (Activity)buttonView.getContext();
		SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("favoriteVendors", json);
		editor.commit();
		Globals.setFavoriteVendors(favorites);
	}
	
	public void removeFavoriteItem(View buttonView, int i)
	{
		ArrayList<Item> favorites = Globals.getFavoriteItems();
		favorites.remove(i);
		Gson gson = new Gson();
		String json = gson.toJson(favorites);
		Activity activity = (Activity)buttonView.getContext();
		SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("favoriteItems", json);
		editor.commit();
		Globals.setFavoriteItems(favorites);
	}
}