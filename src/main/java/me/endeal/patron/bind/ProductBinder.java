package me.endeal.patron.bind;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.view.View;
import android.view.View.OnClickListener;

import me.endeal.patron.listeners.ButtonSupplementsListener;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.listeners.ToggleButtonFavoriteListener;
import me.endeal.patron.lists.ListFonts;
import me.endeal.patron.model.Attribute;
import me.endeal.patron.model.Category;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Item;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

import java.util.List;
import java.text.NumberFormat;

public class ProductBinder implements SimpleAdapter.ViewBinder
{
	public boolean setViewValue(View view, Object data, String textRepresentation)
    {
		// Name text view
		if (view.getId() == R.id.productListItemTextName)
		{
			TextView text = (TextView) view;
			Fragment fragment = (Fragment)data;
			String itemId = fragment.getItem().getId();
			String name = fragment.getItem().getName();
			if (Globals.getVendor().getRewardItems().get(itemId) != null)
			{
				name = name + "\n(+" + Globals.getVendor().getRewardItems().get(itemId).toString() + " points)";
			}
			text.setText(name);
			return true;
		}
		// Price text view
		if (view.getId() == R.id.productListItemTextPrice)
		{
			TextView text = (TextView)view;
			Fragment fragment = (Fragment)data;
			Item item = fragment.getItem();
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			String price = formatter.format(item.getPrice());
			text.setText(price);
			return true;
		}

		// Set the categories text view
		/*
		else if (view.getId() == R.id.productListItemTextCategories)
		{
			Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), ListFonts.FONT_MAIN_BOLD);
			TextView text = (TextView) view;
			Fragment fragment = (Fragment)data;
			Item item = fragment.getItem();
			String categoriesText = "";
			for (int j = 0; j < item.getCategories().size(); j++)
			{
					if (!categoriesText.equals(""))
					{
							categoriesText = categoriesText + "\n";
					}
					categoriesText = categoriesText + item.getCategories().get(j).getName();
			}
			System.out.println("FRAGMENT CATEGORIES POSTBIND:" + categoriesText);
			text.setText(categoriesText);
			//text.setTypeface(typeface);
			return true;
		}
		*/

		// Favorite button
		else if (view.getId() == R.id.productListItemToggleButtonFavorite)
		{
			final ToggleButton toggle = (ToggleButton) view;
			Fragment fragment = (Fragment)data;
			final Item item = fragment.getItem();
			ToggleButtonFavoriteListener listener = new ToggleButtonFavoriteListener(item);
			toggle.setOnClickListener(listener);
			listener.setOnApiExecutedListener(new OnApiExecutedListener() {
				@Override
				public void onExecuted()
				{
					toggle.setChecked(Globals.getUser().hasFavoriteItem(item.getId()));
				}
			});
			toggle.setChecked(Globals.getUser().hasFavoriteItem(item.getId()));
			return true;
		}

		// Supplements button
		else if (view.getId() == R.id.productListItemButtonSupplements)
		{
			Fragment fragment = (Fragment)data;
			Button button = (Button)view;
			Item item = fragment.getItem();
			if (item.getSupplements() != null && item.getSupplements().size() > 0)
			{
				ButtonSupplementsListener listener = new ButtonSupplementsListener(fragment);
				button.setOnClickListener(listener);
				button.setVisibility(View.VISIBLE);
			}
			else
			{
				button.setVisibility(View.GONE);
			}
			return true;
		}

		else if (view.getId() == R.id.productListItemLayout)
		{
			RelativeLayout relativeLayout = (RelativeLayout)view;
			Fragment fragment = (Fragment)data;
			Item item = fragment.getItem();

			// Remove excess attributes.
			for (int i = 0; i < relativeLayout.getChildCount(); i++)
			{
				View v = relativeLayout.getChildAt(i);
				if (v.getTag() != null && v.getTag().equals("attribute"))
				{
					relativeLayout.removeViewAt(i);
				}
			}

			// Create the attributes
			if (item.getAttributes() != null &&
					!item.getAttributes().isEmpty())
			{
				Spinner lastSpinner = null;
				for (int i = 0; i < item.getAttributes().size(); i++)
				{
					Attribute attribute = item.getAttributes().get(i);
					String[] optionNames = new String[attribute.getOptions().size()];
					for (int j = 0; j < attribute.getOptions().size(); j++)
					{
						optionNames[j] = attribute.getOptions().get(j).getName();
					}
					ArrayAdapter<String>arrayAdapter = new
							ArrayAdapter<String>(view.getContext(),
									android.R.layout.simple_spinner_item,
									optionNames);
					Spinner spinner = new Spinner(view.getContext());
					spinner.setTag("attribute");
					spinner.setAdapter(arrayAdapter);
					float marginBottom = Globals.convertDpToPixel(10, view.getContext());
					float marginRight = Globals.convertDpToPixel(10, view.getContext());
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 0, (int)marginRight, (int)marginBottom);
					params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					if (lastSpinner == null)
					{
						params.addRule(RelativeLayout.BELOW,
								R.id.productListItemTextPrice);
					}
					else
					{
						params.addRule(RelativeLayout.BELOW, lastSpinner.getId());

					}
					spinner.setLayoutParams(params);

					// Add the attribute to the view.
					relativeLayout.addView(spinner);
					spinner.setId(1 + i);
					lastSpinner = spinner;
				}
			}
			return true;
		}
  return false;
  }
}
