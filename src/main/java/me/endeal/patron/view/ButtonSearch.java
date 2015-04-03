package me.endeal.patron.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.model.Category;
import me.endeal.patron.model.Item;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

public class ButtonSearch extends ButtonFilter implements OnClickListener
{
  private OnApiExecutedListener listener = null;
  private boolean checked = false;
  private EditText editTextSearch;

  public ButtonSearch(Context context)
  {
    super(context);
    init();
  }

  public ButtonSearch(Context context, OnApiExecutedListener listener)
  {
    super(context);
    this.listener = listener;
    init();
  }

  public ButtonSearch(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init();
  }

  public ButtonSearch(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    init();
  }

  public void setListener(OnApiExecutedListener listener)
  {
    this.listener = listener;
  }

  public OnApiExecutedListener getListener()
  {
    return this.listener;
  }

  public void init()
  {
    setBackgroundResource(R.drawable.button_search_unpressed);
    setTextAppearance(getContext(), R.style.ButtonSearch);
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
    if (this.listener != null)
    {
      this.listener.onExecuted();
    }
  }

  @Override
  public void setChecked(boolean checked)
  {
    super.setChecked(checked);
    View rootView = ((Activity)getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
    editTextSearch = (EditText)rootView.findViewById(R.id.menuEditTextSearch);
    if (checked)
    {
      setBackgroundResource(R.drawable.button_search_pressed);
      editTextSearch.setVisibility(View.VISIBLE);
      boolean focused = editTextSearch.requestFocus();
      if (focused)
      {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
      }
      editTextSearch.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable s)
          {
            if (s == null)
              return;
            String term = s.toString();
            List<Item> items = new ArrayList<Item>();
            for (int i = 0; i < Globals.getVendor().getItems().size(); i++)
            {
                Item item = Globals.getVendor().getItems().get(i);
                if (item.getName().toLowerCase().contains(term.toLowerCase()))
                {
                  items.add(item);
                }
            }
            Globals.getVendor().setFilteredItems(items);

            if (getListener() != null)
            {
              getListener().onExecuted();
            }
          }
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
          {
          }
          public void onTextChanged(CharSequence s, int start, int before, int count) {}
       });
    }
    else
    {
      setBackgroundResource(R.drawable.button_search_unpressed);
      editTextSearch.setVisibility(View.GONE);
      Globals.getVendor().setFilteredItems(Globals.getVendor().getItems());
      InputMethodManager inputManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
      inputManager.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
    }
  }
}
