package com.patron.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.patron.listeners.OnTaskCompletedListener;

import android.os.AsyncTask;

public class ApiTask extends AsyncTask<HttpUriRequest, Void, Map<URI, HttpEntity>>
{
	private OnTaskCompletedListener onTaskCompletedListener;

	public ApiTask(OnTaskCompletedListener onTaskCompletedListener)
	{
		setOnTaskCompletedListener(onTaskCompletedListener);
	}

	public void setOnTaskCompletedListener(OnTaskCompletedListener onTaskCompletedListener)
	{
		this.onTaskCompletedListener = onTaskCompletedListener;
	}

	@Override
	protected Map<URI, HttpEntity> doInBackground(HttpUriRequest requests...)
	{
		try
		{
			Map<URI, HttpEntity> data = new HashMap<URI, HttpEntity>();
			for (int i = 0; i < params.length; i++)
			{
				HttpUriRequest request = requests[i];
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				data.put(request.getURI(), response.getEntity());
			}
			return data;
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

		return null;
	}

	@Override
	protected void onPostExecute(Map<URI, HttpEntity> data)
	{
		if (onTaskCompletedListener != null)
		{
			onTaskCompletedListener.onComplete(data);
		}
	}
}
