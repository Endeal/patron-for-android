package me.endeal.patron.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Special
{
    public static enum Day
    {
        Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
    }

    private String id;
    private String name;
    private List<Item> items;
    private Map<DateTime, DateTime> times;
    private List<Day> days;

    public Special(String id, String name, List<Item> items, Map<DateTime, DateTime> times,
            List<Day> days)
    {
        setId(id);
        setName(name);
        setItems(items);
        setTimes(times);
        setDays(days);
    }

    public Day parseDay(String day)
    {
        if (day.equals("Monday"))
            return Day.Monday;
        else if (day.equals("Tuesday"))
            return Day.Tuesday;
        else if (day.equals("Wednesday"))
            return Day.Wednesday;
        else if (day.equals("Thursday"))
            return Day.Thursday;
        else if (day.equals("Friday"))
            return Day.Friday;
        else if (day.equals("Saturday"))
            return Day.Saturday;
        else
            return Day.Sunday;
    }

    // Setters
    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setItems(List<Item> items)
    {
        this.items = items;
    }

    public void setTimes(Map<DateTime, DateTime> times)
    {
        this.times = times;
    }

    public void setDays(List<Day> days)
    {
        this.days = days;
    }

    // Getters
    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public List<Item> getItems()
    {
        return this.items;
    }

    public Map<DateTime, DateTime> getTimes()
    {
        return this.times;
    }

    public List<Day> getDays()
    {
        return this.days;
    }

}
