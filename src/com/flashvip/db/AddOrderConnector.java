package com.flashvip.db;

import java.io.IOException;


import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.widget.Toast;

import com.flashvip.main.Product;
import com.flashvip.main.FlashClient;
import com.flashvip.main.Globals;
import com.urbanairship.push.PushManager;

public class AddOrderConnector extends AsyncTask<URL, Void, ArrayList<Product>>
{
	/**
	 * Private variables.
	 */
	private ArrayList<Product> products;
	private String result;

	/**
	 * Default constructor.
	 */
	public AddOrderConnector()
	{
		products = new ArrayList<Product>();
	}

	@Override
	protected void onPreExecute()
	{
		FlashClient.beginLoading();
	}

	@Override
	protected ArrayList<Product> doInBackground(URL... params)
	{
		// The list of drinks.
		HttpResponse response = null;
		if (Globals.getCurrentServer() == null)
			return null;

		// HTTP Post.
		try
		{
			URL path = params[0];
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(path.toURI());
			ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();

			// Set up the strings to be encoded as name-value pairs.
			String drinkIds = "";
			String alcoholIds = "";
			String quantities = "";
			String apid = PushManager.shared().getAPID();
			System.out.println("size:" + Globals.getTabProducts().size());
			for (int i = 0; i < Globals.getTabProducts().size(); i++)
			{
				drinkIds = drinkIds + Globals.getTabProducts().get(i).getDrink().getId();
				alcoholIds = alcoholIds + Globals.getAlcohols().get(Globals.getTabProducts(
						).get(i).getAlcoholPosition()).getId();
				quantities = quantities + (Globals.getTabProducts().get(i).getQuantityPosition() + 1);
				if (i < Globals.getTabProducts().size() - 1)
				{
					drinkIds = drinkIds + ",";
					alcoholIds = alcoholIds + ",";
					quantities = quantities + ",";
				}
			}

			// Set up the name-value pairs.
			NameValuePair nvpTableNumber = new BasicNameValuePair("table",
					Globals.getCurrentServer().getId());
			NameValuePair nvpClientId = new BasicNameValuePair("client_id", apid);
			NameValuePair nvpDrinkIds = new BasicNameValuePair("drink_ids", drinkIds);
			NameValuePair nvpAlcoholIds = new BasicNameValuePair("alcohol_ids", alcoholIds);
			NameValuePair nvpQuantities = new BasicNameValuePair("quantities", quantities);

			// Add the name-value pairs to the post data.
			postData.add(nvpTableNumber);
			postData.add(nvpClientId);
			postData.add(nvpDrinkIds);
			postData.add(nvpAlcoholIds);
			postData.add(nvpQuantities);

			// Set the post data to the request and execute the request.
			post.setEntity(new UrlEncodedFormEntity(postData));
			response = client.execute(post);
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
			if (!result.equals("1"))
			{
				Toast toast = Toast.makeText(Globals.getContext(), result, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		catch (IOException e)
		{
			products = null;
		}

		// Return drinks.
		return products;
	}

	@Override
	protected void onPostExecute(ArrayList<Product> list)
	{	
		Globals.getTabProducts().clear();
		Globals.goToMainScreen();
		FlashClient.updateAll();
		FlashClient.endLoading();
	}
}