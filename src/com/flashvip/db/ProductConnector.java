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

import com.flashvip.main.Product;
import com.flashvip.main.FlashLocations;
import com.flashvip.main.Globals;

public class ProductConnector extends AsyncTask<URL, Void, ArrayList<Product>>
{
	/**
	 * Private variables.
	 */
	private ArrayList<Product> products;
	private ArrayList<Product> alcohols;
	private String result;
	private FlashLocations activity;
	
	/**
	 * Custom constructor.
	 */
	public ProductConnector(FlashLocations activity)
	{
		products = new ArrayList<Product>();
		alcohols = new ArrayList<Product>();
		this.activity = activity;
	}
	
	@Override
	protected ArrayList<Product> doInBackground(URL... params)
	{
		// The list of drinks.
		HttpResponse response = null;
		if (Globals.getCurrentLocation() == null)
			return null;
		
		// HTTP Post.
		try
		{
			URL path = params[0];
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(path.toURI());
			ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
			NameValuePair tableNumber = new BasicNameValuePair("table_number",
					Globals.getCurrentLocation().getId());
			postData.add(tableNumber);
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
			JSONObject obj = new JSONObject(result);
			
			// Create JSON Arrays out of the JSON Object.
			JSONArray array_id = obj.getJSONArray("id");
			JSONArray array_name = obj.getJSONArray("name");
			JSONArray array_type = obj.getJSONArray("type");
			JSONArray array_alcohol = obj.getJSONArray("alcohol");
			JSONArray array_price = obj.getJSONArray("price");
			
			// Create arrays out of the JSON Arrays.
			if (array_id != null && array_name != null && array_type != null &&
					array_alcohol != null && array_price != null)
			{
				int length = array_id.length();
				for (int i = 0; i < length; i++)
				{
					String id = array_id.getString(i);
					String name = array_name.getString(i);
					String type = array_type.getString(i);
					String alcohol = array_alcohol.getString(i);
					String price = array_price.getString(i);
					Product product = new Product(id, name, type, alcohol, Float.parseFloat(price));
					if (product.getType() == Product.Type.PREMIUM)
						alcohols.add(product);
					else
						products.add(product);
				}
			}
		}
		catch (Exception e)
		{
			return null;
		}
		
		// Return drinks.
		return products;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Product> products)
	{
		Globals.setProducts(products);
		Globals.setCurrentProducts(products);
		Globals.setAlcohols(alcohols);
		if (Globals.getCurrentLocation() != null &&
				Globals.getProducts() != null && 
				Globals.getProducts().size() > 0)
		{
			Globals.setFavoriteProducts(Globals.getCurrentLocation().getTopDrinks());
		}
		
		activity.loadedMenu();
	}
}
