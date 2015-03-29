package com.patron.bind;

import com.patron.lists.ListFonts;
import com.patron.R;

import android.graphics.Color;
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
			TextView text = (TextView) view;
			String string = data.toString();
			text.setText(string);
			return true;
		}
		else if (view.getId() == R.id.codeListItemTextStatus)
		{
			TextView text = (TextView) view;
			String string = data.toString();
            if (string.toLowerCase().equals("ready"))
            {
                text.setTextColor(Color.GREEN);
            }
            if (!string.toLowerCase().equals("waiting"))
            {
                text.setTextColor(Color.YELLOW);
            }
			text.setText(string);
			return true;
		}
		else if (view.getId() == R.id.codeListItemTextTime)
		{
			TextView text = (TextView) view;
			String string = data.toString();
			text.setText(string);
			return true;
		}
        return false;
    }
}
