package com.endeal.patron.model;

import java.io.Serializable;
import java.util.List;

public class Feedback implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private Patron patron;
    private Vendor vendor;
    private List<Survey> surveys;

    public Feedback(String id, Patron patron, Vendor vendor, List<Survey> surveys)
    {
        setId(id);
        setPatron(patron);
        setVendor(vendor);
        setSurveys(surveys);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setPatron(Patron patron)
    {
        this.patron = patron;
    }

    public void setVendor(Vendor vendor)
    {
        this.vendor = vendor;
    }

    public void setSurveys(List<Survey> surveys)
    {
        this.surveys = surveys;
    }

    public String getId()
    {
        return this.id;
    }

    public Patron getPatron()
    {
        return this.patron;
    }

    public Vendor getVendor()
    {
        return this.vendor;
    }

    public List<Survey> getSurveys()
    {
        return this.surveys;
    }
}
