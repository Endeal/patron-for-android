package com.patron.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;

import com.patron.R;

public class DialogLoading extends AlertDialog
{
    public DialogLoading(final Context context)
    {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
    }
}
