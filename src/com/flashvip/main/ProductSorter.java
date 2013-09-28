package com.flashvip.main;

import java.util.ArrayList;

import com.flashvip.main.Product.Type;

public class ProductSorter
{
	public static ArrayList<Product> getByType(ArrayList<Product> products, Type type, boolean ascending)
	{
		ArrayList<Product> newProducts = new ArrayList<Product>();
		if (products != null & !products.isEmpty())
		{
			for (int i = 0; i < products.size(); i++)
			{
				if (products.get(i).getType() == type)
				{
					if (ascending)
						newProducts.add(products.get(i));
					else
						newProducts.add(0, products.get(i));
				}
			}
		}
		return newProducts;
	}
	
	public static ArrayList<Product> getByFavorites(ArrayList<Product> products, boolean ascending)
	{
		ArrayList<Product> newProducts = new ArrayList<Product>();
		if (products != null && !products.isEmpty())
		{
			for (int i = 0; i < products.size(); i++)
			{
				Product currentProduct = products.get(i);
				if (Globals.getFavoriteProducts() != null && Globals.getFavoriteProducts().contains(currentProduct))
				{
					if (ascending)
						newProducts.add(currentProduct);
					else
						newProducts.add(0, currentProduct);
				}
			}
		}
		return newProducts;
	}
}