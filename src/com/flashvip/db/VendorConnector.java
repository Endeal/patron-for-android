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

import com.flashvip.main.FlashVendors;
import com.flashvip.main.Globals;
import com.flashvip.model.Vendor;

public class VendorConnector extends AsyncTask<URL, Void, ArrayList<Vendor>>
{
	/**
	 * Private variables.
	 */
	private ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	private String result;
	private HttpResponse response;
	private FlashVendors activity;
	
	public VendorConnector(FlashVendors activity)
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
				String vendorId = rawVendor.getString("vendor_id");
				String name = rawVendor.getString("name");
				String address = rawVendor.getString("address");
				String city = rawVendor.getString("city");
				String state = rawVendor.getString("state");
				String zip = rawVendor.getString("zip");
				String phone = rawVendor.getString("phone");
				Vendor vendor = new Vendor(vendorId, name,
						address, city, state, zip, phone, null, null);
				vendors.add(vendor);
			}
		}
		catch(Exception e)
		{
			/*Toast toast = Toast.makeText(activity, result, Toast.LENGTH_SHORT);
			toast.show();*/
			vendors = null;
		}
		return vendors;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Vendor> list)
	{
		Globals.setVendors(list);
		if (Globals.getVendors() == null ||
				Globals.getVendors().size() <= 0)
		{
			Toast noBarsAvailable = Toast.makeText(activity,
					"Failed to retrieve the list of bars available.",
					Toast.LENGTH_SHORT);
			noBarsAvailable.show();
		}
		
		activity.updateList();
	}
}
