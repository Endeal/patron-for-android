package fvip.db;

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

import android.os.AsyncTask;
import android.widget.Toast;
import fvip.main.Drink;
import fvip.main.Globals;
import fvip.main.FlashClient;

public class DrinkConnector extends AsyncTask<URL, Void, ArrayList<Drink>>
{
	/**
	 * Private variables.
	 */
	private ArrayList<Drink> drinks;
	private ArrayList<Drink> alcohols;
	private String result;
	
	/**
	 * Default constructor.
	 */
	public DrinkConnector()
	{
		drinks = new ArrayList<Drink>();
		alcohols = new ArrayList<Drink>();
	}
	
	@Override
	protected void onPreExecute()
	{
		FlashClient.beginLoading();
	}
	
	@Override
	protected ArrayList<Drink> doInBackground(URL... params)
	{
		// The list of drinks.
		HttpResponse response = null;
		if (Globals.getCurrentServer() == null)
			return null;
		
		// HTTP Post.
		try
		{
			URL path = params[0];
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(path.toURI());
			ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
			NameValuePair tableNumber = new BasicNameValuePair("table_number",
					Globals.getCurrentServer().getId());
			postData.add(tableNumber);
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
			JSONArray array_id = obj.getJSONArray("id");
			JSONArray array_name = obj.getJSONArray("name");
			JSONArray array_type = obj.getJSONArray("type");
			JSONArray array_alcohol = obj.getJSONArray("alcohol");
			JSONArray array_price = obj.getJSONArray("price");
			
			// Create arrays out of the JSON Arrays.
			if (array_id != null && array_name != null && array_type != null &&
					array_alcohol != null && array_price != null)
			{
				int length = array_id.length();
				for (int i = 0; i < length; i++)
				{
					String id = array_id.getString(i);
					String name = array_name.getString(i);
					String type = array_type.getString(i);
					String alcohol = array_alcohol.getString(i);
					String price = array_price.getString(i);
					Drink drink = new Drink(id, name, type, alcohol, Double.parseDouble(price));
					if (type.equals("Premium Liquor"))
						alcohols.add(drink);
					else
						drinks.add(drink);
				}
			}
		}
		catch (Exception e)
		{
			Toast toast = Toast.makeText(Globals.getContext(), result, Toast.LENGTH_SHORT);
			toast.show();
			drinks = null;
		}
		
		// Return drinks.
		return drinks;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Drink> drinks)
	{
		Globals.setOrders(drinks);
		Globals.setAlcohols(alcohols);
		if (Globals.getCurrentServer() != null &&
				Globals.getAllOrders() != null && 
				Globals.getAllOrders().size() > 0)
		{
			Globals.setMyTopOrders(Globals.getCurrentServer().getTopDrinks());
			Globals.goToMainScreen();
			FlashClient.updateAll();
		}
		else
		{
			Toast toastNoSearch = Toast.makeText(Globals.getContext(),
					"Failed to retrieve the list of drinks.",
					Toast.LENGTH_SHORT);
			toastNoSearch.show();
		}
		
		FlashClient.endLoading();
	}
}
