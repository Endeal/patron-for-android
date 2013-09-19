package com.flashvip.main;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import com.flashvip.listeners.ButtonDeleteListener;

public class TabDrinkBinder implements SimpleAdapter.ViewBinder
{
	public boolean setViewValue(View view, Object data, String textRepresentation)
    {
        // Add the tag to each ImageButton.
        if (view.getId() == R.id.cartListItemImageButtonDelete)
        { 
            ((ImageButton)view).setTag(data.toString());
            ((ImageButton)view).setOnClickListener(new ButtonDeleteListener());
            return true;
        }
        // Set up the alcohol spinner.
        else if (view.getId() == R.id.cartListItemSpinnerAlcohol)
        {
        	// Set the array of alcohol names.
        	ArrayList<String> names = new ArrayList<String>();
        	
        	// Add alcohols based on current drink's alcohol type.
        	String[] drinkInfo = data.toString().split(";");
        	Drink currentDrink = Globals.getOrderById(drinkInfo[0]);
        	for (int i = 0; i < Globals.getAlcohols().size(); i++)
        	{
        		if (Globals.getAlcohols().get(i).getAlcohol() == currentDrink.getAlcohol())
        		{
        			names.add(Globals.getAlcohols().get(i).getName());
        		}
        	}
        	ArrayAdapter<String> alcohol_adapter = new ArrayAdapter<String>(Globals.getContext(),
        	        android.R.layout.simple_spinner_item,
        	            names);
        	((Spinner)view).setAdapter(alcohol_adapter);
        	
        	// Set the listener and the default item
        	final int item = Integer.parseInt(drinkInfo[1]);
        	((Spinner)view).setSelection(Globals.getTabDrinks().get(item).getAlcoholPosition());
        	((Spinner)view).setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> adapter, View view,
						int position, long id)
				{
					Globals.getTabDrinks().get(item).setAlcohol(position);
				}

				public void onNothingSelected(AdapterView<?> adapter)
				{

				}
        	});
        	return true;
        }
        // Set up the quantity spinner.
        else if (view.getId() == R.id.cartListItemSpinnerQuantity)
        {
        	final int item = Integer.parseInt(data.toString());
        	((Spinner)view).setSelection(Globals.getTabDrinks().get(item).getQuantityPosition());
        	((Spinner)view).setOnItemSelectedListener(new OnItemSelectedListener() {

        		public void onItemSelected(AdapterView<?> adapter, View view,
        				int position, long id)
        		{
        			Globals.getTabDrinks().get(item).setQuantity(position);
        		}

				public void onNothingSelected(AdapterView<?> adapter)
				{
					
				}
        	});
        	return true;
        }
        return false;
    }
}