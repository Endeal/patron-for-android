package com.endeal.patron.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.bind.FunderViewHolder;
import com.endeal.patron.model.ApiResult;
import com.endeal.patron.model.Attribute;
import com.endeal.patron.model.Funder;
import com.endeal.patron.model.Option;
import com.endeal.patron.model.Patron;
import com.endeal.patron.system.ApiExecutor;
import com.endeal.patron.system.Globals;
import com.endeal.patron.R;

public class FunderAdapter extends RecyclerView.Adapter<FunderViewHolder>
{
    private Context context;
    private List<Funder> funders;

    public FunderAdapter(Context context, List<Funder> funders)
    {
        this.context = context;
        this.funders = funders;
    }

    public void setFunders(List<Funder> funders)
    {
        this.funders = funders;
    }

    @Override
    public FunderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_funder, null);
        FunderViewHolder viewHolder = new FunderViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FunderViewHolder funderViewHolder, int i)
    {
        // Progress Indicator
        final ProgressBar progressIndicator = new ProgressBar(funderViewHolder.itemView.getContext());
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressIndicator.setLayoutParams(params);
        progressIndicator.setBackgroundColor(Color.TRANSPARENT);

        final Funder funder = funders.get(i);
        funderViewHolder.getTitle().setText(funder.toString());
        funderViewHolder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                final RelativeLayout relativeLayout = (RelativeLayout)view.getRootView().findViewById(R.id.settingsRelativeLayoutContent);
                new AlertDialog.Builder(view.getContext())
                    .setTitle("Delete credit/debit card")
                    .setMessage("Are you sure you want to delete this card from your account?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which)
                        {
                            relativeLayout.addView(progressIndicator);
                            ApiExecutor executor = new ApiExecutor();
                            executor.removeFunder(funder, new OnApiExecutedListener() {
                                @Override
                                public void onExecuted(ApiResult result)
                                {
                                    relativeLayout.removeView(progressIndicator);
                                    setFunders(Globals.getPatron().getFunders());
                                    notifyDataSetChanged();
                                    if (result.getStatusCode() != 200)
                                    {
                                        Snackbar.make(relativeLayout, result.getMessage(), Snackbar.LENGTH_SHORT).show();
                                    }
                                    else if (Globals.getOrder() != null && Globals.getOrder().getFunder() != null)
                                    {
                                        if (funder.getId().equals(Globals.getOrder().getFunder().getId()))
                                        {
                                            Globals.getOrder().setFunder(null);
                                        }
                                    }
                                }
                            });
                        }
                    }).create().show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return (null != funders ? funders.size() : 0);
    }
}
