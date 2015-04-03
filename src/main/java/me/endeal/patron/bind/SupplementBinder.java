package me.endeal.patron.bind;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

import me.endeal.patron.listeners.ToggleButtonSupplementListener;
import me.endeal.patron.lists.ListFonts;
import me.endeal.patron.R;
import me.endeal.patron.model.Supplement;

import java.text.NumberFormat;

public class SupplementBinder implements SimpleAdapter.ViewBinder
{
    public boolean setViewValue(View view, Object data, String textRepresentation)
    {
        ToggleButtonSupplementListener listener = (ToggleButtonSupplementListener)data;
        Supplement supplement = listener.getSupplement();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String price = formatter.format(supplement.getPrice());
        Button button = (Button)view;
        String text = supplement.getName() + "\n" + price;
        button.setOnClickListener(listener);
        button.setText(text);
        if (listener.getFragment().hasSupplement(supplement))
        {
          button.setBackgroundResource(R.drawable.button_supplement_pressed);
        }
        else
        {
          button.setBackgroundResource(R.drawable.button_supplement_unpressed);
        }
        return true;
    }
}
