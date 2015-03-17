package com.patron.db;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.os.AsyncTask;
import android.content.Context;

import com.balancedpayments.android.Balanced;
import com.balancedpayments.android.Card;
import com.balancedpayments.android.BankAccount;
import com.balancedpayments.android.exception.*;

import com.google.gson.Gson;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import com.patron.main.FlashCart;
import com.patron.system.Globals;
import com.patron.system.Loadable;
import com.patron.lists.ListLinks;

public class AddCardConnector extends AsyncTask<Context, Void, String>
{
	private Loadable activity;
	private String name;
	private String number;
	private String code;
	private int month;
	private int year;

	public AddCardConnector(Loadable activity, String name, String number, String code, int month,
		int year)
	{
		this.activity = activity;
		this.name = name;
		this.number = number;
		this.code = code;
		this.month = month;
		this.year = year;
	}

	@Override
	protected String doInBackground(Context... contexts)
	{
		Balanced balanced = new Balanced(contexts[0]);
		String cardHref = null;
		Map<String, Object> response = null;
		HashMap<String, Object> optionalFields = new HashMap<String, Object>();
		optionalFields.put("name", name);
		optionalFields.put("cvv", code);
		optionalFields.put("expiration_month", month);
		optionalFields.put("expiration_year", year);
		try
		{
			// Create the card in the Balanced API.
			response = balanced.createCard(number, month, year, optionalFields);
			if (response == null)
				return null;
			Gson gson = new Gson();
			String json = gson.toJson(response);
			Map<String, Object> cardResponse = (Map<String, Object>) ((ArrayList)response.get("cards")).get(0);
			cardHref = cardResponse.get("href").toString();
        	System.out.println("Card:" + cardHref);
        	System.out.println("URL:" + ListLinks.LINK_ADD_CARD);

			// Associate the card to the customer.
			HttpClient client = new DefaultHttpClient();
        	HttpPost post = new HttpPost(ListLinks.LINK_ADD_CARD);
        	ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        	pairs.add(new BasicNameValuePair("email", Globals.getUser().getEmail()));
        	pairs.add(new BasicNameValuePair("password", Globals.getUser().getPassword()));
        	pairs.add(new BasicNameValuePair("card", cardHref));
        	post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
        	HttpResponse httpResponse = client.execute(post);
        	StatusLine statusLine = httpResponse.getStatusLine();
        	String result = null;
        	if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK)
        	{
        		result = EntityUtils.toString(httpResponse.getEntity());
        	}
        	else
        	{
        		System.out.println("HTTP Error:" + statusLine.getStatusCode());
        	}

			return result;
		}
		catch (CreationFailureException e)
		{
			activity.message("Failed to create card information.");
			return null;
		}
		catch (FundingInstrumentNotValidException e)
		{
			activity.message("Invalid credit/debit card.");
			return null;
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
		catch (IOException e)
		{
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result)
	{
		activity.endLoading();
		if (result != null)
		{
			activity.message("Added card.");
			activity.update();
			System.out.println(result);
		}
		else
		{
			activity.message("Failed to add card.");
		}
	}
}
