package com.patron.db;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.os.AsyncTask;
import android.content.Context;

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
import com.patron.model.Funder;

public class RemoveBankAccountConnector extends AsyncTask<Context, Void, String>
{
	private Loadable activity;
	private Funder funder;

	public RemoveBankAccountConnector(Loadable activity, Funder funder)
	{
		this.activity = activity;
		this.funder = funder;
	}

	@Override
	protected String doInBackground(Context... contexts)
	{
		String bankAccountHref = funder.getHref();
		Map<String, Object> response = null;
		try
		{
			// Remove the card from the customer.
			HttpClient client = new DefaultHttpClient();
        	HttpPost post = new HttpPost(ListLinks.LINK_REMOVE_BANK_ACCOUNT);
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
		if (result.equals("1"))
		{
			activity.message("Removed bank account.");
			activity.update();
		}
		else
		{
			activity.message("Failed to remove bank account.");
			System.out.println(result);
		}
	}
}