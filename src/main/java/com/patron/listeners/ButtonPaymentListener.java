package com.patron.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.patron.bind.PaymentBinder;
import com.patron.model.Funder;
import com.patron.R;
import com.patron.system.Globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonPaymentListener implements OnClickListener
{
    private OnApiExecutedListener listener;

    public ButtonPaymentListener(OnApiExecutedListener listener)
    {
        this.listener = listener;
    }

    public void onClick(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.DialogMain);
        final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_payment, null);
        builder.setView(dialogView);
        ListView listPayment = (ListView)dialogView.findViewById(R.id.dialogPaymentListMain);
        Button buttonCancel = (Button)dialogView.findViewById(R.id.dialogPaymentButtonCancel);
        final AlertDialog dialog = builder.create();

        // Set up the payment dialog list
        List<Map<String, String>> payments = new ArrayList<Map<String, String>>();
        String[] from = {"number", "bankName", "type", "address"};
        int[] to = {R.id.paymentListItemTextNumber,R.id.paymentListItemTextBankName,
            R.id.paymentListItemTextType, R.id.paymentListItemTextAddress};
        for (int i = 0; i < Globals.getUser().getFunders().size(); i++)
        {
            Map<String, String> mapping = new HashMap<String, String>();
            Funder funder = Globals.getUser().getFunders().get(i);
            mapping.put("number", funder.getNumber());
            mapping.put("bankName", funder.getBankName());
            mapping.put("type", funder.getType().substring(0, 1).toUpperCase() + funder.getType().substring(1));
            if (funder.getAddress() == null || funder.getCity() == null || funder.getState() == null)
                mapping.put("address", "");
            else
                mapping.put("address", funder.getAddress() + ", " + funder.getCity() + " " + funder.getState());
            payments.add(mapping);
        }
        SimpleAdapter adapter = new SimpleAdapter(view.getContext(), payments, R.layout.list_item_payment, from, to);
        adapter.setViewBinder(new PaymentBinder());
        listPayment.setAdapter(adapter);
        dialog.show();

        listPayment.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (Globals.getUser() != null && Globals.getUser().getFunders() != null &&
                    Globals.getUser().getFunders().size() > position)
                {
                    Funder funder = Globals.getUser().getFunders().get(position);
                    Globals.getOrder().setFunder(funder);
                    listener.onExecuted();
                    dialog.dismiss();
                }
            }
        });

        // Set up the payment dialog cancel button
        buttonCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
    }
}
