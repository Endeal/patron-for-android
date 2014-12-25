package com.patron.db;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.patron.main.FlashCart;
import com.patron.system.Globals;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class AddOrderConnector extends AsyncTask<URL, Void, String>
{
	/**
	 * Private variables.
	 */
	private String result;
	private FlashCart activity;

	/**
	 * Default constructor.
	 */
	public AddOrderConnector(FlashCart activity)
	{
		this.activity = activity;
	}

	@Override
	protected String doInBackground(URL... params)
	{
		// The list of drinks.
		HttpResponse response = null;
		if (Globals.getVendor() == null)
			return null;

		// HTTP Post.
		try
		{
			URL path = params[0];
			HttpClient client = new DefaultHttpClient();
			HttpPut request = new HttpPut(path.toURI());
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept", "application/json");
			Gson gson = new Gson();
			JsonElement order = gson.toJsonTree(Globals.getOrder());
			order.getAsJsonObject().addProperty("email", Globals.getUser().getEmail());
			order.getAsJsonObject().addProperty("password", Globals.getUser().getPassword());
			order.getAsJsonObject().addProperty("deviceType", "1");
			String postData = gson.toJson(order);

			// Set the request data to the request and execute the request.
			request.setEntity(new StringEntity(postData));
			response = client.execute(request);

		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// HTTP Response
		try
		{
			result = EntityUtils.toString(response.getEntity());
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// Return drinks.
		result = result.replaceAll("\\n", "");
		return result;
	}

	@Override
	protected void onPostExecute(String result)
	{
		Globals.setOrder(null);
		activity.orderFinished();
		if (result.equals("1"))
		{
			activity.message("Order placed.");
		}
		else
		{
			System.out.println("ERROR:" + result);
			activity.message(result);
		}
	}
}
