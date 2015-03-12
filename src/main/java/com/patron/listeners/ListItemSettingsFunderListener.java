
package com.patron.listeners;

import com.patron.model.User;
import com.patron.model.Funder;
import com.patron.system.ApiExecutor;
import com.patron.system.Globals;
import com.patron.system.Patron;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListItemSettingsFunderListener implements OnItemClickListener
{
    private OnApiExecutedListener listener;
    private static boolean removing;

    public ListItemSettingsFunderListener(OnApiExecutedListener listener)
    {
        this.listener = listener;
        this.removing = false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        final Context context = view.getContext();
        if (Globals.getUser() != null && Globals.getUser().getFunders() != null &&
            Globals.getUser().getFunders().size() > position)
        {
            Funder funder = Globals.getUser().getFunders().get(position);
            ApiExecutor executor = new ApiExecutor();
            if (!removing)
            {
                ListItemSettingsFunderListener.removing = true;
                executor.removeFunder(funder, new OnApiExecutedListener() {
                    @Override
                    public void onExecuted()
                    {
                        Toast.makeText(context, "Finished attempt funding instrument.", Toast.LENGTH_SHORT).show();
                        ListItemSettingsFunderListener.removing = false;
                        listener.onExecuted();
                    }
                });
            }
            else
            {
                Toast.makeText(context, "Already attempting to remove funding instrument.", Toast.LENGTH_SHORT).show();
                listener.onExecuted();
            }
        }
    }
}
