
package me.endeal.patron.listeners;

import me.endeal.patron.model.Funder;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.PatronApplication;
import me.endeal.patron.view.QustomDialogBuilder;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
        /*
        final Context context = view.getContext();

        // If removing, stop right there.
        if (removing)
        {
          Toast.makeText(context, "Already attempting to remove funding instrument.", Toast.LENGTH_SHORT).show();
          listener.onExecuted();
          return;
        }

        if (Globals.getUser() != null && Globals.getUser().getFunders() != null &&
            Globals.getUser().getFunders().size() > position)
        {
            final Funder funder = Globals.getUser().getFunders().get(position);
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                  switch (which)
                  {
                    case DialogInterface.BUTTON_POSITIVE:
                        ListItemSettingsFunderListener.removing = true;
                        ApiExecutor executor = new ApiExecutor();
                        executor.removeFunder(funder, new OnApiExecutedListener() {
                            @Override
                            public void onExecuted()
                            {
                                ListItemSettingsFunderListener.removing = false;
                                listener.onExecuted();
                            }
                        });
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                      break;
                  }
                }
            };
            QustomDialogBuilder builder = new QustomDialogBuilder(context);
            builder = builder.setMessage("Are you sure you want to remove this funding instrument?");
            builder = builder.setMessageColor("#FFFFFF");
            AlertDialog.Builder newBuilder = builder.setPositiveButton("Yes", dialogClickListener);
            newBuilder = newBuilder.setNegativeButton("No", dialogClickListener);
            newBuilder.show();
        }
        */
    }
}
