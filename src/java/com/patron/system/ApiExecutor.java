package com.patron.system;

import com.patron.listeners.OnTaskCompletedListener;
import com.patron.lists.ListLinks;
import com.patron.model.BankAccount;
import com.patron.model.Card;
import com.patron.model.Code;
import com.patron.model.Order;
import com.patron.model.User;
import com.patron.model.Vendor;
import com.patron.system.ApiTask;
import com.patron.system.Globals;
import com.patron.system.Parser;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;

public class ApiExecutor
{
    private OnTaskCompletedListener onTaskCompletedListener;
    private ApiTask apiTask;
    private Map<URI, HttpEntity> data;

    public ApiExecutor()
    {
        setOnTaskCompletedListener(null);
        setApiTask(new ApiTask(null));
        setData(null);
    }

    public ApiExecutor(OnTaskCompletedListener onTaskCompletedListener)
    {
        setOnTaskCompletedListener(onTaskCompletedListener);
        setApiTask(new ApiTask(null));
        setData(null);
    }

    private void setOnTaskCompletedListener(OnTaskCompletedListener onTaskCompletedListener)
    {
        this.onTaskCompletedListener = onTaskCompletedListener;
    }

    private void setApiTask(ApiTask apiTask)
    {
        this.apiTask = apiTask;
    }

    private void setData(Map<URI, HttpEntity> data)
    {
        this.data = data;
    }

    private void callback()
    {
        if (onTaskCompletedListener != null)
        {
            onTaskCompletedListener.onComplete(data);
        }
    }

    // Login
    public void loginPatron(String email, String password)
    {
        HttpPost request = new HttpPost(ListLinks.API_LOGIN_PATRON);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair pairEmail = new BasicNameValuePair("email", email);
        NameValuePair pairPassword = new BasicNameValuePair("password", password);
        pairs.add(pairEmail);
        pairs.add(pairPassword);
        request.setEntity(new URLEncodedFormEntity(pairs, "UTF-8"));
        apiTask(new OnTaskCompletedListener() {
                public void onComplete(Map<URI, HttpEntity> data)
                {
                    for (Map.Entry<URI, HttpEntity> entry : map.entrySet())
                    {
                        String rawUri = EntityUtils.getString(entry.getKey());
                        String rawUser = EntityUtils.getString(entry.getValue()));
                        if (rawUri.equals(ListLinks.API_LOGIN_PATRON))
                        {
                            User user = Parser.getUser(rawUser);
                            user.setPassword(password);
                            Globals.setUser(user);
                        }
                    }
                }
        });
        apiTask.execute(request);
        callback();
    }

    public void loginFacebook()
    {
        callback();
    }

    public void loginTwitter()
    {
        callback();
    }

    public void loginGooglePlus()
    {
        callback();
    }

    // Vendor Interaction
    public void getVendors()
    {
        callback();
    }

    public void findNearestVendor()
    {
        callback();
    }

    public void getItems(Vendor vendor)
    {
        callback();
    }

    public void getCodes()
    {
        callback();
    }

    public void getScan(Code code)
    {
        callback();
    }

    public void addOrder(Order order)
    {
        callback();
    }

    // Profile
    public void createAccount(Map<String, String> data)
    {
        callback();
    }

    public void addCard(Card card)
    {
        callback();
    }

    public void removeCard(Map<String, String> data)
    {
        callback();
    }

    public void addBankAccount(Map<String, String> data)
    {
        callback();
    }

    public void removeBankAccount(BankAccount bankAccount)
    {
        callback();
    }

    public void updateProfile(Map<String, String> data)
    {
        callback();
    }
}
