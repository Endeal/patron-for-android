package me.endeal.patron.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.model.*;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class DialogOrderInfo extends AlertDialog
{
    private OnApiExecutedListener listener;
    private Order order;

    public DialogOrderInfo(Context context)
    {
        super(context);
    }

    public DialogOrderInfo(Context context, int theme)
    {
        super(context, theme);
    }

    public DialogOrderInfo(Context context, boolean cancelable, DialogInterface.OnCancelListener listener)
    {
        super(context, cancelable, listener);
    }

    public DialogOrderInfo(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener,
            OnApiExecutedListener listener, Order order)
    {
        super(context, cancelable, cancelListener);
        this.listener = listener;
        this.order = order;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Price Breakdown");

        // Set the price breakdown text.
        String text = "";
        List<Fragment> fragments = order.getFragments();
        Price sum = new Price(0, "USD");
        for (int i = 0; i < fragments.size(); i++)
        {
            Fragment fragment = fragments.get(i);
            Item item = fragment.getItem();
            String name = item.getName();
            Price price = item.getPrice();
            int quantity = fragment.getQuantity();
            text = text + name + " ($" + price.toString() + ")";
            List<Selection> selections = fragment.getSelections();
            if (selections != null && selections.size() > 0)
            for (int j = 0; j < selections.size(); j++)
            {
                Selection selection = selections.get(j);
                Option option = selection.getOption();
                text = text + "\n  " + option.getName() + "($" + option.getPrice().toString() + ")";
            }
            List<Option> supplements = fragment.getOptions();
            if (supplements != null && supplements.size() > 0)
            for (int j = 0; j < supplements.size(); j++)
            {
                Option supplement = supplements.get(j);
                text = text + "\n    " + supplement.getName() + "($" + supplement.getPrice().toString() + ")";
            }
            sum.add(fragment.getPrice());
            text = text + "\n x " + quantity + " = $" + fragment.getPrice().toString() + " ($" + sum.toString() + ")";
            text = text + "\n\n";
        }
        double taxRate = Globals.getVendor().getTaxRate() * 100;
        text = text + "Tax: $" + order.getTax().toString() + " (" + taxRate + "% of $" + order.getPrice().toString() + ")";
        sum.add(order.getTax());
        text = text + "\n  = $" + sum.toString();
        text = text + "\n\nTip: $" + order.getTip().toString();
        sum.add(order.getTip());
        text = text + "\n  = $" + sum.toString();
        text = text + "\n\nTotal: $" + order.getTotalPrice().toString();

        // Set the message
        setMessage(text);

        // Set done button
        final Dialog dialog = this;
        setButton(DialogInterface.BUTTON_NEUTRAL, "Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which)
            {
                if (listener != null)
                    listener.onExecuted();
                dialog.dismiss();
            }
        });

        // Set refund button
        setButton(DialogInterface.BUTTON_POSITIVE, "Request Refund", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which)
            {
                if (listener != null)
                    listener.onExecuted();
                dialog.dismiss();
            }
        });
    }
}
