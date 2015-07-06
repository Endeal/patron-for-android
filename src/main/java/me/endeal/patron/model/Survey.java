package me.endeal.patron.model;

import java.io.Serializable;

public class Survey implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Query query;
    private boolean acknowledgment;
    private int rating;
    private String reply;

    public Survey(Query query, boolean acknowledgment, int rating, String reply)
    {
        setQuery(query);
        setAcknowledgment(acknowledgment);
        setRating(rating);
        setReply(reply);
    }

    public void setQuery(Query query)
    {
        this.query = query;
    }

    public void setAcknowledgment(boolean acknowledgment)
    {
        this.acknowledgment = acknowledgment;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public void setReply(String reply)
    {
        this.reply = reply;
    }

    public Query getQuery()
    {
        return this.query;
    }

    public boolean getAcknowledgment()
    {
        return this.acknowledgment;
    }

    public int getRating()
    {
        return this.rating;
    }

    public String getReply()
    {
        return this.reply;
    }
}
