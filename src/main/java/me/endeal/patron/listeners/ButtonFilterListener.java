package me.endeal.patron.listeners;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.transitions.everywhere.TransitionManager;
import android.transitions.everywhere.Explode;

import me.endeal.patron.model.Vendor;
import me.endeal.patron.model.Item;
import me.endeal.patron.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonFilterListener implements OnClickListener
{
    @Override
    public void onClick(View view)
    {
        final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.RIGHT | Gravity.BOTTOM);


        popup.getMenu().add("Favorites");
        popup.getMenu().add("Search...");
        /*
        Animator anim = ViewAnimationUtils.createCircularReveal(view, view.getRight(), view.getBottom(), 0, 100);
        anim.setDuration(800);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation)
            {
                super.onAnimationStart(animation);
                popup.show();
            }
        });
        anim.start();
        */
        popup.show();
    }
}
