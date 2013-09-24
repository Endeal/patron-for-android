package com.flashvip.listeners;

import java.net.MalformedURLException;

import java.net.URL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.flashvip.db.AddOrderConnector;
import com.flashvip.lists.ListLinks;
import com.flashvip.lists.ListScreens;
import com.flashvip.main.FlashClient;
import com.flashvip.main.Globals;

public class ButtonTabListener implements OnClickListener
{

	public void onClick(View view)
	{
		if (Globals.getTabProducts() != null &&
				!Globals.getTabProducts().isEmpty())
		{
			if (Globals.getCurrentScreen() != ListScreens.SCREEN_TAB)
			{
				Globals.setCurrentScreen(ListScreens.SCREEN_TAB);
			}
			else
			{
				Button button_tab = (Button)view;
				button_tab.setText("Close Tab");
				DialogInterface.OnClickListener dialogClickListener =
						new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				        	AddOrderConnector aoconnect = new AddOrderConnector();
				        	URL url;
							try
							{
								url = new URL(ListLinks.LINK_ADD_ORDER);
								aoconnect.execute(url);
							}
							catch (MalformedURLException e)
							{
								Toast toast = Toast.makeText(Globals.getContext(), 
										"Connection error.", Toast.LENGTH_SHORT);
								toast.show();
							}
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
				            break;
				        }
				    }
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(Globals.getContext());
				builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
				    .setNegativeButton("No", dialogClickListener).show();
			}
			FlashClient.updateListViewAdapter();
		}
		else
		{
			Toast error = Toast.makeText(Globals.getContext(),
					"No drinks added to tab yet.",
					Toast.LENGTH_SHORT);
			error.show();
		}
	}

}
