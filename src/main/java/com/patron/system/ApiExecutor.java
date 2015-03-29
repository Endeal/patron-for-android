package com.patron.system;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import com.appboy.Appboy;

import com.balancedpayments.android.Balanced;
import com.balancedpayments.android.exception.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import com.patron.listeners.OnApiExecutedListener;
import com.patron.listeners.OnTaskCompletedListener;
import com.patron.listeners.UserLocationListener;
import com.patron.lists.ListLinks;
import com.patron.main.FlashLogin;
import com.patron.model.BankAccount;
import com.patron.model.Card;
import com.patron.model.Code;
import com.patron.model.Funder;
import static com.patron.model.Funder.Type;
import com.patron.model.Item;
import com.patron.model.Order;
import com.patron.model.User;
import com.patron.model.Vendor;
import com.patron.R;
import com.patron.system.ApiTask;
import com.patron.system.Globals;
import com.patron.system.Parser;
import com.patron.system.Patron;
import com.patron.view.QustomDialogBuilder;

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
    public void login(String email, String password, String provider, final OnApiExecutedListener... listeners)
    {
        final String finalEmail = email;
        final String finalPassword = password;
        HttpPost request = new HttpPost(ListLinks.API_LOGIN_PATRON);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair pairEmail = new BasicNameValuePair("email", email);
        NameValuePair pairPassword = new BasicNameValuePair("password", password);
        NameValuePair pairOauth = new BasicNameValuePair("oauth", provider);
        pairs.add(pairEmail);
        pairs.add(pairPassword);
        pairs.add(pairOauth);
        try
        {
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
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
                        Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                        callback(listeners);
                        return;
                      }
                        for (Map.Entry<URI, byte[]> entry : data.entrySet())
                        {
                            String rawUri = entry.getKey().toString();
                            String rawUser = new String(entry.getValue());
                            rawResponse = rawUser;
                            if (rawUri.equals(ListLinks.API_LOGIN_PATRON))
                            {
                                User user = Parser.getUser(new JSONObject(rawUser));
                                user.setEmail(finalEmail);
                                user.setPassword(finalPassword);
                                Globals.setUser(user);
                            }
                        }
                    }
                    catch (NetworkOnMainThreadException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(Patron.getContext(), "Failed to login, please restart and try again", Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(Patron.getContext(), rawResponse, Toast.LENGTH_SHORT).show();
                    }
                    catch (NullPointerException e)
                    {
                        Toast.makeText(Patron.getContext(), "Failed to login", Toast.LENGTH_SHORT).show();
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
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
                      if (data == null || data.entrySet() == null)
                      {
                        Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                        callback(listeners);
                        return;
                      }
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

    public void resetPassword(String userEmail, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_RESET_PASSWORD;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", userEmail);
        pairs.add(email);
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
                    Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                    callback(listeners);
                    return;
                  }
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        if (rawUri.equals(url))
                        {
                            String response = new String(entry.getValue());
                            if (response.equals("1"))
                            {
                                Toast.makeText(Patron.getContext(), "Password successfully reset.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Patron.getContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        callback(listeners);
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
          Toast.makeText(Patron.getContext(), "Failed to encode request.", Toast.LENGTH_SHORT).show();
          e.printStackTrace();
          callback(listeners);
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
                  if (data == null || data.entrySet() == null)
                  {
                    Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                    callback(listeners);
                    return;
                  }
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
                        Location location = null;
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
                                  callback(listeners);
                                  break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                  Globals.setVendor(null);
                                  dialog.dismiss();
                                  callback(listeners);
                                  break;
                              }
                            }
                        };
                        if (Globals.getVendor() != null)
                        {
                            QustomDialogBuilder builder = new QustomDialogBuilder(context);
                            builder = builder.setMessage("Are you at " + Globals.getVendor().getName() + "?");
                            builder = builder.setMessageColor("#FFFFFF");
                            AlertDialog.Builder newBuilder = builder.setPositiveButton("Yes", dialogClickListener);
                            newBuilder = newBuilder.setNegativeButton("No", dialogClickListener);
                            newBuilder.show();
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

    private void selectNearestLocation(Location location, Context context)
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
              double latDiff = Math.abs(vendor.getLatitude() - latitude);
              double longDiff = Math.abs(vendor.getLongitude() - longitude);
              double newDistance = latDiff + longDiff;
              Vendor closestVendor = vendors.get(closest);
              latDiff = Math.abs(closestVendor.getLatitude() - latitude);
              longDiff = Math.abs(closestVendor.getLongitude() - longitude);
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
                  if (data == null || data.entrySet() == null)
                  {
                    Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                    callback(listeners);
                    return;
                  }
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
    }

    public void getCodes(final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_GET_CODES;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", Globals.getUser().getEmail());
        NameValuePair password = new BasicNameValuePair("password", Globals.getUser().getPassword());
        NameValuePair provider = new BasicNameValuePair("oauth", Globals.getUser().getProvider());
        pairs.add(email);
        pairs.add(password);
        pairs.add(provider);
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
                    Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                    callback(listeners);
                    return;
                  }
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        if (rawUri.equals(url))
                        {
                            String response = new String(entry.getValue());
                            try
                            {
                              JSONArray rawCodes = new JSONArray(response);
                              List<Code> codes = Parser.getCodes(rawCodes);
                              Globals.setCodes(codes);
                              callback(listeners);
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(Patron.getContext(), "Failed to get orders: " + response, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                callback(listeners);
                            }
                            catch (ParseException e)
                            {
                                e.printStackTrace();
                                callback(listeners);
                            }
                        }
                        callback(listeners);
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
          Toast.makeText(Patron.getContext(), "Failed to encode request.", Toast.LENGTH_SHORT).show();
          callback(listeners);
        }
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
            jsonOrder.getAsJsonObject().addProperty("oauth", Globals.getUser().getProvider());
            jsonOrder.getAsJsonObject().addProperty("deviceType", "1");
            String postData = gson.toJson(jsonOrder);
            System.out.println(postData);
            request.setEntity(new StringEntity(postData));
            ApiTask apiTask = new ApiTask();
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                    if (data == null || data.entrySet() == null)
                    {
                        Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                        callback(listeners);
                        return;
                    }
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        String rawOrder = new String(entry.getValue());
                        System.out.println("prepended string:" + rawOrder);
                        rawOrder = rawOrder.replaceAll("\\n", "");
                        if (rawUri.equals(ListLinks.API_ADD_ORDER))
                        {
                            if (rawOrder.equals("1"))
                            {
                                Globals.setOrder(null);
                                Toast.makeText(context, "Successfully placed order.", Toast.LENGTH_SHORT).show();
                                callback(listeners);
                            }
                            else
                            {
                                Toast.makeText(context, rawOrder, Toast.LENGTH_SHORT).show();
                                callback(listeners);
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

    public void addCard(final String name, final String number, final String code,
        final int month, final int year, final Context context, final OnApiExecutedListener... listeners)
    {
        final StringBuilder success = new StringBuilder();
        final StringBuilder message = new StringBuilder();
        // AsyncTask for balanced.
        final StringBuilder builder = new StringBuilder();
        class BalancedTask extends ApiTask {
            @Override
            protected Map<URI, byte[]> doInBackground(HttpUriRequest... requests)
            {
                try
                {
                    // Create the card in the Balanced API.
                    final Balanced balanced = new Balanced(context);
                    final String cardHref = null;
                    final HashMap<String, Object> optionalFields = new HashMap<String, Object>();
                    optionalFields.put("name", name);
                    optionalFields.put("cvv", code);
                    optionalFields.put("expiration_month", month);
                    optionalFields.put("expiration_year", year);
                    Map<String, Object> response = balanced.createCard(number, month, year, optionalFields);
                    if (response == null)
                      throw new CreationFailureException(name + number);
                    Gson gson = new Gson();
                    String json = gson.toJson(response);
                    Map<String, Object> cardResponse = (Map<String, Object>) ((ArrayList)response.get("cards")).get(0);
                    String href = cardResponse.get("href").toString();
                    builder.append(href);
                    success.append("1");
                }
                catch (CreationFailureException e)
                {
                    e.printStackTrace();
                    //Toast.makeText(context, "Failed to create card information.", Toast.LENGTH_SHORT).show();
                    message.append("Failed to create card information");
                    callback(listeners);
                }
                catch (FundingInstrumentNotValidException e)
                {
                    e.printStackTrace();
                    //Toast.makeText(context, "Invalid credit/debit card.", Toast.LENGTH_SHORT).show();
                    message.append("Invalid credit/debit card");
                    callback(listeners);
                }
                return null;
            }
        }
        BalancedTask balancedTask = new BalancedTask() {
            @Override
            protected void onPostExecute(Map<URI, byte[]> balancedResult)
            {
                if (!success.toString().equals("1"))
                {
                    Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                try
                {
                    // Associate the card to the customer.
                    String cardHref = builder.toString();
                    ApiTask apiTask = new ApiTask();
                    final String url = ListLinks.API_ADD_CARD;
                    HttpPost request = new HttpPost(url);
                    ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
                    pairs.add(new BasicNameValuePair("email", Globals.getUser().getEmail()));
                    pairs.add(new BasicNameValuePair("password", Globals.getUser().getPassword()));
                    pairs.add(new BasicNameValuePair("oauth", Globals.getUser().getProvider()));
                    pairs.add(new BasicNameValuePair("card", cardHref));
                    request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
                    apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                        @Override
                        public void onComplete(Map<URI, byte[]> data)
                        {
                          if (data == null || data.entrySet() == null)
                          {
                            Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                            callback(listeners);
                            return;
                          }
                            for (Map.Entry<URI, byte[]> entry : data.entrySet())
                            {
                                String rawUri = entry.getKey().toString();
                                if (rawUri.equals(url))
                                {
                                    String response = new String(entry.getValue());
                                    if (response.equals("1"))
                                    {
                                      Toast.makeText(context, "Added card.", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                      Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                login(Globals.getUser().getEmail(), Globals.getUser().getPassword(), Globals.getUser().getProvider(), listeners);
                            }
                        }
                    });
                    apiTask.execute(request);
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                    callback(listeners);
                }
            }
        };
        balancedTask.execute();
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
        NameValuePair oauth = new BasicNameValuePair("oauth", Globals.getUser().getProvider());
        NameValuePair instrument = new BasicNameValuePair(key, funder.getHref());
        pairs.add(email);
        pairs.add(password);
        pairs.add(oauth);
        pairs.add(instrument);
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
                    Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                    callback(listeners);
                    return;
                  }
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
                                Toast.makeText(Patron.getContext(), "Failed to remove funding instrument: " + response, Toast.LENGTH_SHORT).show();
                            }
                            login(Globals.getUser().getEmail(), Globals.getUser().getPassword(), Globals.getUser().getProvider(), listeners);
                        }
                        else
                        {
                            callback(listeners);
                        }
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            callback(listeners);
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

    public void updateAccount(final String email, final String password, String firstName, String lastName, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_UPDATE_ACCOUNT;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair pairCurrentEmail = new BasicNameValuePair("currentEmail", Globals.getUser().getEmail());
        NameValuePair pairCurrentPassword = new BasicNameValuePair("currentPassword", Globals.getUser().getPassword());
        NameValuePair pairOauth = new BasicNameValuePair("oauth", Globals.getUser().getProvider());
        NameValuePair pairEmail = new BasicNameValuePair("email", email);
        NameValuePair pairPassword = new BasicNameValuePair("password", password);
        NameValuePair pairFirstName = new BasicNameValuePair("firstName", firstName);
        NameValuePair pairLastName = new BasicNameValuePair("lastName", lastName);
        pairs.add(pairCurrentEmail);
        pairs.add(pairCurrentPassword);
        pairs.add(pairOauth);
        pairs.add(pairEmail);
        pairs.add(pairPassword);
        pairs.add(pairFirstName);
        pairs.add(pairLastName);
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
                    Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                    callback(listeners);
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
                                User user = Parser.getUser(new JSONObject(rawUser));
                                if (!Globals.getProvider().equals("fb") &&
                                    !Globals.getProvider().equals("tw") &&
                                    !Globals.getProvider().equals("gp"))
                                {
                                    user.setEmail(email);
                                    user.setPassword(password);
                                }
                                Globals.setUser(user);
                            }
                            catch (NetworkOnMainThreadException e)
                            {
                                e.printStackTrace();
                                Toast.makeText(Patron.getContext(), "Failed to retrieve updated info, please login again", Toast.LENGTH_SHORT).show();
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                                Toast.makeText(Patron.getContext(), rawUser, Toast.LENGTH_SHORT).show();
                            }
                            catch (NullPointerException e)
                            {
                                e.printStackTrace();
                                Toast.makeText(Patron.getContext(), "Failed to retrieve updated info, please login again", Toast.LENGTH_SHORT).show();
                            }
                        }
                        callback(listeners);
                    }
                }
            });
            apiTask.execute(request);
        }
        catch (UnsupportedEncodingException e)
        {
          Toast.makeText(Patron.getContext(), "Failed to encode request.", Toast.LENGTH_SHORT).show();
          e.printStackTrace();
          callback(listeners);
        }
    }

    public void addFavoriteVendor(Vendor vendor, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_ADD_FAVORITE_VENDOR;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", Globals.getUser().getEmail());
        NameValuePair password = new BasicNameValuePair("password", Globals.getUser().getPassword());
        NameValuePair oauth = new BasicNameValuePair("oauth", Globals.getUser().getProvider());
        NameValuePair vendorId = new BasicNameValuePair("vendorId", vendor.getId());
        pairs.add(email);
        pairs.add(password);
        pairs.add(oauth);
        pairs.add(vendorId);
        try
        {
            ApiTask apiTask = new ApiTask();
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                  if (data == null || data.entrySet() == null)
                  {
                    Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                    callback(listeners);
                    return;
                  }
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        if (rawUri.equals(url))
                        {
                            String response = new String(entry.getValue());
                            if (response.equals("1"))
                            {
                                Toast.makeText(Patron.getContext(), "Added favorite vendor.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Patron.getContext(), "Failed to add favorite vendor.", Toast.LENGTH_SHORT).show();
                            }
                            login(Globals.getUser().getEmail(), Globals.getUser().getPassword(), Globals.getUser().getProvider(), listeners);
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

    public void removeFavoriteVendor(Vendor vendor, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_REMOVE_FAVORITE_VENDOR;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", Globals.getUser().getEmail());
        NameValuePair password = new BasicNameValuePair("password", Globals.getUser().getPassword());
        NameValuePair oauth = new BasicNameValuePair("oauth", Globals.getUser().getProvider());
        NameValuePair vendorId = new BasicNameValuePair("vendorId", vendor.getId());
        pairs.add(email);
        pairs.add(password);
        pairs.add(oauth);
        pairs.add(vendorId);
        try
        {
            ApiTask apiTask = new ApiTask();
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                  if (data == null || data.entrySet() == null)
                  {
                    Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                    callback(listeners);
                    return;
                  }
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        if (rawUri.equals(url))
                        {
                            String response = new String(entry.getValue());
                            if (response.equals("1"))
                            {
                                Toast.makeText(Patron.getContext(), "Removed favorite vendor.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Patron.getContext(), "Failed to remove favorite vendor.", Toast.LENGTH_SHORT).show();
                            }
                            login(Globals.getUser().getEmail(), Globals.getUser().getPassword(), Globals.getUser().getProvider(), listeners);
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

    public void addFavoriteItem(Item item, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_ADD_FAVORITE_ITEM;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", Globals.getUser().getEmail());
        NameValuePair password = new BasicNameValuePair("password", Globals.getUser().getPassword());
        NameValuePair oauth = new BasicNameValuePair("oauth", Globals.getUser().getProvider());
        NameValuePair itemId = new BasicNameValuePair("itemId", item.getId());
        pairs.add(email);
        pairs.add(password);
        pairs.add(oauth);
        pairs.add(itemId);
        try
        {
            ApiTask apiTask = new ApiTask();
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                  if (data == null || data.entrySet() == null)
                  {
                    Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                    callback(listeners);
                    return;
                  }
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        if (rawUri.equals(url))
                        {
                            String response = new String(entry.getValue());
                            if (response.equals("1"))
                            {
                                Toast.makeText(Patron.getContext(), "Added favorite item.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Patron.getContext(), "Failed to add favorite item.", Toast.LENGTH_SHORT).show();
                            }
                            login(Globals.getUser().getEmail(), Globals.getUser().getPassword(), Globals.getUser().getProvider(), listeners);
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

    public void removeFavoriteItem(Item item, final OnApiExecutedListener... listeners)
    {
        final String url = ListLinks.API_REMOVE_FAVORITE_ITEM;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        NameValuePair email = new BasicNameValuePair("email", Globals.getUser().getEmail());
        NameValuePair password = new BasicNameValuePair("password", Globals.getUser().getPassword());
        NameValuePair oauth = new BasicNameValuePair("oauth", Globals.getUser().getProvider());
        NameValuePair itemId = new BasicNameValuePair("itemId", item.getId());
        pairs.add(email);
        pairs.add(password);
        pairs.add(oauth);
        pairs.add(itemId);
        try
        {
            ApiTask apiTask = new ApiTask();
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            apiTask.setOnTaskCompletedListener(new OnTaskCompletedListener() {
                @Override
                public void onComplete(Map<URI, byte[]> data)
                {
                  if (data == null || data.entrySet() == null)
                  {
                    Toast.makeText(Patron.getContext(), "Network error, check your internet connection.", Toast.LENGTH_SHORT).show();
                    callback(listeners);
                    return;
                  }
                    for (Map.Entry<URI, byte[]> entry : data.entrySet())
                    {
                        String rawUri = entry.getKey().toString();
                        if (rawUri.equals(url))
                        {
                            String response = new String(entry.getValue());
                            if (response.equals("1"))
                            {
                                Toast.makeText(Patron.getContext(), "Removed favorite item.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Patron.getContext(), "Failed to remove favorite item.", Toast.LENGTH_SHORT).show();
                            }
                            login(Globals.getUser().getEmail(), Globals.getUser().getPassword(), Globals.getUser().getProvider(), listeners);
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
