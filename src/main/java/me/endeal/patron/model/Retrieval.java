package me.endeal.patron.model;

import java.io.Serializable;

public class Retrieval implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String method;
    private Station station;
    private Locale locale;
    private Location location;

    public Retrieval(String method, Station station, Locale locale, Location location)
    {
        setMethod(method);
        setStation(station);
        setLocale(locale);
        setLocation(location);
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public void setStation(Station station)
    {
        this.station = station;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public String getMethod()
    {
        return this.method;
    }

    public Station getStation()
    {
        return this.station;
    }

    public Locale getLocale()
    {
        return this.locale;
    }

    public Location getLocation()
    {
        return this.location;
    }
}
