package com.flashvip.listeners;

import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.flashvip.main.Product;
import com.flashvip.main.Globals;
import com.flashvip.main.CartProduct;

public class ListItemMenuAddListener implements OnItemClickListener
{	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int row,
			long rowId)
	{
		// Get the layout containing this view, the 'Add' button
		RelativeLayout rl = (RelativeLayout) v;
		
		// Get the spinners, which are the 4th and 5th view in the list_item_drink.xml layout file.
		Spinner spinnerAlcohol = (Spinner) rl.getChildAt(5);
		Spinner spinnerQuantity = (Spinner) rl.getChildAt(6);
		
		// Gets the drink for this given row.
		Product product = Globals.getCurrentProducts().get(row);
		
		// Creates a TabDrink that has the given drink, gets the spinners position, and quantities position.
		int alcoholRow;
		
		if (spinnerAlcohol.getVisibility() == View.INVISIBLE)
			alcoholRow = -1;
		else
			alcoholRow = spinnerAlcohol.getSelectedItemPosition();
		
		CartProduct td = new CartProduct(product, alcoholRow,
				spinnerQuantity.getSelectedItemPosition());
		
		// Adds the TabDrink that was just created to the tab.
		Globals.addCartProduct(td);
		
		// Creates a Toast that informs the user that the drink has been added to the tab.
		Toast toast = Toast.makeText(v.getContext(),
				"Drink added to tab.", Toast.LENGTH_SHORT);
		toast.show();
	}
}