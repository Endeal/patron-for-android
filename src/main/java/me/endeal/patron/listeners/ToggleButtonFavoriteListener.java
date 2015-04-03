package me.endeal.patron.listeners;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.model.Item;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;
import com.google.gson.Gson;

public class ToggleButtonFavoriteListener implements OnClickListener
{
	// The favorite product if a product toggle button.
	private Item item;

	// The favorite location if a location toggle button.
	private Vendor vendor;

	private OnApiExecutedListener listener;

	private boolean loading;

	public ToggleButtonFavoriteListener(Item item)
	{
		this.item = item;
		this.vendor = null;
	}

	public ToggleButtonFavoriteListener(Vendor vendor)
	{
		this.item = null;
		this.vendor = vendor;
	}

	public void setOnApiExecutedListener(OnApiExecutedListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void onClick(View buttonView)
	{
				if (loading)
					return;
				ApiExecutor executor = new ApiExecutor();
        final Context context = buttonView.getContext();
				final RotateAnimation animation = new RotateAnimation(0.0f, 360.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				animation.setDuration(1000);
				animation.setRepeatCount(Animation.INFINITE);
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation startAnimation) {}
						@Override
						public void onAnimationEnd(Animation endAnimation) {}
					@Override
					public void onAnimationRepeat(Animation repeatingAnimation)
					{
						if (!loading)
						{
							repeatingAnimation.cancel();
						}
					}
				});
				buttonView.startAnimation(animation);

				// Finished listener
				OnApiExecutedListener finishedListener = new OnApiExecutedListener() {
					@Override
					public void onExecuted()
					{
						loading = false;
					}
				};

		// A product was selected.
		if (item != null)
		{
			if (Globals.getUser().hasFavoriteItem(item.getId()))
			{
				executor.removeFavoriteItem(item, listener, finishedListener);
			}
			else
			{
				executor.addFavoriteItem(item, listener, finishedListener);
			}
		}

		// A location was selected.
		else
		{
            if (Globals.getUser().hasFavoriteVendor(vendor.getId()))
            {
                executor.removeFavoriteVendor(vendor, listener, finishedListener);
            }
            else
            {
                executor.addFavoriteVendor(vendor, listener, finishedListener);
            }
		}
	}

}
