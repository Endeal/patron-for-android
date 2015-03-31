package com.patron.listeners;

import android.content.Context;
import android.content.Intent;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

import com.patron.bind.CodeBinder;
import com.patron.main.FlashMenu;
import com.patron.main.FlashScan;
import com.patron.model.Code;
import com.patron.model.Order;
import com.patron.R;
import com.patron.system.Globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnCodesRefreshListener implements OnApiExecutedListener
{
  private ListView listCodes;

  public OnCodesRefreshListener(ListView listCodes)
  {
    this.listCodes = listCodes;
  }

  @Override
  public void onExecuted()
  {
    List<Order> codes = Globals.getOrders();

    if (codes == null || codes.size() == 0)
    {
        Context context = listCodes.getContext();
        Intent intent = new Intent(context, FlashMenu.class);
        context.startActivity(intent);
    }

    List<Map<String, String>> codesMap = new ArrayList<Map<String, String>>();

    String[] from = {"textTime",
        "textStatus",
        "textOrders"};

    int[] to = {R.id.codeListItemTextTime,
        R.id.codeListItemTextStatus,
        R.id.codeListItemTextProducts};

    // Don't update the list if the codes are empty.
    if (codes == null || codes.isEmpty())
      return;

    // Map the values to the elements of the list item.
    for (int i = 0; i < codes.size(); i++)
    {
      Map<String, String> mapping = new HashMap<String, String>();
      Order order = codes.get(i);
      mapping.put("textTime", order.getTime());
      mapping.put("textStatus", Order.getStatusText(order.getStatus()));
      mapping.put("textOrders", order.getOrderText());
      codesMap.add(mapping);
    }

    // Bind the values to the elements of the list item.
    SimpleAdapter adapter = new SimpleAdapter(listCodes.getContext(), codesMap, R.layout.list_item_code, from, to);
    adapter.setViewBinder(new CodeBinder());
    listCodes.setAdapter(adapter);
    listCodes.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapter, View v, int item, long row)
  		{
  			Intent intent = new Intent(v.getContext(), FlashScan.class);
  			intent.putExtra("orderRow", "" + item);
  			v.getContext().startActivity(intent);
  		}
    });
    adapter.notifyDataSetChanged();
  }
}
