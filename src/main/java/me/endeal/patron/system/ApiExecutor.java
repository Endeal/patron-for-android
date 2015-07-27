package me.endeal.patron.system;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import com.balancedpayments.android.Balanced;
import com.balancedpayments.android.exception.*;

import com.stripe.android.*;
import com.stripe.android.model.Token;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.listeners.OnTaskCompletedListener;
import me.endeal.patron.listeners.UserLocationListener;
import me.endeal.patron.lists.ListLinks;
import me.endeal.patron.lists.ListKeys;
import me.endeal.patron.activity.LoginActivity;
import me.endeal.patron.model.*;
import me.endeal.patron.R;
import me.endeal.patron.system.ApiTask;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.PatronApplication;
import me.endeal.patron.view.QustomDialogBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.NullPointerException;
import java.lang.Math;
import java.lang.Runnable;
import java.lang.Thread;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stripe.exception.AuthenticationException;

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
    private final Gson gson = new Gson();

    public ApiExecutor()
    {
        setApiTask(new ApiTask());
        setData(null);
    }

    private void setMockData(ApiTask apiTask, String data, String link)
    {
        // Mock data for offline testing.
        if (!PatronApplication.DEBUGGING_OFFLINE)
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

    public void callback(ApiResult result, OnApiExecutedListener... listeners)
    {
        if (listeners == null || listeners.length == 0)
        {
            return;
        }
        for (OnApiExecutedListener listener : listeners)
        {
          if (listener != null)
          {
            listener.onExecuted(result);
          }
        }
    }

    // Login
    public void login(final Credential credential, final OnApiExecutedListener... listeners)
    {
        HttpPost request = new HttpPost(ListLinks.API_LOGIN_PATRON);
        Globals.setCredential(credential);
        ApiTask apiTask = new ApiTask();
        apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
            @Override
            public void onComplete(Map<URI, byte[]> data)
            {
                String rawResponse = "";
                try
                {
                    if (data == null || data.entrySet() == null)
                    {
                        callback(new ApiResult(408, "Network error, check your internet connection", null), listeners);
                        return;
                    }
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        String rawData = new String(entry.getValue());
                        JSONObject json = new JSONObject(rawData);
                        if (rawUri.equals(ListLinks.API_LOGIN_PATRON))
                        {
                            if (json.getString("statusCode").equals("200"))
                            {
                                Patron user = gson.fromJson(json.getString("data"), Patron.class);
                                Globals.setPatron(user);
                            }
                        }
                        callback(new ApiResult(200, "Sucessfully logged in", json.getString("data")), listeners);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    callback(new ApiResult(503, "Bad response, try again laster", null), listeners);
                }
                catch (NetworkOnMainThreadException e)
                {
                    e.printStackTrace();
                    callback(new ApiResult(400, "Network request failed on main thread", null), listeners);
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                    callback(new ApiResult(503, "Bad response, try again laster", null), listeners);
                }
            }
        });
        String data = "{'patronId':'1','firstName':'James','lastName':'Whiteman','email':'jameswhiteman@outlook.com','birthday':'1993-09-14'," +
            "'balancedId':'1','facebookId':'1','twitterId':'1','googlePlusId':'1','cards':{'cards':[]},'bankAccounts':{'bank_accounts':[]}}";
        setMockData(apiTask, data, ListLinks.API_LOGIN_PATRON);
        apiTask.setMocking(PatronApplication.DEBUGGING_OFFLINE);
        apiTask.execute(request);
    }

    public void createAccount(final Patron patron, final OnApiExecutedListener... listeners)
    {
        HttpPost request = new HttpPost(ListLinks.API_ADD_ACCOUNT);
        final String postData = gson.toJson(patron);
        System.out.println(postData);
        try
        {
            request.setEntity(new StringEntity(postData));
            ApiTask apiTask = new ApiTask();
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                    if (data == null || data.entrySet() == null)
                    {
                        callback(new ApiResult(408, "Network error, check your internet connection", null), listeners);
                        return;
                    }
                    try
                    {
                        for (Map.Entry<URI, byte[]> entry : data.entrySet())
                        {
                            String response = new String(entry.getValue());
                            JSONObject json = new JSONObject(response);
                            if (!json.getString("statusCode").equals("201"))
                            {
                                callback(new ApiResult(Integer.parseInt(json.getString("statusCode")), json.getString("message"), null), listeners);
                                return;
                            }
                            patron.setId(json.getString("data"));
                            Globals.setCredential(patron.getIdentity().getCredentials().get(0));
                            Globals.setPatron(patron);
                            callback(new ApiResult(201, "Successfully created account", response), listeners);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        callback(new ApiResult(503, "Bad response, try again later", null), listeners);
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            callback(new ApiResult(400, "Failed to create request", null), listeners);
        }
    }

    public void resetPassword(Credential credential, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_RESET_PASSWORD;
        Globals.setCredential(credential);
        HttpPost request = new HttpPost(url);
        ApiTask apiTask = new ApiTask();
        apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
            @Override
            public void onComplete(Map<URI, byte[]> data)
            {
                if (data == null || data.entrySet() == null)
                {
                    callback(new ApiResult(408, "Network error, check your internet connection", null), listeners);
                    return;
                }
                for (Map.Entry<URI, byte[]> entry : data.entrySet())
                {
                    String rawUri = entry.getKey().toString();
                    if (rawUri.equals(url))
                    {
                        try
                        {
                            String response = new String(entry.getValue());
                            JSONObject json = new JSONObject(response);
                            if (json.getString("statusCode").equals("200"))
                            {
                                Toast.makeText(PatronApplication.getContext(), "Password successfully reset.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(PatronApplication.getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            callback(new ApiResult(Integer.parseInt(json.getString("statusCode")), json.getString("message"), json.getString("data")), listeners);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            callback(new ApiResult(503, "Bad response, try again laster", null), listeners);
                        }
                    }
                }
            }
        });
        apiTask.execute(request);
    }

    // Vendor Interaction
    public void getVendors(final OnApiExecutedListener... listeners)
    {
        HttpGet request = new HttpGet(ListLinks.API_GET_VENDORS);
        ApiTask apiTask = new ApiTask();
        apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
            @Override
            public void onComplete(Map<URI, byte[]> data)
            {
                if (data == null || data.entrySet() == null)
                {
                    callback(new ApiResult(408, "Network error, check your internet connection", null), listeners);
                    return;
                }
                try
                {
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        String rawResponse =  new String(entry.getValue());
                        if (rawUri.equals(ListLinks.API_GET_VENDORS))
                        {
                            JSONObject json = new JSONObject(rawResponse);
                            List<Vendor> vendors = new ArrayList<Vendor>();
                            JSONArray rawVendors = json.getJSONArray("data");
                            for (int i = 0; i < rawVendors.length(); i++)
                            {
                                JSONObject rawVendor = rawVendors.getJSONObject(i);
                                Vendor vendor = gson.fromJson(rawVendor.toString(), Vendor.class);
                                vendors.add(vendor);
                            }
                            Globals.setVendors(vendors);
                            callback(new ApiResult(200, "Successfully retrieved vendors", rawVendors.toString()), listeners);
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    callback(new ApiResult(503, "Bad response, please try again", null), listeners);
                }
            }
        });
        apiTask.execute(request);
    }

    public void selectNearestVendor(final Context context, final OnApiExecutedListener... listeners)
    {
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
                        callback(new ApiResult(400, "Both GPS and Network are disabled", null), listeners);
                    }
                    else
                    {
                        long minTime = (long)1;
                        float minDistance = (float)1;
                        boolean canGetLocation = true;
                        android.location.Location location = null;
                        UserLocationListener listener = new UserLocationListener(context, executor, listeners);
                        if (isGPSEnabled)
                        {
                          locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null);
                          location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                        else if (isNetworkEnabled)
                        {
                          locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
                          location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                        selectNearestLocation(location, context);

                        // Confirm the user wants to use this vendor.
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                              switch (which)
                              {
                                case DialogInterface.BUTTON_POSITIVE:
                                  dialog.dismiss();
                                  callback(new ApiResult(200, "Vendor found and selected", null), listeners);
                                  break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                  Globals.setVendor(null);
                                  dialog.dismiss();
                                  callback(new ApiResult(202, "Vendor found but not selected", null), listeners);
                                  break;
                              }
                            }
                        };
                        if (Globals.getVendor() != null)
                        {
                            AlertDialog alert = new AlertDialog.Builder(context)
                                .setMessage("Are you at " + Globals.getVendor().getName() + "?")
                                .setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener)
                                .create();
                            alert.setCancelable(false);
                            alert.setCanceledOnTouchOutside(false);
                            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface)
                                {
                                    callback(new ApiResult(202, "Dismissed vendor auto-selection", null), listeners);
                                }
                            });
                            alert.show();
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    callback(new ApiResult(500, "Failed to locate nearest vendor", null), listeners);
                }
            }
        };
        getVendors(new OnApiExecutedListener() {
            @Override
            public void onExecuted(ApiResult result)
            {
                runnable.run();
            }
        });
    }

    private void selectNearestLocation(android.location.Location location, Context context)
    {
      if (location != null)
      {
          double latitude = location.getLatitude();
          double longitude = location.getLongitude();
          System.out.println("POSITION:" + latitude + " " + longitude);
          int closest = 0;
          final List<Vendor> vendors = Globals.getVendors();
          for (int i = 0; i < vendors.size(); i++)
          {
              Vendor vendor = vendors.get(i);
              double latDiff = Math.abs(vendor.getLocation().getLatitude() - latitude);
              double longDiff = Math.abs(vendor.getLocation().getLongitude() - longitude);
              double newDistance = latDiff + longDiff;
              Vendor closestVendor = vendors.get(closest);
              latDiff = Math.abs(closestVendor.getLocation().getLatitude() - latitude);
              longDiff = Math.abs(closestVendor.getLocation().getLongitude() - longitude);
              double oldDistance = latDiff + longDiff;
              if (newDistance < oldDistance)
              {
                closest = i;
              }
          }
          Globals.setVendor(vendors.get(closest));
      }
      else
      {
        Toast.makeText(context, "GPS/Network positioning failed.", Toast.LENGTH_SHORT).show();
      }
    }

    public void getItems(final OnApiExecutedListener... listeners)
    {
        HttpGet request = new HttpGet(ListLinks.API_GET_ITEMS);
        ApiTask apiTask = new ApiTask();
        apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
            @Override
            public void onComplete(Map<URI, byte[]> data)
            {
                if (data == null || data.entrySet() == null)
                {
                    callback(new ApiResult(408, "Network error, check your internet connection", null), listeners);
                    return;
                }
                try
                {
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        String response = new String(entry.getValue());
                        JSONObject json = new JSONObject(response);
                        if (json.getString("statusCode").equals("200"))
                        {
                            JSONArray rawItems = json.getJSONArray("data");
                            List<Item> items = new ArrayList<Item>();
                            if (rawUri.equals(ListLinks.API_GET_ITEMS))
                            {
                                for (int i = 0; i < rawItems.length(); i++)
                                {
                                    JSONObject rawItem = rawItems.getJSONObject(i);
                                    Item item = gson.fromJson(rawItem.toString(), Item.class);
                                    items.add(item);
                                }
                                Globals.getVendor().setItems(items);
                                Globals.getVendor().setFilteredItems(items);
                            }
                            callback(new ApiResult(200, "Successfully retrieved items", null), listeners);
                        }
                        else
                        {
                            callback(new ApiResult(Integer.parseInt(json.getString("statusCode")), json.getString("message"), null), listeners);
                        }
                    }
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                    callback(new ApiResult(503, "Bad response, try again later", null), listeners);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    callback(new ApiResult(503, "Bad response, try again later", null), listeners);
                }
            }
        });
        apiTask.execute(request);
    }

    public void getOrders(final OnApiExecutedListener... listeners)
    {
        HttpGet request = new HttpGet(ListLinks.API_GET_ORDERS);
        ApiTask apiTask = new ApiTask();
        apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
            @Override
            public void onComplete(Map<URI, byte[]> data)
            {
                if (data == null || data.entrySet() == null)
                {
                    callback(new ApiResult(408, "Network error, check your internet connection", null), listeners);
                    return;
                }
                try
                {
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        String response = new String(entry.getValue());
                        JSONObject json = new JSONObject(response);
                        if (json.getString("statusCode").equals("200"))
                        {
                            JSONArray rawOrders = json.getJSONArray("data");
                            List<Order> orders = new ArrayList<Order>();
                            if (rawUri.equals(ListLinks.API_GET_ORDERS))
                            {
                                for (int i = 0; i < rawOrders.length(); i++)
                                {
                                    JSONObject rawOrder = rawOrders.getJSONObject(i);
                                    Order order = gson.fromJson(rawOrder.toString(), Order.class);
                                    orders.add(order);
                                    System.out.println("Added order");
                                }
                            }
                            Globals.setOrders(orders);
                            callback(new ApiResult(200, "Successfully retrieved orders", null), listeners);
                        }
                        else
                        {
                            callback(new ApiResult(Integer.parseInt(json.getString("statusCode")), json.getString("message"), null), listeners);
                        }
                    }
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                    callback(new ApiResult(503, "Bad response, try again later", null), listeners);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    callback(new ApiResult(503, "Bad response, try again later", null), listeners);
                }
            }
        });
        apiTask.execute(request);
    }

    public void getScan(Order order, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_GET_SCAN;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", "jameswhiteman@outlook.com");//Globals.getUser().getEmail());
        NameValuePair password = new BasicNameValuePair("password", "froggy");//Globals.getUser().getPassword());
        NameValuePair provider = new BasicNameValuePair("oauth", "endeal");//Globals.getUser().getProvider());
        NameValuePair orderId = new BasicNameValuePair("orderId", order.getId());
        pairs.add(email);
        pairs.add(password);
        pairs.add(provider);
        pairs.add(orderId);
        ApiTask apiTask = new ApiTask();
        try
        {
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                    if (data == null || data.entrySet() == null)
                    {
                        callback(new ApiResult(408, "Network error, check your internet connection", null), listeners);
                        return;
                    }
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        if (rawUri.equals(url))
                        {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(entry.getValue(), 0, entry.getValue().length);
                            if (bitmap == null)
                            {
                                Toast.makeText(PatronApplication.getContext(), "Failed to retrieve QR code", Toast.LENGTH_SHORT).show();
                            }
                            Globals.setScan(bitmap);
                        }
                        callback(null, listeners);
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
            Toast.makeText(PatronApplication.getContext(), "Failed to encode request.", Toast.LENGTH_SHORT).show();
            callback(null, listeners);
        }
    }

    public void addOrder(final Order order, final OnApiExecutedListener... listeners)
    {
        HttpPost request = new HttpPost(ListLinks.API_ADD_ORDER);
        String postData = gson.toJson(order);
        System.out.println(postData);
        try
        {
            request.setEntity(new StringEntity(postData));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            callback(new ApiResult(503, "This order cannot be encoded", null), listeners);
            return;
        }
        ApiTask apiTask = new ApiTask();
        apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
            @Override
            public void onComplete(Map<URI, byte[]> data)
            {
                if (data == null || data.entrySet() == null)
                {
                    callback(new ApiResult(408, "Network error, check your internet connection", null), listeners);
                    return;
                }
                try
                {
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        String response = new String(entry.getValue());
                        JSONObject json = new JSONObject(response);
                        if (rawUri.equals(ListLinks.API_ADD_ORDER))
                        {
                            if (json.getString("statusCode").equals("201"))
                            {
                                Globals.setOrder(null);
                                callback(new ApiResult(201, "Successfully placed order", json.getString("data")), listeners);
                            }
                            else
                            {
                                callback(new ApiResult(Integer.parseInt(json.getString("statusCode")), json.getString("message"), null), listeners);
                            }
                        }
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    callback(new ApiResult(503, "Bad response, try again later", null), listeners);
                }
            }
        });
        apiTask.execute(request);
    }

    public void updatePatron(final Patron patron, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_UPDATE_ACCOUNT + patron.getId();
        HttpPut request = new HttpPut(url);
        final String postData = gson.toJson(patron);
        ApiTask apiTask = new ApiTask();
        try
        {
            request.setEntity(new StringEntity(postData));
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                    if (data == null || data.entrySet() == null)
                    {
                        callback(new ApiResult(408, "Network error, check your internet connection", null), listeners);
                        return;
                    }
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        String rawUser = new String(entry.getValue());
                        if (rawUri.equals(url))
                        {
                            try
                            {
                                JSONObject json = new JSONObject(rawUser);
                                if (json.getString("statusCode").equals("200"))
                                {
                                    Patron user = gson.fromJson(json.getString("data"), Patron.class);
                                    Globals.setPatron(user);
                                    Globals.setCredential(user.getIdentity().getCredentials().get(0));
                                }
                                callback(new ApiResult(Integer.parseInt(json.getString("statusCode")),
                                                "Successfully updated patron", json.getString("data")), listeners);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                                System.out.println("url:" + url);
                                System.out.println("response:" + rawUser);
                                System.out.println("post data:" + postData);
                                callback(new ApiResult(503, "Bad response, try again later", null), listeners);
                            }
                            catch (NetworkOnMainThreadException e)
                            {
                                e.printStackTrace();
                                callback(new ApiResult(400, "Network request failed on main thread", null), listeners);
                            }
                            catch (NullPointerException e)
                            {
                                e.printStackTrace();
                                callback(new ApiResult(503, "Null response, try again later", null), listeners);
                            }
                        }
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
          e.printStackTrace();
          callback(new ApiResult(400, "Failed to encode malformed request", null), listeners);
        }
    }

    public void addFunder(final String tokenId, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_ADD_FUNDER;
        HttpPost request = new HttpPost(url);
        ApiTask apiTask = new ApiTask();
        request.addHeader("x-stripe-token", tokenId);
        apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
            @Override
            public void onComplete(Map<URI, byte[]> data)
            {
                if (data == null || data.entrySet() == null)
                {
                    callback(new ApiResult(408, "Network error, check your internet connection", null), listeners);
                    return;
                }
                for (Map.Entry<URI, byte[]> entry : data.entrySet())
                {
                    String rawUri = entry.getKey().toString();
                    String rawUser = new String(entry.getValue());
                    if (rawUri.equals(url))
                    {
                        try
                        {
                            JSONObject json = new JSONObject(rawUser);
                            if (json.getString("statusCode").equals("201"))
                            {
                                Patron user = gson.fromJson(json.getString("data"), Patron.class);
                                Globals.setPatron(user);
                            }
                            callback(new ApiResult(Integer.parseInt(json.getString("statusCode")),
                                            "Successfully added funder to user", json.getString("data")), listeners);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            callback(new ApiResult(503, "Bad response, try again later", null), listeners);
                        }
                        catch (NetworkOnMainThreadException e)
                        {
                            e.printStackTrace();
                            callback(new ApiResult(400, "Network request failed on main thread", null), listeners);
                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();
                            callback(new ApiResult(503, "Null response, try again later", null), listeners);
                        }
                    }
                }
            }
        });
        apiTask.execute(request);
    }

    public void removeFunder(final Funder funder, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_REMOVE_FUNDER + Globals.getPatron().getId();
        System.out.println("url:" + url);
        HttpDelete request = new HttpDelete(url);
        request.addHeader("x-stripe-card", funder.getId());
        System.out.println("funder-id:" + funder.getId());
        ApiTask apiTask = new ApiTask();
        apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
            @Override
            public void onComplete(Map<URI, byte[]> data)
            {
                if (data == null || data.entrySet() == null)
                {
                    callback(new ApiResult(408, "Network error, check your internet connection", null), listeners);
                    return;
                }
                for (Map.Entry<URI, byte[]> entry : data.entrySet())
                {
                    String rawUri = entry.getKey().toString();
                    String rawUser = new String(entry.getValue());
                    if (rawUri.equals(url))
                    {
                        try
                        {
                            JSONObject json = new JSONObject(rawUser);
                            if (json.getString("statusCode").equals("200"))
                            {
                                Patron user = gson.fromJson(json.getString("data"), Patron.class);
                                Globals.setPatron(user);
                            }
                            callback(new ApiResult(Integer.parseInt(json.getString("statusCode")),
                                            "Successfully removed funder to user", json.getString("data")), listeners);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            callback(new ApiResult(503, "Bad response, try again later", null), listeners);
                        }
                        catch (NetworkOnMainThreadException e)
                        {
                            e.printStackTrace();
                            callback(new ApiResult(400, "Network request failed on main thread", null), listeners);
                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();
                            callback(new ApiResult(503, "Null response, try again later", null), listeners);
                        }
                    }
                }
            }
        });
        apiTask.execute(request);
    }
}
