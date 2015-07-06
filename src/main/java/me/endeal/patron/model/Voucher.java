package me.endeal.patron.model;

import java.io.Serializable;
import java.util.List;

public class Voucher implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Constraint> constraints;
    private List<Bonus> bonuses;
    private List<Period> periods;
    private boolean singular;
    private int max;

    public Voucher(String name, List<Constraint> constraints, List<Bonus> bonuses, List<Period> periods,
            boolean singular, int max)
    {
        setName(name);
        setConstraints(constraints);
        setBonuses(bonuses);
        setPeriods(periods);
        setSingular(singular);
        setMax(max);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setConstraints(List<Constraint> constraints)
    {
        this.constraints = constraints;
    }

    public void setBonuses(List<Bonus> bonuses)
    {
        this.bonuses = bonuses;
    }

    public void setPeriods(List<Period> periods)
    {
        this.periods = periods;
    }

    public void setSingular(boolean singular)
    {
        this.singular = singular;
    }

    public void setMax(int max)
    {
        this.max = max;
    }

    public String getName()
    {
        return this.name;
    }

    public List<Constraint> getConstraints()
    {
        return this.constraints;
    }

    public List<Bonus> getBonuses()
    {
        return this.bonuses;
    }

    public List<Period> getPeriods()
    {
        return this.periods;
    }

    public boolean getSingular()
    {
        return this.singular;
    }

    public int getMax()
    {
        return this.max;
    }
}
