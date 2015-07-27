package me.endeal.patron.model;

public class ApiResult
{
    private int statusCode;
    private String message;
    private String data;

    public ApiResult(int statusCode, String message, String data)
    {
        setStatusCode(statusCode);
        setMessage(message);
        setData(data);
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public int getStatusCode()
    {
        return this.statusCode;
    }

    public String getMessage()
    {
        return this.message;
    }

    public String getData()
    {
        return this.data;
    }
}
