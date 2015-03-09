package com.patron.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.patron.R;
import com.patron.system.Globals;

public class ButtonCommentListener implements OnClickListener
{
    private OnApiExecutedListener listener;

    public ButtonCommentListener(OnApiExecutedListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.DialogMain);
        final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_comment, null);
        builder.setView(dialogView);
        Button buttonDone = (Button)dialogView.findViewById(R.id.dialogCommentButtonDone);
        final EditText fieldComment = (EditText)dialogView.findViewById(R.id.dialogCommentFieldComment);
        final AlertDialog dialog = builder.create();
        if (Globals.getOrder().getComment().length() > 0)
        {
            fieldComment.setText(Globals.getOrder().getComment());
        }
        buttonDone.setOnClickListener(new OnClickListener() {
            public void onClick(View view)
            {
                Globals.getOrder().setComment(fieldComment.getText().toString());
                listener.onExecuted();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
