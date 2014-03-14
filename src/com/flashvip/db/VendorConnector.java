package com.flashvip.db;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.flashvip.model.Vendor;
import com.flashvip.system.Globals;
import com.flashvip.system.Loadable;
import com.flashvip.system.Parser;

public class VendorConnector extends AsyncTask<URL, Void, ArrayList<Vendor>>
{
	/**
	 * Private variables.
	 */
	private ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	private String result;
	private HttpResponse response;
	private Loadable activity;
	
	public VendorConnector(Loadable activity)
	{
		this.activity = activity;
	}
	
	@Override
	protected ArrayList<Vendor> doInBackground(URL... params)
	{
		// The list of locations.
		ArrayList<Vendor> vendors = new ArrayList<Vendor>();
		
		// HTTP Post.
		try
		{
			URL path = params[0];
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(path.toURI());
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = client.execute(post);
		}
		catch (Exception e)
		{
			Log.e("log_tag", "Error in HTTP connection" + e.toString());
			return null;
		}
		
		// Convert HTTP Response to an ArrayList.
		try
		{
			result = EntityUtils.toString(response.getEntity());
			JSONArray rawVendors = new JSONArray(result);
			for (int i = 0; i < rawVendors.length(); i++)
			{
				JSONObject rawVendor = rawVendors.getJSONObject(i);
				Vendor vendor = Parser.getVendor(rawVendor);
				vendors.add(vendor);
			}
		}
		catch(JSONException e)
		{
			if (result != null && result.length() > 0)
			{
				activity.message(result);
			}
			vendors = null;
		}
		catch (IOException e)
		{
			activity.message("Failed to convert HTTP result.");
			vendors = null;
		}
		return vendors;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Vendor> vendors)
	{
		Globals.setVendors(vendors);
		Globals.setFilteredVendors(vendors);
		activity.endLoading();
	}
}
