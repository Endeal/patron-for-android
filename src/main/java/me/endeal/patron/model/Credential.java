package me.endeal.patron.model;

import java.io.Serializable;

public class Credential implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String identifier;
    private String verifier;
    private String provider;

    public Credential(String identifier, String verifier, String provider)
    {
        setIdentifier(identifier);
        setVerifier(verifier);
        setProvider(provider);
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public void setVerifier(String verifier)
    {
        this.verifier = verifier;
    }

    public void setProvider(String provider)
    {
        this.provider = provider;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    public String getVerifier()
    {
        return this.verifier;
    }

    public String getProvider()
    {
        return this.provider;
    }
}
