package com.patron.system;

public class ApiTask extends AsyncTask<String, Integer, List<String>>
{
	private Loadable loadable;
	private List<NameValuePair> pairs;

	public ApiTask(Loadable loadable, Map<String, String> mappings)
	{
		this.loadable = loadable;
		pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
	}

	protected void onPreExecute()
	{
		if (loadable != null)
			loadable.beginLoading();
	}

	protected List<String> doInBackground(String... urls)
	{
		HttpClient client = new DefaultHttpClient();
		List<String> results = new ArrayList<String>();
		for (String url : urls)
		{
        	HttpPost post = new HttpPost(url);
        	post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
        	HttpResponse httpResponse = client.execute(post);
        	StatusLine statusLine = httpResponse.getStatusLine();
        	String result = null;
        	if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK)
        	{
        		result = EntityUtils.toString(httpResponse.getEntity());
        		results.add(result);
        	}
        	else
        	{
        		System.out.println("HTTP Error:" + statusLine.getStatusCode());
        	}
    	}
    	return results;
	}

	protected void onProgressUpdate(Integer... progress)
	{

	}

	protected String onPostExecute(List<String> results)
	{
		if (loadable != null)
		{
			loadable.endLoading(results);
		}

		System.out.println("API Task completed");
		for (String result : results)
		{
			System.out.println(result);
		}
	}
}