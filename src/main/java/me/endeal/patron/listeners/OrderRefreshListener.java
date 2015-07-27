package me.endeal.patron.listeners;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import me.endeal.patron.adapters.OrderAdapter;
import me.endeal.patron.model.ApiResult;
import me.endeal.patron.system.Globals;

public class OrderRefreshListener implements OnApiExecutedListener
{
    private SwipeRefreshLayout layout;
    private RecyclerView recycler;

    public OrderRefreshListener(SwipeRefreshLayout layout, RecyclerView recycler)
    {
        this.layout = layout;
        this.recycler = recycler;
    }

    @Override
    public void onExecuted(ApiResult result)
    {
        OrderAdapter adapter = new OrderAdapter(recycler.getContext(), Globals.getOrders());
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
