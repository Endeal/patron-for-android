package fvip.listeners;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;
import fvip.db.CodeConnector;
import fvip.db.DrinkConnector;
import fvip.db.ServerConnector;
import fvip.lists.ListLinks;
import fvip.lists.ListScreens;
import fvip.main.FlashClient;
import fvip.main.Globals;
import fvip.main.Server;

public class ListMainListeners
{
	// URLs
	public static URL server_url;
	public static URL drinks_url;

	public static OnItemClickListener getMainListener()
	{
		OnItemClickListener listener;
		switch (Globals.getCurrentScreen())
		{
		case ListScreens.SCREEN_CHOOSE_BAR: // Select a bar out of all of them.
			listener = new ScreenBarListener();
			break;
		case ListScreens.SCREEN_SEARCH_SERVERS: // Select a bar when searching.
			listener = new ScreenBarListener();
			break;
		case ListScreens.SCREEN_BROWSE_DRINKS: // Browse drinks at current bar
			listener = new ScreenDrinksListener();
			break;
		case ListScreens.SCREEN_BAR_TOP: // Top Drinks at Current Bar screen
			listener = new ScreenDrinksListener();
			break;
		case ListScreens.SCREEN_MY_TOP: // My Top 7 screen
			listener = new ScreenDrinksListener();
			break;
		case ListScreens.SCREEN_SEARCH_DRINKS: // Search Drinks screen
			listener = new ScreenDrinksListener();
			break;
		case ListScreens.SCREEN_TAB: // Tab screen
			listener = new ScreenTabListener();
			break;
		case ListScreens.SCREEN_CODES: // Purchased Codes screen
			listener = new ScreenPurchasedCodesListener();
			break;
		default: // Main Menu screen
			listener = new ScreenMainListener();
			break;
		}
		return listener;
	}

	// Main Menu screen listener
	public static class ScreenMainListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> adapter, View v, int item,
				long row)
		{
			switch (item)
			{
			case 0: // Choose Bar...
				
				ServerConnector dbconnector = new ServerConnector();
				try
				{
					try
					{
						server_url = new URL(ListLinks.LINK_GET_SERVERS);
						
					}
					catch (MalformedURLException e)
					{
						e.printStackTrace();
					}
					dbconnector.execute(server_url);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				break;
			case 1: // Browse Drinks
				if (Globals.getCurrentServer() != null &&
				Globals.getAllOrders() != null &&
				!Globals.getAllOrders().isEmpty())
				{
					Globals.setCurrentScreen(ListScreens.SCREEN_BROWSE_DRINKS);
					FlashClient.updateListViewAdapter();
				}
			case 2: // Bar's Top 7
				if (Globals.getCurrentServer() != null &&
				Globals.getServerTopOrders() != null &&
				Globals.getServerTopOrders().size() > 0)
				{
					Globals.setCurrentScreen(ListScreens.SCREEN_BAR_TOP);
					FlashClient.updateListViewAdapter();
				}
				else
				{
					Toast noBarTop7 = Toast.makeText(Globals.getContext(),
							"Failed to retrieve list of the bar's top drinks.",
							Toast.LENGTH_SHORT);
					noBarTop7.show();
				}
				break;
			case 3: // My Top 7
				if (Globals.getCurrentServer() != null &&
				Globals.getMyTopOrders() != null &&
				Globals.getMyTopOrders().size() > 0)
				{
					Globals.setCurrentScreen(ListScreens.SCREEN_MY_TOP);
					FlashClient.updateListViewAdapter();
				}
				else
				{
					Toast noMyTop7 = Toast.makeText(Globals.getContext(),
							"Failed to retrieve list of my top drinks.",
							Toast.LENGTH_SHORT);
					noMyTop7.show();
				}
				break;
			case 4: // Add Random Drink to Tab
				if (Globals.getCurrentServer() != null &&
				Globals.getAllOrders() != null && 
				!Globals.getAllOrders().isEmpty())
				{
					Toast toastRandomDrinkSelected = Toast.makeText(Globals.getContext(),
							"Random drink selected and added to tab.",
							Toast.LENGTH_LONG);
					toastRandomDrinkSelected.show();
				}
				else
				{
					Toast toastNoRandomDrinkSelected = Toast.makeText(Globals.getContext(), 
							"Failed to retrieve the list of drinks.",
							Toast.LENGTH_LONG);
					toastNoRandomDrinkSelected.show();
				}
				break;
			case 5: // Purchase Codes
				try
				{
					URL url = new URL(ListLinks.LINK_GET_CODES);
					CodeConnector codeconnect = new CodeConnector();
					codeconnect.execute(url);
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
				break;
			case 6: // Settings
				Globals.setCurrentScreen(ListScreens.SCREEN_SETTINGS);
				FlashClient.updateListViewAdapter();
				break;
			}
		}
	}

	// Choose Bar screen listener
	public static class ScreenBarListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> adapter, View v, int item,
				long row)
		{
			Server server = Globals.getSearchServers().get(item);
			Globals.setServerName(server.getName());
			Globals.setCurrentServer(server);
			DrinkConnector dbdrink = new DrinkConnector();
			try
			{
				drinks_url = new URL(ListLinks.LINK_GET_DRINKS);
				dbdrink.execute(drinks_url);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	// Select Drinks screen listener
	public static class ScreenDrinksListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> adapter, View view, int item,
				long row)
		{
			//System.out.println("Drink clicked!");
			//Drink order = Globals.getAllOrders().get(item);
			//Globals.addOrderToTab(order);
		}
	}
	
	// Tab Drinks screen listener
	public static class ScreenTabListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> adapter, View view, int item,
				long row)
		{
			//Drink order = Globals.getAllOrders().get(item);
			//Globals.addOrderToTab(order);
		}
	}

	// Bar's Top 7 screen listener
	public static class ScreenBarTopListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> adapter, View view, int item,
				long row)
		{
			
		}
	}

	// My Top 7 screen listener
	public static class ScreenMyTopListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> adapter, View view, int item,
				long row)
		{
			
		}
	}
	
	// Purchased Codes screen listener
	public static class ScreenPurchasedCodesListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> adapter, View view, int item, long row)
		{
			InputStream is = null;
			try
			{
				is = (InputStream) new URL(ListLinks.LINK_DIRECTORY_CODES +
						Globals.getOrderCodes().get(item)).getContent();
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
	        Drawable d = Drawable.createFromStream(is, "QR Code");
	        
	        // Display the code
	        final ImageView code = new ImageView(Globals.getContext());
	        LinearLayout linearlayout2 = (LinearLayout) view.getParent().getParent();
	        final ListView lv = (ListView) view.getParent();
	        lv.setVisibility(View.GONE);
	        code.setImageDrawable(d);
	        linearlayout2.addView(code, new LayoutParams(linearlayout2.getWidth(),
	        		linearlayout2.getWidth()));
	        code.setOnClickListener(new OnClickListener() {
				public void onClick(View view)
				{
					code.setVisibility(View.GONE);
					lv.setVisibility(View.VISIBLE);
				}
	        });
		}
	}
}
