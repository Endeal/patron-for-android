package com.endeal.patron.model;

import java.io.Serializable;

public class Contact
{
    private static final long serialVersionUID = 1L;

    private String phone;
    private String email;
    private String facebook;
    private String twitter;
    private String googlePlus;

    public Contact(String phone, String email, String facebook, String twitter, String googlePlus)
    {
        setPhone(phone);
        setEmail(email);
        setFacebook(facebook);
        setTwitter(twitter);
        setGooglePlus(googlePlus);
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setFacebook(String facebook)
    {
        this.facebook = facebook;
    }

    public void setTwitter(String twitter)
    {
        this.twitter = twitter;
    }

    public void setGooglePlus(String googlePlus)
    {
        this.googlePlus = googlePlus;
    }

    public String getPhone()
    {
        return this.phone;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getFacebook()
    {
        return this.facebook;
    }

    public String getTwitter()
    {
        return this.twitter;
    }

    public String getGooglePlus()
    {
        return this.googlePlus;
    }
}
