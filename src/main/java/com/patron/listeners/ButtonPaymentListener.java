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
import android.widget.Toast;

import com.patron.bind.PaymentBinder;
import com.patron.model.Funder;
import com.patron.R;
import com.patron.system.Globals;
import com.patron.view.ListViewFunders;

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
        // Cancel out if there are no funders.
        if (Globals.getUser().getFunders() == null || Globals.getUser().getFunders().size() == 0)
        {
          Toast.makeText(view.getContext(), "You have no payment options.", Toast.LENGTH_SHORT).show();
          return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.DialogMain);
        final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_payment, null);
        builder.setView(dialogView);
        ListViewFunders listPayment = (ListViewFunders)dialogView.findViewById(R.id.dialogPaymentListMain);
        Button buttonCancel = (Button)dialogView.findViewById(R.id.dialogPaymentButtonCancel);
        final AlertDialog dialog = builder.create();

        // Set up the payment dialog list
        dialog.show();
        listPayment.update();
        listPayment.setOnItemClickListener(new ListItemPaymentListener(listener, dialog));

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
