package me.endeal.patron.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.exception.AuthenticationException;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.lists.ListKeys;
import me.endeal.patron.model.ApiResult;
import me.endeal.patron.R;
import me.endeal.patron.system.ApiExecutor;

import org.joda.time.DateTime;

public class CardDialog extends Dialog
{
    public CardDialog(Context context)
    {
        super(context);
        init();
    }

    public CardDialog(Context context, int theme)
    {
        super(context, theme);
        init();
    }

    public void init()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_card);
        final EditText number = (EditText)findViewById(R.id.dialogCardEditTextNumber);
        final EditText cvv = (EditText)findViewById(R.id.dialogCardEditTextCvv);
        final Button month = (Button)findViewById(R.id.dialogCardButtonMonth);
        final Button year = (Button)findViewById(R.id.dialogCardButtonYear);
        final Button submit = (Button)findViewById(R.id.dialogCardButtonSubmit);

        // Set month
        final DateTime currentTime = new DateTime();
        month.setText(currentTime.getMonthOfYear() + "");
        month.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
                for (int i = 1; i < 12; i++)
                {
                    popup.getMenu().add(Menu.NONE, i, Menu.NONE, i + "");
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        month.setText(menuItem.getItemId() + "");
                        popup.dismiss();
                        return true;
                    }
                });
                popup.show();
            }
        });

        // Set year
        year.setText(currentTime.getYear() + "");
        year.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
                for (int i = currentTime.getYear(); i < currentTime.getYear() + 10; i++)
                {
                    popup.getMenu().add(Menu.NONE, i, Menu.NONE, i + "");
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        year.setText(menuItem.getItemId() + "");
                        popup.dismiss();
                        return true;
                    }
                });
                popup.show();
            }
        });

        // Submit button
        final Dialog dialog = this;
        submit.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                final Card card = new Card(number.getText().toString(), Integer.parseInt(month.getText().toString()),
                    Integer.parseInt(year.getText().toString()), cvv.getText().toString());
                if (!card.validateCard())
                {
                    Snackbar.make(view.getRootView(), "Invalid card", Snackbar.LENGTH_SHORT).show();
                }
                Stripe stripe = null;
                try
                {
                    stripe = new Stripe(ListKeys.STRIPE_PUBLIC_KEY);
                    stripe.createToken(card, new TokenCallback() {
                        public void onSuccess(Token token)
                        {
                            String tokenId = token.getId();
                            System.out.println("Token:" + tokenId);
                            ApiExecutor executor = new ApiExecutor();
                            executor.addFunder(tokenId, new OnApiExecutedListener() {
                                @Override
                                public void onExecuted(ApiResult result)
                                {
                                    if (result.getStatusCode() == 201)
                                    {
                                        dialog.dismiss();
                                    }
                                    else
                                        Snackbar.make(view.getRootView(), result.getMessage(), Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        }

                        public void onError(Exception error)
                        {
                            Snackbar.make(view.getRootView(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
                catch (AuthenticationException e)
                {
                    e.printStackTrace();
                    Snackbar.make(view.getRootView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
