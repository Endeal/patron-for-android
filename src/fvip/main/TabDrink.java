package fvip.main;

public class TabDrink
{
	private Drink drink;
	private int posAlcohol;
	private int posQuantity;
	private double total_price;
	
	public TabDrink(Drink drink, int posAlcohol, int posQuantity)
	{
		this.drink = drink;
		this.posAlcohol = posAlcohol;
		this.posQuantity = posQuantity;
		Drink d = Globals.getAlcohols().get(posAlcohol);
		if (d != null)
			total_price = drink.getPrice() + d.getPrice();
		else
			total_price = drink.getPrice();
	}

	// SETTERS
	public void setDrink(Drink drink)
	{
		this.drink = drink;
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
	public Drink getDrink()
	{
		return drink;
	}
	
	public Drink getAlcohol()
	{
		Drink d = null;
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
