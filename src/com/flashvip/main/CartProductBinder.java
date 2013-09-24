package com.flashvip.main;

import java.util.ArrayList;


import android.graphics.Typeface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.TextView;

import com.flashvip.listeners.ButtonRemoveListener;
import com.flashvip.listeners.SpinnerAlcoholListener;
import com.flashvip.listeners.SpinnerQuantityListener;
import com.flashvip.lists.ListFonts;

public class CartProductBinder implements ViewBinder
{
	public boolean setViewValue(View view, Object data, String textRepresentation)
	{
		// Change the font of any text.
		if (view.getId() == R.id.cartListItemTextName ||
				view.getId() == R.id.cartListItemTextPrice)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_BOLD);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
		}
		else if (view.getId() == R.id.cartListItemTextType ||
				view.getId() == R.id.cartListItemTextAlcohol)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_REGULAR);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
		}
        // Add the tag to each Remove Button.
		else if (view.getId() == R.id.cartListItemButtonRemove)
        { 
			int row = Integer.parseInt(data.toString());
            CartProduct product = Globals.getCartProducts().get(row);
            ButtonRemoveListener listener = new ButtonRemoveListener((FlashCart)view.getContext(), product);
            ((Button)view).setOnClickListener(listener);
            System.out.println("GOT HERE");
            return true;
        }
		// Set up the alcohol spinner.
		else if (view.getId() == R.id.cartListItemSpinnerAlcohol)
		{
			// Set the array of alcohol names.
			ArrayList<String> names = new ArrayList<String>();

			// Add alcohols based on current drink's alcohol type.
			int row = Integer.parseInt(data.toString());
			CartProduct product = Globals.getCartProducts().get(row);
			for (int i = 0; i < Globals.getAlcohols().size(); i++)
			{
				if (Globals.getAlcohols().get(i).getAlcohol() == product.getDrink().getAlcohol())
				{
					names.add(Globals.getAlcohols().get(i).getName());
				}
			}
			
			// Set to invisible if empty.
			if (names.size() == 0)
			{
				view.setVisibility(View.INVISIBLE);
			}
			else
			{
				// Set to visible if not empty.
				view.setVisibility(View.VISIBLE);
				
				// Bind the spinner adapter to the names.
				ArrayAdapter<String> alcohol_adapter = new ArrayAdapter<String>(view.getContext(),
						android.R.layout.simple_spinner_item,
						names);
				((Spinner)view).setAdapter(alcohol_adapter);

				// Set the listener and the default item
				((Spinner)view).setSelection(product.getAlcoholPosition());
				((Spinner)view).setOnItemSelectedListener(new SpinnerAlcoholListener((FlashCart)view.getContext(), row));
			}
			return true;
		}
        // Set up the quantity spinner.
        else if (view.getId() == R.id.cartListItemSpinnerQuantity)
        {
        	final int row = Integer.parseInt(data.toString());
        	((Spinner)view).setSelection(Globals.getCartProducts().get(row).getQuantityPosition());
        	((Spinner)view).setOnItemSelectedListener(new SpinnerQuantityListener((FlashCart)view.getContext(), row));
        	return true;
        }
        return false;
    }
}