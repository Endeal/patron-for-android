package fvip.main;

import java.util.ArrayList;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import fvip.listeners.ButtonDeleteListener;

public class TabDrinkBinder implements SimpleAdapter.ViewBinder
{
	public boolean setViewValue(View view, Object data, String textRepresentation)
    {
        // Add the tag to each ImageButton.
        if (view.getId() == R.id.imgbut_delete)
        { 
            ((ImageButton)view).setTag(data.toString());
            ((ImageButton)view).setOnClickListener(new ButtonDeleteListener());
            return true;
        }
        // Set up the alcohol spinner.
        else if (view.getId() == R.id.spinner_tab_alcohol)
        {
        	// Set the array of alcohol names.
        	ArrayList<String> names = new ArrayList<String>();
        	
        	// Add alcohols based on current drink's alcohol type.
        	String[] drinkInfo = data.toString().split(";");
        	Drink currentDrink = Globals.getOrderById(drinkInfo[0]);
        	for (int i = 0; i < Globals.getAlcohols().size(); i++)
        	{
        		System.out.println("a1");
        		if (Globals.getAlcohols().get(i).getAlcohol() == currentDrink.getAlcohol())
        		{
        			System.out.println("a2");
        			names.add(Globals.getAlcohols().get(i).getName());
        			System.out.println("a3");
        		}
        	}
        	System.out.println("a4");
        	ArrayAdapter<String> alcohol_adapter = new ArrayAdapter<String>(Globals.getContext(),
        	        android.R.layout.simple_spinner_item,
        	            names);
        	System.out.println("a5");
        	((Spinner)view).setAdapter(alcohol_adapter);
        	System.out.println("a6");
        	
        	// Set the listener and the default item
        	System.out.println("a7:DrinkInfo[1]:" + drinkInfo[1] + ";");
        	final int item = Integer.parseInt(drinkInfo[1]);
        	System.out.println("a8");
        	((Spinner)view).setSelection(Globals.getTabDrinks().get(item).getAlcoholPosition());
        	System.out.println("a9");
        	((Spinner)view).setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> adapter, View view,
						int position, long id)
				{
					Globals.getTabDrinks().get(item).setAlcohol(position);
					System.out.println("d" + item + " set to " + position);
				}

				public void onNothingSelected(AdapterView<?> adapter)
				{

				}
        	});
        	System.out.println("a10");
        	return true;
        }
        // Set up the quantity spinner.
        else if (view.getId() == R.id.spinner_tab_quantity)
        {
        	final int item = Integer.parseInt(data.toString());
        	((Spinner)view).setSelection(Globals.getTabDrinks().get(item).getQuantityPosition());
        	((Spinner)view).setOnItemSelectedListener(new OnItemSelectedListener() {

        		public void onItemSelected(AdapterView<?> adapter, View view,
        				int position, long id)
        		{
        			Globals.getTabDrinks().get(item).setQuantity(position);
        			System.out.println("q" + item + " set to " + position);
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