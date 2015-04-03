package me.endeal.patron.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import me.endeal.patron.model.*;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ButtonReviewListener implements OnClickListener
{
    private OnApiExecutedListener listener;

    public ButtonReviewListener(OnApiExecutedListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.DialogMain);
        final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_review, null);
        builder.setView(dialogView);
        TextView textViewMain = (TextView)dialogView.findViewById(R.id.dialogReviewTextViewMain);
        Button buttonDone = (Button)dialogView.findViewById(R.id.dialogReviewButtonDone);

        // Set the price breakdown text.
        String text = "";
        Order order = Globals.getOrder();
        List<Fragment> fragments = order.getFragments();
        BigDecimal sum = new BigDecimal(0);
        for (int i = 0; i < fragments.size(); i++)
        {
            Fragment fragment = fragments.get(i);
            Item item = fragment.getItem();
            String name = item.getName();
            BigDecimal price = item.getPrice();
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
            List<Supplement> supplements = fragment.getSupplements();
            if (supplements != null && supplements.size() > 0)
            for (int j = 0; j < supplements.size(); j++)
            {
                Supplement supplement = supplements.get(j);
                text = text + "\n    " + supplement.getName() + "($" + supplement.getPrice().toString() + ")";
            }
            sum = sum.add(fragment.getPrice());
            text = text + "\n x " + quantity + " = $" + fragment.getPrice().toString() + " ($" + sum.toString() + ")";
            text = text + "\n\n";
        }
        BigDecimal taxRate = new BigDecimal(Globals.getVendor().getTaxRate());
        taxRate = taxRate.multiply(new BigDecimal(100));
        taxRate = taxRate.setScale(2, RoundingMode.FLOOR);
        text = text + "Tax: $" + order.getTax().toString() + " (" + taxRate + "% of $" + order.getPrice().toString() + ")";
        text = text + "\n  = $" + sum.add(order.getTax());
        text = text + "\n\nTip: $" + order.getTip().toString();
        text = text + "\n  = $" + sum.add(order.getTax()).add(order.getTip());
        text = text + "\n\nTotal: $" + order.getTotalPrice().toString();
        textViewMain.setText(text);

        final AlertDialog dialog = builder.create();
        buttonDone.setOnClickListener(new OnClickListener() {
            public void onClick(View view)
            {
                listener.onExecuted();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
