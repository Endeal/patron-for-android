package com.endeal.patron.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.model.ApiResult;
import com.endeal.patron.model.Patron;
import com.endeal.patron.model.Credential;
import com.endeal.patron.R;
import com.endeal.patron.system.ApiExecutor;
import com.endeal.patron.system.Globals;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class UpdateAccountButtonListener implements OnClickListener
{
    private OnApiExecutedListener listener;
    private boolean submitting;

    public UpdateAccountButtonListener(OnApiExecutedListener listener)
    {
        this.listener = listener;
        this.submitting = false;
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
        final ProgressBar progressBar = new ProgressBar(view.getContext());
        progressBar.setIndeterminate(true);
        buttonDone.setOnClickListener(new OnClickListener() {
            public void onClick(final View view)
            {
                if (submitting)
                    return;
                submitting = true;
                final RelativeLayout layout = (RelativeLayout)view.getRootView().findViewById(R.id.dialogUpdateAccountRelativeLayoutMain);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                layout.addView(progressBar, params);
                ApiExecutor executor = new ApiExecutor();
                final String email = editTextEmail.getText().toString();
                final String password = editTextPassword.getText().toString();

                // Validate e-mail format.
                Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher m = p.matcher(email);
                boolean matchFound = m.matches();
                if (!matchFound)
                {
                    Snackbar.make(view.getRootView(), "Please enter a valid e-mail address", Snackbar.LENGTH_SHORT).show();
                    submitting = false;
                    layout.removeView(progressBar);
                    return;
                }

                // Validate text length.
                else if (email.length() == 0)
                {
                    Snackbar.make(view.getRootView(), "Please enter an e-mail address", Snackbar.LENGTH_SHORT).show();
                    submitting = false;
                    layout.removeView(progressBar);
                    return;
                }
                else if (password.length() < 6)
                {
                    Snackbar.make(view.getRootView(), "Password must be at least 6 characters", Snackbar.LENGTH_SHORT).show();
                    submitting = false;
                    layout.removeView(progressBar);
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
                        submitting = false;
                        layout.removeView(progressBar);
                        if (result.getStatusCode() == 200)
                        {
                            Snackbar.make(view.getRootView(), "Successfully updated account", Snackbar.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else
                        {
                            Snackbar.make(view.getRootView(), result.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();
    }
}
