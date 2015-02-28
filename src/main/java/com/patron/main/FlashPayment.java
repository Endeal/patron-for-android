package com.patron.main;

import java.lang.Exception;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.widget.EditText;

import com.patron.R;
import com.patron.system.Loadable;

public class FlashPayment extends Activity implements Loadable
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_payment);
		init();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
    	if (item.getItemId() == android.R.id.home)
    	{
        	Intent intent = new Intent(this, FlashProfile.class);
        	this.finish();
        	startActivity(intent);
        	return true;
    	}
    	return super.onOptionsItemSelected(item);
	}

	@Override
	public void beginLoading()
	{
	}

	@Override
	public void load()
	{
	}

	@Override
	public void endLoading()
	{
	}

	@Override
	public void update()
	{
	}

	@Override
	public void message(String msg)
	{
	}

	// Button actions
	public void init()
	{
		// Get the layout elements.
		ListView listOptions = (ListView)findViewById(R.id.paymentListOptions);

		// Create the list of payment options.
		ArrayList<String> options = new ArrayList<String>();
		options.add("Credit/Debit Card");
		options.add("Bank Account");
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
        listOptions.setAdapter(arrayAdapter);

        // Define the actions of each payment option upon being clicked.
        listOptions.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id)
            {
            	Intent intent;
            	switch (pos)
            	{
            		case 0:
            			intent = new Intent(view.getContext(), FlashAddCard.class);
            			startActivity(intent);
            			break;
            		case 1:
            			intent = new Intent(view.getContext(), FlashAddBankAccount.class);
            			startActivity(intent);
            			break;
            		default:
            			break;
            	}
            }
        });
	}
}
