package com.endeal.patron.model;

import java.io.Serializable;
import java.text.NumberFormat;

public class Price implements Serializable
{
    private static final long serialVersionUID = 1L;

    /*
     * Currencies: USD, EUR, BTC
     */

    private int value;
    private String currency;

    public Price(int value, String currency)
    {
        setValue(value);
        setCurrency(currency);
    }

    public void add(Price price)
    {
        this.value += price.getValue();
    }

    public void multiply(double num)
    {
        this.value *= num;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public int getValue()
    {
        return this.value;
    }

    public String getCurrency()
    {
        return this.currency;
    }

    @Override
    public String toString()
    {
        double denominated = (double)(value / 100.0);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(denominated);
    }
}
