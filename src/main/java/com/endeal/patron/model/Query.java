package com.endeal.patron.model;

import java.io.Serializable;

public class Query implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String title;
    private int type; // 0 = acknowledgement, 1 = rating, 2 = reply

    public Query(String title, int type)
    {
        setTitle(title);
        setType(type);
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getTitle()
    {
        return this.title;
    }

    public int getType()
    {
        return this.type;
    }
}
