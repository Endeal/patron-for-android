package com.flashvip.main;

public class TabProduct
{
	private Product product;
	private int posAlcohol;
	private int posQuantity;
	private double total_price;
	
	public TabProduct(Product product, int posAlcohol, int posQuantity)
	{
		this.product = product;
		this.posAlcohol = posAlcohol;
		this.posQuantity = posQuantity;
		Product d = Globals.getAlcohols().get(posAlcohol);
		if (d != null)
			total_price = product.getPrice() + d.getPrice();
		else
			total_price = product.getPrice();
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
	
}
