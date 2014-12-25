package com.patron.bind;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.LinearLayout.LayoutParams;

import com.patron.lists.ListFonts;
import com.patron.main.R;

public class NavigationBinder implements SimpleAdapter.ViewBinder
{
    public boolean setViewValue(View view, Object data, String textRepresentation)
    {
        TextView text = (TextView)view;
        /*
        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_BOLD);
        text.setTypeface(typeface);
        */
        text.setText(data.toString());
        return true;
    }
}
