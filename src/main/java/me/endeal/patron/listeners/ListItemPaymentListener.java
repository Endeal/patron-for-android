package me.endeal.patron.listeners;

import me.endeal.patron.model.User;
import me.endeal.patron.model.Funder;
import me.endeal.patron.system.Globals;

import java.util.List;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ListItemPaymentListener implements OnItemClickListener
{
    OnApiExecutedListener listener;
    Dialog dialog;

    public ListItemPaymentListener(OnApiExecutedListener listener, Dialog dialog)
    {
        this.listener = listener;
        this.dialog = dialog;
    }

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
}
