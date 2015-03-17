package com.patron.listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.patron.listeners.OnApiExecutedListener;
import com.patron.main.FlashAddCard;
import com.patron.main.FlashCart;
import com.patron.main.FlashCodes;
import com.patron.model.Funder;
import com.patron.R;
import com.patron.system.ApiExecutor;
import com.patron.system.Globals;
import com.patron.system.Patron;
import com.patron.view.DialogLoading;

import java.math.BigDecimal;
import java.util.List;

public class ButtonFinishListener implements OnClickListener
{
    @Override
	public void onClick(View tempView)
	{
        final View view = tempView;

        // Dialog listener for adding card.
        DialogInterface.OnClickListener addCardListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Intent intent = new Intent(view.getContext(), FlashAddCard.class);
                    intent.putExtra("activity", "cart");
                    view.getContext().startActivity(intent);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
                }
            }
        };

        // Dialog listener for placing order.
        DialogInterface.OnClickListener placeOrderListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    // Save the tip and payment method
                    Context context = Patron.getContext();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("com.patron", Context.MODE_PRIVATE);
                    Editor editor = sharedPreferences.edit();
                    editor.putString("tip", Globals.getOrder().getTipPercent().toString());
                    editor.putString("funder", Globals.getOrder().getFunder().getId());
                    editor.commit();

                    // Show loading dialog
                    final DialogLoading loadingDialog = new DialogLoading(view.getContext());
                    loadingDialog.show();

                    ApiExecutor apiExecutor = new ApiExecutor();
                    apiExecutor.addOrder(Globals.getOrder(), view.getContext(), new OnApiExecutedListener() {
                        @Override
                        public void onExecuted()
                        {
                            Activity activity = (Activity)view.getContext();
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                activity, view, "buttonFinish");
                            Bundle bundle = options.toBundle();
                            Intent intent = new Intent(activity, FlashCodes.class);
                            //ActivityCompat.startActivity(activity, intent, bundle);
                            activity.startActivity(intent);
                            loadingDialog.dismiss();
                            activity.finish();
                        }
                    });
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.DialogMain);

        // Add card dialog
        if (Globals.getUser().getFunders() == null || Globals.getUser().getFunders().size() == 0)
        {
            builder.setMessage("You have to add a debit/credit card to place an order. Add one now?").
                setPositiveButton("Yes", addCardListener).setNegativeButton("No", addCardListener).show();
        }

        // Place order dialog
        else
        {
          builder.setMessage("Are you sure you want to place this order?").setPositiveButton("Yes", placeOrderListener)
              .setNegativeButton("No", placeOrderListener).show();
        }
	}
}
