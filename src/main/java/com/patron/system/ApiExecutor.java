package com.patron.system;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import com.patron.listeners.OnApiExecutedListener;
import com.patron.listeners.OnTaskCompletedListener;
import com.patron.listeners.UserLocationListener;
import com.patron.lists.ListLinks;
import com.patron.model.BankAccount;
import com.patron.model.Card;
import com.patron.model.Code;
import com.patron.model.Funder;
import static com.patron.model.Funder.Type;
import com.patron.model.Item;
import com.patron.model.Order;
import com.patron.model.User;
import com.patron.model.Vendor;
import com.patron.system.ApiTask;
import com.patron.system.Globals;
import com.patron.system.Parser;
import com.patron.system.Patron;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.NullPointerException;
import java.lang.Runnable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiExecutor
{
    private ApiTask apiTask;
    private Map<URI, byte[]> data;

    public ApiExecutor()
    {
        setApiTask(new ApiTask());
        setData(null);
    }

    private void setMockData(ApiTask apiTask, String data, String link)
    {
        // Mock data for offline testing.
        if (!Patron.DEBUGGING_OFFLINE)
        {
            return;
        }
        try
        {
            apiTask.setMocking(true);
            Map<URI, byte[]> mockData = new HashMap<URI, byte[]>();
            URI uri = new URI(link);
            byte[] bytes = data.getBytes("UTF-8");
            mockData.put(uri, bytes);
            apiTask.setMockData(mockData);
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    private void setApiTask(ApiTask apiTask)
    {
        this.apiTask = apiTask;
    }

    private void setData(Map<URI, byte[]> data)
    {
        this.data = data;
    }

    public void callback(OnApiExecutedListener... listeners)
    {
        if (listeners == null || listeners.length == 0)
        {
            return;
        }
        for (OnApiExecutedListener listener : listeners)
        {
            listener.onExecuted();
        }
    }

    // Login
    public void loginPatron(String email, String password, OnApiExecutedListener... tempListeners)
    {
        final String finalEmail = email;
        final String finalPassword = password;
        final OnApiExecutedListener[] listeners = tempListeners;
        HttpPost request = new HttpPost(ListLinks.API_LOGIN_PATRON);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair pairEmail = new BasicNameValuePair("email", email);
        NameValuePair pairPassword = new BasicNameValuePair("password", password);
        pairs.add(pairEmail);
        pairs.add(pairPassword);
        System.out.println("login1");
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
                      System.out.println("login3");
                        for (Map.Entry<URI, byte[]> entry : data.entrySet())
                        {
                            System.out.println("login4");
                            String rawUri = entry.getKey().toString();
                            String rawUser = new String(entry.getValue());
                            if (rawUri.equals(ListLinks.API_LOGIN_PATRON))
                            {
                                System.out.println("login5");
                                User user = Parser.getUser(new JSONObject(rawUser));
                                user.setEmail(finalEmail);
                                user.setPassword(finalPassword);
                                Globals.setUser(user);
                                System.out.println("login6");
                            }
                        }
                    }
                    catch (NetworkOnMainThreadException e)
                    {
                        e.printStackTrace();
                        System.out.println("login7");
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        System.out.println("login8");
                    }
                    catch (NullPointerException e)
                    {
                        System.out.println("login9");
                        Toast.makeText(Patron.getContext(), "Failed to login.", Toast.LENGTH_SHORT).show();
                        System.out.println("login10");
                    }
                    callback(listeners);
                }
            });
            String data = "{'patronId':'1','firstName':'James','lastName':'Whiteman','email':'jameswhiteman@outlook.com','birthday':'1993-09-14'," +
                "'balancedId':'1','facebookId':'1','twitterId':'1','googlePlusId':'1','cards':{'cards':[]},'bankAccounts':{'bank_accounts':[]}}";
            setMockData(apiTask, data, ListLinks.API_LOGIN_PATRON);
            apiTask.setMocking(Patron.DEBUGGING_OFFLINE);
            System.out.println("login11");
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            System.out.println("login12");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("login13");
        }
        System.out.println("login14");
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

    public void createAccount(String firstName, String lastName, String email, String password, String birthday,
            OnApiExecutedListener... tempListeners)
    {
            final OnApiExecutedListener[] listeners = tempListeners;
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
                        callback(listeners);
                    }
                });
            }
            catch (Exception e)
            {
            }
    }

    // Vendor Interaction
    public void getVendors(OnApiExecutedListener... tempListeners)
    {
        final OnApiExecutedListener[] listeners = tempListeners;
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
                    callback(listeners);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
        apiTask.execute(request);
    }

    public void selectNearestVendor(Context tempContext, OnApiExecutedListener... tempListeners)
    {
        final Context context = tempContext;
        final OnApiExecutedListener[] listeners = tempListeners;
        final ApiExecutor executor = this;
        final Runnable runnable = new Runnable() {
            public void run()
            {
                try
                {
                    LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
                    boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    if (isGPSEnabled == false && isNetworkEnabled == false)
                    {
                        // no network provider is enabled
                        callback(listeners);
                    }
                    else
                    {
                        long minTime = (long)1;
                        float minDistance = (float)1;
                        boolean canGetLocation = true;
                        Location location;
                        UserLocationListener listener = new UserLocationListener(context, executor, listeners);
                        if (isGPSEnabled)
                        {
                            location = null;
                            if (location == null)
                            {
                                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
                                if (locationManager != null)
                                {
                                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null)
                                    {
                                        double latitude = location.getLatitude();
                                        double longitude = location.getLongitude();
                                        Toast.makeText(context, "lat:" + latitude + "\nlong:" + longitude,
                                            Toast.LENGTH_SHORT).show();
                                        System.out.println("POSITION:" + latitude + " " + longitude);
                                        int closest = 0;
                                        List<Vendor> vendors = Globals.getVendors();
                                        for (int i = 0; i < vendors.size(); i++)
                                        {
                                            closest = i;
                                        }
                                        Globals.setVendor(vendors.get(closest));
                                        callback(listeners);
                                    }
                                }
                            }
                        }
                        else if (isNetworkEnabled)
                        {
                            location = null;
                            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null);
                            if (locationManager != null)
                            {
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null)
                                {
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();
                                    Toast.makeText(context, "lat:" + latitude + "\nlong:" + longitude,
                                        Toast.LENGTH_SHORT).show();
                                    System.out.println("POSITION:" + latitude + " " + longitude);
                                    int closest = 0;
                                    List<Vendor> vendors = Globals.getVendors();
                                    for (int i = 0; i < vendors.size(); i++)
                                    {
                                        closest = i;
                                    }
                                    Globals.setVendor(vendors.get(closest));
                                    callback(listeners);
                                }
                            }
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        getVendors(new OnApiExecutedListener() {
            @Override
            public void onExecuted()
            {
                runnable.run();
            }
        });
    }

    public void getItems(String vendorId, OnApiExecutedListener... tempListeners)
    {
        final OnApiExecutedListener[] listeners = tempListeners;
        HttpPost request = new HttpPost(ListLinks.API_GET_ITEMS);
        User user = Globals.getUser();
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", user.getEmail());
        NameValuePair password = new BasicNameValuePair("password", user.getPassword());
        NameValuePair vendor = new BasicNameValuePair("vendorId", vendorId);
        pairs.add(email);
        pairs.add(password);
        pairs.add(vendor);
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
                            callback(listeners);
                        }
                    }
                    catch (NullPointerException e)
                    {
                        e.printStackTrace();
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

    public void addOrder(Order order, Context tempContext, OnApiExecutedListener... tempListeners)
    {
        final OnApiExecutedListener[] listeners = tempListeners;
        final Context context = tempContext;
        final Toast failureToast = Toast.makeText(context, "Failed to place order.", Toast.LENGTH_SHORT);
        try
        {
            HttpPut request = new HttpPut(ListLinks.API_ADD_ORDER);
            request.setHeader("Content-type", "application/json");
            request.setHeader("Accept", "application/json");
            Gson gson = new Gson();
            JsonElement jsonOrder = gson.toJsonTree(order);
            jsonOrder.getAsJsonObject().addProperty("email", Globals.getUser().getEmail());
            jsonOrder.getAsJsonObject().addProperty("password", Globals.getUser().getPassword());
            jsonOrder.getAsJsonObject().addProperty("deviceType", "1");
            String postData = gson.toJson(jsonOrder);
            request.setEntity(new StringEntity(postData));
            ApiTask apiTask = new ApiTask();
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                    System.out.println("ao1");
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        System.out.println("ao2");
                        String rawUri = entry.getKey().toString();
                        System.out.println("ao3");
                        String rawOrder = new String(entry.getValue());
                        System.out.println("ao4");
                        System.out.println("prepended string:" + rawOrder);
                        rawOrder = rawOrder.replaceAll("\\n", "");
                        System.out.println("ao5");
                        if (rawUri.equals(ListLinks.API_ADD_ORDER))
                        {
                            System.out.println("ao6");
                            if (rawOrder.equals("1"))
                            {
                                System.out.println("ao7");
                                Toast.makeText(context, "Successfully placed order.", Toast.LENGTH_SHORT).show();
                                System.out.println("ao8");
                                callback(listeners);
                                System.out.println("ao9");
                            }
                            else
                            {
                                System.out.println("ao10");
                                System.out.println(rawOrder);
                                System.out.println("ao11");
                                Toast.makeText(context, rawOrder, Toast.LENGTH_SHORT).show();
                                System.out.println("ao12");
                            }
                        }
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
            failureToast.show();
            e.printStackTrace();
        }
        catch (IOException e)
        {
            failureToast.show();
            e.printStackTrace();
        }
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

    public void removeFunder(Funder funder, final OnApiExecutedListener... listeners)
    {
        String link = ListLinks.API_REMOVE_CARD;
        String key = "card";
        if (funder.getFunderType() == Funder.Type.BankAccount)
        {
            link = ListLinks.API_REMOVE_BANK_ACCOUNT;
            key = "bankAccount";
        }
        final String url = link;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", Globals.getUser().getEmail());
        NameValuePair password = new BasicNameValuePair("password", Globals.getUser().getPassword());
        NameValuePair instrument = new BasicNameValuePair(key, funder.getHref());
        pairs.add(email);
        pairs.add(password);
        pairs.add(instrument);
        ApiTask apiTask = new ApiTask();
        try
        {
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        if (rawUri.equals(url))
                        {
                            String response = new String(entry.getValue());
                            if (response.equals("1"))
                            {
                                Toast.makeText(Patron.getContext(), "Removed funding instrument.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Patron.getContext(), "Failed to remove funding instrument.", Toast.LENGTH_SHORT).show();
                            }
                            loginPatron(Globals.getUser().getEmail(), Globals.getUser().getPassword(), listeners);
                        }
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {

        }
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

    public void addFavoriteVendor(Vendor vendor, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_ADD_FAVORITE_VENDOR;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", Globals.getUser().getEmail());
        NameValuePair password = new BasicNameValuePair("password", Globals.getUser().getPassword());
        NameValuePair vendorId = new BasicNameValuePair("vendorId", vendor.getId());
        pairs.add(email);
        pairs.add(password);
        pairs.add(vendorId);
        System.out.println("afv1");
        try
        {
            ApiTask apiTask = new ApiTask();
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            System.out.println("afv2");
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                    System.out.println("afvdone");
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        System.out.println("afv3");
                        if (rawUri.equals(url))
                        {
                            String response = new String(entry.getValue());
                            System.out.println("afv4");
                            if (response.equals("1"))
                            {
                                System.out.println("afv5");
                                Toast.makeText(Patron.getContext(), "Successfully added favorite vendor.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                System.out.println("afv6");
                                Toast.makeText(Patron.getContext(), "Failed to add favorite vendor.", Toast.LENGTH_SHORT).show();
                            }
                            loginPatron(Globals.getUser().getEmail(), Globals.getUser().getPassword(), listeners);
                        }
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            System.out.println("afv8");
        }
    }

    public void removeFavoriteVendor(Vendor vendor, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_REMOVE_FAVORITE_VENDOR;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", Globals.getUser().getEmail());
        NameValuePair password = new BasicNameValuePair("password", Globals.getUser().getPassword());
        NameValuePair vendorId = new BasicNameValuePair("vendorId", vendor.getId());
        pairs.add(email);
        pairs.add(password);
        pairs.add(vendorId);
        try
        {
            ApiTask apiTask = new ApiTask();
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        if (rawUri.equals(url))
                        {
                            String response = new String(entry.getValue());
                            if (response.equals("1"))
                            {
                                Toast.makeText(Patron.getContext(), "Successfully removed favorite vendor.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Patron.getContext(), "Failed to remove favorite vendor.", Toast.LENGTH_SHORT).show();
                            }
                            loginPatron(Globals.getUser().getEmail(), Globals.getUser().getPassword(), listeners);
                        }
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
}
