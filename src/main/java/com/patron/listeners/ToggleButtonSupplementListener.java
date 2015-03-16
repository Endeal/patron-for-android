package com.patron.listeners;

import android.view.View;
import android.view.View.OnClickListener;

import com.patron.model.Fragment;
import com.patron.model.Supplement;
import com.patron.R;

import java.util.List;

public class ToggleButtonSupplementListener implements OnClickListener
{
  private Fragment fragment;
  private Supplement supplement;

  public ToggleButtonSupplementListener(Fragment fragment, Supplement supplement)
  {
    setFragment(fragment);
    setSupplement(supplement);
  }

  @Override
  public void onClick(View view)
  {
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
  }

  public void setFragment(Fragment fragment)
  {
    this.fragment = fragment;
  }

  public void setSupplement(Supplement supplement)
  {
    this.supplement = supplement;
  }

  public Fragment getFragment()
  {
    return this.fragment;
  }

  public Supplement getSupplement()
  {
    return this.supplement;
  }
}
