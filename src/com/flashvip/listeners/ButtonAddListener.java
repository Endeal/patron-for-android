package com.flashvip.listeners;

import android.view.View;


import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.flashvip.main.Drink;
import com.flashvip.main.Globals;
import com.flashvip.main.TabDrink;

public class ButtonAddListener implements OnClickListener
{	
	public void onClick(View v)
	{
		// Get the drink ID, which is tagged in the DrinkBinder class to this view.
		String drinkId = (String)v.getTag();
		
		// Get the layout containing this view, the 'Add' button
		RelativeLayout rl = (RelativeLayout) v.getParent();
		
		// Get the spinners, which are the 4th and 5th view in the list_item_drink.xml layout file.
		Spinner spinnerAlcohol = (Spinner) rl.getChildAt(4);
		Spinner spinnerQuantity = (Spinner) rl.getChildAt(5);
		
		// Gets the drink that was tagged with this view.
		Drink d = Globals.getOrderById(drinkId);
		System.out.println("Drink: " + d.getId() + ", Name: " + d.getName()); // TEMP
		
		// Creates a TabDrink that has the given drink, gets the spinners position, and quantities position.
		TabDrink td = new TabDrink(d, spinnerAlcohol.getSelectedItemPosition(),
				spinnerQuantity.getSelectedItemPosition());
		
		// Adds the TabDrink that was just created to the tab.
		Globals.addOrderToTab(td);
		
		// Creates a Toast that informs the user that the drink has been added to the tab.
		Toast toast = Toast.makeText(Globals.getContext(),
				"Drink added to tab.", Toast.LENGTH_SHORT);
		toast.show();
	}
}