package me.endeal.patron.listeners;

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

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.main.FlashAddCard;
import me.endeal.patron.main.FlashCart;
import me.endeal.patron.main.FlashOrders;
import me.endeal.patron.model.Funder;
import me.endeal.patron.R;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.Patron;
import me.endeal.patron.view.DialogLoading;
import me.endeal.patron.view.QustomDialogBuilder;

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
                    SharedPreferences sharedPreferences = context.getSharedPreferences("me.endeal.patron", Context.MODE_PRIVATE);
                    Editor editor = sharedPreferences.edit();
                    editor.putString("tip", Globals.getOrder().getTipPercent().toString());
                    editor.putString("funder", Globals.getOrder().getFunder().getId());
                    editor.commit();

                    // Show loading dialog
                    final DialogLoading loadingDialog = new DialogLoading(view.getContext());
                    loadingDialog.setCancelable(false);
                    loadingDialog.setCanceledOnTouchOutside(false);
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
                            Intent intent = new Intent(activity, FlashOrders.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            //ActivityCompat.startActivity(activity, intent, bundle);
                            activity.startActivity(intent);
                            loadingDialog.dismiss();
                        }
                    });
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
                }
            }
        };
        QustomDialogBuilder builder = new QustomDialogBuilder(view.getContext());
        builder = builder.setMessageColor("#FFFFFF");
        AlertDialog.Builder newBuilder;
        String dialogMessage = "";
        // Add card dialog
        if (Globals.getOrder().getFunder() == null)
        {
            dialogMessage = "You have to add a debit/credit card to place an order. Add one now?";
            newBuilder = builder.setPositiveButton("Yes", addCardListener);
            newBuilder = builder.setPositiveButton("Yes", addCardListener).setNegativeButton("No", addCardListener);
        }

        // Place order dialog
        else
        {
            dialogMessage = "Are you sure you want to place this order?";
            newBuilder = builder.setPositiveButton("Yes", placeOrderListener);
            newBuilder = newBuilder.setNegativeButton("No", placeOrderListener);
        }
        builder = builder.setMessage(dialogMessage);
        newBuilder.show();
	}
}
