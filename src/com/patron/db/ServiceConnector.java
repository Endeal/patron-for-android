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
import android.os.Bundle;
import android.content.Context;

import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.brickred.socialauth.Profile;

import com.patron.system.Loadable;
import com.patron.system.Globals;
import com.google.gson.Gson;

public class ServiceConnector extends AsyncTask<URL, Void, String>
{
	/**
	 * Private variables.
	 */
	private Loadable activity;
	private Context context;
	private String result;
	private String provider;
	private SocialAuthAdapter socialAuthAdapter;

	/**
	 * Default constructor.
	 */
	public ServiceConnector(Loadable activity, SocialAuthAdapter socialAuthAdapter)
	{
		this.activity = activity;
		this.socialAuthAdapter = socialAuthAdapter;
	}

	@Override
	protected String doInBackground(URL... params)
	{
		// HTTP Post.
		//try
		//{
			// Authorize the service.
			System.out.println("POOPY FACE POOP DONE");
			socialAuthAdapter.getUserProfileAsync(new ProfileDataListener());
			System.out.println("CHICKEN BAKE");

			// Authorize the service's information with Patron info.
			/*URL path = params[0];
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(path.toURI());ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();

			NameValuePair pairEmail = new BasicNameValuePair("email", email);
			NameValuePair pairPassword = new BasicNameValuePair("password", password);

			System.out.println("POOP EMAIL:" + email + ";");
			System.out.println("POOP PASS:" + password + ";");

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
		catch (Exception e)
		{
			e.printStackTrace();
		}*/

		// Return generated Patron ID
		return result;
	}

	@Override
	protected void onPostExecute(String result)
	{
	}

	private final class ProfileDataListener implements SocialAuthListener<Profile>
	{
		@Override
		public void onExecute(String provider, Profile profile)
		{
			System.out.println("ZOO TYCOON!");
			String id = profile.getValidatedId();
			System.out.println("HARVARD!");
			String firstName = profile.getFirstName();
			System.out.println("ENCANTADO!");
			String lastName = profile.getLastName();
			System.out.println("NIGERIA!");
			String email = profile.getEmail();
			System.out.println("FLOATING SPATULA!");
			String image = profile.getProfileImageURL();
			System.out.println("info:" + id + firstName + lastName + email + image + ";");
		}

		public void onError(SocialAuthError e)
		{
			System.out.println("ERRRRRROR!");
		}	
	}
}