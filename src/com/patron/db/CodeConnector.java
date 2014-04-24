package com.patron.db;

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

import com.patron.main.FlashCodes;
import com.patron.model.Code;
import com.patron.system.Globals;
import com.patron.system.Loadable;
import com.patron.system.Parser;

import android.os.AsyncTask;

public class CodeConnector extends AsyncTask<URL, Void, ArrayList<Code>>
{
	/**
	 * Private variables.
	 */
	private Loadable activity;
	private ArrayList<Code> codes;
	private String result;

	/**
	 * Default constructor.
	 */
	public CodeConnector(FlashCodes activity)
	{
		this.activity = activity;
		codes = new ArrayList<Code>();
	}

	@Override
	protected ArrayList<Code> doInBackground(URL... params)
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

			postData.add(deviceId);
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
			// Convert results to string, then as a JSON Object.
			result = EntityUtils.toString(response.getEntity());
			JSONArray rawCodes = new JSONArray(result);
			for (int i = 0; i < rawCodes.length(); i++)
			{
				JSONObject rawCode = rawCodes.getJSONObject(i);
				Code code = Parser.getCode(rawCode);
				codes.add(code);
			}
		}
		catch (Exception e)
		{
			codes = null;
		}

		// Return codes.
		return codes;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Code> codes)
	{
		if (codes != null && codes.size() > 0)
		{
			Globals.setCodes(codes);
		}
		else
		{
			Globals.setCodes(null);
			activity.message(result);
		}
		activity.endLoading();
	}
}
