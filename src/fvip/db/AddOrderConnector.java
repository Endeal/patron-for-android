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

import android.os.AsyncTask;
import android.widget.Toast;
import fvip.main.Drink;
import fvip.main.FlashClient;
import fvip.main.Globals;

public class AddOrderConnector extends AsyncTask<URL, Void, ArrayList<Drink>>
{
	/**
	 * Private variables.
	 */
	private ArrayList<Drink> drinks;
	private String result;

	/**
	 * Default constructor.
	 */
	public AddOrderConnector()
	{
		drinks = new ArrayList<Drink>();
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

			// Set up the strings to be encoded as name-value pairs.
			String drinkIds = "";
			String alcoholIds = "";
			String quantities = "";
			System.out.println("size:" + Globals.getTabDrinks().size());
			for (int i = 0; i < Globals.getTabDrinks().size(); i++)
			{
				System.out.println(Globals.getTabDrinks().get(i).getDrink().getId());
				System.out.println(Globals.getAlcohols().get(Globals.getTabDrinks(
						).get(i).getAlcoholPosition()).getId());

				drinkIds = drinkIds + Globals.getTabDrinks().get(i).getDrink().getId();
				alcoholIds = alcoholIds + Globals.getAlcohols().get(Globals.getTabDrinks(
						).get(i).getAlcoholPosition()).getId();
				quantities = quantities + (Globals.getTabDrinks().get(i).getQuantityPosition() + 1);
				if (i < Globals.getTabDrinks().size() - 1)
				{
					drinkIds = drinkIds + ",";
					alcoholIds = alcoholIds + ",";
					quantities = quantities + ",";
				}
			}

			// Set up the name-value pairs.
			NameValuePair nvpTableNumber = new BasicNameValuePair("table",
					Globals.getCurrentServer().getId());
			NameValuePair nvpClientId = new BasicNameValuePair("client_id", "jww93");
			NameValuePair nvpDrinkIds = new BasicNameValuePair("drink_ids", drinkIds);
			NameValuePair nvpAlcoholIds = new BasicNameValuePair("alcohol_ids", alcoholIds);
			NameValuePair nvpQuantities = new BasicNameValuePair("quantities", quantities);

			// Add the name-value pairs to the post data.
			postData.add(nvpTableNumber);
			postData.add(nvpClientId);
			postData.add(nvpDrinkIds);
			postData.add(nvpAlcoholIds);
			postData.add(nvpQuantities);

			// Set the post data to the request and execute the request.
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
			result = EntityUtils.toString(response.getEntity());
			if (!result.equals("1"))
			{
				Toast toast = Toast.makeText(Globals.getContext(), result, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		catch (IOException e)
		{
			drinks = null;
		}

		// Return drinks.
		return drinks;
	}

	@Override
	protected void onPostExecute(ArrayList<Drink> list)
	{	
		Globals.getTabDrinks().clear();
		Globals.goToMainScreen();
		FlashClient.updateAll();
		FlashClient.endLoading();
	}
}