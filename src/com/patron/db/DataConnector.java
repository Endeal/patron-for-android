/**
 * @author James Whiteman
 * 
 * This class fetches all the data that the app needs.
 * First it gets the codes and vendors simultaneously.
 * Then it gets the vendor ID corresponding to the
 * supplied order ID. Then it gets the items belonging
 * to the vendor. The final list of codes is ultimately
 * delivered back.
 */

package com.patron.db;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.patron.lists.ListLinks;
import com.patron.model.Code;
import com.patron.model.Vendor;
import com.patron.system.Globals;
import com.patron.system.Loadable;
import com.patron.system.Parser;

public class DataConnector extends AsyncTask<String, Void, ArrayList<Code>>
{
	private Loadable loadable;

	public DataConnector(Loadable loadable)
	{
		this.loadable = loadable;
	}

	@Override
	protected ArrayList<Code> doInBackground(String... params)
	{
		String orderId = params[0];

		// Get the codes and vendors.
		URI uriVendors = null;
		URI uriCodes = null;
		try
		{
			uriVendors = new URI(ListLinks.LINK_GET_VENDORS);
			uriCodes = new URI(ListLinks.LINK_GET_CODES);
			HttpClient clientVendors = new DefaultHttpClient();
			HttpClient clientCodes = new DefaultHttpClient();
			HttpPost postVendors = new HttpPost(uriVendors);
			HttpPost postCodes = new HttpPost(uriCodes);
			NameValuePair pairCodes = new BasicNameValuePair("deviceId", Globals.getDeviceId());
			List<NameValuePair> pairsCodes = new ArrayList<NameValuePair>();
			pairsCodes.add(pairCodes);
			postCodes.setEntity(new UrlEncodedFormEntity(pairsCodes));

			HttpResponse responseVendors = clientVendors.execute(postVendors);
			HttpResponse responseCodes = clientCodes.execute(postCodes);

			HttpEntity entityVendors = responseVendors.getEntity();
			HttpEntity entityCodes = responseCodes.getEntity();

			String dataVendors = EntityUtils.toString(entityVendors);
			String dataCodes = EntityUtils.toString(entityCodes);

			// Parse the codes.
			ArrayList<Code> codes = new ArrayList<Code>();
			String vendorId = null;
			try
			{
				JSONArray rawCodes = new JSONArray(dataCodes);
				for (int i = 0; i < rawCodes.length(); i++)
				{
					JSONObject rawCode = rawCodes.getJSONObject(i);
					Code code = Parser.getCode(rawCode);
					codes.add(code);
					if (code.getOrder().getOrderId().equals(orderId))
					{
						vendorId = code.getOrder().getVendorId();
					}
				}
			}
			catch(JSONException e)
			{
				loadable.message(dataCodes);
				return null;
			}

			// Parse the vendors.
			ArrayList<Vendor> vendors = new ArrayList<Vendor>();
			try
			{
				JSONArray rawVendors = new JSONArray(dataVendors);
				for (int i = 0; i < rawVendors.length(); i++)
				{
					JSONObject rawVendor = rawVendors.getJSONObject(i);
					Vendor vendor = Parser.getVendor(rawVendor);
					vendors.add(vendor);
					if (vendor.getVendorId().equals(vendorId))
					{
						Globals.setVendor(vendor);
					}
				}
			}
			catch(JSONException e)
			{
				loadable.message(dataVendors);
			}

			// TODO: Check for errors, if vendors, vendor, items, or codes aren't set properly.

			Globals.setVendors(vendors);

			return codes;
		}
		catch (Exception e)
		{
			loadable.message("Connection error.");
			return null;
		}
	}

	@Override
	public void onPostExecute(ArrayList<Code> codes)
	{

		Globals.setCodes(codes);
		loadable.endLoading();
	}
}
