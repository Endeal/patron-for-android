package me.endeal.patron.model;

import java.io.Serializable;
import java.util.List;

public class Constraint implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private List<Fragment> fragments;
    private Price min;
    private Price max;
    private List<Constraint> constraints;

    public Constraint(String id, String name, List<Fragment> fragments, Price min,
            Price max, List<Constraint> constraints)
    {
        setId(id);
        setName(name);
        setFragments(fragments);
        setMin(min);
        setMax(max);
        setConstraints(constraints);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setFragments(List<Fragment> fragments)
    {
        this.fragments = fragments;
    }

    public void setMin(Price min)
    {
        this.min = min;
    }

    public void setMax(Price max)
    {
        this.max = max;
    }

    public void setConstraints(List<Constraint> constraints)
    {
        this.constraints = constraints;
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public List<Fragment> getFragments()
    {
        return this.fragments;
    }

    public Price getMin()
    {
        return this.min;
    }

    public Price getMax()
    {
        return this.max;
    }

    public List<Constraint> getConstraints()
    {
        return this.constraints;
    }
}
