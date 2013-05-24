package fvip.main;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleAdapter;

public class DrinkAdapter extends SimpleAdapter
{

	public DrinkAdapter(Context context, List<Map<String, ?>> data,
			int resource, String[] from, int[] to)
	{
		super(context, data, resource, from, to);
	}
}
