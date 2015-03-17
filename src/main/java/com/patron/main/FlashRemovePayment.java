package com.patron.main;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.DialogInterface;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.widget.EditText;

import com.patron.R;
import com.patron.system.Loadable;
import com.patron.system.Globals;
import com.patron.db.RemoveCardConnector;
import com.patron.db.RemoveBankAccountConnector;
import com.patron.model.User;
import com.patron.model.Funder;
import com.patron.bind.PaymentBinder;

public class FlashRemovePayment extends Activity implements Loadable
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
		ListView removePaymentListMain = (ListView)findViewById(R.id.removePaymentListMain);
		List<Map<String, String>> payments = new ArrayList<Map<String, String>>();

		String[] from = {"number",
		"bankName",
		"type"};

		int[] to = {R.id.paymentListItemTextNumber,
			R.id.paymentListItemTextBankName,
			R.id.paymentListItemTextType};
		for (int i = 0; i < Globals.getUser().getFunders().size(); i++)
		{
			Map<String, String> mapping = new HashMap<String, String>();
			Funder funder = Globals.getUser().getFunders().get(i);
			mapping.put("number", funder.getNumber());
			mapping.put("bankName", funder.getBankName());
			mapping.put("type", funder.getType().substring(0, 1).toUpperCase() + funder.getType().substring(1));
			payments.add(mapping);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, payments, R.layout.list_item_payment, from, to);
		adapter.setViewBinder(new PaymentBinder());
		removePaymentListMain.setAdapter(adapter);

		removePaymentListMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
					final int pos = position;
					final View v = view;
					new AlertDialog.Builder(v.getContext()).setTitle("Remove Payment Method").setMessage("Are you sure you want to remove this payment method?")
    				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        				public void onClick(DialogInterface dialog, int which)
        				{
        					Funder funder = Globals.getUser().getFunders().get(pos);
        					if (funder.getType().equals("checking") || funder.getType().equals("savings"))
        					{
        						RemoveBankAccountConnector removeBankAccountConnector = new RemoveBankAccountConnector(getActivity(), funder);
        						removeBankAccountConnector.execute(v.getContext());
        					}
        					else
        					{
        						RemoveCardConnector removeCardConnector = new RemoveCardConnector(getActivity(), funder);
        						removeCardConnector.execute(v.getContext());
        					}
        				}
     				})
    				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
        				public void onClick(DialogInterface dialog, int which)
        				{
        				}
    				})
    				.setIcon(android.R.drawable.ic_dialog_alert).show();
			}
		});
	}

	public Loadable getActivity()
	{
		return this;
	}
}
