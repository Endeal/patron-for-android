package com.patron.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.patron.system.Globals;

public abstract class ButtonFilter extends Button
{
  private boolean checked;

  public ButtonFilter(Context context)
  {
    super(context);
  }

  public ButtonFilter(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }

  public ButtonFilter(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
  }

  public void setChecked(boolean checked)
  {
    if (checked == true)
    {
      if (Globals.getButtonFilter() != null)
      {
        Globals.getButtonFilter().setChecked(false);
      }
      Globals.setButtonFilter(this);
    }
    else
    {
      Globals.setButtonFilter(null);
    }
    this.checked = checked;
  }

  public boolean getChecked()
  {
    return this.checked;
  }
}
