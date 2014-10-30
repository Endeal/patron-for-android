package com.patron.main;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.content.Intent;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
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

import com.patron.system.Loadable;
import com.patron.system.Globals;
import com.patron.db.RemoveCardConnector;
import com.patron.db.RemoveBankAccountConnector;
import com.patron.model.User;
import com.patron.model.Card;
import com.patron.model.BankAccount;
import com.patron.bind.PaymentBinder;

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
		ListView removePaymentListMain = (ListView)findViewById(R.id.removePaymentListMain);
		List<Map<String, String>> payments = new ArrayList<Map<String, String>>();

		String[] from = {"number",
		"bankName",
		"type",
		"address"};

		int[] to = {R.id.removePaymentListItemTextNumber,
			R.id.removePaymentListItemTextBankName,
			R.id.removePaymentListItemTextType,
			R.id.removePaymentListItemTextAddress};
		for (int i = 0; i < Globals.getUser().getCards().size(); i++)
		{	
			Map<String, String> mapping = new HashMap<String, String>();
			Card card = Globals.getUser().getCards().get(i);
			mapping.put("number", card.getNumber());
			mapping.put("bankName", card.getBankName());
			mapping.put("type", card.getType().substring(0, 1).toUpperCase() + card.getType().substring(1));
			if (card.getAddress() == null || card.getCity() == null || card.getState() == null)
				mapping.put("address", "");
			else
				mapping.put("address", card.getAddress() + ", " + card.getCity() + " " + card.getState());
			payments.add(mapping);
		}
		for (int i = 0; i < Globals.getUser().getBankAccounts().size(); i++)
		{	
			Map<String, String> mapping = new HashMap<String, String>();
			BankAccount bankAccount = Globals.getUser().getBankAccounts().get(i);
			mapping.put("number", bankAccount.getNumber());
			mapping.put("bankName", bankAccount.getBankName());
			mapping.put("type", bankAccount.getType().substring(0, 1).toUpperCase() + bankAccount.getType().substring(1));
			if (bankAccount.getAddress() == null || bankAccount.getCity() == null || bankAccount.getState() == null)
				mapping.put("address", "");
			else
				mapping.put("address", bankAccount.getAddress() + ", " + bankAccount.getCity() + " " + bankAccount.getState());
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
        					if (pos <= Globals.getUser().getCards().size() - 1)
        					{
        						Card card = Globals.getUser().getCards().get(pos);
        						RemoveCardConnector removeCardConnector = new RemoveCardConnector(getActivity(), card);
        						removeCardConnector.execute(v.getContext());
        					}
        					else
        					{
        						int num = pos - Globals.getUser().getBankAccounts().size();
        						BankAccount bankAccount = Globals.getUser().getBankAccounts().get(num);
        						RemoveBankAccountConnector removeBankAccountConnector = new RemoveBankAccountConnector(getActivity(), bankAccount);
        						removeBankAccountConnector.execute(v.getContext());
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