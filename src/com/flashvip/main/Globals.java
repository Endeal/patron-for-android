package com.flashvip.main;

import java.util.ArrayList;
import java.util.Locale;

import android.util.SparseArray;
import android.util.SparseIntArray;

public class Globals
{	
	private static ArrayList<String> codes = new ArrayList<String>();

	private static ArrayList<Location> locations = new ArrayList<Location>();
	private static Location currentServer;
	private static String locationName;
	
	private static ArrayList<Product> products = new ArrayList<Product>();
	private static ArrayList<Product> currentProducts = new ArrayList<Product>();
	private static ArrayList<Product> alcohols = new ArrayList<Product>();
	private static ArrayList<Product> recommendations = new ArrayList<Product>();
	private static ArrayList<Product> favoriteProducts =  new ArrayList<Product>();
	private static ArrayList<Location> favoriteLocations = new ArrayList<Location>();
	private static ArrayList<CartProduct> cartProducts = new ArrayList<CartProduct>();
	
	private static SparseArray<String> mapSpinnerToAlcohol = new SparseArray<String>();
	private static SparseIntArray mapArrayToAlcohol = new SparseIntArray();
	private static SparseIntArray mapArrayToQuantity = new SparseIntArray();
	
	private static float cartTotal;
	
	// MAIN METHODS
	
	public static void addCode(String code)
	{
		codes.add(code);
	}
	
	public static void clearCodes()
	{
		codes.clear();
	}
	
	public static void addCartProduct(CartProduct order)
	{
		cartProducts.add(order);
	}
	
	public static void removeProductFromCart(CartProduct product)
	{
		cartProducts.remove(product);
	}
	
	public static void addFavoriteProduct(Product order)
	{
		if (favoriteProducts == null)
			favoriteProducts = new ArrayList<Product>();
		if (order != null)
			favoriteProducts.add(order);
	}
	
	public static void addFavoriteLocation(Location location)
	{
		if (favoriteLocations == null)
			favoriteLocations = new ArrayList<Location>();
		if (location != null)
			favoriteLocations.add(location);
	}
	
	public static void removeFavoriteProduct(int position)
	{
		favoriteProducts.remove(position);
	}
	
	public static void removeFavoriteLocation(int position)
	{
		favoriteLocations.remove(position);
	}
	
	public static void addAlcohol(Product alcohol)
	{
		alcohols.add(alcohol);
		mapSpinnerToAlcohol.put(alcohols.size() - 1, alcohol.getId());
	}
	
	public static void clearAlcohols()
	{
		alcohols.clear();
		mapSpinnerToAlcohol.clear();
	}
	
	public static void addRecommendation(Product order)
	{
		recommendations.add(order);
	}
	
	public static void clearRecommendations()
	{
		recommendations.clear();
	}
	
	public static void updateCartTotal()
	{
		float total = 0.0f;
		for (int i = 0; i < cartProducts.size(); i++)
		{
			total += cartProducts.get(i).getPrice();
		}
		cartTotal = total;
	}
	
	public static Product getProductById(String id)
	{
		Product d = null;
		for (int j = 0; j < products.size(); j++)
		{
			if (products.get(j).getId().equals(id))
				d = products.get(j);
		}
		return d;
	}
	
	public static void clearSpinnerMappings()
	{
		mapArrayToAlcohol.clear();
		mapArrayToQuantity.clear();
	}
	
	// SETTER METHODS
	
	public static void setCodes(ArrayList<String> newCodes)
	{
		codes = newCodes;
	}
	
	public static void setLocationName(String s)
	{
		locationName = s;
	}
	
	public static void setLocations(ArrayList<Location> newLocations)
	{
		locations = newLocations;
	}
	
	public static void setProducts(ArrayList<Product> newProducts)
	{
		products = newProducts;
	}
	
	public static void setCurrentProducts(ArrayList<Product> newCurrentProducts)
	{
		currentProducts = newCurrentProducts;
	}
	
	public static void setAlcohols(ArrayList<Product> newAlcohols)
	{
		alcohols = newAlcohols;
	}
	
	public static void setRecommendations(ArrayList<Product> products)
	{
		recommendations = products;
	}
	
	public static void setFavoriteProducts(ArrayList<Product> products)
	{
		favoriteProducts = products;
	}
	
	public static void setFavoriteLocations(ArrayList<Location> locations)
	{
		favoriteLocations = locations;
	}
	
	public static void setCartProducts(ArrayList<CartProduct> product)
	{
		cartProducts = product;
	}
	
	public static void setCartTotal(float total)
	{
		cartTotal = total;
	}
	
	public static void setCurrentLocation(Location s)
	{
		currentServer = s;
	}
	
	public static void setArrayToAlcohol(int listPosition, int selection)
	{
		mapArrayToAlcohol.put(listPosition, selection);
	}
	
	public static void setArrayToQuantity(int listPosition, int selection)
	{
		mapArrayToQuantity.put(listPosition, selection);
	}
	
	// ACCESSOR METHODS
	
	public static String getLocationName()
	{
		if (locationName != null && locationName.length() > 0)
			return locationName;
		else
			return "(Select Bar)";
	}
	
	public static ArrayList<Location> getLocations()
	{
		return locations;
	}
	
	public static ArrayList<Location> searchLocations(String query)
	{
		String s = query.toLowerCase(Locale.US);
		ArrayList<Location> items = new ArrayList<Location>();
		for (int i = 0; i < locations.size(); i++)
		{
			if (locations.get(i).getName().toLowerCase(Locale.US).contains(s))
				items.add(locations.get(i));
		}
		return items;
	}
	
	public static ArrayList<Product> getProducts()
	{
		return products;
	}
	
	public static ArrayList<Product> getCurrentProducts()
	{
		return currentProducts;
	}
	
	public static ArrayList<Product> searchProducts(String query)
	{
		String s = query.toLowerCase(Locale.US);
		ArrayList<Product> items = new ArrayList<Product>();
		for (int i = 0; i < products.size(); i++)
		{
			if (products.get(i).getName().toLowerCase(Locale.US).contains(s))
				items.add(products.get(i));
		}
		return items;
	}
	
	public static ArrayList<Product> getRecommendations()
	{
		return recommendations;
	}
	
	public static ArrayList<Product> getFavoriteProducts()
	{
		return favoriteProducts;
	}
	
	public static ArrayList<Location> getFavoriteLocations()
	{
		return favoriteLocations;
	}
	
	public static ArrayList<CartProduct> getCartProducts()
	{
		return cartProducts;
	}
	
	public static ArrayList<CartProduct> searchCartProducts(String query)
	{
		String s = query.toLowerCase(Locale.US);
		ArrayList<CartProduct> items = new ArrayList<CartProduct>();
		for (int i = 0; i < cartProducts.size(); i++)
		{
			if (cartProducts.get(i).getDrink().getName().toLowerCase(Locale.US).contains(s))
				items.add(cartProducts.get(i));
		}
		return items;
	}
	
	public static ArrayList<Product> getAlcohols()
	{
		return alcohols;
	}
	
	public static String getAlcoholIdForSpinnerPosition(int position)
	{
		String alcohol_id = mapSpinnerToAlcohol.get(position);
		return alcohol_id;
	}
	
	public static double getCartTotal()
	{
		return cartTotal;
	}
	
	public static Location getCurrentLocation()
	{
		return currentServer;
	}
	
	public static int getAlcoholSelectionForKey(int listPosition)
	{
		return mapArrayToAlcohol.get(listPosition);
	}
	
	public static int getQuantitySelectionForKey(int listPosition)
	{
		return mapArrayToQuantity.get(listPosition);
	}
	
	public static ArrayList<String> getCodes()
	{
		return codes;
	}
}
