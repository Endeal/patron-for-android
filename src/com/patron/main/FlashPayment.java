package com.patron.main;

import java.lang.Exception;
import java.util.ArrayList;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
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
import android.app.AlertDialog;
import android.widget.EditText;

import com.patron.system.Loadable;

import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthError;

public class FlashPayment extends ActionBarActivity implements Loadable
{
	private SocialAuthAdapter socialAuthAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_payment);
		init();
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
            	switch (pos)
            	{
            		case 0:
            			Intent intent = new Intent(view.getContext(), FlashAddCard.class);
            			startActivity(intent);
            			break;
            		case 1:
            			//Intent intent = new Intent(view.getContext(), FlashAddBank.class);
            			//startActivity(intent);
            			break;
            		default:
            			break;
            	}
            }
        });
	}
}
