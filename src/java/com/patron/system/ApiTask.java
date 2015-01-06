package com.patron.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
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

public class ApiTask extends AsyncTask<HttpUriRequest, Void, Map<URI, byte[]>>
{
	private OnTaskCompletedListener onTaskCompletedListener;

	public void setOnTaskCompletedListener(OnTaskCompletedListener onTaskCompletedListener)
	{
		this.onTaskCompletedListener = onTaskCompletedListener;
	}

	@Override
	protected Map<URI, byte[]> doInBackground(HttpUriRequest... requests)
	{
        final HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpConnectionParams.setSoTimeout(params, 10000);
        HttpClient client = new DefaultHttpClient(params);
	    try
		{
			Map<URI, byte[]> data = new HashMap<URI, byte[]>();
			for (int i = 0; i < requests.length; i++)
			{
				HttpUriRequest request = requests[i];
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);
				data.put(request.getURI(), bytes);
			}
			return data;
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
        catch (UnknownHostException e)
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
	protected void onPostExecute(Map<URI, byte[]> data)
	{
		if (onTaskCompletedListener != null)
		{
			onTaskCompletedListener.onComplete(data);
		}
	}
}
