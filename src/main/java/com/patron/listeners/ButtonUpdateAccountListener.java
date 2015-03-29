
package com.patron.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.patron.listeners.OnApiExecutedListener;
import com.patron.R;
import com.patron.system.ApiExecutor;
import com.patron.system.Globals;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ButtonUpdateAccountListener implements OnClickListener
{
    private OnApiExecutedListener listener;

    public ButtonUpdateAccountListener(OnApiExecutedListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.DialogMain);
        final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_update_account, null);
        builder.setView(dialogView);
        final EditText editTextEmail = (EditText)dialogView.findViewById(R.id.dialogUpdateAccountEditTextEmail);
        final EditText editTextPassword = (EditText)dialogView.findViewById(R.id.dialogUpdateAccountEditTextPassword);

        // Set default e-mail
        if (!Globals.getProvider().equals("fb") &&
                !Globals.getProvider().equals("tw") &&
                !Globals.getProvider().equals("gp"))
        {
            editTextEmail.setText(Globals.getEmail());
        }

        final Button buttonDone = (Button)dialogView.findViewById(R.id.dialogUpdateAccountButtonDone);
        final AlertDialog dialog = builder.create();
        buttonDone.setOnClickListener(new OnClickListener() {
            public void onClick(View view)
            {
                ApiExecutor executor = new ApiExecutor();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String firstName = Globals.getUser().getFirstName();
                String lastName = Globals.getUser().getLastName();

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

                executor.updateAccount(email, password, firstName, lastName, new OnApiExecutedListener() {
                    @Override
                    public void onExecuted()
                    {
                        if (listener != null)
                        {
                            listener.onExecuted();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }
}
