package me.endeal.patron.model;

import java.io.Serializable;

public class Period implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long start;
    private long end;

    public Period(long start, long end)
    {
        setStart(start);
        setEnd(end);
    }

    public void setStart(long start)
    {
        this.start = start;
    }

    public void setEnd(long end)
    {
        this.end = end;
    }

    public long getStart()
    {
        return this.start;
    }

    public long getEnd()
    {
        return this.end;
    }
}
