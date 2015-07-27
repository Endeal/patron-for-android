package me.endeal.patron.system;

import android.util.Log;

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

import me.endeal.patron.listeners.OnTaskCompletedListener;
import me.endeal.patron.model.Credential;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.system.Globals;

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
				this.connectionTimeout = 20000;
				this.socketTimeout = 20000;
		}

		public ApiTask(OnTaskCompletedListener listener)
		{
			this.listener = listener;
			this.mocking = false;
			this.mockData = null;
			this.connectionTimeout = 20000;
			this.socketTimeout = 20000;
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

        // Add authentication headers
        final HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, getConnectionTimeout());
        HttpConnectionParams.setSoTimeout(params, getSocketTimeout());
        HttpClient client = new DefaultHttpClient(params);
	    try
        {
            Map<URI, byte[]> data = new HashMap<URI, byte[]>();
            for (int i = 0; i < requests.length; i++)
            {
                HttpUriRequest request = requests[i];
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Accept", "application/json");
                Credential credential = Globals.getCredential();
                if (credential != null)
                {
                    request.addHeader("x-identity-identifier", credential.getIdentifier());
                    request.addHeader("x-identity-verifier", credential.getVerifier());
                    request.addHeader("x-identity-provider", credential.getProvider());
                }
                if (Globals.getVendor() != null && Globals.getVendor().getId() != null)
                {
                    request.addHeader("x-vendor-id", Globals.getVendor().getId());
                }
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);
                data.put(request.getURI(), bytes);
                String encoding = new String(bytes);

                String TAG = "PATRON";
                if (encoding.length() > 4000) {
                    Log.v(TAG, "encoding.length = " + encoding.length());
                    int chunkCount = encoding.length() / 4000;     // integer division
                    for (int j = 0; j <= chunkCount; j++) {
                        int max = 4000 * (j + 1);
                        if (max >= encoding.length()) {
                            Log.v(TAG, "chunk " + j + " of " + chunkCount + ":" + encoding.substring(4000 * j));
                        } else {
                            Log.v(TAG, "chunk " + j + " of " + chunkCount + ":" + encoding.substring(4000 * j, max));
                        }
                    }
                } else {
                    Log.v(TAG, encoding.toString());
                }
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
		if (listener != null)
		{
			listener.onComplete(data);
		}
	}
}
