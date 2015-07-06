package me.endeal.patron.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import me.endeal.patron.bind.SupplementBinder;
import me.endeal.patron.R;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Item;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonSupplementsListener implements OnClickListener
{
  private Fragment fragment;

  public ButtonSupplementsListener(Fragment fragment)
  {
    this.fragment = fragment;
  }

  @Override
  public void onClick(View view)
  {
      /*
    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.DialogMain);
    final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View dialogView = inflater.inflate(R.layout.dialog_supplements, null);
    builder.setView(dialogView);
    Button buttonDone = (Button)dialogView.findViewById(R.id.dialogSupplementsButtonDone);
    GridView grid = (GridView)dialogView.findViewById(R.id.dialogSupplementsListViewMain);

    // Get the adapter
    List<Map<String, ToggleButtonSupplementListener>> supplements =
        new ArrayList<Map<String, ToggleButtonSupplementListener>>();
    String[] from = {"name"};
    int[] to = {R.id.listItemSupplementToggleButtonMain};
    Item item = fragment.getItem();
    for (int i = 0; i < item.getSupplements().size(); i++)
    {
      Supplement supplement = item.getSupplements().get(i);
      ToggleButtonSupplementListener listener =
          new ToggleButtonSupplementListener(fragment, supplement);
      Map<String, ToggleButtonSupplementListener> mapping =
          new HashMap<String, ToggleButtonSupplementListener>();
      mapping.put("name", listener);
      supplements.add(mapping);
    }
    SimpleAdapter adapter = new SimpleAdapter(view.getContext(), supplements, R.layout.list_item_supplement, from, to);
    adapter.setViewBinder(new SupplementBinder());

    // Create the dialog
    grid.setAdapter(adapter);
    final AlertDialog dialog = builder.create();
    buttonDone.setOnClickListener(new OnClickListener() {
        public void onClick(View view)
        {
            dialog.dismiss();
        }
    });
    dialog.show();
    */
  }
}
