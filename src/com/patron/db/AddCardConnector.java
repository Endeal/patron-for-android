package com.patron.db;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.content.Context;

import com.balancedpayments.android.Balanced;
import com.balancedpayments.android.Card;
import com.balancedpayments.android.BankAccount;
import com.balancedpayments.android.exception.*;

import com.google.gson.Gson;

import com.patron.main.FlashCart;
import com.patron.system.Globals;

public class AddCardConnector extends AsyncTask<Context, Void, String>
{
	private Context context;
	private String name;
	private String number;
	private String code;
	private int month;
	private int year;
	private String address;
	private String state;
	private String city;
	private String postalCode;

	public AddCardConnector(Context context, String name, String number, String code, int month,
		int year, String address, String state, String city, String postalCode)
	{
		this.context = context;
		this.name = name;
		this.number = number;
		this.code = code;
		this.month = month;
		this.year = year;
		this.address = address;
		this.state = state;
		this.city = city;
		this.postalCode = postalCode;
	}

	@Override
	protected String doInBackground(Context... contexts)
	{
		Balanced balanced = new Balanced(contexts[0]);
		String cardHref = null;
		Map<String, Object> response = null;
		HashMap<String, String> addressMap = new HashMap<String, String>();
		addressMap.put("line1", address);
		addressMap.put("state", state);
		addressMap.put("city", city);
		addressMap.put("postal_code", postalCode);
		HashMap<String, Object> optionalFields = new HashMap<String, Object>();
		optionalFields.put("name", name);
		optionalFields.put("cvv", code);
		optionalFields.put("address", addressMap);
		try
		{
			System.out.println("THIS TOTES HAPPENED!");
			response = balanced.createCard(number, month, year, optionalFields);
			if (response == null)
			System.out.println("IT'S SO DAMN NULL!");
			else
			{
				Gson gson = new Gson();
				String json = gson.toJson(response);
				System.out.println("NOT NULL!\n" + json + "\nSee? It's all there...");
			}
			Map<String, Object> cardResponse = (Map<String, Object>) ((ArrayList)response.get("cards")).get(0);
			cardHref = cardResponse.get("href").toString();
			return cardHref;
		}
		catch (CreationFailureException e)
		{
			System.out.println("CREATION FAILURE EXCEPTION!");
			return null;
		}
		catch (FundingInstrumentNotValidException e)
		{
			System.out.println("FUNDING INSTRUMENT NOT VALID EXCEPTION!");
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result)
	{
		System.out.println("POOOOOOP!");
		System.out.println("HREF:" + result + ";");
	}
}