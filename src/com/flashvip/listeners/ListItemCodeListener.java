package com.flashvip.listeners;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;

import com.flashvip.lists.ListLinks;
import com.flashvip.system.Globals;

public class ListItemCodeListener implements OnItemClickListener
{
	public void onItemClick(AdapterView<?> adapter, View view, int item, long row)
	{
		InputStream is = null;
		try
		{
			is = (InputStream) new URL(ListLinks.LINK_DIRECTORY_CODES +
					Globals.getCodes().get(item)).getContent();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
        Drawable d = Drawable.createFromStream(is, "QR Code");
        
        // Display the code
        final ImageView code = new ImageView(view.getContext());
        LinearLayout linearlayout2 = (LinearLayout) view.getParent().getParent();
        final ListView lv = (ListView) view.getParent();
        lv.setVisibility(View.GONE);
        code.setImageDrawable(d);
        linearlayout2.addView(code, new LayoutParams(linearlayout2.getWidth(),
        		linearlayout2.getWidth()));
        code.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				code.setVisibility(View.GONE);
				lv.setVisibility(View.VISIBLE);
			}
        });
	}
}
