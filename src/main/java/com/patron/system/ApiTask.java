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
		private OnTaskCompletedListener listener;
    private boolean mocking;
    private Map<URI, byte[]> mockData;
    private int connectionTimeout;
    private int socketTimeout;

		public ApiTask()
		{
				this.listener = null;
				this.mocking = false;
				this.mockData = null;
				this.connectionTimeout = 10000;
				this.socketTimeout = 10000;
		}

		public ApiTask(OnTaskCompletedListener listener)
		{
			this.listener = listener;
			this.mocking = false;
			this.mockData = null;
			this.connectionTimeout = 10000;
			this.socketTimeout = 10000;
		}

	public void setOnTaskCompletedListener(OnTaskCompletedListener listener)
	{
		this.listener = listener;
	}

    public OnTaskCompletedListener getOnTaskCompletedListener()
    {
        return this.listener;
    }

    public void setMocking(boolean mocking)
    {
        this.mocking = mocking;
    }

    public boolean getMocking()
    {
        return this.mocking;
    }

    public void setMockData(Map<URI, byte[]> mockData)
    {
        this.mockData = mockData;
    }

    public Map<URI, byte[]> getMockData()
    {
        return this.mockData;
    }

    public void setConnectionTimeout(int connectionTimeout)
    {
        this.connectionTimeout = connectionTimeout;
    }

    public int getConnectionTimeout()
    {
        return this.connectionTimeout;
    }

    public void setSocketTimeout(int socketTimeout)
    {
        this.socketTimeout = socketTimeout;
    }

    public int getSocketTimeout()
    {
        return this.socketTimeout;
    }

	@Override
	protected Map<URI, byte[]> doInBackground(HttpUriRequest... requests)
	{
        // Immediately return mock data if the task is designed as such.
        if (this.mocking)
        {
            return this.mockData;
        }

				System.out.println("task1");
        final HttpParams params = new BasicHttpParams();
				System.out.println("task2");
        HttpConnectionParams.setConnectionTimeout(params, getConnectionTimeout());
				System.out.println("task3");
        HttpConnectionParams.setSoTimeout(params, getSocketTimeout());
				System.out.println("task4");
        HttpClient client = new DefaultHttpClient(params);
				System.out.println("task5");
	    try
			{
					Map<URI, byte[]> data = new HashMap<URI, byte[]>();
					System.out.println("task6");
					for (int i = 0; i < requests.length; i++)
					{
								HttpUriRequest request = requests[i];
								System.out.println("task7");
                HttpResponse response = client.execute(request);
								System.out.println("task8");
                HttpEntity entity = response.getEntity();
								System.out.println("task9");
                byte[] bytes = EntityUtils.toByteArray(entity);
								System.out.println("task10");
								data.put(request.getURI(), bytes);
								System.out.println("task11");
								System.out.println("data:" + new String(bytes));
								System.out.println(data.toString());
					}
					return data;
			}
		catch (NullPointerException e)
		{
			System.out.println("task12");
			e.printStackTrace();
		}
        catch (UnknownHostException e)
        {
				System.out.println("task13");
            e.printStackTrace();
        }
		catch (UnsupportedEncodingException e)
		{
		System.out.println("task14");
			e.printStackTrace();
		}
		catch (ClientProtocolException e)
		{
		System.out.println("task15");
			e.printStackTrace();
		}
		catch (IOException e)
		{
		System.out.println("task16");
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Map<URI, byte[]> data)
	{
		System.out.println("taskpost1");
		if (listener != null)
		{
			System.out.println("taskpost2");
			listener.onComplete(data);
			System.out.println("taskpost3");
		}
	}
}
