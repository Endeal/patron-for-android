package com.endeal.patron.model;

import java.io.Serializable;
import java.util.List;

public class Locale implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String _id;
    private String name;
    private int number;

    public Locale(String id, String name, int number)
    {
        setId(id);
        setName(name);
        setNumber(number);
    }

    public void setId(String id)
    {
        this._id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public String getId()
    {
        return this._id;
    }

    public String getName()
    {
        return this.name;
    }

    public int getNumber()
    {
        return this.number;
    }

    @Override
    public String toString()
    {
        if (getName() == null)
            return getNumber() + "";
        return getName() + " " + getNumber();
    }
}
