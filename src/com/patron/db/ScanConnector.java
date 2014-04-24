package com.patron.db;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.patron.model.Order;
import com.patron.system.Globals;
import com.patron.system.Loadable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class ScanConnector extends AsyncTask<URL, Void, Bitmap>
{
	/**
	 * Private variables.
	 */
	private Loadable activity;
	private Bitmap image;
	private Order order;

	/**
	 * Default constructor.
	 */
	public ScanConnector(Loadable activity, Order order)
	{
		this.activity = activity;
		this.order = order;
	}

	@Override
	protected Bitmap doInBackground(URL... params)
	{
		// The list of drinks.
		HttpResponse response = null;

		// HTTP Post.
		try
		{
			URL path = params[0];
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(path.toURI());
			ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
			NameValuePair deviceId = new BasicNameValuePair("deviceId", Globals.getDeviceId());
			NameValuePair vendorId = null;
			NameValuePair orderId = null;
			if (order != null)
			{
				vendorId = new BasicNameValuePair("vendorId", order.getVendorId());
				orderId = new BasicNameValuePair("orderId", order.getOrderId());
			}
			postData.add(deviceId);
			postData.add(vendorId);
			postData.add(orderId);
			post.setEntity(new UrlEncodedFormEntity(postData));
			response = client.execute(post);
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
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
			// Convert results to string, then as a JSON Object.
			HttpEntity entity = response.getEntity();
			byte[] bytes = EntityUtils.toByteArray(entity);
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			image = bitmap;
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			activity.message("Error converting HTTP response.");
			image = null;
		}

		// Return codes.
		return image;
	}
	
	@Override
	protected void onPostExecute(Bitmap bitmap)
	{
		Globals.setScan(bitmap);
		activity.endLoading();
	}
}
