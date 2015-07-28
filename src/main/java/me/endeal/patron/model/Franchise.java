package com.endeal.patron.model;

import java.io.Serializable;

public class Franchise implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;

    public Franchise(String id, String name)
    {
        setId(id);
        setName(name);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }
}
