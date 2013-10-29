package com.flashvip.db;

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
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.flashvip.main.FlashLocations;
import com.flashvip.main.Globals;
import com.flashvip.main.Location;

public class LocationConnector extends AsyncTask<URL, Void, ArrayList<Location>>
{
	/**
	 * Private variables.
	 */
	private ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	private String result;
	private HttpResponse response;
	private FlashLocations activity;
	
	public LocationConnector(FlashLocations activity)
	{
		this.activity = activity;
	}
	
	@Override
	protected ArrayList<Location> doInBackground(URL... params)
	{
		// The list of locations.
		ArrayList<Location> servers = new ArrayList<Location>();
		
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
			JSONObject obj = new JSONObject(result);
			JSONArray array_id = obj.getJSONArray("id");
			JSONArray array_name = obj.getJSONArray("name");
			JSONArray array_address = obj.getJSONArray("address");
			JSONArray array_city = obj.getJSONArray("city");
			JSONArray array_state = obj.getJSONArray("state");
			JSONArray array_zip = obj.getJSONArray("zip");
			JSONArray array_phone = obj.getJSONArray("phone");
			if (array_id != null && array_name != null)
			{
				int length = array_id.length();
				for (int i = 0; i < length; i++)
				{
					String id = array_id.getString(i);
					String name = array_name.getString(i);
					if (name.length() > 24)
						name = name.substring(0, 21) + "...";
					String address = array_address.getString(i);
					String city = array_city.getString(i);
					String state = array_state.getString(i).toUpperCase() + " ";
					String zip = array_zip.getString(i);
					String phone = array_phone.getString(i);
					if (phone.length() > 6)
					phone = "(" + phone.substring(0, 3) + ") " + phone.substring(3, 6) +
							"-" + phone.substring(6);
					servers.add(new Location(name, id, address, city, state, zip, phone));
				}
			}
		}
		catch(Exception e)
		{
			Toast toast = Toast.makeText(activity, result, Toast.LENGTH_SHORT);
			toast.show();
			servers = null;
		}
		return servers;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Location> list)
	{
		Globals.setLocations(list);
		if (Globals.getLocations() == null ||
				Globals.getLocations().size() <= 0)
		{
			Toast noBarsAvailable = Toast.makeText(activity,
					"Failed to retrieve the list of bars available.",
					Toast.LENGTH_SHORT);
			noBarsAvailable.show();
		}
		
		activity.updateList();
	}
}