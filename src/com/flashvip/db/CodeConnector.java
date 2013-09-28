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

import com.flashvip.main.Globals;
import com.urbanairship.push.PushManager;

import android.os.AsyncTask;
import android.widget.Toast;

public class CodeConnector extends AsyncTask<URL, Void, ArrayList<String>>
{
	/**
	 * Private variables.
	 */
	private ArrayList<String> codes;
	private String result;
	private String apid;
	
	/**
	 * Default constructor.
	 */
	public CodeConnector()
	{
		codes = new ArrayList<String>();
		apid = PushManager.shared().getAPID();
	}

	@Override
	protected ArrayList<String> doInBackground(URL... params)
	{
		// The list of drinks.
		HttpResponse response = null;

		// HTTP Post.
		if (Globals.getCurrentLocation() != null)
		{
			try
			{
				URL path = params[0];
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(path.toURI());
				ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
				NameValuePair tableNumber = new BasicNameValuePair("table_number",
						Globals.getCurrentLocation().getId());
				NameValuePair clientId = new BasicNameValuePair("client_id", apid);

				postData.add(tableNumber);
				postData.add(clientId);
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
				JSONObject obj = new JSONObject(result);

				// Create JSON Arrays out of the JSON Object.
				JSONArray array_codes = obj.getJSONArray("codes");

				// Create arrays out of the JSON Arrays.
				if (array_codes != null)
				{
					int length = array_codes.length();
					for (int i = 0; i < length; i++)
					{
						codes.add(array_codes.getString(i));
					}
				}
			}
			catch (Exception e)
			{
//				Toast toast = Toast.makeText(Globals.getContext(), result, Toast.LENGTH_SHORT);
//				toast.show();
				codes = null;
			}
		}
		
		// Return codes.
		return codes;
	}
	
	@Override
	protected void onPostExecute(ArrayList<String> codes)
	{
		if (codes != null && codes.size() > 0)
		{
			Globals.setCodes(codes);
		}
		else
		{
//			Toast toastNoSearch = Toast.makeText(Globals.getContext(),
//					"Failed to retrieve the list of order codes.",
//					Toast.LENGTH_SHORT);
//			toastNoSearch.show();
		}
	}
}
