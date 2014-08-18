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
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.widget.EditText;

import com.patron.system.Loadable;

public class FlashRemovePayment extends ActionBarActivity implements Loadable
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_remove_payment);
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
		// = (ListView)findViewById(R.id.paymentListOptions);
	}
}
