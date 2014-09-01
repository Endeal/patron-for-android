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

import android.os.AsyncTask;

import com.patron.system.Loadable;
import com.patron.system.Globals;
import com.google.gson.Gson;

public class CreateAccountConnector extends AsyncTask<URL, Void, String>
{
	/**
	 * Private variables.
	 */
	private String result;
	private Loadable activity;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String birthday;

	/**
	 * Default constructor.
	 */
	public CreateAccountConnector(Loadable activity, String firstName, String lastName, String email, String password, String birthday)
	{
		this.activity = activity;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.birthday = birthday;
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

			NameValuePair pairFirstName = new BasicNameValuePair("firstName", firstName);
			NameValuePair pairLastName = new BasicNameValuePair("lastName", lastName);
			NameValuePair pairEmail = new BasicNameValuePair("email", email);
			NameValuePair pairPassword = new BasicNameValuePair("password", password);
			NameValuePair pairBirthday = new BasicNameValuePair("birthday", birthday);

			postData.add(pairFirstName);
			postData.add(pairLastName);
			postData.add(pairEmail);
			postData.add(pairPassword);
			postData.add(pairBirthday);

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
			int id = Integer.parseInt(result);
			activity.message("Successfully created account.");
			activity.update();
		}
		catch (NumberFormatException e)
		{
			activity.message("Error creating account:\n" + result);
		}
	}
}