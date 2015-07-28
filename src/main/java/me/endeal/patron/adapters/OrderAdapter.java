package com.endeal.patron.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.DateTime;

import com.endeal.patron.bind.OrderViewHolder;
import com.endeal.patron.dialogs.OrderDialog;
import com.endeal.patron.model.Attribute;
import com.endeal.patron.model.Fragment;
import com.endeal.patron.model.Option;
import com.endeal.patron.model.Order;
import com.endeal.patron.model.Retrieval;
import com.endeal.patron.model.Location;
import com.endeal.patron.model.Price;
import com.endeal.patron.model.Vendor;
import com.endeal.patron.R;
import static com.endeal.patron.model.Order.Status;
import static com.endeal.patron.model.Retrieval.Method;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder>
{
    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders)
    {
        this.context = context;
        this.orders = orders;
        sortOrders();
    }

    public void setOrders(List<Order> orders)
    {
        this.orders = orders;
        sortOrders();
    }

    private void sortOrders()
    {
        // Sort orders by timestamp
        boolean foundHigher = true;
        if (orders != null && orders.size() > 1)
        while (foundHigher)
        {
            foundHigher = false;
            for (int i = 1; i < orders.size(); i++)
            {
                Order order = orders.get(i);
                if (order.getTime() > orders.get(i - 1).getTime())
                {
                    orders.remove(i);
                    orders.add(i - 1, order);
                    foundHigher = true;
                    break;
                }
            }
        }
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_order, null);
        OrderViewHolder viewHolder = new OrderViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder orderViewHolder, int position)
    {
        final Order order = orders.get(position);
        orderViewHolder.itemView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OrderDialog dialog = new OrderDialog(view.getContext(), order);
                dialog.show();
            }
        });

        // Set title
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < order.getFragments().size(); i++)
        {
            Fragment fragment = order.getFragments().get(i);
            builder.append(fragment.toString());
            if (i < order.getFragments().size() - 1)
                builder.append("\n");
        }
        orderViewHolder.getTitle().setText(builder.toString());

        // Set time
        DateTimeZone utcTimeZone = DateTimeZone.UTC;
        long localTimeStamp = utcTimeZone.convertUTCToLocal(order.getTime());
        DateTime date = new DateTime(localTimeStamp);
        DateTime current = new DateTime();
        String s = date.toString("hh:mma MM/dd/yyyy");
        if (date.year().get() == current.year().get() && date.monthOfYear().get() == current.monthOfYear().get() && date.dayOfMonth().get() == current.dayOfMonth().get())
            s = date.toString("hh:mma") + " Today";
        orderViewHolder.getSubtitle().setText(s);

        // Set status
        if (order.getStatus() == Status.WAITING)
        {
            int color = R.color.waiting;
            orderViewHolder.getStatus().setImageTintList(ColorStateList.valueOf(context.getResources().getColor(color)));
            orderViewHolder.getStatus().setImageResource(R.drawable.ic_access_time_black_48dp);
        }
        else if (order.getStatus() == Status.READY)
        {
            int color = R.color.ready;
            orderViewHolder.getStatus().setImageTintList(ColorStateList.valueOf(context.getResources().getColor(color)));
            orderViewHolder.getStatus().setImageResource(R.drawable.ic_check_circle_black_48dp);
        }
        else if (order.getStatus() == Status.COMPLETED)
        {
            int color = R.color.completed;
            orderViewHolder.getStatus().setImageTintList(ColorStateList.valueOf(context.getResources().getColor(color)));
            orderViewHolder.getStatus().setImageResource(R.drawable.ic_history_black_48dp);
        }
        else
        {
            int color = R.color.returned;
            orderViewHolder.getStatus().setImageTintList(ColorStateList.valueOf(context.getResources().getColor(color)));
            orderViewHolder.getStatus().setImageResource(R.drawable.ic_error_black_48dp);
        }

        // Set retrieval
        Retrieval retrieval = order.getRetrieval();
        if (retrieval == null || retrieval.getMethod() == null)
            return;
        else if (retrieval.getMethod() == Method.Pickup)
        {
            orderViewHolder.getRetrieval().setImageResource(R.drawable.ic_directions_walk_black_48dp);
        }
        else if (retrieval.getMethod() == Method.Delivery)
        {
            orderViewHolder.getRetrieval().setImageResource(R.drawable.ic_local_shipping_black_48dp);
        }
        else if (retrieval.getMethod() == Method.Service)
        {
            orderViewHolder.getRetrieval().setImageResource(R.drawable.ic_pin_drop_black_48dp);
        }
        else
        {
            orderViewHolder.getRetrieval().setImageResource(R.drawable.ic_local_grocery_store_black_48dp);
        }
    }

    @Override
    public int getItemCount()
    {
        return (null != orders ? orders.size() : 0);
    }
}
