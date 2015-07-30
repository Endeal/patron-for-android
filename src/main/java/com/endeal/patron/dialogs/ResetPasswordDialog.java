package com.endeal.patron.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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

public class ResetPasswordDialog extends Dialog
{
    private boolean submitting;

    public ResetPasswordDialog(Context context)
    {
        super(context);
        init();
    }

    public ResetPasswordDialog(Context context, int theme)
    {
        super(context, theme);
        init();
    }

    public void init()
    {
        submitting = false;
        final Dialog dialogView = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_request_password);
        final EditText editTextDialogResetPasswordEmail = (EditText)findViewById(R.id.dialogResetPasswordEditTextEmail);
        final Button buttonDialogResetPasswordSubmit = (Button)findViewById(R.id.dialogResetPasswordButtonSubmit);
        final ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminate(true);
        buttonDialogResetPasswordSubmit.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                if (submitting)
                    return;
                submitting = true;
                final RelativeLayout layout = (RelativeLayout)view.getRootView().findViewById(R.id.dialogResetPasswordRelativeLayoutMain);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                layout.addView(progressBar, params);
                ApiExecutor executor = new ApiExecutor();
                String email = editTextDialogResetPasswordEmail.getText().toString();
                Credential credential = new Credential(email, "", "endeal");
                executor.resetPassword(credential, new OnApiExecutedListener() {
                    @Override
                    public void onExecuted(ApiResult result)
                    {
                        submitting = false;
                        layout.removeView(progressBar);
                        if (result.getStatusCode() == 200)
                        {
                            dialogView.dismiss();
                        }
                        else
                        {
                            Snackbar.make(view.getRootView(), result.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
