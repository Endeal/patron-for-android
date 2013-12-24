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
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.flashvip.main.FlashVendors;
import com.flashvip.main.Globals;
import com.flashvip.model.Item;
import com.flashvip.model.Parser;
import com.flashvip.sort.ProductSorter;

public class ItemConnector extends AsyncTask<URL, Void, ArrayList<Item>>
{
	/**
	 * Private variables.
	 */
	private ArrayList<Item> items;
	private String result;
	private FlashVendors activity;
	
	/**
	 * Custom constructor.
	 */
	public ItemConnector(FlashVendors activity)
	{
		items = new ArrayList<Item>();
		this.activity = activity;
	}
	
	@Override
	protected ArrayList<Item> doInBackground(URL... params)
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
			HttpPost post = new HttpPost(path.toURI());
			ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
			NameValuePair vendorId = new BasicNameValuePair("vendor_id",
					Globals.getVendor().getVendorId());
			postData.add(vendorId);
			post.setEntity(new UrlEncodedFormEntity(postData));
			response = client.execute(post);
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		
		// HTTP Response
		try
		{
			// Convert results to string, then as a JSON Object.
			result = EntityUtils.toString(response.getEntity());
			JSONArray rawItems = new JSONArray(result);
			
			// Add the items.
			for (int i = 0; i < rawItems.length(); i++)
			{
				JSONObject rawItem = rawItems.getJSONObject(i);
				Item item = Parser.getItem(rawItem);
				items.add(item);
			}
		}
		catch (Exception e)
		{
			return null;
		}
		
		items = ProductSorter.getByName(items, true);
		return items;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Item> items)
	{
		Globals.setOrder(null);
		Globals.getVendor().setItems(items);
		Globals.getVendor().setFilteredItems(items);
		if (Globals.getVendor() != null &&
				Globals.getVendor().getItems() != null && 
				Globals.getVendor().getItems().size() > 0)
		{
			//Globals.setFavoriteProducts(Globals.getVendor().getTopDrinks());
		}
		
		activity.loadedMenu();
	}
}
