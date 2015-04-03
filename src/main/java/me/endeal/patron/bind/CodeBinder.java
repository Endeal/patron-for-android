package me.endeal.patron.bind;

import me.endeal.patron.lists.ListFonts;
import me.endeal.patron.R;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
            if (string.toLowerCase().equals("waiting"))
            {
                text.setTextColor(Color.YELLOW);
            }
            else if (string.toLowerCase().equals("ready"))
            {
                text.setTextColor(Color.GREEN);
            }
            else
            {
                text.setTextColor(Color.RED);
            }
			text.setText(string);
			return true;
		}
		else if (view.getId() == R.id.codeListItemTextTime)
		{
			TextView text = (TextView) view;
			String string = data.toString();

			// Update the time for locale and format it properly.
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			DateTime gmtTime = formatter.parseDateTime(string);
			DateTimeZone timezone = DateTimeZone.getDefault();
			long offset = timezone.getOffset(gmtTime);
			DateTime date = new DateTime(gmtTime.getMillis() + offset);
			String s = date.toString("hh:mma, E MM/dd/yyyy");
			text.setText(s);
			return true;
		}
        return false;
    }
}
