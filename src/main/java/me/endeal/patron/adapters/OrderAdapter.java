package me.endeal.patron.adapters;

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
import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.DateTime;

import me.endeal.patron.bind.OrderViewHolder;
import me.endeal.patron.model.Attribute;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Option;
import me.endeal.patron.model.Order;
import me.endeal.patron.model.Retrieval;
import me.endeal.patron.model.Location;
import me.endeal.patron.model.Price;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;
import static me.endeal.patron.model.Order.Status;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder>
{
    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders)
    {
        this.context = context;
        this.orders = orders;
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
        Order order = orders.get(position);
        orderViewHolder.setOrder(order);

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
        DateTimeZone timezone = DateTimeZone.getDefault();
        long offset = timezone.getOffset(order.getTime());
        DateTime date = new DateTime(order.getTime() + offset);
        //String s = date.toString("hh:mma, E MM/dd/yyyy");
        String s = date.toString("hh:mma MM/dd/yyyy");
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
        else if (retrieval.getMethod().equals("pickup"))
        {
            orderViewHolder.getRetrieval().setImageResource(R.drawable.ic_directions_walk_black_48dp);
        }
        else if (retrieval.getMethod().equals("delivery"))
        {
            orderViewHolder.getRetrieval().setImageResource(R.drawable.ic_local_shipping_black_48dp);
        }
        else if (retrieval.getMethod().equals("service"))
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
