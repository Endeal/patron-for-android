package fvip.main;

import java.util.ArrayList;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import fvip.listeners.ButtonAddListener;

public class DrinkBinder implements SimpleAdapter.ViewBinder
{
	public boolean setViewValue(View view, Object data, String textRepresentation)
    {
        // Add the tag to each ImageButton.
        if (view.getId() == R.id.imgbut_add)
        {
            ((ImageButton)view).setTag(data.toString());
            ((ImageButton)view).setOnClickListener(new ButtonAddListener());
            return true;
        }
        // Set up the alcohol spinner.
        if (view.getId() == R.id.spinner_alcohol)
        {
        	ArrayList<String> names = new ArrayList<String>();
        	
        	// Add alcohols based on current drink's alcohol type.
        	Drink currentDrink = Globals.getOrderById(data.toString());
        	for (int i = 0; i < Globals.getAlcohols().size(); i++)
        	{
        		if (Globals.getAlcohols().get(i).getAlcohol() == currentDrink.getAlcohol())
        			names.add(Globals.getAlcohols().get(i).getName());
        	}
        	ArrayAdapter<String> alcohol_adapter = new ArrayAdapter<String>(Globals.getContext(),
        	        android.R.layout.simple_spinner_item, names);
        	((Spinner)view).setAdapter(alcohol_adapter);
        	
        	// Set the listener for the spinner
        	final int drink_id = Integer.parseInt(data.toString());
        	((Spinner)view).setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> adapter, View view,
						int position, long id)
				{
					Globals.setArrayToAlcohol(drink_id, position);
				}

				public void onNothingSelected(AdapterView<?> arg0)
				{
					
				}
        	});
        	return true;
        }
        else if (view.getId() == R.id.spinner_quantity)
        {
        	// Set the listener for the spinner
        	final int drink_id = Integer.parseInt(data.toString());
        	((Spinner)view).setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> adapter, View view,
						int position, long id)
				{
					Globals.setArrayToAlcohol(drink_id, position);
				}

				public void onNothingSelected(AdapterView<?> arg0)
				{
				}
        	});
        	return true;
        }
        return false;
    }
}