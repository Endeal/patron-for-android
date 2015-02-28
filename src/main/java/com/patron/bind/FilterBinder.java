package com.patron.bind;

import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.LinearLayout.LayoutParams;

import com.patron.lists.ListFonts;
import com.patron.R;

public class FilterBinder implements SimpleAdapter.ViewBinder
{
	public boolean setViewValue(View view, Object data, String textRepresentation)
    {
		// Change the font and create the button.
		if (view.getId() == R.id.menuLayoutTypes)
		{
			Button button = new Button(view.getContext());
			button.setBackgroundResource(R.drawable.button_category);
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_BOLD);
			button.setTypeface(typeface);

			LayoutParams params = new LayoutParams(55, 55);
			params.setMargins(10, 10, 10, 10);
			button.setLayoutParams(params);

			RelativeLayout relativeLayout = (RelativeLayout)view;
			relativeLayout.addView(button);
			return true;
		}

        return false;
    }
}
