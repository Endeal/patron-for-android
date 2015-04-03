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

public class ButtonCategory extends ButtonFilter implements OnClickListener
{
  private Category category = null;
  private OnApiExecutedListener listener = null;
  private boolean checked = false;

  public ButtonCategory(Context context)
  {
    super(context);
    init();
  }

  public ButtonCategory(Context context, Category category, OnApiExecutedListener listener)
  {
    super(context);
    this.category = category;
    this.listener = listener;
    init();
  }

  public ButtonCategory(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init();
  }

  public ButtonCategory(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    init();
  }

  public void init()
  {
    setTag(category.getId());
    setBackgroundResource(R.drawable.checkbox_category_unpressed);
    setTextAppearance(getContext(), R.style.ButtonCategory);
    setAllCaps(false);
    float width = Globals.convertDpToPixel(60, getContext());
    float height = Globals.convertDpToPixel(60, getContext());
    float marginPx = Globals.convertDpToPixel(5, getContext());
    int margin = (int)marginPx;
    LayoutParams params = new LayoutParams((int)width, (int)height);
    params.gravity = Gravity.CENTER;
    params.setMargins(margin, margin, margin, margin);
    setLayoutParams(params);
    setText(category.getName());
    setOnClickListener(this);
  }

  @Override
  public void onClick(View view)
  {
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
      List<Category> categories = item.getCategories();
      for (int j = 0; j < categories.size(); j++)
      {
        String categoryId = categories.get(j).getId();
        if (category.getId().equals(categoryId))
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
      setBackgroundResource(R.drawable.button_category_pressed);
    }
    else
    {
      setBackgroundResource(R.drawable.button_category_unpressed);
    }
  }

  public void setCategory(Category category)
  {
    this.category = category;
  }

  public Category getCategory()
  {
    return category;
  }
}
