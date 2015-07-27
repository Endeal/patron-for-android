package me.endeal.patron.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.model.ApiResult;
import me.endeal.patron.model.Patron;
import me.endeal.patron.model.Credential;
import me.endeal.patron.R;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class UpdateAccountButtonListener implements OnClickListener
{
    private OnApiExecutedListener listener;

    public UpdateAccountButtonListener(OnApiExecutedListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(final View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_update_account, null);
        builder.setView(dialogView);
        final EditText editTextEmail = (EditText)dialogView.findViewById(R.id.dialogUpdateAccountEditTextEmail);
        final EditText editTextPassword = (EditText)dialogView.findViewById(R.id.dialogUpdateAccountEditTextPassword);

        // Set default e-mail
        if (Globals.getCredential() != null &&
                !Globals.getCredential().getProvider().equals("facebook") &&
                !Globals.getCredential().getProvider().equals("twitter") &&
                !Globals.getCredential().getProvider().equals("googlePlus"))
        {
            editTextEmail.setText(Globals.getCredential().getIdentifier());
        }

        final Button buttonDone = (Button)dialogView.findViewById(R.id.dialogUpdateAccountButtonDone);
        final AlertDialog dialog = builder.create();
        buttonDone.setOnClickListener(new OnClickListener() {
            public void onClick(final View view)
            {
                ApiExecutor executor = new ApiExecutor();
                final String email = editTextEmail.getText().toString();
                final String password = editTextPassword.getText().toString();

                // Validate e-mail format.
                Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher m = p.matcher(email);
                boolean matchFound = m.matches();
                if (!matchFound)
                {
                    Toast.makeText(view.getContext(), "Please enter a valid e-mail address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate text length.
                else if (email.length() == 0)
                {
                    Toast.makeText(view.getContext(), "Please enter an e-mail address", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (password.length() < 6)
                {
                    Toast.makeText(view.getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Patron patron = (Patron)Globals.deepClone(Globals.getPatron());
                for (int i = 0; i < patron.getIdentity().getCredentials().size(); i++)
                {
                    if (patron.getIdentity().getCredentials().get(i).getProvider().equals("endeal"))
                    {
                        patron.getIdentity().getCredentials().get(i).setIdentifier(email);
                        patron.getIdentity().getCredentials().get(i).setVerifier(password);
                    }
                }
                executor.updatePatron(patron, new OnApiExecutedListener() {
                    @Override
                    public void onExecuted(ApiResult result)
                    {
                        if (listener != null)
                        {
                            listener.onExecuted(null);
                        }
                        for (int i = 0; i < patron.getIdentity().getCredentials().size(); i++)
                        {
                            Credential credential = patron.getIdentity().getCredentials().get(i);
                            if (credential.getProvider().equals("endeal") &&
                                credential.getIdentifier().equals(email) &&
                                credential.getVerifier().equals(password))
                            {
                                dialog.dismiss();
                            }
                            else
                            {
                                Snackbar.make(view.getRootView(), "Failed to update account", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
        dialog.show();
    }
}
