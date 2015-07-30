package com.endeal.patron.model;

import java.io.Serializable;

public class Retrieval implements Serializable
{
    private static final long serialVersionUID = 1L;

    public static enum Method {
        Pickup, Service, Delivery, SelfServe
    }

    private Method method;
    private Station station;
    private Locale locale;
    private Location location;

    public Retrieval(Method method, Station station, Locale locale, Location location)
    {
        setMethod(method);
        setStation(station);
        setLocale(locale);
        setLocation(location);
    }

    public void setMethod(Method method)
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

    public Method getMethod()
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
