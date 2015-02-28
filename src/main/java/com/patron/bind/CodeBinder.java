package com.patron.bind;

import com.patron.lists.ListFonts;
import com.patron.R;

import android.graphics.Typeface;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CodeBinder implements SimpleAdapter.ViewBinder
{
	public boolean setViewValue(View view, Object data, String textRepresentation)
    {
		// Change the font of any text.
		if (view.getId() == R.id.codeListItemTextProducts)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(),
					ListFonts.FONT_MAIN_REGULAR);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
			String string = data.toString();
			text.setText(string);
			return true;
		}
		else if (view.getId() == R.id.codeListItemTextStatus)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(),
					ListFonts.FONT_MAIN_BOLD);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
			String string = data.toString();
			text.setText(string);
			return true;
		}
		else if (view.getId() == R.id.codeListItemTextTime)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(),
					ListFonts.FONT_MAIN_LIGHT);
			TextView text = (TextView) view;
			text.setTypeface(typeface);
			String string = data.toString();
			text.setText(string);
			return true;
		}
        return false;
    }
}
