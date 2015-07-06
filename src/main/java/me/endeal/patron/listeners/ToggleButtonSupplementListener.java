package me.endeal.patron.listeners;

import android.view.View;
import android.view.View.OnClickListener;

import me.endeal.patron.model.Fragment;
import me.endeal.patron.R;

import java.util.List;

public class ToggleButtonSupplementListener implements OnClickListener
{
  private Fragment fragment;

  public ToggleButtonSupplementListener(Fragment fragment)
  {
    setFragment(fragment);
  }

  @Override
  public void onClick(View view)
  {
      /*
    if (fragment.hasSupplement(supplement))
    {
      List<Supplement> supplements = fragment.getSupplements();
      supplements.remove(supplement);
      fragment.setSupplements(supplements);
      view.setBackgroundResource(R.drawable.button_supplement_unpressed);
    }
    else
    {
      List<Supplement> supplements = fragment.getSupplements();
      supplements.add(supplement);
      fragment.setSupplements(supplements);
      view.setBackgroundResource(R.drawable.button_supplement_pressed);
    }
    */
  }

  public void setFragment(Fragment fragment)
  {
    this.fragment = fragment;
  }

  public Fragment getFragment()
  {
    return this.fragment;
  }
}
