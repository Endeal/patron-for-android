package me.endeal.patron.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import me.endeal.patron.adapters.CartAdapter;
import me.endeal.patron.model.Order;
import me.endeal.patron.system.Globals;
import me.endeal.patron.R;

import org.joda.time.DateTime;

public class EditCartDialog extends Dialog
{
    public EditCartDialog(Context context)
    {
        super(context);
        init();
    }

    public EditCartDialog(Context context, int theme)
    {
        super(context, theme);
        init();
    }

    public void init()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cart);

        RecyclerView recycler = (RecyclerView)findViewById(R.id.dialogCartRecyclerViewMain);
        Button done = (Button)findViewById(R.id.dialogCartButtonDone);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        CartAdapter adapter = new CartAdapter(getContext(), Globals.getOrder().getFragments());
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        final Dialog dialog = this;
        done.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
    }
}
