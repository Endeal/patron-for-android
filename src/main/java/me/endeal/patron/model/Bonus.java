package me.endeal.patron.model;

import java.io.Serializable;
import java.util.List;

public class Bonus implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private List<Fragment> fragments;
    private Price amount;
    private int percent;
    private List<Bonus> bonuses;

    public Bonus(String id, List<Fragment> fragments, Price amount, int percent, List<Bonus> bonuses)
    {
        setId(id);
        setFragments(fragments);
        setAmount(amount);
        setPercent(percent);
        setBonuses(bonuses);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setFragments(List<Fragment> fragments)
    {
        this.fragments = fragments;
    }

    public void setAmount(Price amount)
    {
        this.amount = amount;
    }

    public void setPercent(int percent)
    {
        this.percent = percent;
    }

    public void setBonuses(List<Bonus> bonuses)
    {
        this.bonuses = bonuses;
    }

    public String getId()
    {
        return this.id;
    }

    public List<Fragment> getFragments()
    {
        return this.fragments;
    }

    public Price getAmount()
    {
        return this.amount;
    }

    public int getPercent()
    {
        return this.percent;
    }

    public List<Bonus> getBonuses()
    {
        return this.bonuses;
    }
}
