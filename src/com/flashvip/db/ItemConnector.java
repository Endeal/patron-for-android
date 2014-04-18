package com.flashvip.db;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.flashvip.model.Category;
import com.flashvip.model.Item;
import com.flashvip.sort.ProductSorter;
import com.flashvip.system.Globals;
import com.flashvip.system.Loadable;
import com.flashvip.system.Parser;

public class ItemConnector extends AsyncTask<URL, Void, List<Item>>
{
	/**
	 * Private variables.
	 */
	private List<Item> items;
	private String result;
	private Loadable activity;
	private SharedPreferences preferences;
	
	/**
	 * Custom constructor.
	 */
	public ItemConnector(Loadable activity, SharedPreferences preferences)
	{
		items = new ArrayList<Item>();
		this.activity = activity;
		this.preferences = preferences;
	}
	
	@Override
	protected List<Item> doInBackground(URL... params)
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
			NameValuePair vendorId = new BasicNameValuePair("vendorId",
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
			ArrayList<Category> categories = new ArrayList<Category>();
			Globals.setCategories(categories);
			for (int i = 0; i < rawItems.length(); i++)
			{
				JSONObject rawItem = rawItems.getJSONObject(i);
				Item item = Parser.getItem(rawItem);
				items.add(item);
			}
		}
		catch (JSONException e)
		{
			activity.message(result);
			return null;
		}
		catch (IOException e)
		{
			activity.message("Error converting HTTP results.");
			return null;
		}
		
		items = ProductSorter.getByName(items, true);
		return items;
	}
	
	@Override
	protected void onPostExecute(List<Item> items)
	{
		// Set favorite items.
		String json = preferences.getString("favoriteItems", "");
		System.out.println("Got Favorites: " + json);
		try
		{
			JSONArray rawItems = new JSONArray(json);
			List<Item> favoriteItems = new ArrayList<Item>(); 
			for (int i = 0; i < rawItems.length(); i++)
			{
				JSONObject rawItem = rawItems.getJSONObject(i);
				Item item = Parser.getItem(rawItem);
				favoriteItems.add(item);
			}
			Globals.setFavoriteItems(favoriteItems);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		if (Globals.getVendor() != null)
		{
			Globals.getVendor().setItems(items);
			Globals.getVendor().setFilteredItems(items);
		}
		
		activity.endLoading();
	}
}
