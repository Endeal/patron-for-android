package com.patron.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.patron.bind.PaymentBinder;
import com.patron.model.Funder;
import com.patron.model.User;
import com.patron.R;
import com.patron.system.Globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewFunders extends ListView
{
    private Context context;

    public ListViewFunders(Context context)
    {
        super(context);
    }

    public ListViewFunders(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
    }

    public ListViewFunders(Context context, AttributeSet attributeSet, int styleAttr)
    {
        super(context, attributeSet, styleAttr);
    }

    public void update()
    {
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
            {
                mapping.put("address", "");
            }
            else
            {
                mapping.put("address", funder.getAddress() + ", " + funder.getCity() + " " + funder.getState());
            }
            payments.add(mapping);
        }
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_payment, null);
        SimpleAdapter adapter = new SimpleAdapter(getContext(), payments, R.layout.list_item_payment, from, to);
        adapter.setViewBinder(new PaymentBinder());
        setAdapter(adapter);
    }
}
