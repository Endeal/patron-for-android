package com.flashvip.main;

public class CartProduct
{
	private Product product;
	private int posAlcohol;
	private int posQuantity;
	private double total_price;
	
	public CartProduct(Product product, int posAlcohol, int posQuantity)
	{
		this.product = product;
		this.posAlcohol = posAlcohol;
		this.posQuantity = posQuantity;
		updatePrice();
	}

	// SETTERS
	public void setDrink(Product product)
	{
		this.product = product;
	}
	
	public void setAlcohol(int posAlcohol)
	{
		this.posAlcohol = posAlcohol;
	}
	
	public void setQuantity(int posQuantity)
	{
		this.posQuantity = posQuantity;
	}
	
	// GETTERS
	public Product getDrink()
	{
		return product;
	}
	
	public Product getAlcohol()
	{
		Product d = null;
		d = Globals.getAlcohols().get(posAlcohol);
		return d;
	}
	
	public int getAlcoholPosition()
	{
		return posAlcohol;
	}
	
	public int getQuantityPosition()
	{
		return posQuantity;
	}
	
	public double getPrice()
	{
		return total_price;
	}
	
	// Main
	
	public void updatePrice()
	{
		float alcoholPrice = 0.0f;
		if (posAlcohol != -1 && Globals.getAlcohols() != null)
		{
			Product alcohol = Globals.getAlcohols().get(posAlcohol);
			if (alcohol != null)
				alcoholPrice = (float) alcohol.getPrice();
		}
		total_price = (product.getPrice() + alcoholPrice) * (posQuantity + 1);
	}
	
}
