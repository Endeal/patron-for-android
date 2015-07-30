package com.endeal.patron.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.lang.Exception;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

public class Patron implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String _id;
    private String stripeId;
    private Identity identity;
    private List<Funder> funders;
    private List<String> franchises;
    private List<String> vendors;
    private List<String> items;

    public Patron(String id, String stripeId, Identity identity, List<Funder> funders, List<String> franchises, List<String> vendors, List<String> items)
    {
        setId(id);
        setStripeId(stripeId);
        setIdentity(identity);
        setFunders(funders);
        setFranchises(franchises);
        setVendors(vendors);
        setItems(items);
    }

    public void setId(String id)
    {
        this._id = id;
    }

    public void setStripeId(String stripeId)
    {
        this.stripeId = stripeId;
    }

    public void setIdentity(Identity identity)
    {
        this.identity = identity;
    }

    public void setFunders(List<Funder> funders)
    {
        this.funders = funders;
    }

    public void setFranchises(List<String> franchises)
    {
        this.franchises = franchises;
    }

    public void setVendors(List<String> vendors)
    {
        this.vendors = vendors;
    }

    public void setItems(List<String> items)
    {
        this.items = items;
    }

    public String getId()
    {
        return this._id;
    }

    public String getStripeId()
    {
        return this.stripeId;
    }

    public Identity getIdentity()
    {
        return this.identity;
    }

    public List<Funder> getFunders()
    {
        return this.funders;
    }

    public List<String> getFranchises()
    {
        return this.franchises;
    }

    public List<String> getVendors()
    {
        return this.vendors;
    }

    public List<String> getItems()
    {
        return this.items;
    }
}
