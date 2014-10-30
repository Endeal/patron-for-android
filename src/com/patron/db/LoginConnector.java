package com.patron.db;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.lang.NumberFormatException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.patron.system.Loadable;
import com.patron.system.Globals;
import com.patron.system.Parser;
import com.patron.model.User;
import com.google.gson.Gson;

public class LoginConnector extends AsyncTask<URL, Void, String>
{
	/**
	 * Private variables.
	 */
	private String result;
	private Loadable activity;
	private String email;
	private String password;

	/**
	 * Default constructor.
	 */
	public LoginConnector(Loadable activity, String email, String password)
	{
		this.activity = activity;
		this.email = email;
		this.password = password;
	}

	@Override
	protected String doInBackground(URL... params)
	{
		// HTTP Post.
		try
		{
			URL path = params[0];
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(path.toURI());ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();

			NameValuePair pairEmail = new BasicNameValuePair("email", email);
			NameValuePair pairPassword = new BasicNameValuePair("password", password);

			postData.add(pairEmail);
			postData.add(pairPassword);

			post.setEntity(new UrlEncodedFormEntity(postData));
			HttpResponse response = client.execute(post);
			result = EntityUtils.toString(response.getEntity());
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

		// Return generated Patron ID
		return result;
	}

	@Override
	protected void onPostExecute(String result)
	{
		activity.endLoading();
		try
		{
			JSONObject patron = new JSONObject(result);
			User user = Parser.getUser(patron, password);
			Globals.setUser(user);
			System.out.println(user.getCards().size());
			System.out.println(user.getCards().get(0).getBankName());
			System.out.println(user.getCards().get(0).getNumber());
			System.out.println(user.getCards().get(0).getType());
			activity.update();
		}
		catch (NumberFormatException e)
		{
			activity.message("Error logging in:\n" + result);
		}
		catch (JSONException e)
		{
			activity.message("Invalid response received.");
			System.out.println(result);
		}
	}
}