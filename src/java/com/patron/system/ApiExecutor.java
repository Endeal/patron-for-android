package com.patron.system;

import android.os.NetworkOnMainThreadException;

import com.patron.listeners.OnTaskCompletedListener;
import com.patron.lists.ListLinks;
import com.patron.model.BankAccount;
import com.patron.model.Card;
import com.patron.model.Code;
import com.patron.model.Item;
import com.patron.model.Order;
import com.patron.model.User;
import com.patron.model.Vendor;
import com.patron.system.ApiTask;
import com.patron.system.Globals;
import com.patron.system.Parser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiExecutor
{
    private OnTaskCompletedListener onTaskCompletedListener;
    private ApiTask apiTask;
    private Map<URI, byte[]> data;

    public ApiExecutor()
    {
        setOnTaskCompletedListener(null);
        setApiTask(new ApiTask());
        setData(null);
    }

    public ApiExecutor(OnTaskCompletedListener onTaskCompletedListener)
    {
        setOnTaskCompletedListener(onTaskCompletedListener);
        setApiTask(new ApiTask());
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

    private void setData(Map<URI, byte[]> data)
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
        final String finalPassword = password;
        HttpPost request = new HttpPost(ListLinks.API_LOGIN_PATRON);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair pairEmail = new BasicNameValuePair("email", email);
        NameValuePair pairPassword = new BasicNameValuePair("password", password);
        pairs.add(pairEmail);
        pairs.add(pairPassword);
        try
        {
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            ApiTask apiTask = new ApiTask();
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                    try
                    {
                        for (Map.Entry<URI, byte[]> entry : data.entrySet())
                        {
                            String rawUri = entry.getKey().toString();
                            String rawUser = new String(entry.getValue());
                            if (rawUri.equals(ListLinks.API_LOGIN_PATRON))
                            {
                                User user = Parser.getUser(new JSONObject(rawUser));
                                user.setPassword(finalPassword);
                                Globals.setUser(user);
                            }
                        }
                    }
                    catch (NetworkOnMainThreadException e)
                    {
                        e.printStackTrace();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    callback();
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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

    public void createAccount(String firstName, String lastName, String email, String password, String birthday)
    {
            HttpPost request = new HttpPost(ListLinks.API_ADD_ACCOUNT);
            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
			NameValuePair pairFirstName = new BasicNameValuePair("firstName", firstName);
			NameValuePair pairLastName = new BasicNameValuePair("lastName", lastName);
			NameValuePair pairEmail = new BasicNameValuePair("email", email);
			NameValuePair pairPassword = new BasicNameValuePair("password", password);
			NameValuePair pairBirthday = new BasicNameValuePair("birthday", birthday);
			pairs.add(pairFirstName);
			pairs.add(pairLastName);
			pairs.add(pairEmail);
			pairs.add(pairPassword);
			pairs.add(pairBirthday);
            try
            {
                request.setEntity(new UrlEncodedFormEntity(pairs));
                ApiTask apiTask = new ApiTask();
                apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                    @Override
                    public void onComplete(Map<URI, byte[]> data)
                    {
                        for (Map.Entry<URI, byte[]> entry : data.entrySet())
                        {
                            String response = new String(entry.getValue());
                        }
                    }
                });
            }
            catch (Exception e)
            {
            }
    }

    // Vendor Interaction
    public void getVendors()
    {
        HttpGet request = new HttpGet(ListLinks.API_GET_VENDORS);
        ApiTask apiTask = new ApiTask();
        apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
            @Override
            public void onComplete(Map<URI, byte[]> data)
            {
                try
                {
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        String rawVendors =  new String(entry.getValue());
                        if (rawUri.equals(ListLinks.API_GET_VENDORS))
                        {
                            List<Vendor> vendors = Parser.getVendors(new JSONArray(rawVendors));
                            Globals.setVendors(vendors);
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
        apiTask.execute(request);
        callback();
    }

    public void findNearestVendor()
    {
        callback();
    }

    public void getItems(Vendor vendor)
    {
        HttpPost request = new HttpPost(ListLinks.API_GET_ITEMS);
        User user = Globals.getUser();
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", user.getEmail());
        NameValuePair password = new BasicNameValuePair("password", user.getPassword());
        pairs.add(email);
        pairs.add(password);
        try
        {
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            ApiTask apiTask = new ApiTask();
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                    try
                    {
                        for (Map.Entry<URI, byte[]> entry : data.entrySet())
                        {
                            String rawUri = entry.getKey().toString();
                            String rawItems = new String(entry.getValue());
                            if (rawUri.equals(ListLinks.API_GET_ITEMS))
                            {
                                List<Item> items = Parser.getItems(new JSONArray(rawItems));
                                Globals.getVendor().setItems(items);
                                Globals.getVendor().setFilteredItems(items);
                            }
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
