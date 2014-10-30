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
import com.balancedpayments.android.BankAccount.AccountType;
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

public class AddBankAccountConnector extends AsyncTask<Context, Void, String>
{
	private Loadable activity;
	private String name;
	private String number;
	private String type;
	private String routing;
	private String address;
	private String state;
	private String city;
	private String postalCode;

	public AddBankAccountConnector(Loadable activity, String name, String number, String type, String routing,
		String address, String state, String city, String postalCode)
	{
		this.activity = activity;
		this.name = name;
		this.number = number;
		this.type = type;
		this.routing = routing;
		this.address = address;
		this.state = state;
		this.city = city;
		this.postalCode = postalCode;
	}

	@Override
	protected String doInBackground(Context... contexts)
	{
		Balanced balanced = new Balanced(contexts[0]);
		String bankAccountHref = null;
		Map<String, Object> response = null;
		HashMap<String, String> addressMap = new HashMap<String, String>();
		addressMap.put("line1", address);
		addressMap.put("state", state);
		addressMap.put("city", city);
		addressMap.put("postal_code", postalCode);
		HashMap<String, Object> optionalFields = new HashMap<String, Object>();
		optionalFields.put("name", name);
		optionalFields.put("account_number", number);
		optionalFields.put("account_type", type);
		optionalFields.put("routing_number", routing);
		optionalFields.put("address", addressMap);
		try
		{
			// Create the card in the Balanced API.
			AccountType balancedType = AccountType.CHECKING;
			if (type.equals("savings"))
				balancedType = AccountType.SAVINGS;
			response = balanced.createBankAccount(routing, number, balancedType, name, optionalFields);
			if (response == null)
				return null;
			Gson gson = new Gson();
			String json = gson.toJson(response);
			Map<String, Object> bankAccountResponse = (Map<String, Object>) ((ArrayList)response.get("bank_accounts")).get(0);
			bankAccountHref = bankAccountResponse.get("href").toString();
        	System.out.println("Bank Account:" + bankAccountHref);
        	System.out.println("URL:" + ListLinks.LINK_ADD_BANK_ACCOUNT);

			// Associate the card to the customer.
			HttpClient client = new DefaultHttpClient();
        	HttpPost post = new HttpPost(ListLinks.LINK_ADD_BANK_ACCOUNT);
        	ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        	pairs.add(new BasicNameValuePair("email", Globals.getUser().getEmail()));
        	pairs.add(new BasicNameValuePair("password", Globals.getUser().getPassword()));
        	pairs.add(new BasicNameValuePair("bankAccount", bankAccountHref));
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
			activity.message("Failed to validate account information.");
			return null;
		}
		catch (FundingInstrumentNotValidException e)
		{
			activity.message("Invalid bank account.");
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
			activity.message("Added bank account.");
			activity.update();
			System.out.println(result);
		}
		else
		{
			activity.message("Failed to add bank account.");
		}
	}
}