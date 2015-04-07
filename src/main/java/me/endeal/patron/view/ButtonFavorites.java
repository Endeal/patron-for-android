package me.endeal.patron.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.model.Category;
import me.endeal.patron.model.Item;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

public class ButtonFavorites extends ButtonFilter implements OnClickListener
{
  private OnApiExecutedListener listener = null;
  private boolean checked = false;

  public ButtonFavorites(Context context)
  {
    super(context);
    init();
  }

  public ButtonFavorites(Context context, OnApiExecutedListener listener)
  {
    super(context);
    this.listener = listener;
    init();
  }

  public ButtonFavorites(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init();
  }

  public ButtonFavorites(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    init();
  }

  public void setListener(OnApiExecutedListener listener)
  {
    this.listener = listener;
  }

  public void init()
  {
    setBackgroundResource(R.drawable.button_filter_favorites_unpressed);
    setTextAppearance(getContext(), R.style.ButtonFavorites);
    float width = Globals.convertDpToPixel(60, getContext());
    float height = Globals.convertDpToPixel(60, getContext());
    LayoutParams params = new LayoutParams((int)width, (int)height);
    params.gravity = Gravity.CENTER;
    params.setMargins(10, 10, 10, 10);
    setLayoutParams(params);
    setOnClickListener(this);
  }

  @Override
  public void onClick(View view)
  {
      if (Globals.getVendor() == null || Globals.getItems() == null || Globals.getItems().size() == 0)
          return;
    if (getChecked())
    {
      setChecked(false);
      Globals.getVendor().setFilteredItems(Globals.getVendor().getItems());
      if (this.listener != null)
      {
        this.listener.onExecuted();
      }
      return;
    }
    setChecked(true);
    List<Item> items = new ArrayList<Item>();
    for (int i = 0; i < Globals.getVendor().getItems().size(); i++)
    {
      Item item = Globals.getVendor().getItems().get(i);
      for (int j = 0; j < Globals.getUser().getItems().size(); j++)
      {
        String itemId = Globals.getUser().getItems().get(j);
        if (itemId.equals(item.getId()))
        {
          items.add(item);
        }
      }
    }
    Globals.getVendor().setFilteredItems(items);
    if (this.listener != null)
    {
      this.listener.onExecuted();
    }
  }

  @Override
  public void setChecked(boolean checked)
  {
    super.setChecked(checked);
    if (checked)
    {
      setBackgroundResource(R.drawable.button_filter_favorites_pressed);
    }
    else
    {
      setBackgroundResource(R.drawable.button_filter_favorites_unpressed);
    }
  }
}
